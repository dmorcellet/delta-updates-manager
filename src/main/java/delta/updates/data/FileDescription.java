package delta.updates.data;

/**
 * File description.
 * @author DAM
 */
public class FileDescription extends DirectoryEntryDescription
{
  private long _size;
  private long _crc;

  /**
   * Constructor.
   */
  public FileDescription()
  {
    super();
  }

  /**
   * Get the file size.
   * @return a size in bytes.
   */
  public long getSize()
  {
    return _size;
  }

  /**
   * Set the file size.
   * @param size Size to set.
   */
  public void setSize(long size)
  {
    _size=size;
  }

  /**
   * Get the file CRC.
   * @return a CRC.
   */
  public long getCRC()
  {
    return _crc;
  }

  /**
   * Set the file CRC.
   * @param crc CRC to set.
   */
  public void setCRC(long crc)
  {
    _crc=crc;
  }

  @Override
  public String toString()
  {
    return "File: name="+getName()+", size="+_size+", CRC="+_crc;
  }
}
