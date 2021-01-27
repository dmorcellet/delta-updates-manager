package delta.updates.data.io.xml;

import java.io.File;

import delta.updates.data.DirectoryEntryDescription;
import delta.updates.utils.DescriptionBuilder;
import junit.framework.TestCase;

/**
 * Test class for XML I/O of directory entries.
 * @author DAM
 */
public class DirectoryEntriesXmlIOTest extends TestCase
{
  /**
   * Test XML I/O.
   */
  public void testXmlIO()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    File currentDir=new File(".");
    DirectoryEntryDescription description=builder.build(currentDir);
    File toFile=new File("entries.xml");
    DirectoryEntriesXmlIO.writeFile(toFile,description);
  }
}
