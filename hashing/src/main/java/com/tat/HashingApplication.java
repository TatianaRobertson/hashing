package com.tat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.tat.data.entity.Server;
import com.tat.data.entity.ServerReplicaMapping;
import com.tat.data.entity.StoredObject;

@SpringBootApplication
public class HashingApplication {
	static List<StoredObject> objects  = new ArrayList<StoredObject>();
	static List<Server> servers  = new ArrayList<Server>();
	static List<ServerReplicaMapping> replicaMappings = new ArrayList<ServerReplicaMapping>();
	
	public static void main(String[] args) {
		//SpringApplication.run(HashingApplication.class, args);

		//Do the initial setup of random objects and 3 servers(A,B,C) to start with. 
		//Each object has a string value, hashcode and calculated angle from 0 to 360 degree. 
		//The angle will determine where in the circle the object will fall 
		objects = populateInitialNodes();
        servers = populateInitialServers();

        //print the initial setup
        showResult("Object list",objects);
        showServerList("Server list",servers);
       
        
        /*assign objects to the servers.
        servers has a angle property that determine where the server on the circle. 
        for the object to be assign to the server: the objects angle has to be less than the server angle, but more than the angle of the previous server.
        if the angle of the object = the angle of the server, that this object belong to this server. */
        assingObjects(objects, servers);
        showResult("Assign objects to servers",objects);
       
        
    	/* Adding a new element into the object list and assign it to the nearest server*/
        StoredObject newObj = new StoredObject("new");	
        addObject(objects, newObj);
		showResult("add new Object into the list of objects "+newObj.getObjectKey(),objects);
        
        
        
        /* Add server and reassign the Objects that will change*/
        Server newServer = new Server("D",10);
  	    addServer(objects, servers, newServer);
  	    showResult("add new server "+newServer.getServerName(),objects);
        
  	    
        /* Remove server and reassign the object that used to belong to this server */
        Server serverToRemove = null;
        for(Server server : servers){
        	if(server.getServerName().equals("C")){
        		serverToRemove = server;
        	}
        }
  	    removeServer(objects, servers, serverToRemove);
        showResult("remove the server "+serverToRemove.getServerName(),objects);
            
        
        
        /*Add Replicas to the newly created server*/ 
        showServerList("list of servers before addign replicas ",servers);
        newServer = new Server("F",10);
        addReplicaServers(newServer,60,7,servers, objects);
        showServerList("list of servers after adding replica for "+newServer.getServerName(),servers);
        showResult("Objects after adding replica for "+newServer.getServerName(),objects);
        
        
        /*Add Replicas to theexisting server from the list*/ 
        Server serverToReplicate = null;
        for(Server server : servers){
        	if(server.getServerName().equals("A")){
        		serverToReplicate = server;
        	}
        }
        addReplicaServers(serverToReplicate,10,7, servers, objects);
        showServerList("Server list Added replicas to "+serverToReplicate.getServerName(),servers);
        showResult("objects after adding replicas to "+serverToReplicate.getServerName(),objects);
        
        /* show the objects assigne servers and what this possibly a replica server true name is */
        showTrueServers(objects,servers);
  
	}
	
	private static void showTrueServers(List<StoredObject> objects, List<Server> servers) {
		 for(StoredObject obj : objects){
			String machineName = obj.getMachineName();
			Server servToCheck = null;
			Server trueServer;
			String trueServerName = null; 
			for (Server server: servers){
				if(server.getServerName().equals(machineName)) 
			 		servToCheck = server;
			}
			for (ServerReplicaMapping ServerMap : replicaMappings){
			  	if(ServerMap.getTrueServer().equals(servToCheck)){
			  		trueServer = ServerMap.getTrueServer();
			   		trueServerName = trueServer.getServerName();
			   	}
			   	else if (ServerMap.getServerReplica().equals(servToCheck)){
			   		trueServer = ServerMap.getTrueServer();
			   		trueServerName = trueServer.getServerName();
			   	}
			}
			System.out.println(obj.getObjectKey()+"\t hashCode="+obj.getHachCode()+"\t angle="+obj.getAngle()+"\t obj.getMachineName()="+obj.getMachineName()+" true ServerName="+trueServerName);
		    }
	}

	private static void showServerList(String message, List<Server> servers) {
		  System.out.println("_____________"+message+"_____________");
	        for(Server server : servers){
	        	System.out.println(server.getServerName()+"\t angle="+server.getAngle());
	        }
		
	}

	private static void showResult(String message, List<StoredObject> objects) {
		 System.out.println("_______________"+message+"_____________");
        for(StoredObject obj : objects){
			System.out.println(obj.getObjectKey()+"\t hashCode="+obj.getHachCode()+"\t angle="+obj.getAngle()+"\t obj.getMachineName()="+obj.getMachineName());
		}
		
	}

	/*Add new server to the list, sort the list. Find the next element on the list, that will be the server with elements we will reassign  */
	private static void addServer(List<StoredObject> objects, List<Server> servers, Server newServer) {
		servers.add(newServer);
		Collections.sort(servers, new ServerComparator());
		//find a server which elements we want to reassign
		// if we adding the server to the end of the array (close to 360 degree angle) then next server(the one we want to reassign) would be the first server.
		int indx;
		if(servers.indexOf(newServer) == servers.size()-1)
		   indx = 0;
		else
		   indx= servers.indexOf(newServer)+1;
		for (StoredObject object : objects){
			if(object.getMachineName().equals(servers.get(indx).getServerName())){
				object.setMachineName(findClosestServer(object, servers));
			}
		}
	}


	/* remove server from the list of servers, reassign objects that used to belong to this server*/
	private static void removeServer(List<StoredObject> objects, List<Server> servers, Server serverToRemove) {
		servers.remove(serverToRemove);
		for (StoredObject object : objects){
			if(object.getMachineName().equals(serverToRemove.getServerName())){
				object.setMachineName(findClosestServer(object, servers));
			}
		}
		
	}

    /*add object to the object list and find which server it element will belong to */ 
	private static void addObject(List<StoredObject> objects, StoredObject newObj) {
		objects.add(newObj);
		newObj.setMachineName(findClosestServer(newObj, servers));
	}


    /* initial list population*/  
	private static  List<StoredObject> populateInitialNodes() {
		objects.add(new StoredObject("a"));
		objects.add(new StoredObject("zzz"));
		objects.add(new StoredObject("on"));
		objects.add( new StoredObject("cat"));
		objects.add( new StoredObject("bob"));
		objects.add( new StoredObject("cut"));
		objects.add( new StoredObject("rum"));
		objects.add( new StoredObject("at"));
		objects.add( new StoredObject("up"));
		objects.add( new StoredObject("t"));
		objects.add( new StoredObject("az"));
		objects.add( new StoredObject("aa9"));
		objects.add( new StoredObject("aaa"));
		objects.add( new StoredObject("zz"));
		objects.add( new StoredObject("zz9"));
		objects.add( new StoredObject("a11"));
		return objects;
	}

	/* initial server population */
	private static List<Server> populateInitialServers(){
		 servers.add(new Server("A",0));
	     servers.add(new Server("B",120));
	     servers.add(new Server("C",300));
		return servers;
	}

	/* Add the replicas element for the server, the numberOfReplicas will be inserted into the circle angleDifference degree between them starting from the angle of the server to replicate. 
	  assuming that replica servers will be called serverName||REPLICA_NUMBER 
	  add mapping between true server and replicas into the replicaMappings array*/ 
	private static void addReplicaServers(Server server, double angleDifference, int numberOfReplicas, List<Server> servers, List<StoredObject> objects){
		//found the angle of the server we want to replicate
		Server replicaServer = null;
		double newAngle = server.getAngle();
		for (int i=0; i<numberOfReplicas; i++ ){
			newAngle = newAngle + angleDifference;
			if(newAngle > 360){
				System.out.println("Ups... trying to assign the angle that is grater that 360 degrees");
				break;
			}else{
				replicaServer= new Server(server.getServerName()+""+i,newAngle);
				replicaMappings.add(new ServerReplicaMapping(server,replicaServer));
				servers.add(replicaServer);
			}
		}
		Collections.sort(servers, new ServerComparator());
		assingObjects(objects,servers);
	}

	/* assign the machineNumber(the server name ) to all objects in the objects list */
	private static void assingObjects(List<StoredObject> objects, List<Server> servers) {
		Collections.sort(servers, new ServerComparator());
		for(StoredObject obj : objects){
			obj.setMachineName(findClosestServer(obj,servers));
		}
		
	}
	
	
	/* Find the closest server for each element and assign machineNumber of this server to the element 
	   Rules: if the angle of the object equals angle of the server -> this object belong to this server
	          the angle of the object is smaller than the angle of the server it assigned to, but the angle of the object is bigger than the previous server in the circle.
	          if there is no servers with the angle grater than the object angle than assign this object to the first server(smallest angle)
	 */
	static String findClosestServer(StoredObject obj, List<Server> servers){
		if(obj.getAngle()>servers.get(servers.size()-1).getAngle() && obj.getAngle() < 361 ){
			return servers.get(0).getServerName();
		}
	    Server servCur = null;
	    Server servPrev = null;
		for (int i= 0; i<servers.size(); i++){
			servCur = servers.get(i);
			if (i != 0)
			  servPrev = servers.get(i-1);
			else 
			  servPrev = servers.get(servers.size()-1);
			if (obj.getAngle() == servCur.getAngle()){
				return servCur.getServerName();
			}else if(obj.getAngle() < servCur.getAngle() && obj.getAngle() >servPrev.getAngle()){
				return servCur.getServerName();
			}
		}
		return null;
	}
	
	
}
