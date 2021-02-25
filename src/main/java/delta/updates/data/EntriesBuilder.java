package delta.updates.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for directory entries.
 * @author DAM
 */
public class EntriesBuilder
{
  private Map<String,DirectoryDescription> _directoriesCache;

  /**
   * Constructor.
   */
  public EntriesBuilder()
  {
    _directoriesCache=new HashMap<String,DirectoryDescription>();
  }

  /**
   * Build a directory entry from a raw path.
   * @param path Path to use.
   * @return A directory description.
   */
  public DirectoryDescription buildDirectoryFromPath(String path)
  {
    DirectoryDescription ret=_directoriesCache.get(path);
    if (ret!=null)
    {
      return ret;
    }
    int index=path.indexOf(Constants.PATH_ENTRY_SEPARATOR);
    if (index!=-1)
    {
      String parentPath=path.substring(0,index);
      DirectoryDescription parentDir=buildDirectoryFromPath(parentPath);
      ret=new DirectoryDescription();
      String name=path.substring(index+Constants.PATH_ENTRY_SEPARATOR.length());
      ret.setName(name);
      ret.setParent(parentDir);
    }
    else
    {
      ret=new DirectoryDescription();
      ret.setName(path);
    }
    _directoriesCache.put(path,ret);
    return ret;
  }

  /**
   * Build a file entry from a raw path.
   * @param path Path to use.
   * @return A file description.
   */
  public FileDescription buildFileFromPath(String path)
  {
    FileDescription ret;
    int index=path.indexOf(Constants.PATH_ENTRY_SEPARATOR);
    if (index!=-1)
    {
      String parentPath=path.substring(0,index);
      DirectoryDescription parentDir=buildDirectoryFromPath(parentPath);
      ret=new FileDescription();
      String name=path.substring(index+Constants.PATH_ENTRY_SEPARATOR.length());
      ret.setName(name);
      ret.setParent(parentDir);
    }
    else
    {
      ret=new FileDescription();
      ret.setName(path);
    }
    return ret;
  }
}
