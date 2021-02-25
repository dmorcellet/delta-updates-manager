package delta.updates.contents;

import java.util.ArrayList;
import java.util.List;

import delta.updates.data.FileDescription;

/**
 * Archived contents description.
 * @author DAM
 */
public class ArchivedContents extends ContentsDescription
{
  /**
   * Archived files, at least one!
   */
  private List<FileDescription> _files;

  /**
   * Constructor.
   */
  public ArchivedContents()
  {
    super();
    _files=new ArrayList<FileDescription>();
  }

  /**
   * Add a content file.
   * @param file File to add.
   */
  public void addFile(FileDescription file)
  {
    _files.add(file);
  }

  /**
   * Get the files in this archive.
   * @return A list of files.
   */
  public List<FileDescription> getFiles()
  {
    return new ArrayList<FileDescription>(_files);
  }
}
