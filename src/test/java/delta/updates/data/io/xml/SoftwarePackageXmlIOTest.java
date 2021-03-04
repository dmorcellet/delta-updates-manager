package delta.updates.data.io.xml;

import java.io.File;

import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.SoftwarePackage;
import delta.updates.data.SoftwarePackageSummary;
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
    SoftwarePackageSummary summary=softwarePackage.getSummary();
    summary.setName("LotroCompanion");
    summary.setVersion(1400);
    summary.setVersionLabel("14.0 Update 28.2.1");
    summary.setDescription("Lotro Companion 14.0 with data of Update 28.2.1");
    softwarePackage.setRootEntry(description);
    File file=new File("lotrocompanion.xml");
    SoftwarePackageXmlIO.writeFile(file,softwarePackage);
    // Read
    SoftwarePackage softwarePackage2=SoftwarePackageXmlIO.parseFile(file);
    // Asserts
    Assert.assertNotNull(softwarePackage2);
    SoftwarePackageSummary summary2=softwarePackage2.getSummary();
    Assert.assertEquals(summary.getName(),summary2.getName());
    Assert.assertEquals(summary.getVersion(),summary2.getVersion());
    Assert.assertEquals(summary.getVersionLabel(),summary2.getVersionLabel());
    Assert.assertEquals(summary.getDescription(),summary2.getDescription());
  }
}
