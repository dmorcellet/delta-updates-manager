package delta.updates.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Directory description.
 * @author DAM
 */
public class DirectoryDescription extends DirectoryEntryDescription
{
  private List<DirectoryEntryDescription> _entries;

  /**
   * Constructor.
   */
  public DirectoryDescription()
  {
    super();
    _entries=new ArrayList<DirectoryEntryDescription>();
  }

  /**
   * Add an entry.
   * @param entry Entry to add.
   */
  public void addEntry(DirectoryEntryDescription entry)
  {
    _entries.add(entry);
  }

  /**
   * Remove an entry using its name.
   * @param name Name of entry to remove.
   */
  public void removeEntry(String name)
  {
    DirectoryEntryDescription entry=getEntryByName(name);
    removeEntry(entry);
  }

  /**
   * Remove an entry.
   * @param entry Entry to remove.
   */
  public void removeEntry(DirectoryEntryDescription entry)
  {
    if (entry!=null)
    {
      _entries.remove(entry);
      entry.setParent(null);
    }
  }

  /**
   * Find an entry using its path.
   * @param path Path to use.
   * @return An entry or <code>null</code> if not found.
   */
  public DirectoryEntryDescription findByPath(String path)
  {
    int index=path.indexOf(Constants.PATH_ENTRY_SEPARATOR);
    if (index!=-1)
    {
      // Forward to child directory, if it exists
      String name=path.substring(0,index);
      DirectoryEntryDescription child=getEntryByName(name);
      if (child instanceof DirectoryDescription)
      {
        DirectoryDescription childDir=(DirectoryDescription)child;
        String childPath=path.substring(index+Constants.PATH_ENTRY_SEPARATOR.length());
        return childDir.findByPath(childPath);
      }
    }
    else
    {
      // Direct child
      return getEntryByName(path);
    }
    // Not found
    return null;
  }

  /**
   * Get all the managed entries.
   * @return a list of entries.
   */
  public List<DirectoryEntryDescription> getEntries()
  {
    return new ArrayList<DirectoryEntryDescription>(_entries);
  }

  /**
   * Get an entry using its name.
   * @param name Name of the entry to get.
   * @return A directory entry or <code>null</code> if not found.
   */
  public DirectoryEntryDescription getEntryByName(String name)
  {
    for(DirectoryEntryDescription entry : _entries)
    {
      if (entry.getName().equals(name))
      {
        return entry;
      }
    }
    return null;
  }

  /**
   * Get the entry names.
   * @return a list of sorted entry names.
   */
  public List<String> getEntryNames()
  {
    List<String> ret=new ArrayList<String>();
    for(DirectoryEntryDescription entry : _entries)
    {
      ret.add(entry.getName());
    }
    Collections.sort(ret);
    return ret;
  }

  /**
   * Get all files in this directory (recursively).
   * @return A possibly empty but never <code>null</code> list of file descriptions.
   */
  public List<FileDescription> getAllFiles()
  {
    List<FileDescription> ret=new ArrayList<FileDescription>();
    for(DirectoryEntryDescription entry : _entries)
    {
      if (entry instanceof FileDescription)
      {
        ret.add((FileDescription)entry);
      }
      else if (entry instanceof DirectoryDescription)
      {
        DirectoryDescription directory=(DirectoryDescription)entry;
        ret.addAll(directory.getAllFiles());
      }
    }
    return ret;
  }

  @Override
  public String toString()
  {
    return "Directory: name="+getName();
  }
}
