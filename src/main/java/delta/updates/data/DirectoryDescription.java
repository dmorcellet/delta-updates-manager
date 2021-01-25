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

  @Override
  public String toString()
  {
    return "Directory: name="+getName();
  }
}
