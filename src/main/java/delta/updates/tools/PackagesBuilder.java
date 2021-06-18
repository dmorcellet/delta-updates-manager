package delta.updates.tools;

import java.io.File;
import java.util.List;

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

  /**
   * Constructor.
   * @param from Source location for files.
   * @param config Tool configuration.
   */
  public PackagesBuilder(File from, ToolsConfig config)
  {
    _from=from;
    _config=config;
  }

  /**
   * Update the provided software with the given packages.
   * @param software Software to update.
   * @param packages Packages to add.
   */
  public void updateSoftware(SoftwareDescription software, List<SoftwarePackageDescription> packages)
  {
    // Update software description
    String baseURL=_config.getBaseURL();
    String urlOfDescription=baseURL.replace("${file}","software.xml");
    software.setDescriptionURL(urlOfDescription);
    for(SoftwarePackageDescription packageDescription : packages)
    {
      SoftwareReference packageReference=packageDescription.getReference();
      int packageId=packageReference.getId();
      // Package
      ArchivedContents contents=packageDescription.getContents();
      if (contents!=null)
      {
        String packageSourceURL=baseURL.replace("${file}","packages/"+packageId+".zip");
        packageDescription.addSourceURL(packageSourceURL);
      }
      // Usage
      SoftwarePackageUsage usage=new SoftwarePackageUsage(packageDescription.getReference());
      String packageDescriptionURL=baseURL.replace("${file}","packages/"+packageId+".xml");
      usage.setDescriptionURL(packageDescriptionURL);
      usage.setDetailedDescription(packageDescription);
      software.addPackage(usage);
    }
    // Build local metadata
    LocalDataManager localData=new LocalDataManager(_from);
    localData.setSoftware(software);
    localData.writeMetadata();
    // Write metadata
    writeMetadata(software);
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

  /**
   * Build a package.
   * @param packageID Package identifier.
   * @param packageName Package name.
   * @param directoryEntry Directory entry.
   * @return A package description.
   */
  public SoftwarePackageDescription buildPackage(int packageID, String packageName, DirectoryDescription directoryEntry)
  {
    ArchivedContents contents=null;
    if (directoryEntry!=null)
    {
      // Remove from parent
      DirectoryDescription parent=directoryEntry.getParent();
      if (parent!=null)
      {
        parent.removeEntry(directoryEntry.getName());
      }
      // Build archive
      contents=buildArchivedContents(packageID,packageName,directoryEntry);
    }
    // Build package
    SoftwarePackageDescription packageDescription=new SoftwarePackageDescription();
    SoftwareReference ref=new SoftwareReference(packageID);
    ref.setName(packageName);
    packageDescription.setReference(ref);
    packageDescription.setContents(contents);
    return packageDescription;
  }

  private ArchivedContents buildArchivedContents(int packageID, String packageName, DirectoryDescription sourceDescription)
  {
    ArchivedContents ret=new ArchivedContents();
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
    FileDescription archiveFile=new FileDescription();
    DescriptionBuilder.fillFileEntry(archiveFile,toFile);
    ret.setDataFile(archiveFile);
    if (sourceDescription.getName().length()==0)
    {
      // Multiple files/directories at root directory
      for(DirectoryEntryDescription childEntry : sourceDescription.getEntries())
      {
        ret.addEntry(childEntry);
      }
    }
    else
    {
      // Single root directory
      ret.addEntry(sourceDescription);
    }
    return ret;
  }

  private boolean buildArchive(File toFile, DirectoryDescription sourceDescription)
  {
    toFile.getParentFile().mkdirs();
    ArchiveBuilder builder=new ArchiveBuilder(toFile);
    if (!builder.start())
    {
      return false;
    }
    if (sourceDescription.getName().length()==0)
    {
      // Multiple files/directories at root directory
      for(DirectoryEntryDescription childEntry : sourceDescription.getEntries())
      {
        childEntry.setParent(null);
        handleArchiveEntry(builder,childEntry);
      }
    }
    else
    {
      // Single root directory
      handleArchiveEntry(builder,sourceDescription);
    }
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
      File sourceFile=new File(_from,path);
      File archiveEntry=new File(path);
      builder.addFile(sourceFile,archiveEntry);
    }
  }
}
