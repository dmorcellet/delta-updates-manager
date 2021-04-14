package delta.updates.data.operations;

import delta.updates.data.DirectoryEntryDescription;

/**
 * Elementary update operation.
 * @author DAM
 */
public class UpdateOperation
{
  private OperationType _operation;
  private DirectoryEntryDescription _entry;

  /**
   * Constructor.
   * @param operation Operation type.
   * @param entry Targeted directory entry.
   */
  public UpdateOperation(OperationType operation, DirectoryEntryDescription entry)
  {
    _operation=operation;
    _entry=entry;
  }

  /**
   * Get the involved operation.
   * @return the involved operation.
   */
  public OperationType getOperation()
  {
    return _operation;
  }

  /**
   * Get the targeted directory entry.
   * @return a directory entry.
   */
  public DirectoryEntryDescription getEntry()
  {
    return _entry;
  }

  @Override
  public String toString()
  {
    return _operation+" "+_entry;
  }
}
