package delta.updates.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods related to files.
 * @author DAM
 */
public class FileUtils
{
  private static final Logger LOGGER=LoggerFactory.getLogger(FileUtils.class);

  /**
   * Move a file from one path to another (same disk).
   * @param from From file.
   * @param to To file.
   * @throws IOException If the move fails.
   */
  public static void move(Path from, Path to) throws IOException
  {
    try
    {
      LOGGER.debug("Attempt to move '{}' to '{}'...",from,to);
      Files.move(from,to,StandardCopyOption.REPLACE_EXISTING);
      LOGGER.info("Moved '{}' to '{}'.",from,to);
    }
    catch(IOException e)
    {
      String msg="Could not move '"+from+"' to '"+to+"'!";
      throw new IOException(msg,e);
    }
  }
}
