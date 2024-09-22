package delta.updates.data.io.xml;

import java.io.File;

import org.junit.jupiter.api.Test;

import delta.updates.data.DirectoryEntryDescription;
import delta.updates.utils.DescriptionBuilder;

/**
 * Test class for XML I/O of directory entries.
 * @author DAM
 */
class DirectoryEntriesXmlIOTest
{
  /**
   * Test XML I/O.
   */
  @Test
  void testXmlIO()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    File currentDir=new File(".");
    DirectoryEntryDescription description=builder.build(currentDir);
    File toFile=new File("entries.xml");
    DirectoryEntriesXmlIO.writeFile(toFile,description);
  }
}
