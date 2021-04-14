package delta.updates.engine;

import java.util.List;
import java.util.Objects;

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
        handleDirectories((DirectoryDescription)entry1,(DirectoryDescription)entry2);
      }
      else
      {
        remove(entry1);
        add(entry2);
      }
    }
    else if (entry1 instanceof FileDescription)
    {
      if (entry2 instanceof FileDescription)
      {
        handleFiles((FileDescription)entry1,(FileDescription)entry2);
      }
      else
      {
        remove(entry1);
        add(entry2);
      }
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
    long size1=file1.getSize();
    long size2=file2.getSize();
    if (size1!=size2)
    {
      update(file1);
      return;
    }
    long crc1=file1.getCRC();
    long crc2=file2.getCRC();
    if (crc1!=crc2)
    {
      update(file1);
      return;
    }
  }


  private void remove(DirectoryEntryDescription d)
  {
    if (d instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)d;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        remove(childEntry);
      }
    }
    UpdateOperation operation=new UpdateOperation(OperationType.DELETE,d);
    _operations.addOperation(operation);
    //System.out.println("Remove entry: "+d);
  }

  private void add(DirectoryEntryDescription d)
  {
    if (d instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)d;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        add(childEntry);
      }
    }
    UpdateOperation operation=new UpdateOperation(OperationType.ADD,d);
    _operations.addOperation(operation);
    //System.out.println("Add entry: "+d);
  }

  private void update(FileDescription file)
  {
    UpdateOperation operation=new UpdateOperation(OperationType.UPDATE,file);
    _operations.addOperation(operation);
    //System.out.println("Update entry: "+file);
  }
}
