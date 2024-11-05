package delta.updates.engine;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;
import delta.updates.data.operations.OperationType;
import delta.updates.data.operations.UpdateOperation;
import delta.updates.data.operations.UpdateOperations;

/**
 * Builds update operations to update an entry to another.
 * @author DAM
 */
public class UpdateOperationsBuilder
{
  private static final Logger LOGGER=LoggerFactory.getLogger(UpdateOperationsBuilder.class);

  private UpdateOperations _operations;

  /**
   * Recursively compute update operations between 2 entries.
   * @param entry1 Entry 1.
   * @param entry2 Entry 2.
   * @return the computed operations.
   */
  public UpdateOperations computeDiff(DirectoryEntryDescription entry1, DirectoryEntryDescription entry2)
  {
    _operations=new UpdateOperations();
    boolean namesAreEqual=Objects.equals(entry1.getName(),entry2.getName());
    if (!namesAreEqual)
    {
      remove(entry1);
      add(entry2);
      return _operations;
    }
    handleEntries(entry1,entry2);
    return _operations;
  }

  /**
   * Handle entries with the same name.
   * @param entry1 Entry 1.
   * @param entry2 Entry 2.
   */
  private void handleEntries(DirectoryEntryDescription entry1, DirectoryEntryDescription entry2)
  {
    if (entry1 instanceof DirectoryDescription)
    {
      if (entry2 instanceof DirectoryDescription)
      {
        // Both are directories
        handleDirectories((DirectoryDescription)entry1,(DirectoryDescription)entry2);
      }
      else
      {
        // 1 is a directory, 2 is a file
        remove(entry1);
        add(entry2);
      }
    }
    else if (entry1 instanceof FileDescription)
    {
      if (entry2 instanceof FileDescription)
      {
        // Both are files
        handleFiles((FileDescription)entry1,(FileDescription)entry2);
      }
      else
      {
        // 1 is a file, 2 is a directory
        remove(entry1);
        add(entry2);
      }
    }
    else
    {
      LOGGER.warn("Unsupported entry type: "+entry1);
    }
  }

  private void handleDirectories(DirectoryDescription dir1, DirectoryDescription dir2)
  {
    List<String> names1=dir1.getEntryNames();
    List<String> names2=dir2.getEntryNames();
    for(String name : names1)
    {
      DirectoryEntryDescription entry1=dir1.getEntryByName(name);
      DirectoryEntryDescription entry2=dir2.getEntryByName(name);
      if (entry2!=null)
      {
        names2.remove(name);
        handleEntries(entry1,entry2);
      }
      else
      {
        remove(entry1);
      }
    }
    for(String name : names2)
    {
      DirectoryEntryDescription entry2=dir2.getEntryByName(name);
      add(entry2);
    }
  }

  private void handleFiles(FileDescription file1, FileDescription file2)
  {
    // Update if size or CRC are not the same
    long size1=file1.getSize();
    long size2=file2.getSize();
    if (size1!=size2)
    {
      update(file2);
      return;
    }
    long crc1=file1.getCRC();
    long crc2=file2.getCRC();
    if (crc1!=crc2)
    {
      update(file2);
      return;
    }
  }


  private void remove(DirectoryEntryDescription entry)
  {
    if (entry instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)entry;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        remove(childEntry);
      }
    }
    UpdateOperation operation=new UpdateOperation(OperationType.DELETE,entry);
    _operations.addOperation(operation);
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Remove entry: "+entry);
    }
  }

  private void add(DirectoryEntryDescription entry)
  {
    if (entry instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)entry;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        add(childEntry);
      }
    }
    UpdateOperation operation=new UpdateOperation(OperationType.ADD,entry);
    _operations.addOperation(operation);
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Add entry: "+entry);
    }
  }

  private void update(FileDescription newFile)
  {
    UpdateOperation operation=new UpdateOperation(OperationType.UPDATE,newFile);
    _operations.addOperation(operation);
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Update entry: "+newFile);
    }
  }
}
