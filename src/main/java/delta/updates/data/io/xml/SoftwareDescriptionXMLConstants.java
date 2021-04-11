package delta.updates.data.io.xml;

/**
 * Constants for tags and attribute names used in the
 * XML persistence of software descriptions.
 * @author DAM
 */
public class SoftwareDescriptionXMLConstants
{
  /**
   * Tag 'software'.
   */
  public static final String SOFTWARE_TAG="software";
  /**
   * Tag 'package'.
   */
  public static final String PACKAGE_TAG="package";
  /**
   * Tag 'software','package', attribute 'id'.
   */
  public static final String ID_ATTR="id";
  /**
   * Tag 'software','package', attribute 'name'.
   */
  public static final String NAME_ATTR="name";
  /**
   * Tag 'software','package', attribute 'version'.
   */
  public static final String VERSION_ATTR="version";
  /**
   * Tag 'software','package', attribute 'versionLabel'.
   */
  public static final String VERSION_LABEL_ATTR="versionLabel";
  /**
   * Tag 'software', attribute 'date'.
   */
  public static final String SOFTWARE_DATE_ATTR="date";
  /**
   * Tag 'software', attribute 'contentsDescription'.
   */
  public static final String CONTENTS_DESCRIPTION_ATTR="contentsDescription";
  /**
   * Tag 'software', attribute 'descriptionURL'.
   */
  public static final String DESCRIPTION_URL_ATTR="descriptionURL";

  /**
   * Tag 'packageUsage'.
   */
  public static final String PACKAGE_USAGE_TAG="packageUsage";
  /**
   * Tag 'packageUsage', attribute 'relativePath'.
   */
  public static final String PACKAGE_USAGE_RELATIVE_PATH_ATTR="relativePath";
  /**
   * Tag 'packageUsage', attribute 'descriptionUrl'.
   */
  public static final String PACKAGE_USAGE_URL_ATTR="descriptionUrl";

  /**
   * Tag 'sourceURL'.
   */
  public static final String SOURCE_URL_TAG="sourceURL";
  /**
   * Tag 'sourceURL', attribute 'url'.
   */
  public static final String URL_ATTR="URL";
}
