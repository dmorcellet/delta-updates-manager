package delta.updates.engine;

import java.io.File;

import delta.common.utils.files.FilesDeleter;
import delta.downloads.Downloader;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.SoftwarePackage;
import delta.updates.engine.operations.OperationType;
import delta.updates.engine.operations.UpdateOperation;
import delta.updates.engine.operations.UpdateOperations;
import delta.updates.engine.providers.HttpProvider;
import delta.updates.utils.DescriptionBuilder;
import delta.updates.utils.UpdateOperationsBuilder;

/**
 * Update engine.
 * @author DAM
 */
public class UpdateEngine
{
  private Downloader _downloader;
  private HttpProvider _provider;
  private File _tmpDir;
  private File _toDir;

  /**
   * Constructor.
   * @param toDir Directory of data to update.
   * @param rootUrl Source URL for updates.
   */
  public UpdateEngine(File toDir, String rootUrl)
  {
    _toDir=toDir;
    _tmpDir=new File(_toDir,"__tmp");
    _downloader=new Downloader();
    _provider=new HttpProvider(_downloader,rootUrl,_tmpDir);
  }

  /**
   * Perform update.
   */
  public void doIt()
  {
    // Fetch package description
    SoftwarePackage softwarePackage=getSoftwarePackage();
    // Update
    update(softwarePackage);
    // Cleanup
    FilesDeleter deleter=new FilesDeleter(_tmpDir,null,true);
    deleter.doIt();
  }

  private SoftwarePackage getSoftwarePackage()
  {
    // TODO Otherwise
    File from=new File("D:/shared/damien/dev/lotrocompanion/releases/14.0/LotRO Companion/app");
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription files=builder.build(from);
    SoftwarePackage softwarePackage=new SoftwarePackage();
    softwarePackage.setFiles(files);
    return softwarePackage;
  }

  private void update(SoftwarePackage target)
  {
    if (!_toDir.exists())
    {
      _toDir.mkdirs();
    }
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription to=builder.build(_toDir);
    UpdateOperationsBuilder updatesBuilder=new UpdateOperationsBuilder();
    UpdateOperations operations=updatesBuilder.computeDiff(to,target.getFiles());
    handleOperations(operations);
  }

  private void handleOperations(UpdateOperations operations)
  {
    getNewFiles(operations);
    applyUpdates(operations);
  }

  private void getNewFiles(UpdateOperations operations)
  {
    for(UpdateOperation operation : operations.getOperations())
    {
      OperationType type=operation.getOperation();
      if ((type==OperationType.ADD) || (type==OperationType.UPDATE))
      {
        DirectoryEntryDescription entry=operation.getEntry();
        String path=EntryUtils.getPath(entry);
        if (entry instanceof DirectoryDescription)
        {
          File newDir=new File(_tmpDir,path);
          newDir.mkdirs();
        }
        else
        {
          boolean ok=_provider.getFile(entry);
          if (!ok)
          {
            System.out.println("Failed: "+path);
          }
        }
      }
    }
  }

  private void applyUpdates(UpdateOperations operations)
  {
    for(UpdateOperation operation : operations.getOperations())
    {
      DirectoryEntryDescription entry=operation.getEntry();
      String path=EntryUtils.getPath(entry);
      OperationType type=operation.getOperation();
      if ((type==OperationType.UPDATE) || (type==OperationType.ADD))
      {
        // Move file/directory
        File from=new File(_tmpDir,path);
        File to=new File(_toDir,path);
        from.renameTo(to);
      }
      else if (type==OperationType.DELETE)
      {
        // Delete file/directory
        File to=new File(_toDir,path);
        to.delete();
      }
    }
  }
}
