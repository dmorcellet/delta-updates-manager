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
import delta.updates.data.SoftwarePackageDescription;
import delta.updates.data.SoftwareReference;
import delta.updates.utils.DescriptionBuilder;

/**
 * Packages builder.
 * @author DAM
 */
public class PackagesBuilder
{
  private File _from;
  private File _to;
  private List<SoftwarePackageDescription> _packages;
  private Set<String> _pathsToArchive;

  /**
   * Constructor.
   * @param from Source location for files.
   * @param to Target location for generated files.
   */
  public PackagesBuilder(File from, File to)
  {
    _from=from;
    _to=to;
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
   * @return the newly built contents manager.
   */
  public List<SoftwarePackageDescription> doIt()
  {
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription inputDescription=builder.build(_from);
    _packages=new ArrayList<SoftwarePackageDescription>();
    handleDirectoryEntry(inputDescription);
    handleRemainingFiles((DirectoryDescription)inputDescription);
    return _packages;
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
      ArchivedContents contents=buildArchivedContents(directoryEntry);
      SoftwarePackageDescription packageDescription=buildPackage(directoryEntry,contents);
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
    ArchivedContents contents=buildArchivedContents(inputDescription);
    SoftwarePackageDescription packageDescription=buildPackage(inputDescription,contents);
    packageDescription.getReference().setName("main");
    _packages.add(packageDescription);
  }

  private SoftwarePackageDescription buildPackage(DirectoryDescription rootEntry, ArchivedContents contents)
  {
    SoftwarePackageDescription packageDescription=new SoftwarePackageDescription();
    SoftwareReference ref=new SoftwareReference(_packages.size());
    ref.setName(rootEntry.getName());
    packageDescription.setReference(ref);
    packageDescription.setContents(contents);
    return packageDescription;
  }

  private ArchivedContents buildArchivedContents(DirectoryEntryDescription sourceDescription)
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
    String archiveFilename=sourceName+".zip";
    archiveFile.setName(sourceName);
    ret.setDataFile(archiveFile);
    // Build archive file
    File rootPackagesDir=new File(_to,"packages");
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
