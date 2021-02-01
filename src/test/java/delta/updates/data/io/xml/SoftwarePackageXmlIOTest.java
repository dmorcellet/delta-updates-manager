package delta.updates.data.io.xml;

import java.io.File;

import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.SoftwarePackage;
import delta.updates.utils.DescriptionBuilder;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test class for XML I/O of software packages.
 * @author DAM
 */
public class SoftwarePackageXmlIOTest extends TestCase
{
  /**
   * Test XML I/O.
   */
  public void testXmlIO()
  {
    // Write
    DescriptionBuilder builder=new DescriptionBuilder();
    File currentDir=new File(".");
    DirectoryEntryDescription description=builder.build(currentDir);
    SoftwarePackage softwarePackage=new SoftwarePackage();
    softwarePackage.setName("Test");
    softwarePackage.setVersion(2100);
    softwarePackage.setVersionLabel("2.1.0");
    softwarePackage.setDescription("A simple test package");
    softwarePackage.setFiles(description);
    File file=new File("package.xml");
    SoftwarePackageXmlIO.writeFile(file,softwarePackage);
    // Read
    SoftwarePackage softwarePackage2=SoftwarePackageXmlIO.parseFile(file);
    // Asserts
    Assert.assertNotNull(softwarePackage2);
    Assert.assertEquals(softwarePackage.getName(),softwarePackage2.getName());
    Assert.assertEquals(softwarePackage.getVersion(),softwarePackage2.getVersion());
    Assert.assertEquals(softwarePackage.getVersionLabel(),softwarePackage2.getVersionLabel());
    Assert.assertEquals(softwarePackage.getDescription(),softwarePackage2.getDescription());
  }
}
