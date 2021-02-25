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
    File from=new File("D:/shared/damien/dev/lotrocompanion/releases/14.0/LotRO Companion/app");
    DirectoryEntryDescription description=builder.build(from);
    SoftwarePackage softwarePackage=new SoftwarePackage();
    softwarePackage.setName("LotroCompanion");
    softwarePackage.setVersion(1400);
    softwarePackage.setVersionLabel("14.0 Update 28.2.1");
    softwarePackage.setDescription("Lotro Companion 14.0 with data of Update 28.2.1");
    softwarePackage.setRootEntry(description);
    File file=new File("lotrocompanion.xml");
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
