package delta.updates.engine;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test for the update controller.
 * @author DAM
 */
public class UpdateControllerTest extends TestCase
{
  /**
   * Test update.
   */
  public void testUpdate()
  {
    UpdateController ctrl=new UpdateController();
    File rootAppDir=new File("d:/tmp/lc15");
    ctrl.doIt(rootAppDir);
  }
}
