package delta.updates.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.downloads.async.DownloadsManager;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.Version;
import delta.updates.engine.listener.UpdateStatus;
import delta.updates.engine.listener.UpdateStatusController;

/**
 * Update engine.
 * @author DAM
 */
public class UpdateEngine
{
  private static final Logger LOGGER=LoggerFactory.getLogger(UpdateEngine.class);

  // Data
  private LocalDataManager _localData;
  private RemoteDataManager _remoteData;
  private PackagesWorkspace _workspace;
  // Services
  private DownloadsManager _downloadsMgr;
  // Status management
  private UpdateStatusController _statusController;

  /**
   * Constructor.
   * @param rootAppDir Root application directory.
   * @param downloader Downloader.
   */
  public UpdateEngine(File rootAppDir, DownloadsManager downloader)
  {
    _downloadsMgr=downloader;
    _remoteData=new RemoteDataManager(downloader);
    _localData=new LocalDataManager(rootAppDir);
    File tmpDir=new File("__tmp");
    _statusController=new UpdateStatusController();
    _workspace=new PackagesWorkspace(downloader,tmpDir,_statusController);
  }

  /**
   * Get the update status controller.
   * @return the update status controller.
   */
  public UpdateStatusController getStatusController()
  {
    return _statusController;
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
        boolean ok=_remoteData.resolvePackage(remotePackage);
        if (!ok)
        {
          throw new IllegalStateException("Cannot resolve package: "+packageReference);
        }
      }
    }
    return ret;
  }

  /**
   * Download an update package.
   * @param neededPackage Package to use.
   * @return <code>true</code> if download was successfull, <code>false</code> otherwise.
   */
  public boolean downloadPackage(SoftwarePackageUsage neededPackage)
  {
    SoftwareReference packageReference=neededPackage.getPackage();
    String packageName=packageReference.getName();
    boolean ok=_workspace.getPackage(neededPackage);
    if (!ok)
    {
      String endMessage="Failed to download package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.FAILED,endMessage);
      return false;
    }
    return ok;
  }

  /**
   * Integrate a package.
   * Pre-requisite: the package has been downloaded/verified.
   * @param neededPackage Package to use.
   * @return <code>true</code> if successfull, <code>false</code> otherwise.
   */
  public boolean integratePackage(SoftwarePackageUsage neededPackage)
  {
    // Apply package
    SoftwareReference packageReference=neededPackage.getPackage();
    String packageName=packageReference.getName();
    String message="Integrating package '"+packageName+"'";
    _statusController.setImportStatus(UpdateStatus.RUNNING,message);
    PackageIntegrator integrator=new PackageIntegrator(_localData,_workspace);
    boolean ok=integrator.doIt(neededPackage);
    if (ok)
    {
      String endMessage="Integrated package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.RUNNING,endMessage);
    }
    else
    {
      String endMessage="Failed to integrate package '"+packageName+"'";
      _statusController.setImportStatus(UpdateStatus.FAILED,endMessage);
    }
    return ok;
  }

  /**
   * Clean-up.
   */
  public void cleanup()
  {
    LOGGER.debug("Engine cleanup!");
    _workspace.cleanup();
    _downloadsMgr.dispose();
  }

  /**
   * Request cancellation.
   */
  public void requestCancel()
  {
    _workspace.cancel();
  }
}
