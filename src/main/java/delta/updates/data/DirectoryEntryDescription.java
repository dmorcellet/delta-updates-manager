package delta.updates.data;

/**
 * Directory entry.
 * @author DAM
 */
public abstract class DirectoryEntryDescription
{
  private DirectoryDescription _parent;
  private String _name;

  /**
   * Constructor.
   */
  public DirectoryEntryDescription()
  {
    _name="";
  }

  /**
   * Get the parent directory.
   * @return a parent.
   */
  public DirectoryDescription getParent()
  {
    return _parent;
  }

  /**
   * Remove this entry from its parent, if any.
   */
  public void removeFromParent()
  {
    if (_parent!=null)
    {
      _parent.removeEntry(this);
    }
  }

  /**
   * Set the parent directory.
   * @param parent Parent to set.
   */
  public void setParent(DirectoryDescription parent)
  {
    _parent=parent;
  }

  /**
   * Get the name of this entry.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this entry.
   * @param name Name to set.
   */
  public void setName(String name)
  {
    if (name==null)
    {
      name="";
    }
    _name=name;
  }
}
