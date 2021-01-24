package delta.updates.utils;

import java.util.ArrayList;
import java.util.List;

import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;

/**
 * Tool class to build a tree display of entry descriptions.
 * @author DAM
 */
public class DescriptionTreeDisplay
{
  /**
   * Build a displayable tree for the given entry.
   * @param entry Entry to use.
   * @return A displayable list of string.
   */
  public List<String> buildTreeDisplay(DirectoryEntryDescription entry)
  {
    List<String> ret=new ArrayList<String>();
    handleEntry(ret,0,entry);
    return ret;
  }

  private void handleEntry(List<String> output, int indentation, DirectoryEntryDescription entry)
  {
    String display=entry.toString();
    output.add(getLine(display,indentation));
    if (entry instanceof DirectoryDescription)
    {
      DirectoryDescription directory=(DirectoryDescription)entry;
      for(DirectoryEntryDescription childEntry : directory.getEntries())
      {
        handleEntry(output,indentation+1,childEntry);
      }
    }
  }

  private String getLine(String display, int indentation)
  {
    if (indentation==0)
    {
      return display;
    }
    StringBuilder sb=new StringBuilder();
    for(int i=0;i<indentation;i++)
    {
      sb.append('\t');
    }
    sb.append(display);
    return sb.toString();
  }
}
