package org.apache.hadoop.mapred;

import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * created at 2012.04.10
 * @author yangxiaoliang
 *
 */
public interface InstantContactProtocol extends VersionedProtocol {
  public static final long versionID = 1L; 
  public TaskTracker.State contactJobTracker() throws Exception;
}
