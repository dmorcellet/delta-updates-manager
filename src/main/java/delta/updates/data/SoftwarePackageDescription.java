package delta.updates.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description of a software package.
 * @author DAM
 */
public class SoftwarePackageDescription
{
  private SoftwareReference _reference;
  private List<String> _sourceURLs;
  private ArchivedContents _contents;
  private List<String> _toDelete;

  /**
   * Constructor.
   */
  public SoftwarePackageDescription()
  {
    _sourceURLs=new ArrayList<String>();
    _toDelete=new ArrayList<String>();
  }

  /**
   * Get the package reference.
   * @return the package reference.
   */
  public SoftwareReference getReference()
  {
    return _reference;
  }

  /**
   * Set the software package reference.
   * @param reference Reference to set.
   */
  public void setReference(SoftwareReference reference)
  {
    _reference=reference;
  }

  /**
   * Get the source URLs.
   * @return a list of source URLs.
   */
  public List<String> getSourceURLs()
  {
    return new ArrayList<String>(_sourceURLs);
  }

  /**
   * Add a source URL.
   * @param sourceURL Source URL to add.
   */
  public void addSourceURL(String sourceURL)
  {
    _sourceURLs.add(sourceURL);
  }

  /**
   * Get the description of the root entry for this package.
   * @return an entry description.
   */
  public ArchivedContents getContents()
  {
    return _contents;
  }

  /**
   * Set the contents.
   * @param contents Contents description.
   */
  public void setContents(ArchivedContents contents)
  {
    _contents=contents;
  }

  /**
   * Get the entries to delete.
   * @return the entries to delete.
   */
  public List<String> getEntriesToDelete()
  {
    return new ArrayList<String>(_toDelete);
  }

  /**
   * Add an entry to delete.
   * @param path Path to delete.
   */
  public void addEntryToDelete(String path)
  {
    _toDelete.add(path);
  }

  @Override
  public String toString()
  {
    return _reference.toString();
  }
}
