package delta.updates.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import delta.updates.data.DirectoryEntryDescription;

/**
 * Test the class {@link DescriptionBuilder}.
 * @author DAM
 */
class DescriptionBuilderTest
{
  /**
   * Build a description of the current directory.
   */
  @Test
  void testLoadCurrentDirectory()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    File currentDir=new File(".");
    DirectoryEntryDescription description=builder.build(currentDir);
    assertNotNull(description);
    List<String> display=new DescriptionTreeDisplay().buildTreeDisplay(description);
    for(String line : display)
    {
      System.out.println(line);
    }
  }
}
