package com.tat.data.entity;

import javax.persistence.Entity;

import com.tat.constants.GlobalConstants;

@Entity
public class Server implements GlobalConstants{

	
	private String serverName;
	private double angle;
	
	
	public Server(String ServerName){
		this.serverName = ServerName;
		this.angle = (ServerName.hashCode()/MAX_RADIUS)*DEGRE_360;
	}
	
	public Server(String ServerName, double angle){
		this.serverName = ServerName;
		this.angle = angle;
	}
	
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	
}
