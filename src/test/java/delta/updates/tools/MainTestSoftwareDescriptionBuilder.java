package delta.updates.tools;

import java.io.File;
import java.util.Date;
import java.util.List;

import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.Version;
import delta.updates.data.io.xml.SoftwareDescriptionXmlIO;

/**
 * Simple test class for the packages builder.
 * @author DAM
 */
public class MainTestSoftwareDescriptionBuilder
{
  private static final String BASE_URL="http://localhost:8080/delta-web-genea-1.1-SNAPSHOT/app/";
  private void doIt()
  {
    File from=new File("d:/tmp/lc15");
    File to=new File("d:/tmp/lc15-contents");
    PackagesBuilder builder=new PackagesBuilder(from,to);
    builder.addPathToArchive("./data");
    builder.addPathToArchive("./lib");
    List<SoftwarePackageDescription> packages=builder.doIt();
    // Build software description
    SoftwareDescription software=new SoftwareDescription(0);
    software.setName("Lotro Companion");
    software.setVersion(new Version(1500,"15.0.29.0.1"));
    software.setContentsDescription("Version 15 of the famous tool for Lotro");
    software.setDate(new Date().getTime());
    String urlOfDescription=BASE_URL+"software.xml";
    software.setDescriptionURL(urlOfDescription);
    File rootPackagesDir=new File(to,"packages");
    for(SoftwarePackageDescription packageDescription : packages)
    {
      // Package
      String packageName=packageDescription.getReference().getName();
      String packageSourceURL=BASE_URL+"packages/"+packageName+".zip";
      packageDescription.addSourceURL(packageSourceURL);
      File packageFile=new File(rootPackagesDir,packageName+".xml");
      SoftwareDescriptionXmlIO.writeFile(packageFile,packageDescription);
      // Usage
      SoftwarePackageUsage usage=new SoftwarePackageUsage(packageDescription.getReference());
      usage.setRelativePath(".");
      String packageDescriptionURL=BASE_URL+"packages/"+packageName+".xml";
      usage.setDescriptionURL(packageDescriptionURL);
      usage.setDetailedDescription(packageDescription);
      software.addPackage(usage);
    }
    File softwareFile=new File(to,"software.xml");
    SoftwareDescriptionXmlIO.writeFile(softwareFile,software);
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestSoftwareDescriptionBuilder().doIt();
  }
}
