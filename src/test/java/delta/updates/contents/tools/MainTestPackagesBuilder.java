package delta.updates.contents.tools;

import java.io.File;

import delta.updates.tools.PackagesBuilder;

/**
 * Simple test class for the packages builder.
 * @author DAM
 */
public class MainTestPackagesBuilder
{
  private void doIt()
  {
    File from=new File("d:/tmp/lc14");
    File to=new File("d:/tmp/lc14-contents");
    PackagesBuilder builder=new PackagesBuilder(from,to);
    builder.addPathToArchive("./data");
    builder.addPathToArchive("./lib");
    builder.doIt();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestPackagesBuilder().doIt();
  }
}
