package delta.updates.data.io.xml;

import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;
import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageReference;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.Version;
import delta.updates.utils.DescriptionBuilder;

/**
 * Test class for XML I/O of software packages.
 * @author DAM
 */
public class SoftwareDescriptionXmlIOTest extends TestCase
{
  private SoftwarePackageDescription _package=buildPackageData();

  /**
   * Test XML I/O for software descriptions.
   */
  public void testSoftwareDescriptionXmlIO()
  {
    // Software description
    SoftwareDescription software=new SoftwareDescription(1000);
    software.setName("LotroCompanion");
    // Version
    Version version=new Version(1400,"14.0 Update 28.2.1");
    software.setVersion(version);
    // Date
    software.setDate(System.currentTimeMillis());
    // Description
    software.setDescription("Lotro Companion 14.0 with data of Update 28.2.1");
    // Packages
    SoftwarePackageUsage dataPackageUsage=new SoftwarePackageUsage(_package.getReference());
    dataPackageUsage.setRelativePath("data");
    dataPackageUsage.setDescriptionURL("http://localhost/data.zip");
    software.addPackage(dataPackageUsage);
    File file=new File("lotrocompanion.xml");
    SoftwareDescriptionXmlIO.writeFile(file,software);
    // Read
    SoftwareDescription software2=SoftwareDescriptionXmlIO.parseSoftwareDescriptionFile(file);
    // Asserts
    Assert.assertNotNull(software2);
    Assert.assertEquals(software.getName(),software2.getName());
    Assert.assertEquals(software.getDescription(),software2.getDescription());
  }

  private SoftwarePackageDescription buildPackageData()
  {
    // Write
    File from=new File("D:/shared/damien/dev/lotrocompanion/releases/14.0/LotRO Companion/app");
    File data=new File(from,"data");
    SoftwarePackageDescription dataPackage=new SoftwarePackageDescription();
    SoftwarePackageReference ref=new SoftwarePackageReference(1001);
    // Name
    ref.setName("Data");
    // Version
    ref.setVersion(new Version(1400,"14.0 Update 28.2.1"));
    // Reference
    dataPackage.setReference(ref);
    // Contents
    ArchivedContents contents=new ArchivedContents();
    FileDescription archiveFile=new FileDescription();
    archiveFile.setName("data.zip");
    contents.setDataFile(archiveFile);
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription description=builder.build(data);
    contents.addEntry(description);
    dataPackage.setContents(contents);
    return dataPackage;
  }

  /**
   * Test XML I/O for software packages.
   */
  public void testSoftwarePackageXmlIO()
  {
    File file=new File("dataPackage.xml");
    // Software package
    SoftwarePackageDescription softwarePackage=_package;
    SoftwareDescriptionXmlIO.writeFile(file,softwarePackage);
    // Read
    SoftwarePackageDescription softwarePackage2=SoftwareDescriptionXmlIO.parsePackageFile(file);
    // Asserts
    Assert.assertNotNull(softwarePackage2);
    SoftwarePackageReference ref=softwarePackage.getReference();
    SoftwarePackageReference ref2=softwarePackage2.getReference();
    Assert.assertEquals(ref.getName(),ref2.getName());
  }
}
