package delta.updates.engine;

import java.io.File;

import delta.updates.data.SoftwarePackage;
import delta.updates.data.io.xml.SoftwarePackageXmlIO;
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
    UpdateEngine engine=new UpdateEngine(toDir,rootUrl);
    // Software package
    File packageFile=new File("lotrocompanion.xml");
    SoftwarePackage lcPackage=SoftwarePackageXmlIO.parseFile(packageFile);
    engine.doIt(lcPackage);
  }
}
