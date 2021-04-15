package delta.updates.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.updates.data.DirectoryDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.io.xml.SoftwareDescriptionXmlIO;
import delta.updates.tools.SoftwareEntriesBuilder;

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
  private static final Logger LOGGER=Logger.getLogger(LocalDataManager.class);

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
    resolvePackageUsages();
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

  private void resolvePackageUsages()
  {
    if (_software==null)
    {
      return;
    }
    for(SoftwarePackageUsage packageUsage : _software.getPackages())
    {
      SoftwareReference packageReference=packageUsage.getPackage();
      int packageID=packageReference.getId();
      SoftwarePackageDescription packageDescription=getPackageByID(packageID);
      if (packageDescription!=null)
      {
        packageUsage.setDetailedDescription(packageDescription);
      }
      else
      {
        LOGGER.warn("Could not resolve package ID="+packageID);
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
   * Get a package description using its identifier.
   * @param packageID Package identifier.
   * @return A package description.
   */
  public SoftwarePackageDescription getPackageByID(int packageID)
  {
    for(SoftwarePackageDescription packageDescription : _packages)
    {
      SoftwareReference packageReference=packageDescription.getReference();
      if (packageReference.getId()==packageID)
      {
        return packageDescription;
      }
    }
    return null;
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
    writeSoftware();
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

  /**
   * Write software definition to disk.
   * @return <code>true</code> if successfull, <code>false</code> otherwise.
   */
  public boolean writeSoftware()
  {
    File softwareFile=getSoftwareFile();
    File parentFile=softwareFile.getParentFile();
    if (!parentFile.exists())
    {
      boolean ok=parentFile.mkdirs();
      if (!ok)
      {
        return false;
      }
    }
    return SoftwareDescriptionXmlIO.writeFile(softwareFile,_software);
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

  /**
   * Get a directory description for this software.
   * @return a directory description.
   */
  public DirectoryDescription getDirectoryDescription()
  {
    SoftwareEntriesBuilder builder=new SoftwareEntriesBuilder();
    DirectoryDescription ret=builder.build(_software);
    return ret;
  }
}
