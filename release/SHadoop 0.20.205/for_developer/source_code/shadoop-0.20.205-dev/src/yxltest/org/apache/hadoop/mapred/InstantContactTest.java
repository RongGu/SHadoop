package org.apache.hadoop.mapred;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.mapred.InstantContactProtocol;
import org.apache.hadoop.mapred.TaskTracker.State;

public class InstantContactTest{
	
	public static void main(String[] args) {
		try {
//		    Server instantTaskTrackerServer = RPC.getServer(
//		    		new InstantContactTest(), 
//		    		"127.0.0.1", 9010, new Configuration());
//			instantTaskTrackerServer.start();
			InstantContactProtocol instantTaskTracker = 
					(InstantContactProtocol)RPC.getProxy(
					InstantContactProtocol.class, InstantContactProtocol.versionID, 
					new InetSocketAddress("127.0.0.1",9010), 
					new Configuration());
			TaskTracker.State state = instantTaskTracker.contactJobTracker();
			System.out.println(state);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
