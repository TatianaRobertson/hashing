package com.tat.data.entity;

public class ServerReplicaMapping {
  
	private Server trueServer;
	private Server serverReplica;
	public ServerReplicaMapping(Server trueServer, Server serverReplica) {
		this.trueServer = trueServer;
		this.serverReplica = serverReplica;
	}
	public Server getTrueServer() {
		return trueServer;
	}
	public void setTrueServer(Server trueServer) {
		this.trueServer = trueServer;
	}
	public Server getServerReplica() {
		return serverReplica;
	}
	public void setServerReplica(Server serverReplica) {
		this.serverReplica = serverReplica;
	}
	
	
	
	
	
	
	  
}
