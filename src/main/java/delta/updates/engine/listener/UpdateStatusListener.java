package delta.updates.engine.listener;

/**
 * Listener for changes in the update status.
 * @author DAM
 */
public interface UpdateStatusListener
{
  /**
   * Called on status update.
   * @param data Status data.
   */
  public void statusUpdate(UpdateStatusData data);
}
