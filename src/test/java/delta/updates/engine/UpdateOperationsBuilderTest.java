package delta.updates.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.operations.UpdateOperation;
import delta.updates.data.operations.UpdateOperations;
import delta.updates.utils.DescriptionBuilder;

/**
 * Test for the update operations builder.
 * @author DAM
 */
class UpdateOperationsBuilderTest
{
  /**
   * Test diff builder with 2 identical trees.
   */
  @Test
  void testComputeDiff_NoDiffCase()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription entry1=builder.build(new File("."));
    DirectoryEntryDescription entry2=builder.build(new File("."));
    UpdateOperationsBuilder diffBuilder=new UpdateOperationsBuilder();
    UpdateOperations operations=diffBuilder.computeDiff(entry1,entry2);
    assertEquals(0,operations.getOperations().size());
  }

  /**
   * Test diff builder with 2 distinct trees.
   */
  @Test
  void testComputeDiff_StandardCase()
  {
    File rootDir1=new File("D:/shared/damien/dev/lotrocompanion/releases/15.0/LotRO Companion/app");
    File rootDir2=new File("D:/shared/damien/dev/lotrocompanion/releases/14.0/LotRO Companion/app");
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription entry1=builder.build(rootDir1);
    DirectoryEntryDescription entry2=builder.build(rootDir2);
    UpdateOperationsBuilder diffBuilder=new UpdateOperationsBuilder();
    UpdateOperations operations=diffBuilder.computeDiff(entry1,entry2);
    showOperations(operations);
  }

  private void showOperations(UpdateOperations operations)
  {
    for(UpdateOperation operation : operations.getOperations())
    {
      System.out.println(operation);
    }
  }
}
