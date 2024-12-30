package delta.updates.engine;

import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.downloads.async.DownloadsManager;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.engine.listener.UpdateStatus;
import delta.updates.engine.listener.UpdateStatusController;
import delta.updates.ui.UpdateStatusUIController;
import delta.updates.ui.UpdateUI;

/**
 * Update controller.
 * <br>
 * Manages:
 * <ul>
 * <li>the update engine,
 * <li>the update UI.
 * </ul>
 * @author DAM
 */
public class UpdateController
{
  private static final Logger LOGGER=LoggerFactory.getLogger(UpdateController.class);

  private UpdateEngine _engine;

  /**
   * Perform update.
   * @param rootAppDir Root application directory.
   */
  public void doIt(File rootAppDir)
  {
    DownloadsManager downloader=new DownloadsManager();
    _engine=new UpdateEngine(rootAppDir,downloader);
    try
    {
      SoftwareDescription remoteSoftware=_engine.lookForUpdate();
      if (remoteSoftware!=null)
      {
        List<SoftwarePackageUsage> neededPackages=_engine.getNeededPackages(remoteSoftware);
        LocalDataManager local=_engine.getLocalDataManager();
        SoftwareDescription localSoftware=local.getSoftware();
        ResourcesAssessment assessment=_engine.assessResources(neededPackages);
        boolean updateAllowed=UpdateUI.askForUpdate(localSoftware,remoteSoftware,assessment);
        if (updateAllowed)
        {
          showUI();
          performUpdate(localSoftware,remoteSoftware,neededPackages);
        }
      }
    }
    catch(Exception e)
    {
      LOGGER.warn("Error in update!");
    }
    finally
    {
      _engine.cleanup();
    }
  }

  private void performUpdate(SoftwareDescription localSoftware, SoftwareDescription remoteSoftware, List<SoftwarePackageUsage> neededPackages)
  {
    // Download packages
    for(SoftwarePackageUsage packageUsage : neededPackages)
    {
      boolean ok=_engine.downloadPackage(packageUsage);
      if (!ok)
      {
        return;
      }
    }
    // Integrate packages
    for(SoftwarePackageUsage packageUsage : neededPackages)
    {
      boolean ok=_engine.integratePackage(packageUsage);
      if (!ok)
      {
        return;
      }
    }
    // End operations
    localSoftware.setVersion(remoteSoftware.getVersion());
    LocalDataManager local=_engine.getLocalDataManager();
    boolean ok=local.writeSoftware();
    UpdateStatusController statusController=_engine.getStatusController();
    if (ok)
    {
      String endMessage="Updated finished!";
      statusController.setImportStatus(UpdateStatus.FINISHED,endMessage);
    }
    else
    {
      String endMessage="Updated failed!";
      statusController.setImportStatus(UpdateStatus.FAILED,endMessage);
    }
  }

  private void showUI()
  {
    final UpdateStatusUIController updateStatusUIController=new UpdateStatusUIController(UpdateController.this);
    UpdateStatusController statusController=_engine.getStatusController();
    statusController.setListener(updateStatusUIController);
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        JDialog dialog=updateStatusUIController.getDialog();
        dialog.setModal(true);
        dialog.setVisible(true);
      }
    };
    SwingUtilities.invokeLater(r);
  }

  /**
   * Request update cancellation.
   */
  public void cancel()
  {
    _engine.requestCancel();
  }
}
