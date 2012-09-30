package org.apache.hadoop.mapreduce.splitcache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;

public class SplitCacheServerTest {

  /**
   * @param args
   */
  public static void main(String[] args) {
    SplitCacheProtocol serverProxy = null;
    try {
      Configuration conf = new Configuration();
      conf.addResource("test-core-site.xml");
//      conf.set("fs.default.name", "hdfs://192.168.1.254:54310");
      System.out.println("hdfs: "+conf.get("fs.default.name"));
      
      System.out.println("connecting server...");
      serverProxy = (SplitCacheProtocol) RPC.getProxy(
          SplitCacheProtocol.class, 
          SplitCacheProtocol.VersionID, new InetSocketAddress("192.168.1.22",10010),
          conf);
      

      
      FileSystem fs = FileSystem.get(conf);
      
      Path file = new Path("/user/yangxiaoliang/blast/data/nt.seq"); 
          //new Path(args[0]);
      
      
      long len = conf.getLong("mapred.max.split.size", 64*1024*1024L);
      len = Math.min(len, fs.getFileStatus(file).getLen());
      BlockLocation[] blks = fs.getFileBlockLocations(
          fs.getFileStatus(file), 0L, len);
      String[] locs = new String[blks.length];
      FileSplit split = new FileSplit(file, 0L, len, locs);
      
      long start1 = System.currentTimeMillis();
      boolean inCache = serverProxy.lookupSplitInCache(split, conf);
//      System.out.println(inCache);
      int step = 1024*1024;
      int records = 0;
      if(inCache){
        for(int i=0 ; i<split.getLength(); i+=step, records++){
//          BytesWritable bytes = serverProxy.read(split, i, 
//              step <= (int)split.getLength()- i ? step: (int)split.getLength()-i );
//          System.out.println("length: "+bytes.getLength());;
        }
      }
      long end1 = System.currentTimeMillis();
      
      
      long start2 = System.currentTimeMillis();
      FSDataInputStream in = fs.open(split.getPath());
      in.seek(split.getStart());
      byte[] buf = null;
      for(int i2=0; i2<split.getLength(); i2+=step){
        if(buf == null){
          buf = new byte[step];
        } else if(buf.length < step){
          buf = new byte[step];
        }
        in.read(buf,0,step <= (int)split.getLength()- i2 ? 
            step : (int)split.getLength()-i2 );
      }
      
      in.close();
      
      long end2 = System.currentTimeMillis();
      
      System.out.println("socket read time cost:"+(end1 - start1)+" ");// +
//          (end1-start1)/records +"per read");
      System.out.println("hdfs read time cost:"+(end2 - start2));//+" "
//          +(end2-start2)/records +"per read");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if(serverProxy != null){
        RPC.stopProxy(serverProxy);
      }
    }

  }

}
