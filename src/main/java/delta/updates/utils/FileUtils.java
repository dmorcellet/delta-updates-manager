package delta.updates.utils;

import java.io.File;

/**
 * Utility methods related to files.
 * @author DAM
 */
public class FileUtils
{
  /**
   * Move a file from one path to another (same disk).
   * @param from From file.
   * @param to To file.
   * @return <code>true</code> in case of success,<code>false</code> otherwise.
   */
  public static boolean move(File from, File to)
  {
    if (to.exists())
    {
      boolean ok=to.delete();
      if (!ok)
      {
        return false;
      }
    }
    return from.renameTo(to);
  }
}
