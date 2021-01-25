package delta.updates.utils;

import java.util.List;
import java.util.Objects;

import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;

/**
 * Differences builder.
 * @author DAM
 */
public class DiffBuilder
{
  /**
   * Recursively compute differences between 2 entries.
   * @param entry1 Entry 1.
   * @param entry2 Entry 2.
   */
  public void computeDiff(DirectoryEntryDescription entry1, DirectoryEntryDescription entry2)
  {
    boolean namesAreEqual=Objects.equals(entry1.getName(),entry2.getName());
    if (!namesAreEqual)
    {
      remove(entry1);
      add(entry2);
      return;
    }
    handleEntries(entry1,entry2);
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
    System.out.println("Remove entry: "+d);
  }

  private void add(DirectoryEntryDescription d)
  {
    System.out.println("Add entry: "+d);
  }

  private void update(FileDescription file)
  {
    System.out.println("Update entry: "+file);
  }
}
