package delta.updates.contents.tools;

import java.io.File;

import delta.updates.data.SoftwarePackage;
import delta.updates.data.io.xml.SoftwarePackageXmlIO;

/**
 * Simple test class for the contents builder.
 * @author DAM
 */
public class MainTestContentsBuilder
{
  private void doIt()
  {
    File file=new File("lotrocompanion.xml");
    SoftwarePackage softwarePackage=SoftwarePackageXmlIO.parseFile(file);
    File from=new File("d:/tmp/lc14");
    File to=new File("d:/tmp/lc14-contents");
    ContentsBuilder builder=new ContentsBuilder(from,softwarePackage,to);
    builder.addPathToArchive("./data/lore/maps/indexes");
    builder.addPathToArchive("./data/lore/maps/markers");
    builder.doIt();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestContentsBuilder().doIt();
  }
}
