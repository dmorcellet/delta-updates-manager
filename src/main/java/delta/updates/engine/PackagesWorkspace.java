package delta.updates.engine;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import delta.downloads.DownloadException;
import delta.downloads.Downloader;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;

/**
 * Workspace to work with packages.
 * @author DAM
 */
public class PackagesWorkspace
{
  private static final Logger LOGGER=Logger.getLogger(PackagesWorkspace.class);

  private Downloader _downloader;
  private File _rootDir;

  /**
   * Constructor.
   * @param downloader Downloader.
   * @param rootDir Root directory for this workspace.
   */
  public PackagesWorkspace(Downloader downloader, File rootDir)
  {
    _downloader=downloader;
    _rootDir=rootDir;
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
    File packageFile=null;
    for(String sourceURL : sourceURLs)
    {
      packageFile=downloadPackage(packageUsage,sourceURL);
      if (packageFile!=null)
      {
        break;
      }
    }
    if (packageFile==null)
    {
      LOGGER.info("Could not get package: "+packageUsage.getPackage());
      return false;
    }
    // TODO Check: size, contents
    return true;
  }

  private File downloadPackage(SoftwarePackageUsage packageUsage, String url)
  {
    File packagesDir=new File(_rootDir,"packages");
    int id=packageUsage.getPackage().getId();
    String name=id+".zip";
    File packageFile=new File(packagesDir,name);
    packageFile.getParentFile().mkdirs();
    boolean ok=false;
    try
    {
      ok=_downloader.downloadToFile(url,packageFile);
    }
    catch(DownloadException downloadException)
    {
      LOGGER.warn("Could not download package froml: "+url,downloadException);
    }
    return (ok)?packageFile:null;
  }
}
