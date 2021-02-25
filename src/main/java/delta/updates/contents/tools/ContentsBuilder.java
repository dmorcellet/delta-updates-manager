package delta.updates.contents.tools;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import delta.common.utils.files.FileCopy;
import delta.common.utils.files.archives.ArchiveBuilder;
import delta.updates.contents.ArchivedContents;
import delta.updates.contents.ContentsDescription;
import delta.updates.contents.ContentsManager;
import delta.updates.contents.RawContents;
import delta.updates.contents.io.xml.ContentsXmlIO;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.data.SoftwarePackage;
import delta.updates.utils.DescriptionBuilder;

/**
 * Contents builder.
 * @author DAM
 */
public class ContentsBuilder
{
  // Input: a software package
  // Output: contents objects and data files

  private File _from;
  private SoftwarePackage _package;
  private File _to;
  private Set<String> _pathsToArchive;
  private ContentsManager _contentsMgr;

  /**
   * Constructor.
   * @param from Source location for files.
   * @param softwarePackage Software package to use.
   * @param to Target location for generated files.
   */
  public ContentsBuilder(File from, SoftwarePackage softwarePackage, File to)
  {
    _from=from;
    _package=softwarePackage;
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
   */
  public void doIt()
  {
    _contentsMgr=new ContentsManager();
    DirectoryEntryDescription files=_package.getRootEntry();
    handleDirectoryEntry(files);
    File contentsMgrFile=new File("contents.xml");
    ContentsXmlIO.writeFile(contentsMgrFile,_contentsMgr);
  }

  private void handleDirectoryEntry(DirectoryEntryDescription entry)
  {
    if (entry instanceof DirectoryDescription)
    {
      handleDirectory((DirectoryDescription)entry);
    }
    else if (entry instanceof FileDescription)
    {
      handleFile((FileDescription)entry);
    }
  }

  private void handleFile(FileDescription fileEntry)
  {
    boolean doCompress=shallArchiveFile(fileEntry);
    ContentsDescription contents=null;
    if (doCompress)
    {
      contents=buildArchivedContents(fileEntry);
    }
    else
    {
      contents=buildRawContents(fileEntry);
    }
    if (contents!=null)
    {
      _contentsMgr.addContents(contents);
    }
  }

  private boolean shallArchiveFile(FileDescription fileEntry)
  {
    return isCompressableFile(fileEntry.getName());
  }

  private boolean isCompressableFile(String name)
  {
    if (name.endsWith(".zip")) return false;
    if (name.endsWith(".jar")) return false;
    if (name.endsWith(".png")) return false;
    if (name.endsWith(".jpg")) return false;
    return true;
  }

  private void handleDirectory(DirectoryDescription directoryEntry)
  {
    String path=EntryUtils.getPath(directoryEntry);
    if (_pathsToArchive.contains(path))
    {
      ContentsDescription contents=buildArchivedContents(directoryEntry);
      if (contents!=null)
      {
        _contentsMgr.addContents(contents);
      }
    }
    else
    {
      for(DirectoryEntryDescription childEntry : directoryEntry.getEntries())
      {
        handleDirectoryEntry(childEntry);
      }
    }
  }

  private RawContents buildRawContents(FileDescription sourceDescription)
  {
    // Build contents file
    File fromFile=new File(_from,EntryUtils.getPath(sourceDescription));
    File toFile=new File(_to,EntryUtils.getPath(sourceDescription));
    toFile.getParentFile().mkdirs();
    boolean ok=FileCopy.copy(fromFile,toFile);
    if (!ok)
    {
      return null;
    }
    RawContents ret=new RawContents();
    // Compute file path
    ret.setDataFile(sourceDescription);
    return ret;
  }

  private ArchivedContents buildArchivedContents(DirectoryEntryDescription sourceDescription)
  {
    ArchivedContents ret=new ArchivedContents();
    // Compute archive file path
    FileDescription archiveFile=new FileDescription();
    archiveFile.setParent(sourceDescription.getParent());
    String name=sourceDescription.getName()+".zip";
    archiveFile.setName(name);
    ret.setDataFile(archiveFile);
    // Build archive file
    File toFile=new File(_to,EntryUtils.getPath(archiveFile));
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
    File source=new File(_from,EntryUtils.getPath(sourceDescription));
    File archiveEntry=new File(source.getName());
    if (sourceDescription instanceof DirectoryDescription)
    {
      builder.addDirectory(source,archiveEntry);
    }
    else
    {
      builder.addFile(source,archiveEntry);
    }
    builder.terminate();
    return true;
  }
}
