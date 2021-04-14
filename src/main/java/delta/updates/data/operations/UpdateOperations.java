package delta.updates.data.operations;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of update operations.
 * @author DAM
 */
public class UpdateOperations
{
  private List<UpdateOperation> _operations;

  /**
   * Constructor.
   */
  public UpdateOperations()
  {
    _operations=new ArrayList<UpdateOperation>();
  }

  /**
   * Add an operation.
   * @param operation Operation to add.
   */
  public void addOperation(UpdateOperation operation)
  {
    _operations.add(operation);
  }

  /**
   * Get the list of managed operations.
   * @return a list of operations.
   */
  public List<UpdateOperation> getOperations()
  {
    return new ArrayList<UpdateOperation>(_operations);
  }
}
