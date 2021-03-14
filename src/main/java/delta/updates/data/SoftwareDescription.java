package delta.updates.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Software description.
 * @author DAM
 */
public class SoftwareDescription
{
  private int _id;
  private String _name;
  private Version _version;
  private long _date;
  private String _description;
  private List<SoftwarePackageUsage> _packages;

  /**
   * Constructor.
   * @param id Software identifier.
   */
  public SoftwareDescription(int id)
  {
    _id=id;
    _name="";
    _version=new Version();
    _description="";
    _packages=new ArrayList<SoftwarePackageUsage>();
  }

  /**
   * Get the software identifier.
   * @return the software identifier.
   */
  public int getId()
  {
    return _id;
  }

  /**
   * Get the software name.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the software name.
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
   * Get the version of the software.
   * @return a version.
   */
  public Version getVersion()
  {
    return _version;
  }

  /**
   * Set the version.
   * @param version Version to set.
   */
  public void setVersion(Version version)
  {
    if (version!=null)
    {
      _version=version;
    }
  }

  /**
   * Get the date of this software.
   * @return a timestamp.
   */
  public long getDate()
  {
    return _date;
  }

  /**
   * Set the date of this software.
   * @param date Date to set.
   */
  public void setDate(long date)
  {
    _date=date;
  }

  /**
   * Get a description of the software.
   * @return a description.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Set the description of the software.
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

  /**
   * Get the packages that make up this software.
   * @return A list of packages.
   */
  public List<SoftwarePackageUsage> getPackages()
  {
    return new ArrayList<SoftwarePackageUsage>(_packages);
  }

  /**
   * Add a software package.
   * @param packageUsage Package to add.
   */
  public void addPackage(SoftwarePackageUsage packageUsage)
  {
    _packages.add(packageUsage);
  }

  @Override
  public String toString()
  {
    return "Software '"+_name+"', version "+_version;
  }
}
