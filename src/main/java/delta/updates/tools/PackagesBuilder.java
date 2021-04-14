package delta.updates.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.utils.files.archives.ArchiveBuilder;
import delta.updates.data.ArchivedContents;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwareDescription;
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwarePackageUsage;
import delta.updates.data.SoftwareReference;
import delta.updates.data.io.xml.SoftwareDescriptionXmlIO;
import delta.updates.engine.LocalDataManager;
import delta.updates.utils.DescriptionBuilder;

/**
 * Packages builder.
 * @author DAM
 */
public class PackagesBuilder
{
  private File _from;
  private ToolsConfig _config;
  private List<SoftwarePackageDescription> _packages;
  private Set<String> _pathsToArchive;

  /**
   * Constructor.
   * @param from Source location for files.
   * @param config Tool configuration.
   */
  public PackagesBuilder(File from, ToolsConfig config)
  {
    _from=from;
    _config=config;
    _pathsToArchive=new HashSet<String>();
  }

  /**
   * Register a well-known path to archive.
   * @param path Path to archive.
   */
  public void addPathToArchive(String path)
  {
    _pathsToArchive.add(path);
  }

  /**
   * Do the job.
   * @param software Software description.
   */
  public void initSoftware(SoftwareDescription software)
  {
    List<SoftwarePackageDescription> packages=buildPackages();
    updateSoftware(software,packages);
    writeMetadata(software);
  }

  private void updateSoftware(SoftwareDescription software, List<SoftwarePackageDescription> packages)
  {
    // Update software description
    String baseURL=_config.getBaseURL();
    String urlOfDescription=baseURL+"software.xml";
    software.setDescriptionURL(urlOfDescription);
    for(SoftwarePackageDescription packageDescription : packages)
    {
      // Package
      SoftwareReference packageReference=packageDescription.getReference();
      int packageId=packageReference.getId();
      String packageSourceURL=baseURL+"packages/"+packageId+".zip";
      packageDescription.addSourceURL(packageSourceURL);
      // Usage
      SoftwarePackageUsage usage=new SoftwarePackageUsage(packageDescription.getReference());
      usage.setRelativePath(".");
      String packageDescriptionURL=baseURL+"packages/"+packageId+".xml";
      usage.setDescriptionURL(packageDescriptionURL);
      usage.setDetailedDescription(packageDescription);
      software.addPackage(usage);
    }
    // Build local metadata
    LocalDataManager localData=new LocalDataManager(_from);
    localData.setSoftware(software);
    localData.writeMetadata();
  }

  private List<SoftwarePackageDescription> buildPackages()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription inputDescription=builder.build(_from);
    _packages=new ArrayList<SoftwarePackageDescription>();
    handleDirectoryEntry(inputDescription);
    handleRemainingFiles((DirectoryDescription)inputDescription);
    return _packages;
  }

  private void writeMetadata(SoftwareDescription software)
  {
    File resultsDir=_config.getResultsDir();
    // Software
    File softwareFile=new File(resultsDir,"software.xml");
    SoftwareDescriptionXmlIO.writeFile(softwareFile,software);
    File rootPackagesDir=new File(resultsDir,"packages");
    for(SoftwarePackageUsage packageUsage : software.getPackages())
    {
      SoftwareReference packageReference=packageUsage.getPackage();
      int packageID=packageReference.getId();
      File packageFile=new File(rootPackagesDir,packageID+".xml");
      SoftwarePackageDescription packageDescription=packageUsage.getDetailedDescription();
      SoftwareDescriptionXmlIO.writeFile(packageFile,packageDescription);
    }
  }

  private void handleDirectoryEntry(DirectoryEntryDescription entry)
  {
    if (entry instanceof DirectoryDescription)
    {
      handleDirectory((DirectoryDescription)entry);
    }
  }

  private void handleDirectory(DirectoryDescription directoryEntry)
  {
    String path=EntryUtils.getPath(directoryEntry);
    if (_pathsToArchive.contains(path))
    {
      int packageID=_packages.size();
      ArchivedContents contents=buildArchivedContents(directoryEntry,packageID);
      SoftwarePackageDescription packageDescription=buildPackage(directoryEntry,contents,packageID);
      _packages.add(packageDescription);
      directoryEntry.getParent().removeEntry(directoryEntry.getName());
    }
    else
    {
      for(DirectoryEntryDescription childEntry : directoryEntry.getEntries())
      {
        handleDirectoryEntry(childEntry);
      }
    }
  }

  private void handleRemainingFiles(DirectoryDescription inputDescription)
  {
    int packageID=_packages.size();
    ArchivedContents contents=buildArchivedContents(inputDescription,packageID);
    SoftwarePackageDescription packageDescription=buildPackage(inputDescription,contents,packageID);
    packageDescription.getReference().setName("main");
    _packages.add(packageDescription);
  }

  private SoftwarePackageDescription buildPackage(DirectoryDescription rootEntry, ArchivedContents contents, int packageID)
  {
    SoftwarePackageDescription packageDescription=new SoftwarePackageDescription();
    SoftwareReference ref=new SoftwareReference(packageID);
    ref.setName(rootEntry.getName());
    packageDescription.setReference(ref);
    packageDescription.setContents(contents);
    return packageDescription;
  }

  private ArchivedContents buildArchivedContents(DirectoryEntryDescription sourceDescription, int packageID)
  {
    ArchivedContents ret=new ArchivedContents();
    // Compute archive file path
    FileDescription archiveFile=new FileDescription();
    archiveFile.setParent(sourceDescription.getParent());
    String sourceName=sourceDescription.getName();
    if (".".equals(sourceName))
    {
      sourceName="main";
    }
    archiveFile.setName(sourceName);
    ret.setDataFile(archiveFile);
    // Build archive file
    File resultsDir=_config.getResultsDir();
    File rootPackagesDir=new File(resultsDir,"packages");
    String archiveFilename=packageID+".zip";
    File toFile=new File(rootPackagesDir,archiveFilename);
    boolean ok=buildArchive(toFile,sourceDescription);
    if (!ok)
    {
      return null;
    }
    // Setup contents data
    DescriptionBuilder.fillFileEntry(archiveFile,toFile);
    ret.addEntry(sourceDescription);
    return ret;
  }

  private boolean buildArchive(File toFile, DirectoryEntryDescription sourceDescription)
  {
    toFile.getParentFile().mkdirs();
    ArchiveBuilder builder=new ArchiveBuilder(toFile);
    if (!builder.start())
    {
      return false;
    }
    handleArchiveEntry(builder,sourceDescription);
    builder.terminate();
    return true;
  }

  private void handleArchiveEntry(ArchiveBuilder builder, DirectoryEntryDescription source)
  {
    if (source instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)source;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        handleArchiveEntry(builder,childEntry);
      }
    }
    else
    {
      String path=EntryUtils.getPath(source);
      if (path.startsWith("./")) path=path.substring(2);
      File sourceFile=new File(_from,path);
      File archiveEntry=new File(path);
      builder.addFile(sourceFile,archiveEntry);
    }
  }
}
