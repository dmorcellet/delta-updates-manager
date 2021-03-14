package delta.updates.data;

/**
 * Version description.
 * @author DAM
 */
public class Version
{
  private int _id;
  private String _name;

  /**
   * Default constructor.
   */
  public Version()
  {
    this(1,"1.0");
  }

  /**
   * Constructor.
   * @param id Version identifier.
   * @param name Version name.
   */
  public Version(int id, String name)
  {
    _id=id;
    _name=name;
  }

  /**
   * Get the version identifier.
   * @return A version identifier.
   */
  public int getId()
  {
    return _id;
  }

  /**
   * Get the version name.
   * @return A version name.
   */
  public String getName()
  {
    return _name;
  }

  @Override
  public String toString()
  {
    return "ID: "+_id+", name:"+_name;
  }
}
