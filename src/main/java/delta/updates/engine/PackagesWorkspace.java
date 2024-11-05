package delta.updates.engine;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.common.utils.files.FilesDeleter;
import delta.common.utils.files.archives.ArchiveDeflater;
import delta.common.utils.misc.CRC;
import delta.downloads.async.DownloadListener;
import delta.downloads.async.DownloadState;
import delta.downloads.async.DownloadTask;
import delta.downloads.async.DownloadsManager;
import delta.updates.data.ArchivedContents;
import delta.updates.data.FileDescription;
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
  private static final Logger LOGGER=LoggerFactory.getLogger(PackagesWorkspace.class);

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
    if (!ok)
    {
      LOGGER.info("Could not get package: "+packageReference);
      return false;
    }
    LOGGER.info("Downloaded package: "+packageReference);
    ok=checkPackage(packageReference,packageDescription);
    if (!ok)
    {
      LOGGER.info("Package check failed for: "+packageReference);
      return false;
    }
    // Expand
    ok=expandPackage(packageReference);
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

  private boolean checkPackage(SoftwareReference packageReference, SoftwarePackageDescription packageDescription)
  {
    ArchivedContents contents=packageDescription.getContents();
    if (contents==null)
    {
      return true;
    }
    String packageName=packageReference.getName();
    FileDescription dataFile=contents.getDataFile();
    // Check downloaded file
    File packageArchiveFile=getPackageArchiveFile(packageReference);
    // - can read
    if (!packageArchiveFile.canRead())
    {
      LOGGER.error("Cannot read downloaded package '"+packageName+"'");
      return false;
    }
    // - size
    long size=dataFile.getSize();
    long downloadedFileSize=packageArchiveFile.length();
    if (downloadedFileSize!=size)
    {
      LOGGER.error("Bad size for downloadeed package '"+packageName+"': "+downloadedFileSize+"!="+size);
      return false;
    }
    // - CRC
    long crc=dataFile.getCRC();
    long downloadedFileCRC=CRC.computeCRC(packageArchiveFile);
    if (downloadedFileCRC!=crc)
    {
      LOGGER.error("Bad CRC for downloadeed package '"+packageName+"': "+downloadedFileCRC+"!="+crc);
      return false;
    }
    return true;
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
      LOGGER.error("Failed to expand package '"+packageName+"'");
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
