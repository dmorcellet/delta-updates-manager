package delta.updates.data;

/**
 * @author dm
 */
public class SoftwarePackageSummary
{
  private String _name;
  private int _version;
  private String _versionLabel;
  private String _description;

  /**
   * Constructor.
   */
  public SoftwarePackageSummary()
  {
    _name="?";
    _version=1;
    _versionLabel="1.0";
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
   * Set the package name.
   * @param name Name to set.
   */
  public void setName(String name)
  {
    if (name==null)
    {
      name="";
    }
    _name=name;
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
   * Set the version.
   * @param version Version to set.
   */
  public void setVersion(int version)
  {
    _version=version;
  }

  /**
   * Get the version label.
   * @return a version label.
   */
  public String getVersionLabel()
  {
    return _versionLabel;
  }

  /**
   * Set the version label.
   * @param versionLabel Version label to set.
   */
  public void setVersionLabel(String versionLabel)
  {
    if (versionLabel==null)
    {
      versionLabel="";
    }
    _versionLabel=versionLabel;
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
   * Set the description of the package.
   * @param description Description to set.
   */
  public void setDescription(String description)
  {
    if (description==null)
    {
      description="";
    }
    _description=description;
  }

  @Override
  public String toString()
  {
    return "Software package '"+_name+"', version '"+_versionLabel+"' ("+_version+").";
  }
}
