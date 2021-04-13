package delta.updates.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
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
  private SoftwareDescription _software;
  private List<SoftwarePackageDescription> _packages;

  /**
   * Constructor.
   * @param rootDir Root directory for managed files.
   */
  public LocalDataManager(File rootDir)
  {
    _rootDir=rootDir;
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
    File softwareFile=new File(_rootDir,"software.xml");
    if (softwareFile.exists())
    {
      _software=SoftwareDescriptionXmlIO.parseSoftwareDescriptionFile(softwareFile);
    }
  }

  private void loadPackages()
  {
    File packagesDir=new File(_rootDir,".packages");
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
   * Get the managed packages.
   * @return A list of packages.
   */
  public List<SoftwarePackageDescription> getPackages()
  {
    return new ArrayList<SoftwarePackageDescription>(_packages);
  }
}
