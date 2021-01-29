package delta.updates.data;

/**
 * Description of a software package.
 * @author DAM
 */
public class SoftwarePackage
{
  private String _name;
  private int _version;
  private String _description;
  private DirectoryEntryDescription _files;

  /**
   * Constructor.
   */
  public SoftwarePackage()
  {
    _name="?";
    _version=1;
    _description="";
  }

  /**
   * Get the package name.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the version of the package.
   * @return an integer version.
   */
  public int getVersion()
  {
    return _version;
  }

  /**
   * Get a description of the package.
   * @return a description.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Get the description of the files that make up this package.
   * @return a description of files.
   */
  public DirectoryEntryDescription getFiles()
  {
    return _files;
  }

  /**
   * Set the description of the files that make up this package.
   * @param files Files description.
   */
  public void setFiles(DirectoryEntryDescription files)
  {
    _files=files;
  }
}
