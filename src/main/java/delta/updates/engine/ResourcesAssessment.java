package delta.updates.engine;

/**
 * Resources need assessment for an update.
 * @author DAM
 */
public class ResourcesAssessment
{
  private int _packagesCount;
  private long _downloadSize;
  private long _diskSize;

  /**
   * Constructor.
   * @param packagesCount Packages count.
   * @param downloadSize Download size (bytes).
   * @param diskSize Disk size (bytes).
   */
  public ResourcesAssessment(int packagesCount, long downloadSize, long diskSize)
  {
    _packagesCount=packagesCount;
    _downloadSize=downloadSize;
    _diskSize=diskSize;
  }

  /**
   * Get the packages count.
   * @return a count.
   */
  public int getPackagesCount()
  {
    return _packagesCount;
  }

  /**
   * Get the download size (bytes).
   * @return a size in bytes.
   */
  public long getDownloadSize()
  {
    return _downloadSize;
  }

  /**
   * Get the disk size (bytes).
   * @return a size in bytes.
   */
  public long getDiskSize()
  {
    return _diskSize;
  }

  @Override
  public String toString()
  {
    return "Packages: "+_packagesCount+", download size="+_downloadSize+", disk size="+_diskSize;
  }
}
