package delta.updates.engine;

import java.io.File;

import junit.framework.TestCase;
import delta.downloads.Downloader;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.engine.providers.HttpProvider;

/**
 * Test for the update engine.
 * @author DAM
 */
public class UpdateEngineTest extends TestCase
{
  /**
   * Test update.
   */
  public void testUpdate()
  {
    // Directory to update
    File toDir=new File("d:/tmp/lc14");
    // Source of data
    String rootUrl="http://localhost:8080/delta-web-genea-1.1-SNAPSHOT/app";
    Downloader downloader=new Downloader();
    HttpProvider httpPprovider=new HttpProvider(downloader,rootUrl);
    UpdateEngine engine=new UpdateEngine(toDir,httpPprovider);
    // Perform update
    DirectoryEntryDescription target=new DirectoryDescription();
    engine.doIt(target);
  }
}
