package delta.updates.data;

import java.util.ArrayList;
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

  @Override
  public String toString()
  {
    return "Directory: name="+getName();
  }
}
