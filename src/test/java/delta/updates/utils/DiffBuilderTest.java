package delta.updates.utils;

import java.io.File;

import delta.updates.data.DirectoryEntryDescription;
import junit.framework.TestCase;

/**
 * Test for the differences builder.
 * @author DAM
 */
public class DiffBuilderTest extends TestCase
{
  /**
   * Test diff builder with 2 identical trees.
   */
  public void testComputeDiff_NoDiffCase()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription entry1=builder.build(new File("."));
    DirectoryEntryDescription entry2=builder.build(new File("."));
    DiffBuilder diffBuilder=new DiffBuilder();
    diffBuilder.computeDiff(entry1,entry2);
  }

  /**
   * Test diff builder with 2 distinct trees.
   */
  public void testComputeDiff_StandardCase()
  {
    File rootDir1=new File("D:/shared/damien/dev/lotrocompanion/releases/13.0.28.0/LotRO Companion");
    File rootDir2=new File("D:/shared/damien/dev/lotrocompanion/releases/14.0/LotRO Companion");
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription entry1=builder.build(rootDir1);
    DirectoryEntryDescription entry2=builder.build(rootDir2);
    DiffBuilder diffBuilder=new DiffBuilder();
    diffBuilder.computeDiff(entry1,entry2);
  }
}
