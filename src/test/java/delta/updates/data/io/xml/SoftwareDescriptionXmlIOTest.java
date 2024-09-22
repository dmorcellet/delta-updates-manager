package delta.updates.data.io.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.Test;

import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.Version;
import delta.updates.utils.DescriptionBuilder;

/**
 * Test class for XML I/O of software packages.
 * @author DAM
 */
class SoftwareDescriptionXmlIOTest
{
  private SoftwarePackageDescription _package=buildPackageData();

  /**
   * Test XML I/O for software descriptions.
   */
  @Test
  void testSoftwareDescriptionXmlIO()
  {
    // Software description
    SoftwareDescription software=new SoftwareDescription(1000);
    software.setName("LotroCompanion");
    // Version
    Version version=new Version(1400,"14.0 Update 28.2.1");
    software.setVersion(version);
    // Date
    software.setDate(System.currentTimeMillis());
    // Contents description
    software.setContentsDescription("Lotro Companion 14.0 with data of Update 28.2.1");
    software.setDescriptionURL("http://localhost:8080/delta-web-genea-1.1-SNAPSHOT/app/software.xml");
    // Packages
    SoftwarePackageUsage dataPackageUsage=new SoftwarePackageUsage(_package.getReference());
    dataPackageUsage.setDescriptionURL("http://localhost:8080/delta-web-genea-1.1-SNAPSHOT/app/package1.xml");
    software.addPackage(dataPackageUsage);
    File file=new File("lotrocompanion.xml");
    SoftwareDescriptionXmlIO.writeFile(file,software);
    // Read
    SoftwareDescription software2=SoftwareDescriptionXmlIO.parseSoftwareDescriptionFile(file);
    // Asserts
    assertNotNull(software2);
    assertEquals(software.getName(),software2.getName());
    assertEquals(software.getContentsDescription(),software2.getContentsDescription());
  }

  private SoftwarePackageDescription buildPackageData()
  {
    // Write
    File from=new File("D:/shared/damien/dev/lotrocompanion/releases/14.0/LotRO Companion/app");
    File data=new File(from,"data");
    SoftwarePackageDescription dataPackage=new SoftwarePackageDescription();
    SoftwareReference ref=new SoftwareReference(1001);
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
  @Test
  void testSoftwarePackageXmlIO()
  {
    File file=new File("dataPackage.xml");
    // Software package
    SoftwarePackageDescription softwarePackage=_package;
    SoftwareDescriptionXmlIO.writeFile(file,softwarePackage);
    // Read
    SoftwarePackageDescription softwarePackage2=SoftwareDescriptionXmlIO.parsePackageFile(file);
    // Asserts
    assertNotNull(softwarePackage2);
    SoftwareReference ref=softwarePackage.getReference();
    SoftwareReference ref2=softwarePackage2.getReference();
    assertEquals(ref.getName(),ref2.getName());
  }
}
