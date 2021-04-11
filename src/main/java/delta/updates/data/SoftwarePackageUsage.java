package delta.updates.data;

/**
 * Usage of a software package.
 * @author DAM
 */
public class SoftwarePackageUsage
{
  private SoftwareReference _package;
  private String _relativePath;
  private String _descriptionURL;
  private SoftwarePackageDescription _detailedDescription;

  /**
   * Constructor.
   * @param packageRef Package reference.
   */
  public SoftwarePackageUsage(SoftwareReference packageRef)
  {
    _package=packageRef;
  }

  /**
   * Get the package reference.
   * @return the package reference.
   */
  public SoftwareReference getPackage()
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

  /**
   * Get the detailed description of this package.
   * @return a description or <code>null</code> if not set.
   */
  public SoftwarePackageDescription getDetailedDescription()
  {
    return _detailedDescription;
  }

  /**
   * Set the detailed description for this package.
   * @param detailedDescription Description to set.
   */
  public void setDetailedDescription(SoftwarePackageDescription detailedDescription)
  {
    _detailedDescription=detailedDescription; 
  }
}
