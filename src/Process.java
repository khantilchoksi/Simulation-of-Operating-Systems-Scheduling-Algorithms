package scheduler;

import java.io.*;

public class Process{
	long PID = 0;
	static long nextPID = 0;
	long burst = 0; 	// Remaining burst time 
	long initBurst = 0; 	//input
	long priority = 0;	//input
	long arrival = 0; 	//input
	long start = 0;
	long finish = 0;
	long wait = 0;
	long response = 0;
	long lifetime = 0; 	// ready+waiting+running 
	
	boolean arrived = false;
	boolean started = false;
	boolean finished = false;
	boolean active = false;
	
	Process(long b,long a, long p){
		nextPID++;
		PID = nextPID;
		burst = b;
		initBurst = burst;
		arrival = a;
		priority = p;
	}
	
	public synchronized void executing (long timeNow){
		active = true;
		if(timeNow == arrival){
			arrived = true;
		}
		
		if(burst == initBurst){
			started = true;
			start = timeNow;
			response = start - arrival;
		}
		
		burst--;
		lifetime++;
		
		if(burst ==0){
			finished = true;
			finish = timeNow;
		}
		
	}
	
	public synchronized void waiting(long timeNow){
		active = false;
		lifetime++;
		wait++;
		
		if(timeNow==arrival){
			arrived = true;
		}
	}
	
	public long getPID()	{ return PID;}
	public long getBurstTime() { return burst;}
	public long getInitBurstTime()	{ return initBurst;}
	public long getPriority()	{ return priority;}
	public long getArrivalTime()	{ return arrival;}
	public long getStartTime()	{ return start;}
	public long getFinishTime()	{ return finish;}
	public long getWaitTime()	{ return wait;}
	public long getResponseTime()	{ return response;}
	
	public boolean isArrived()	{ return arrived;}
	public boolean isStarted() { return started;}
	public boolean isFinished() { return finished;}
	public boolean isActive() {	return active;}
	
}