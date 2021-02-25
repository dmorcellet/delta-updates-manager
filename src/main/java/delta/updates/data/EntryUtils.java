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
    if (parent==null)
    {
      return entry.getName();
    }
    String parentPath=getPath(parent);
    return parentPath+Constants.PATH_ENTRY_SEPARATOR+entry.getName();
  }

  /**
   * Concatenate 2 paths.
   * @param path1 Parent path.
   * @param path2 Child path.
   * @return A path.
   */
  public static String concatPath(String path1, String path2)
  {
    if ((path1!=null) && (path1.length()>0))
    {
      return path1+Constants.PATH_ENTRY_SEPARATOR+path2;
    }
    return path2;
  }
}
