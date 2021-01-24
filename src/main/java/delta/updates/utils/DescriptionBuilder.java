package delta.updates.utils;

import java.io.File;

import delta.common.utils.misc.CRC;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.FileDescription;

/**
 * Builds descriptions for the given files.
 * @author DAM
 */
public class DescriptionBuilder
{
  /**
   * Build a description for the given file.
   * @param input Input file.
   * @return A description or <code>null</code>.
   */
  public DirectoryEntryDescription build(File input)
  {
    if (input==null)
    {
      return null;
    }
    if (!input.exists())
    {
      return null;
    }
    return buildEntry(input);
  }

  private DirectoryEntryDescription buildEntry(File entry)
  {
    if (entry.isDirectory())
    {
      return buildDirectory(entry);
    }
    if (entry.isFile())
    {
      return buildFile(entry);
    }
    return null;
  }

  private DirectoryDescription buildDirectory(File directory)
  {
    DirectoryDescription ret=new DirectoryDescription();
    ret.setName(directory.getName());
    File[] files=directory.listFiles();
    if (files!=null)
    {
      for(File file : files)
      {
        DirectoryEntryDescription entry=buildEntry(file);
        if (entry!=null)
        {
          entry.setParent(ret);
          ret.addEntry(entry);
        }
      }
    }
    return ret;
  }

  private FileDescription buildFile(File file)
  {
    FileDescription ret=new FileDescription();
    ret.setName(file.getName());
    ret.setSize(file.length());
    long crc=CRC.computeCRC(file);
    ret.setCRC(crc);
    return ret;
  }
}
