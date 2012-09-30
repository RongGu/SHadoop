package org.apache.hadoop.mapreduce.splitcache;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.ipc.VersionedProtocol;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * A simple split cache mechanism for data reuse across multiple invocations 
 * of a job, or multiple jobs those use the same input data
 *   
 * @author yangxiaoliang
 * 2012.04.21
 *
 */
public interface SplitCacheProtocol extends VersionedProtocol {
	
  public static final long VersionID = 1L;

  /**
   * look up a Split in the cache 
   * 
   * @param split
   * @param conf
   * @return true, if the cache server contain the split, false, else
   * @throws IOException
   */
  public boolean lookupSplitInCache(FileSplit split, Configuration conf) 
      throws IOException;
  
  /**
   * read some bytes from the cache,
   * @param buf the buffer to put the bytes to
   * @param off offset in buf to put bytes from
   * @param split the Split which the bytes belong to  
   * @param start the start offset from the Split
   * @param len the length of bytes to read
   * @return the length of bytes been read
   */
  public BytesWritable read(FileSplit split, int start, int len)
      throws IOException;
  
}
