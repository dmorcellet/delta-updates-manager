package delta.updates.data;

/**
 * Description of a software package.
 * @author DAM
 */
public class SoftwarePackage
{
  private SoftwarePackageSummary _summary;
  private DirectoryEntryDescription _rootEntry;

  /**
   * Constructor.
   */
  public SoftwarePackage()
  {
    _summary=new SoftwarePackageSummary();
  }

  /**
   * Get the summary.
   * @return the summary.
   */
  public SoftwarePackageSummary getSummary()
  {
    return _summary;
  }

  /**
   * Get the description of the root entry for this package.
   * @return an entry description.
   */
  public DirectoryEntryDescription getRootEntry()
  {
    return _rootEntry;
  }

  /**
   * Set the description of the files that make up this package.
   * @param rootEntry Root entry description.
   */
  public void setRootEntry(DirectoryEntryDescription rootEntry)
  {
    _rootEntry=rootEntry;
  }

  @Override
  public String toString()
  {
    return _summary.toString();
  }
}
