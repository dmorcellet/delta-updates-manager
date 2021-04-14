package delta.updates.data.operations;

/**
 * Operation type.
 * @author DAM
 */
public enum OperationType
{
  /**
   * Add a file or directory.
   */
  ADD,
  /**
   * Update a file.
   */
  UPDATE,
  /**
   * Delete a file or directory.
   */
  DELETE
}
