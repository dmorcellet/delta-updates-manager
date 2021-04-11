package delta.updates.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Software description.
 * @author DAM
 */
public class SoftwareDescription
{
  private SoftwareReference _reference;
  private long _date;
  private String _contentsDescription;
  private String _descriptionURL;
  private List<SoftwarePackageUsage> _packages;

  /**
   * Constructor.
   * @param id Software identifier.
   */
  public SoftwareDescription(int id)
  {
    _reference=new SoftwareReference(id);
    _contentsDescription="";
    _packages=new ArrayList<SoftwarePackageUsage>();
  }

  /**
   * Get the software identifier.
   * @return the software identifier.
   */
  public int getId()
  {
    return _reference.getId();
  }

  /**
   * Get the software name.
   * @return a name.
   */
  public String getName()
  {
    return _reference.getName();
  }

  /**
   * Set the software name.
   * @param name Name to set.
   */
  public void setName(String name)
  {
    _reference.setName(name);
  }

  /**
   * Get the version of the software.
   * @return a version.
   */
  public Version getVersion()
  {
    return _reference.getVersion();
  }

  /**
   * Set the version.
   * @param version Version to set.
   */
  public void setVersion(Version version)
  {
    _reference.setVersion(version);
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
  public String getContentsDescription()
  {
    return _contentsDescription;
  }

  /**
   * Set the description of the software.
   * @param contentsDescription Description to set.
   */
  public void setContentsDescription(String contentsDescription)
  {
    if (contentsDescription==null)
    {
      contentsDescription="";
    }
    _contentsDescription=contentsDescription;
  }

  /**
   * Get a description URL.
   * @return an URL where we find the current description of this software.
   */
  public String getDescriptionURL()
  {
    return _descriptionURL;
  }

  /**
   * Set the URL of the description of this software.
   * @param descriptionURL URL to set.
   */
  public void setDescriptionURL(String descriptionURL)
  {
    if (descriptionURL==null)
    {
      descriptionURL="";
    }
    _descriptionURL=descriptionURL;
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
    return "Software " + _reference;
  }
}
