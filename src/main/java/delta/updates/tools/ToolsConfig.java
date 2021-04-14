package delta.updates.tools;

import java.io.File;

/**
 * Configuration of the tools.
 * @author DAM
 */
public class ToolsConfig
{
  private String _baseURL;
  private File _resultsDir;

  /**
   * Constructor.
   * @param baseURL Base URL for deployment.
   * @param resultsDir Directory for products.
   */
  public ToolsConfig(String baseURL, File resultsDir)
  {
    _baseURL=baseURL;
    _resultsDir=resultsDir;
  }

  /**
   * Get the base URL for deployment.
   * @return an URL.
   */
  public String getBaseURL()
  {
    return _baseURL;
  }

  /**
   * Get the results directory.
   * @return  the results directory.
   */
  public File getResultsDir()
  {
    return _resultsDir;
  }
}
