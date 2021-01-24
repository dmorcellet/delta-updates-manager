package delta.updates.utils;

import java.io.File;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import delta.updates.data.DirectoryEntryDescription;

/**
 * Test the class {@link DescriptionBuilder}.
 * @author DAM
 */
public class DescriptionBuilderTest extends TestCase
{
  /**
   * Build a description of the current directory.
   */
  public void testLoadCurrentDirectory()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    File currentDir=new File(".");
    DirectoryEntryDescription description=builder.build(currentDir);
    Assert.assertNotNull(description);
    List<String> display=new DescriptionTreeDisplay().buildTreeDisplay(description);
    for(String line : display)
    {
      System.out.println(line);
    }
  }
}
