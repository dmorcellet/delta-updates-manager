package delta.updates.data;

/**
 * Utility methods related to directoy entries.
 * @author DAM
 */
public class EntryUtils
{
  /**
   * Get a path for the given entry.
   * @param entry Entry to use.
   * @return A path.
   */
  public static String getPath(DirectoryEntryDescription entry)
  {
    DirectoryDescription parent=entry.getParent();
    if ((parent==null) || (parent.getName().length()==0))
    {
      return entry.getName();
    }
    String parentPath=getPath(parent);
    return parentPath+Constants.PATH_ENTRY_SEPARATOR+entry.getName();
  }
}
