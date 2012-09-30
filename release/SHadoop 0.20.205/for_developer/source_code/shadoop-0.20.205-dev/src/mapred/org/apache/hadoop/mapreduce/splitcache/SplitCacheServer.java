package org.apache.hadoop.mapreduce.splitcache;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import org.apache.commons.logging.LogFactory;

/**
 * A simple split cache mechanism for data reuse across multiple invocations 
 * of a job, or multiple jobs those use the same input data
 *   
 * @author yangxiaoliang
 * 2012.04.21
 *
 */
public class SplitCacheServer implements SplitCacheProtocol {

  private static final int SPLIT_CACHE_SIZE = 1024*1024*(64*10+32);
  
  private byte[] splitCache = null;
  private int freeIndex = 0;
  
  private byte[] readBuf = null;
  
  private static final Log LOG = LogFactory.getLog(SplitCacheServer.class);
  
  // split index table
  // file split => index in cache
  private Hashtable<String, Integer> splitIndex = null;
  
  @Override
  public long getProtocolVersion(String protocol, long clientVersion)
      throws IOException {
    return SplitCacheProtocol.VersionID;
  }

  /**
   * try to look up a Split in the cache 
   * if a split is not in the cache, firstly try to load it
   * @param split
   * @param conf
   * @return true, if the cache server contain the split, false, else
   * @throws IOException
   */
  @Override
  public boolean lookupSplitInCache(FileSplit split, Configuration conf) 
      throws IOException {
//    LOG.info("lookupSplitInCache() called");
	  if(this.splitIndex.containsKey(split.toString())){
	    return true;
	  }else if (canLoadSplit(split)){
	    if(loadSplit(split, FileSystem.get(conf))){
	      return true;
	    }
	  }
	  
    return false;
  }

  private boolean canLoadSplit(FileSplit split) {
    if(this.splitCache.length - split.getLength() >= 0){
      return true;
    }
    return false;
  }

  public SplitCacheServer(){
    //TODO:
    initialize();
  }
  
  /**
   * read some bytes from the cache,
   * @param buf the buffer to put the bytes to
   * @param off offset in buf to put bytes from
   * @param split the Split which the bytes belong to  
   * @param start the start offset from the Split
   * @param len the length of bytes to read
   * @throws Exception 
   */
  @Override
  public BytesWritable read(FileSplit split, int start, int len)
      throws IOException 
      {
    //TODO:
    // check whether a valid read request
    if (split == null) {
      throw new NullPointerException();
    } else if (start < 0 || len < 0 || len+start > split.getLength()) {
      throw new IOException("reading index out of split range");
    } else if (len == 0) {
        return new BytesWritable();
    } 
    if (!this.splitIndex.containsKey(split.toString())){
      throw new IOException("Split["+split.toString()+"] is not in cache");
    }
    
    // copy bytes to buf
    int splitStart = this.splitIndex.get(split.toString());
    if (this.readBuf == null) {
      this.readBuf = new byte[len];
    } else if(this.readBuf.length < len){
      this.readBuf = new byte[len];
    }
    
    for(int i =0; i<len; i++){
      this.readBuf[i] = this.splitCache[splitStart+start+i];
    }
    BytesWritable bytesWritable = new BytesWritable();
    bytesWritable.set(this.readBuf, 0, len);
    return bytesWritable;
  }
  
  private boolean loadSplit(FileSplit split, FileSystem fs) throws IOException{
    // if already cached
    long startTime = System.currentTimeMillis();
    if(this.splitIndex.containsKey(split.toString())){
      return true;
    }
    LOG.info("lookupSplitInCache() called");
    FSDataInputStream in = fs.open(split.getPath(), 64*1024);
    in.seek(split.getStart());
    if(in.read(this.splitCache, this.freeIndex, (int)split.getLength()) == -1){
      in.close();
      return false;
    }
    in.close();
    int i = 0;
//    while(i++ <= Math.min(200, (int)split.getLength())){
//      System.out.print(this.splitCache[i]);
//    }
    this.splitIndex.put(split.toString(), this.freeIndex);
    this.freeIndex += (int)split.getLength();
    LOG.info("load time cost: "+(System.currentTimeMillis()-startTime));
    return true;
  }

  private void initialize(){
    this.splitCache = new byte[SPLIT_CACHE_SIZE];
    this.splitIndex = new Hashtable<String, Integer>(11);
  }
  
  public static void main(String[] args) {
    SplitCacheServer cacheServerObj = new SplitCacheServer();
    Configuration conf = new Configuration(false);
    try{
      conf.addResource("core-site.xml");
      Server splitCacheServer = RPC.getServer(
          cacheServerObj, "192.168.1.22", 10010, 
          conf);
      splitCacheServer.start();
    } catch(UnknownHostException e){
      e.printStackTrace();
    } catch(IOException e){
      e.printStackTrace();
    }finally {
      //TODO:?
    }
  }


}
