/**
 * 
 */
package org.apache.hadoop.mapreduce.splitcache;

import org.apache.hadoop.ipc.VersionedProtocol;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * @author yangxiaoliang
 *
 */
public interface InputSplitCacheProtocol extends VersionedProtocol {
	
  public static final long VersionID = 1L;

  public CachedSplitRecordReader getCachedSplitRecordReader(FileSplit split,
      org.apache.hadoop.mapreduce.TaskAttemptContext taskContext);
}
