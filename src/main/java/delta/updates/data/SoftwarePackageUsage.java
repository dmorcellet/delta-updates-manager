package delta.updates.data;

/**
 * Usage of a software package.
 * @author DAM
 */
public class SoftwarePackageUsage
{
  private SoftwarePackageReference _package;
  private String _relativePath;
  private String _descriptionURL;

  /**
   * Constructor.
   * @param packageRef Package reference.
   */
  public SoftwarePackageUsage(SoftwarePackageReference packageRef)
  {
    _package=packageRef;
  }

  /**
   * Get the package reference.
   * @return the package reference.
   */
  public SoftwarePackageReference getPackage()
  {
    return _package;
  }

  /**
   * Get the root path of package, relative to root installation directory.
   * @return a relative path.
   */
  public String getRelativePath()
  {
    return _relativePath;
  }

  /**
   * Set the relative path.
   * @param relativePath Relative path to set.
   */
  public void setRelativePath(String relativePath)
  {
    _relativePath=relativePath;
  }

  /**
   * Get the URL of the package description.
   * @return an URL.
   */
  public String getDescriptionURL()
  {
    return _descriptionURL;
  }

  /**
   * Set the URL of the package description.
   * @param descriptionURL URL to set.
   */
  public void setDescriptionURL(String descriptionURL)
  {
    _descriptionURL=descriptionURL;
  }
}
