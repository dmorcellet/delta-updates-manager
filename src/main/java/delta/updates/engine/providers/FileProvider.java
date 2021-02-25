package delta.updates.engine.providers;

import java.io.File;

import delta.updates.data.FileDescription;

/**
 * @author dm
 */
public interface FileProvider
{
  /**
   * Get a file.
   * @param entry Entry to use.
   * @param toDir Destination directory.
   * @return <code>true</code> if file was found, <code>false</code> otherwise.
   */
  boolean getFile(FileDescription entry, File toDir);
}
