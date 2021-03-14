package delta.updates.data;

/**
 * Software package reference.
 * @author DAM
 */
public class SoftwarePackageReference
{
  private int _id;
  private String _name;
  private Version _version;

  /**
   * Constructor.
   * @param id Software package identifier.
   */
  public SoftwarePackageReference(int id)
  {
    _id=id;
    _name="";
    _version=new Version();
  }

  /**
   * Get the software package identifier.
   * @return the software package identifier.
   */
  public int getId()
  {
    return _id;
  }

  /**
   * Get the software package name.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the software package name.
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
   * Get the version of the software package.
   * @return a version.
   */
  public Version getVersion()
  {
    return _version;
  }

  /**
   * Set the version of this software package.
   * @param version Version to set.
   */
  public void setVersion(Version version)
  {
    if (version!=null)
    {
      _version=version;
    }
  }

  @Override
  public String toString()
  {
    return "Software package '"+_name+"', version "+_version;
  }
}
