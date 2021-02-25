package delta.updates.contents.tools;

import java.io.File;
import java.util.List;

import delta.updates.contents.ContentsDescription;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.io.xml.ContentsXmlIO;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwarePackage;
import delta.updates.data.io.xml.SoftwarePackageXmlIO;

/**
 * @author dm
 */
public class MainTestContentsManager
{
  private void doIt()
  {
    File from=new File("contents.xml");
    ContentsManager contentsMgr=ContentsXmlIO.parseFile(from);
    File file=new File("lotrocompanion.xml");
    SoftwarePackage softwarePackage=SoftwarePackageXmlIO.parseFile(file);
    DirectoryDescription directory=(DirectoryDescription)softwarePackage.getRootEntry();
    List<FileDescription> allFiles=directory.getAllFiles();
    List<ContentsDescription> contentsToUse=contentsMgr.getSources(allFiles);
    for(ContentsDescription contents : contentsToUse)
    {
      System.out.println(EntryUtils.getPath(contents.getDataFile()));
    }
  }

  
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestContentsManager().doIt();
  }
}
