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
    File rootAppDir=new File("d:/tmp/lc15");
    UpdateController ctrl=new UpdateController(rootAppDir);
    ctrl.doIt();
  }
}
