package delta.updates.engine.listener;

import org.slf4j.LoggerFactory;

/**
 * Controller for the update status.
 * <br>
 * Manages:
 * <ul>
 * <li>the update status data,
 * <li>an optional update status listener.
 * </ul>
 * @author DAM
 */
public class UpdateStatusController
{
  private static final org.slf4j.Logger LOGGER=LoggerFactory.getLogger(UpdateStatusController.class);

  private UpdateStatusData _status;
  private UpdateStatusListener _listener;

  /**
   * Constructor.
   */
  public UpdateStatusController()
  {
    _status=new UpdateStatusData();
    _listener=null;
  }

  /**
   * Set the status listener.
   * @param listener Listener to set.
   */
  public void setListener(UpdateStatusListener listener)
  {
    _listener=listener;
  }

  /**
   * Set update status.
   * @param updateStatus Update status.
   * @param message Update status message.
   */
  public void setImportStatus(UpdateStatus updateStatus, String message)
  {
    LOGGER.info("Update status: {}, message={}",updateStatus,message);
    _status.setUpdateStatus(updateStatus);
    _status.setStatusMessage(message);
    statusUpdated();
  }

  /**
   * Call the status listener, if any.
   */
  private void statusUpdated()
  {
    if (_listener!=null)
    {
      _listener.statusUpdate(_status);
    }
  }
}
