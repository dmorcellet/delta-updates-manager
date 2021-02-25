package delta.updates.contents;

import java.util.ArrayList;
import java.util.List;

import delta.updates.data.DirectoryEntryDescription;

/**
 * Archived contents description.
 * @author DAM
 */
public class ArchivedContents extends ContentsDescription
{
  /**
   * Archived entries, at least one!
   */
  private List<DirectoryEntryDescription> _entries;

  /**
   * Constructor.
   */
  public ArchivedContents()
  {
    super();
    _entries=new ArrayList<DirectoryEntryDescription>();
  }

  /**
   * Add a content entry.
   * @param entry Entry to add.
   */
  public void addEntry(DirectoryEntryDescription entry)
  {
    _entries.add(entry);
  }

  /**
   * Get the entries in this archive.
   * @return A list of entries.
   */
  public List<DirectoryEntryDescription> getEntries()
  {
    return new ArrayList<DirectoryEntryDescription>(_entries);
  }
}
