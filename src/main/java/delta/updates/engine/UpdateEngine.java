package delta.updates.engine;

import java.io.File;

import org.apache.log4j.Logger;

import delta.common.utils.files.FilesDeleter;
import delta.updates.data.DirectoryDescription;
import delta.updates.data.DirectoryEntryDescription;
import delta.updates.data.EntryUtils;
import delta.updates.data.FileDescription;
import delta.updates.engine.operations.OperationType;
import delta.updates.engine.operations.UpdateOperation;
import delta.updates.engine.operations.UpdateOperations;
import delta.updates.engine.providers.FileProvider;
import delta.updates.utils.DescriptionBuilder;

/**
 * Update engine.
 * @author DAM
 */
public class UpdateEngine
{
  private static final Logger LOGGER=Logger.getLogger(UpdateEngine.class);

  private File _tmpDir;
  private File _toDir;
  private FileProvider _provider;

  /**
   * Constructor.
   * @param toDir Directory of data to update.
   * @param provider File provider.
   */
  public UpdateEngine(File toDir, FileProvider provider)
  {
    _toDir=toDir;
    _provider=provider;
    _tmpDir=new File(_toDir,"__tmp");
  }

  /**
   * Perform update.
   * @param target Target description.
   */
  public void doIt(DirectoryEntryDescription target)
  {
    // Update
    update(target);
    // Cleanup
    FilesDeleter deleter=new FilesDeleter(_tmpDir,null,true);
    deleter.doIt();
  }

  private void update(DirectoryEntryDescription target)
  {
    if (!_toDir.exists())
    {
      _toDir.mkdirs();
    }
    DescriptionBuilder builder=new DescriptionBuilder();
    DirectoryEntryDescription to=builder.build(_toDir);
    UpdateOperationsBuilder updatesBuilder=new UpdateOperationsBuilder();
    UpdateOperations operations=updatesBuilder.computeDiff(to,target);
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
          boolean ok=_provider.getFile((FileDescription)entry,_tmpDir);
          if (!ok)
          {
            LOGGER.error("Failed: "+path);
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
