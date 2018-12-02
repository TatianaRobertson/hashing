package com.tat.data.entity;

import javax.persistence.Entity;

import com.tat.constants.GlobalConstants;

@Entity
public class StoredObject implements GlobalConstants{

  private String objectKey;
  private double hachCode;
  private String machineName;
  private double angle;
  
  public StoredObject(String objectKey){
	  this.objectKey = objectKey;
	  this.hachCode = objectKey.hashCode();
	  this.angle =Math.round((this.hachCode/MAX_RADIUS)*DEGRE_360);
  }
  
  public StoredObject(String objectKey, double angle){
	  this.objectKey = objectKey;
	  this.hachCode = objectKey.hashCode();
	  this.angle =angle;
  }
  
  public StoredObject(){
  }
  
  public double getHachCode() {
	return hachCode;
  }

  public void setHachCode(double hachCode) {
	this.hachCode = hachCode;
  }
  
  public String getObjectKey() {
	return objectKey;
  }
  
  public void setObjectKey(String objectKey) {
	this.objectKey = objectKey;
  }

  public String getMachineName() {
	return machineName;
  }

  public void setMachineName(String machineName) {
	this.machineName = machineName;
  }

  public double getAngle() {
	return angle;
  }

  public void setAngle(double angle) {
	this.angle = angle;
  }
  
}
