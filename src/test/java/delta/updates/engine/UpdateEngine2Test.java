package delta.updates.engine;

import junit.framework.TestCase;
import delta.downloads.Downloader;

/**
 * Test for the update engine.
 * @author DAM
 */
public class UpdateEngine2Test extends TestCase
{
  /**
   * Test update.
   */
  public void testUpdate()
  {
    Downloader downloader=new Downloader();
    UpdateEngine2 engine2=new UpdateEngine2(downloader);
    engine2.doIt();
  }
}
