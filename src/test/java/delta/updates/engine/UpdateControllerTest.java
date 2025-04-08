package delta.updates.engine;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for the update controller.
 * @author DAM
 */
class UpdateControllerTest
{
  /**
   * Test update.
   */
  @Test
  void testUpdate()
  {
    Assertions.assertDoesNotThrow(() -> {
      UpdateController ctrl=new UpdateController();
      File rootAppDir=new File("d:/tmp/lc15");
      ctrl.doIt(rootAppDir);
    });
  }
}
