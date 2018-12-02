package com.tat;

import java.util.Comparator;

import com.tat.data.entity.Server;

public class ServerComparator implements Comparator<Server>{
	@Override
	public int compare(Server server1, Server server2) {
		  if(server1.getAngle() > server2.getAngle() ) return 1;
	  	    else if(server1.getAngle() == server2.getAngle() ) return 0;
		    else  return -1;
	}

}
