package delta.updates.data.io.xml;

/**
 * Constants for tags and attribute names used in the
 * XML persistence of directory entries.
 * @author DAM
 */
public class DirectoryEntriesXMLConstants
{
  /**
   * Tag 'file'.
   */
  public static final String FILE_TAG="file";
  /**
   * Tag 'directory'.
   */
  public static final String DIRECTORY_TAG="directory";
  /**
   * Tag 'file','directory', attribute 'name'.
   */
  public static final String ENTRY_NAME_ATTR="name";
  /**
   * Tag 'file', attribute 'size'.
   */
  public static final String FILE_SIZE_ATTR="size";
  /**
   * Tag 'file', attribute 'crc'.
   */
  public static final String FILE_CRC_ATTR="crc";
}
