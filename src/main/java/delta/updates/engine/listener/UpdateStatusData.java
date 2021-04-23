package delta.updates.engine.listener;

/**
 * Gathers update status data.
 * @author DAM
 */
public class UpdateStatusData
{
  // Update status
  private UpdateStatus _updateStatus;
  private String _statusMessage;

  /**
   * Constructor.
   */
  public UpdateStatusData()
  {
    reset();
  }

  /**
   * Reset contents.
   */
  public void reset()
  {
    _updateStatus=UpdateStatus.OFF;
    _statusMessage="";
  }

  /**
   * Set update status.
   * @param updateStatus Import status.
   */
  public void setUpdateStatus(UpdateStatus updateStatus)
  {
    _updateStatus=updateStatus;
  }

  /**
   * Set the status message.
   * @param statusMessage Status message.
   */
  public void setStatusMessage(String statusMessage)
  {
    _statusMessage=statusMessage;
  }

  /**
   * Set update success.
   * @param message A success message.
   */
  public void setUpdateSuccess(String message)
  {
    _updateStatus=UpdateStatus.FINISHED;
    _statusMessage=message;
  }

  /**
   * Set update failure.
   * @param message A failure message.
   */
  public void setUpdateFailed(String message)
  {
    _updateStatus=UpdateStatus.FAILED;
    _statusMessage=message;
  }

  /**
   * Get the import status.
   * @return the import status.
   */
  public UpdateStatus getUpdateStatus()
  {
    return _updateStatus;
  }

  /**
   * Get the update status message.
   * @return a message.
   */
  public String getStatusMessage()
  {
    return _statusMessage;
  }
}
