package delta.updates.contents.io.xml;

/**
 * Constants for tags and attribute names used in the
 * XML persistence of contents descriptions.
 * @author DAM
 */
public class ContentsXMLConstants
{
  /**
   * Tag 'contents'.
   */
  public static final String CONTENTS_TAG="contents";
  /**
   * Tag 'rawFile'.
   */
  public static final String RAW_FILE_TAG="rawFile";
  /**
   * Tag 'archive'.
   */
  public static final String ARCHIVE_TAG="archive";
  /**
   * Tag 'from'.
   */
  public static final String FROM_TAG="from";
  /**
   * Tag 'file'.
   */
  public static final String FILE_TAG="file";
  /**
   * Tag 'from','file', attribute 'path'.
   */
  public static final String FILE_PATH_ATTR="path";
  /**
   * Tag 'from','file', attribute 'size'.
   */
  public static final String FILE_SIZE_ATTR="size";
  /**
   * Tag 'from','file', attribute 'crc'.
   */
  public static final String FILE_CRC_ATTR="crc";
}