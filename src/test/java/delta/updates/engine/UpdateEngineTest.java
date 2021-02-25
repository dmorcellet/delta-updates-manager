package delta.updates.engine;

import java.io.File;

import delta.downloads.Downloader;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.io.xml.ContentsXmlIO;
import delta.updates.data.SoftwarePackage;
import delta.updates.data.io.xml.SoftwarePackageXmlIO;
import delta.updates.engine.providers.FileProvider;
import delta.updates.engine.providers.HttpProvider;
import delta.updates.engine.providers.SmartProvider;
import junit.framework.TestCase;

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
    File contentsMgrFile=new File("contents.xml");
    ContentsManager contentsMgr=ContentsXmlIO.parseFile(contentsMgrFile);
    FileProvider provider=new SmartProvider(contentsMgr,httpPprovider);
    UpdateEngine engine=new UpdateEngine(toDir,provider);
    // Software package
    File packageFile=new File("lotrocompanion.xml");
    SoftwarePackage lcPackage=SoftwarePackageXmlIO.parseFile(packageFile);
    engine.doIt(lcPackage);
  }
}
