package delta.updates.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.io.xml.SoftwareDescriptionXmlIO;

/**
 * Local data manager for the updates manager:
 * <ul>
 * <li>local software,
 * <li>local packages definitions.
 * </ul>
 * @author DAM
 */
public class LocalDataManager
{
  private File _rootDir;
  private File _updatesMgrDataDir;
  private SoftwareDescription _software;
  private List<SoftwarePackageDescription> _packages;

  /**
   * Constructor.
   * @param rootDir Root directory for managed files.
   */
  public LocalDataManager(File rootDir)
  {
    _rootDir=rootDir;
    _updatesMgrDataDir=new File(rootDir,".updates");
    _packages=new ArrayList<SoftwarePackageDescription>();
    init();
  }

  /**
   * Get the root directory for local data.
   * @return a root dirctory.
   */
  public File getRootDir()
  {
    return _rootDir;
  }

  private void init()
  {
    loadSoftware();
    loadPackages();
  }

  private void loadSoftware()
  {
    File softwareFile=getSoftwareFile();
    if (softwareFile.exists())
    {
      _software=SoftwareDescriptionXmlIO.parseSoftwareDescriptionFile(softwareFile);
    }
  }

  private void loadPackages()
  {
    File packagesDir=new File(_updatesMgrDataDir,"packages");
    if (packagesDir.exists())
    {
      File[] packageFiles=packagesDir.listFiles();
      if (packageFiles!=null)
      {
        for(File packageFile : packageFiles)
        {
          SoftwarePackageDescription packageDescription=SoftwareDescriptionXmlIO.parsePackageFile(packageFile);
          if (packageDescription!=null)
          {
            _packages.add(packageDescription);
          }
        }
      }
    }
  }

  /**
   * Get the managed software.
   * @return the managed software.
   */
  public SoftwareDescription getSoftware()
  {
    return _software;
  }

  /**
   * Set software.
   * @param software Software to set.
   */
  public void setSoftware(SoftwareDescription software)
  {
    _software=software;
  }

  /**
   * Get the managed packages.
   * @return A list of packages.
   */
  public List<SoftwarePackageDescription> getPackages()
  {
    return new ArrayList<SoftwarePackageDescription>(_packages);
  }

  /**
   * Write metadata.
   */
  public void writeMetadata()
  {
    if (_software==null)
    {
      return;
    }
    // Software
    File softwareFile=getSoftwareFile();
    softwareFile.getParentFile().mkdirs();
    SoftwareDescriptionXmlIO.writeFile(softwareFile,_software);
    // Packages
    for(SoftwarePackageUsage packageUsage : _software.getPackages())
    {
      writePackage(packageUsage);
    }
  }

  /**
   * Write package data.
   * @param packageUsage Package usage.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean writePackage(SoftwarePackageUsage packageUsage)
  {
    File packageFile=getPackageFile(packageUsage.getPackage());
    SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
    return SoftwareDescriptionXmlIO.writeFile(packageFile,packageDescription);
  }

  private File getSoftwareFile()
  {
    File softwareFile=new File(_updatesMgrDataDir,"software.xml");
    return softwareFile;
  }

  private File getPackageFile(SoftwareReference packageReference)
  {
    File packagesDir=new File(_updatesMgrDataDir,"packages");
    int id=packageReference.getId();
    String filename=id+".xml";
    return new File(packagesDir,filename);
  }
}
