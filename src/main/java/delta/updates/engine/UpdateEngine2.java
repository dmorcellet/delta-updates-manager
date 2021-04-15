package delta.updates.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.downloads.Downloader;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.Version;

/**
 * Another version of the update engine.
 * @author DAM
 */
public class UpdateEngine2
{
  private static final Logger LOGGER=Logger.getLogger(UpdateEngine2.class);

  private LocalDataManager _localData;
  private RemoteDataManager _remoteData;
  private PackagesWorkspace _workspace;

  /**
   * Constructor.
   * @param rootAppDir Root application directory.
   * @param downloader Downloader.
   */
  public UpdateEngine2(File rootAppDir, Downloader downloader)
  {
    _remoteData=new RemoteDataManager(downloader);
    _localData=new LocalDataManager(rootAppDir);
    File tmpDir=new File("__tmp");
    _workspace=new PackagesWorkspace(downloader,tmpDir);
  }

  /**
   * Get the local data manager.
   * @return the local data manager.
   */
  public LocalDataManager getLocalDataManager()
  {
    return _localData;
  }

  /**
   * Look if an update is available.
   * @return A software description if there is one, or <code>null</code> if problem or nothing to do.
   */
  public SoftwareDescription lookForUpdate()
  {
    SoftwareDescription localDescription=_localData.getSoftware();
    if (localDescription==null)
    {
      LOGGER.warn("Local software description not found!");
      return null;
    }
    String descriptionURL=localDescription.getDescriptionURL();
    SoftwareDescription remoteDescription=_remoteData.loadCurrentDescription(descriptionURL);
    if (remoteDescription==null)
    {
      LOGGER.warn("Remote software description not found!");
      return null;
    }
    boolean same=compareDescriptions(localDescription,remoteDescription);
    if (same)
    {
      LOGGER.info("No update available!");
      return null;
    }
    return remoteDescription;
  }

  /**
   * Perform resources needs assessment.
   * @param neededPackages Packages to use.
   * @return An assessment.
   */
  public ResourcesAssessment assessResources(List<SoftwarePackageUsage> neededPackages)
  {
    ResourcesEvaluator evaluator=new ResourcesEvaluator();
    ResourcesAssessment assessment=evaluator.doIt(neededPackages);
    return assessment;
  }

  private boolean compareDescriptions(SoftwareDescription local, SoftwareDescription remote)
  {
    Version localVersion=local.getVersion();
    Version remoteVersion=remote.getVersion();
    if (localVersion.getId()>=remoteVersion.getId())
    {
      return true;
    }
    return false;
  }

  /**
   * Get the packages to get.
   * @param remoteDescription Remote software description.
   * @return A possibly empty but never <code>null</code> list of packages.
   */
  public List<SoftwarePackageUsage> getNeededPackages(SoftwareDescription remoteDescription)
  {
    SoftwareDescription localDescription=_localData.getSoftware();
    List<SoftwarePackageUsage> ret=new ArrayList<SoftwarePackageUsage>();
    for(SoftwarePackageUsage remotePackage : remoteDescription.getPackages())
    {
      SoftwareReference packageReference=remotePackage.getPackage();
      boolean hasPackage=localDescription.hasPackage(packageReference);
      if (!hasPackage)
      {
        ret.add(remotePackage);
        // Find package details
        _remoteData.resolvePackage(remotePackage);
      }
    }
    return ret;
  }

  /**
   * Handle a package update.
   * @param neededPackage
   * @return <code>true</code> if update was successfull, <code>false</code> otherwise.
   */
  public boolean handlePackage(SoftwarePackageUsage neededPackage)
  {
    // Download package
    boolean ok=_workspace.getPackage(neededPackage);
    if (!ok)
    {
      return ok;
    }
    // Check package
    // TODO Later
    // Apply package
    PackageIntegrator integrator=new PackageIntegrator(_localData,_workspace);
    ok=integrator.doIt(neededPackage);
    return ok;
  }

  /**
   * Clean-up.
   */
  public void cleanup()
  {
    _workspace.cleanup();
  }
}
