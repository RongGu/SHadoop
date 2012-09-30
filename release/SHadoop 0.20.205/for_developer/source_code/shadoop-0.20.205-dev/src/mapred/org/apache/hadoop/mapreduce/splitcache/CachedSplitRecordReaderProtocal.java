/**
 * 
 */
package org.apache.hadoop.mapreduce.splitcache;

import java.io.IOException;

import org.apache.hadoop.ipc.VersionedProtocol;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * 
 * @author yangxiaoliang
 * @created 2012.04.21
 */
public interface CachedSplitRecordReaderProtocal<KEYIN, VALUEIN> extends VersionedProtocol{

	 /**
	   * Called once at initialization.
	   * @param split the split that defines the range of records to read
	   * @param context the information about the task
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  void initialize(InputSplit split,
	                                  TaskAttemptContext context
	                                  ) throws IOException, InterruptedException;

	  /**
	   * Read the next key, value pair.
	   * @return true if a key/value pair was read
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  boolean nextKeyValue() throws IOException, InterruptedException;

	  /**
	   * Get the current key
	   * @return the current key or null if there is no current key
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  KEYIN getCurrentKey() throws IOException, InterruptedException;
	  
	  /**
	   * Get the current value.
	   * @return the object that was read
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  VALUEIN getCurrentValue() throws IOException, InterruptedException;
	  
	  /**
	   * The current progress of the record reader through its data.
	   * @return a number between 0.0 and 1.0 that is the fraction of the data read
	   * @throws IOException
	   * @throws InterruptedException
	   */
	  float getProgress() throws IOException, InterruptedException;
	  
	  /**
	   * Close the record reader.
	   */
	  void close() throws IOException;
}
