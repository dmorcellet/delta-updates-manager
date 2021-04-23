package delta.updates.engine;

import java.io.File;

/**
 * Main class for to test the update controller.
 * @author DAM
 */
public class MainTestUpdateController
{
  /**
   * Not used.
   * @param args
   */
  public static void main(String[] args)
  {
    File rootAppDir=new File("d:/tmp/lc15");
    UpdateController ctrl=new UpdateController(rootAppDir);
    ctrl.doIt();
  }
}
