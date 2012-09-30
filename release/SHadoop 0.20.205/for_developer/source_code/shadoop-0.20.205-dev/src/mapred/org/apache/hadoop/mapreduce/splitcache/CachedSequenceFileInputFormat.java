package org.apache.hadoop.mapreduce.splitcache;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class CachedSequenceFileInputFormat extends CachedFileInputFormat {

  @Override
  public List getSplits(JobContext context) throws IOException,
      InterruptedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public RecordReader createRecordReader(InputSplit split,
      TaskAttemptContext context) throws IOException, InterruptedException {
    // TODO Auto-generated method stub
    return null;
  }

}
