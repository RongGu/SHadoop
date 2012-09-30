package org.apache.hadoop.mapreduce.splitcache;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * A simple split cache mechanism for data reuse across multiple invocations 
 * of a job, or multiple jobs those use the same input data
 *   
 * @author yangxiaoliang
 *
 */
public class InputSplitCacheServer implements InputSplitCacheProtocol {

  private static final int SPLIT_CACHE_SIZE = 1024*1024*(64*10+32);
  
  private byte[] splitCache = null;
  private int freeIndex = 0;
  
  // split index table
  // file split => index in cache
  private Hashtable<String, Integer> splitIndex = null;
  
  @Override
  public long getProtocolVersion(String protocol, long clientVersion)
      throws IOException {
    return InputSplitCacheProtocol.VersionID;
  }

  @Override
  public CachedSplitRecordReader getCachedSplitRecordReader(FileSplit split,
      org.apache.hadoop.mapreduce.TaskAttemptContext taskContext) {
	  
	// TODO Auto-generated method stub
    return null;
  }

  public InputSplitCacheServer(){
    
  }
  
  public boolean loadSplit(FileSplit split, FileSystem fs) throws IOException{
    // if already cached
    if(this.splitIndex.containsKey(split.toString())){
      return true;
    }
    FSDataInputStream in = fs.open(split.getPath(), 64*1024);
    in.seek(split.getStart());
    if(in.read(this.splitCache, this.freeIndex, (int)split.getLength()) == -1){
      return false;
    }
    this.splitIndex.put(split.toString(), this.freeIndex);
    this.freeIndex += (int)split.getLength();
    return true;
  }

  private void initlize(){
    this.splitCache = new byte[SPLIT_CACHE_SIZE];
    this.splitIndex = new Hashtable<String, Integer>(11);
  }

}
