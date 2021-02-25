package delta.updates.contents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;

/**
 * Contents manager.
 * @author DAM
 */
public class ContentsManager
{
  private static final Logger LOGGER=Logger.getLogger(ContentsManager.class);

  private List<ContentsDescription> _contents;
  private Map<String,ContentsDescription> _filePathsToContents;

  /**
   * Constructor.
   */
  public ContentsManager()
  {
    _contents=new ArrayList<ContentsDescription>();
    _filePathsToContents=new HashMap<String,ContentsDescription>();
  }

  /**
   * Add a contents description.
   * @param contents Contents description to add.
   */
  public void addContents(ContentsDescription contents)
  {
    if (contents instanceof RawContents)
    {
      _contents.add(contents);
      addRawContents((RawContents)contents);
    }
    else if (contents instanceof ArchivedContents)
    {
      _contents.add(contents);
      addArchivedContents((ArchivedContents)contents);
    }
  }

  private void addRawContents(RawContents contents)
  {
    FileDescription file=contents.getDataFile();
    registerItem(null,file,contents);
  }

  private void addArchivedContents(ArchivedContents contents)
  {
    DirectoryDescription parent=contents.getDataFile().getParent();
    String parentPath=EntryUtils.getPath(parent);
    for(DirectoryEntryDescription entry : contents.getEntries())
    {
      registerEntry(parentPath,entry,contents);
    }
  }

  private void registerEntry(String parentPath, DirectoryEntryDescription entry, ContentsDescription contents)
  {
    if (entry instanceof FileDescription)
    {
      registerItem(parentPath,(FileDescription)entry,contents);
    }
    else if (entry instanceof DirectoryDescription)
    {
      registerDirectory(parentPath,(DirectoryDescription)entry,contents);
    }
  }

  private void registerItem(String parentPath, FileDescription file, ContentsDescription contents)
  {
    String childPath=EntryUtils.getPath(file);
    String key=EntryUtils.concatPath(parentPath,childPath);
    _filePathsToContents.put(key,contents);
  }

  private void registerDirectory(String parentPath, DirectoryDescription directory, ContentsDescription contents)
  {
    for(DirectoryEntryDescription entry : directory.getEntries())
    {
      registerEntry(parentPath,entry,contents);
    }
  }

  /**
   * Get the source contents description for the given file.
   * @param targetFile File to search.
   * @return A contents description or <code>null</code> if not found.
   */
  public ContentsDescription getSourceContents(FileDescription targetFile)
  {
    String key=EntryUtils.getPath(targetFile);
    return _filePathsToContents.get(key);
  }

  /**
   * Get all neeed sources for the given files.
   * @param targetFiles Files to search.
   * @return A possibly empty but never <code>null</code> list of contents descriptions.
   */
  public List<ContentsDescription> getSources(List<FileDescription> targetFiles)
  {
    Set<ContentsDescription> contents=new HashSet<ContentsDescription>();
    for(FileDescription targetFile : targetFiles)
    {
      ContentsDescription sourceContents=getSourceContents(targetFile);
      if (sourceContents!=null)
      {
        contents.add(sourceContents);
      }
      else
      {
        LOGGER.warn("No source contents for file: "+targetFile);
      }
    }
    List<ContentsDescription> ret=new ArrayList<ContentsDescription>(contents);
    return ret;
  }

  /**
   * Get all the managed contents.
   * @return a list of contents descriptions.
   */
  public List<ContentsDescription> getContents()
  {
    return new ArrayList<ContentsDescription>(_contents);
  }
}
