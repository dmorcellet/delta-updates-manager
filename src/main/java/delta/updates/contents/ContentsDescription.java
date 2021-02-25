package delta.updates.contents;

import delta.updates.data.FileDescription;

/**
 * Base class for contents descriptions.
 * @author DAM
 */
public class ContentsDescription
{
  private FileDescription _dataFile;

  /**
   * Constructor.
   */
  public ContentsDescription()
  {
    _dataFile=null;
  }

  /**
   * Get the source data file.
   * @return a file description.
   */
  public FileDescription getDataFile()
  {
    return _dataFile;
  }

  /**
   * Set the source data file.
   * @param dataFile File to set.
   */
  public void setDataFile(FileDescription dataFile)
  {
    _dataFile=dataFile;
  }
}
