This file is write to people who want to use SHadoop.

Please follow the next steps:
Step 1:Set up a Hadoop 0.20.205 cluster.If you've alrady have one, just skip this step.
Step 2:Make sure that the Hadoop cluster has been shutdowned now.
Step 3:Move the hadoop-core-0.20.205.1-shadoop.jar file in this directory to your "HADOOP_HOME/share/hadoop/" directory,
NOT "HADOOP_HOME/" directory.Remove the orignal hadoop-core-0.20.205.0.jar in "HADOOP_HOME/share/hadoop/" directory. 
Make sure that this change has been made on each node in the cluster.
Step 4.Restart the Hadoop (now SHadoop) cluster, it will work.

Note:All the source code and jars downloaded from this website can only be used for study or reasearch. 
If you want to use SHadoop purpose, please also contact us first.

contact email:gurongwalker at gmail dot com
