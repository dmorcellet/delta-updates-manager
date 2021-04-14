package delta.updates.ui;

/**
 * Utility methods related to sizes.
 * @author DAM
 */
public class SizeUtils
{
  /**
   * Get a uman readable size label.
   * @param size Size in bytes.
   * @return A human readable size label.
   */
  public static String getSizeLabel(long size)
  {
    if (size==1) return "1 byte";
    if (size<1000)
    {
      return size+" bytes";
    }
    if (size<1000000)
    {
      float kBytes=size/1000.0f;
      return String.format("%.1f KB", Float.valueOf(kBytes));
    }
    if (size<1000000000)
    {
      float mBytes=size/1000000.0f;
      return String.format("%.1f MB", Float.valueOf(mBytes));
    }
    float gBytes=size/1000000000.0f;
    return String.format("%.1f GB", Float.valueOf(gBytes));
  }
}
