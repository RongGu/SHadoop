SHadoop
=======

Introduction:

SHadoop is an performance optimized version of the standard Hadoop. It aims at shortening the execution time cost of MapReduce jobs, especially for short jobs.Experimental results show that, compared with Hadoop, SHadoop can reduce execution time by around 30% for different MapReduce benchmark jobs, without losing scalability and speedup.Moreover, SHadoop is seamless compatible with Hadoop, without adopting any other thrid-part software libraries or hardware accelerators. Thus, it's simple to use and can well support the existing programs and applications built on top of Hadoop.

For users:

As we have said that SHadoop is totally compatible with Hadoop, it is quite convenient to switch from Hadoop to SHadoop.If you already have a Hadoop cluster, you can simply download the optimized Hadoop core jar from release folder in this website and replace the corresponding jars in Hadoop (see the readme_for_users.txt). After restart the Hadoop (now SHadoop) cluster, the you will exeperice a faster MapReduce execution speed:) Note that, this project is now just a research work, so we only implemented our optimizations to one Hadoop version, namely Hadoop 0.20.205 (later becomes the Hadoop 1.0), at this moment. However,our optimization ideas can also works for different version Hadoop in principle. For now, the SHadoop jar is designed to works well for Hadoop 0.20.205. It has been tested under a cluster of 36 nodes with 288 cores.Some users report that it also works for Hadoop 0.20.2x series and Hadoop 1.0. However, we recommend you to use Hadoop 0.20.205 for SHadoop.

For researchers and developers:

Generally, SHadoop improves MapReduce performance by optimizing the job/task execution mechanism in standard Hadoop.In this version, we propose two major optimizations to the MapReduce job/task execution mechanisms. First, we optimize the setup and cleanup tasks of a MapReduce job to reduce the time cost during the initialization and termination stages of the job; Second, instead of adopting the loose heartbeat-based communication mechanism to transmit all messages between the JobTracker and TaskTrackers, we add an instant messaging communication mechanism for accelerating performance-sensitive task scheduling and execution messages between the JobTracker and TaskTrackers. 



If you have some problems or suggestions about SHadoop, please feel free to contact us.May you enjoy this:)

Note: All the source code and jars downloaded from this website can only be used for study or reasearch. If you want to use SHadoop purpose, please also contact us first.

contact email:gurongwalker at gmail dot com
