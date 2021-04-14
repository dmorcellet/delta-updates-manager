package delta.updates.tools;

import java.io.File;

import delta.updates.data.DirectoryDescription;
import delta.updates.data.operations.UpdateOperation;
import delta.updates.data.operations.UpdateOperations;
import delta.updates.engine.LocalDataManager;
import delta.updates.engine.UpdateOperationsBuilder;
import delta.updates.utils.DescriptionBuilder;

/**
 * @author dm
 */
public class MainBuildUpdate
{
  // Updates the given directory so that any difference between
  // the registered state and the current state will be used to build an update package

  private void doIt()
  {
    File from=new File("d:/tmp/lc15");
    LocalDataManager local=new LocalDataManager(from);
    DirectoryDescription directory=local.getDirectoryDescription();
    /*
    List<String> display=new DescriptionTreeDisplay().buildTreeDisplay(directory);
    for(String line : display)
    {
      System.out.println(line);
    }
    */
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryDescription description=(DirectoryDescription)builder.build(from);
    description.removeEntry(".updates");
    description.setName("");
    UpdateOperationsBuilder diffBuilder=new UpdateOperationsBuilder();
    UpdateOperations operations=diffBuilder.computeDiff(directory,description);
    for(UpdateOperation operation : operations.getOperations())
    {
      System.out.println(operation);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainBuildUpdate().doIt();
  }
}
