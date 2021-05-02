package delta.updates.engine;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.files.archives.ArchiveDeflater;
import delta.downloads.async.DownloadsManager;
import delta.downloads.async.DownloadListener;
import delta.downloads.async.DownloadState;
import delta.downloads.async.DownloadTask;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.engine.listener.UpdateStatus;
import delta.updates.engine.listener.UpdateStatusController;

/**
 * Workspace to work with packages.
 * @author DAM
 */
public class PackagesWorkspace
{
  private static final Logger LOGGER=Logger.getLogger(PackagesWorkspace.class);

  private DownloadsManager _downloader;
  private File _rootDir;
  private UpdateStatusController _statusController;

  /**
   * Constructor.
   * @param downloader Downloader.
   * @param rootDir Root directory for this workspace.
   * @param statusController Status controller.
   */
  public PackagesWorkspace(DownloadsManager downloader, File rootDir, UpdateStatusController statusController)
  {
    _downloader=downloader;
    _rootDir=rootDir;
    _statusController=statusController;
  }

  /**
   * Get a package.
   * @param packageUsage Package to get.
   * @return <code>true</code> if it was obtained, <code>false</code> otherwise.
   */
  public boolean getPackage(SoftwarePackageUsage packageUsage)
  {
    SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
    List<String> sourceURLs=packageDescription.getSourceURLs();
    if (sourceURLs.size()==0)
    {
      // Deletes only, no archive
      return true;
    }
    SoftwareReference packageReference=packageUsage.getPackage();
    boolean ok=false;
    for(String sourceURL : sourceURLs)
    {
      ok=downloadPackage(packageReference,sourceURL);
      if (ok)
      {
        break;
      }
    }
    if (ok)
    {
      LOGGER.info("Download package: "+packageReference);
    }
    else
    {
      LOGGER.info("Could not get package: "+packageReference);
      return false;
    }
    // Expand
    ok=expandPackage(packageReference);
    // TODO Check: size, contents
    return true;
  }

  private boolean downloadPackage(SoftwareReference packageReference, String url)
  {
    final String packageName=packageReference.getName();
    String message="Downloading package '"+packageName+"'";
    _statusController.setImportStatus(UpdateStatus.RUNNING,message);
    File packageArchiveFile=getPackageArchiveFile(packageReference);
    packageArchiveFile.getParentFile().mkdirs();

    DownloadListener listener=new DownloadListener()
    {
      @Override
      public void downloadTaskUpdated(DownloadTask task)
      {
        DownloadState state=task.getDownloadState();
        if (state==DownloadState.RUNNING)
        {
          Integer expectedSize=task.getExpectedSize();
          int doneSize=task.getDoneSize();
          String percentageStr="? %";
          if (expectedSize!=null)
          {
            float percentage=(100.0f*doneSize)/expectedSize.intValue();
            percentageStr=String.format("%.1f%%",Float.valueOf(percentage));
          }
          String progressMessage="Downloading package '"+packageName+"': "+percentageStr;
          _statusController.setImportStatus(UpdateStatus.RUNNING,progressMessage);
        }
      }
    };
    DownloadTask task=_downloader.syncDownload(url,packageArchiveFile,listener);
    DownloadState state=task.getDownloadState();
    boolean ok=(state==DownloadState.OK);
    if (ok)
    {
      String endMessage="Downloaded package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.RUNNING,endMessage);
    }
    else
    {
      String endMessage="Failed to download package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.FAILED,endMessage);
    }
    return ok;
  }

  /**
   * Expand the given package from the source archive.
   * @param packageReference Package to use.
   * @return <code>true</code> if it succeeded, <code>false</code> otherwise.
   */
  private boolean expandPackage(SoftwareReference packageReference)
  {
    String packageName=packageReference.getName();
    String message="Expanding package '"+packageName+"'";
    _statusController.setImportStatus(UpdateStatus.RUNNING,message);
    File packageArchiveFile=getPackageArchiveFile(packageReference);
    File rootDir=getPackageRootDir(packageReference);
    ArchiveDeflater deflated=new ArchiveDeflater(packageArchiveFile,rootDir);
    boolean ok=deflated.go();
    if (ok)
    {
      String endMessage="Expanded package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.RUNNING,endMessage);
    }
    else
    {
      String endMessage="Failed to expand package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.FAILED,endMessage);
    }
    return ok;
  }

  private File getPackagesDir()
  {
    return new File(_rootDir,"packages");
  }

  private File getPackageArchivesDir()
  {
    File packagesDir=getPackagesDir();
    return new File(packagesDir,"archives");
  }

  private File getExpandedPackagesDir()
  {
    File packagesDir=getPackagesDir();
    return new File(packagesDir,"expanded");
  }

  private File getPackageArchiveFile(SoftwareReference packageReference)
  {
    File packagesArchiveDir=getPackageArchivesDir();
    int id=packageReference.getId();
    String name=id+".zip";
    File packageArchiveFile=new File(packagesArchiveDir,name);
    return packageArchiveFile;
  }

  /**
   * Get the root directory that contains a deflated package.
   * @param packageReference Targeted package.
   * @return A directory.
   */
  public File getPackageRootDir(SoftwareReference packageReference)
  {
    File expandedDir=getExpandedPackagesDir();
    int id=packageReference.getId();
    File rootDir=new File(expandedDir,String.valueOf(id));
    return rootDir;
  }

  /**
   * Clean-up.
   */
  public void cleanup()
  {
    if (_rootDir.exists())
    {
      FilesDeleter deleter=new FilesDeleter(_rootDir,null,true);
      deleter.doIt();
    }
  }
}
