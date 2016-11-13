package scheduler;

import java.io.*;
import java.util.*;
import java.text.*;

public class Scheduler{
	
	static final int RoundRobin = 1;
	static final int FCFS = 2;
	
	static final int DEF_PROC_COUNT = 5;
	
	long currentTime = 0;
	long idle = 0;
	long busy = 0;
	long quantum = 10;
	long quantumCounter = quantum;
	//long turnCounter = 0;
	long procsIn = 0;
	long procsOut = 0;	// no of completed jobs
	
	boolean preemptive = false;
	boolean priority = false;
	
	int algorithm = this.RoundRobin;
	
	Vector allProcs = new Vector(DEF_PROC_COUNT);
	Vector jobQueue = new Vector(DEF_PROC_COUNT);
	Vector readyQueue = new Vector(DEF_PROC_COUNT);	// Processes arrived and require cpu time
	
	Process activeJob = null;
	int activeIndex = -1;
	
	Scheduler(String filename){
		activeJob = null;
		Process proc = null;
		String s= null;
		long b=0,a=0,p=0;
		
		try{
			BufferedReader input = new BufferedReader( new FileReader(filename));
			while( (s = input.readLine()) != null){
				StringTokenizer st = new StringTokenizer(s);
				b = Long.parseLong(st.nextToken());
				a = Long.parseLong(st.nextToken());
				p = Long.parseLong(st.nextToken());
				
				proc = new Process(b,a,p);
				allProcs.add(proc);
			} 
		}	catch(FileNotFoundException fnfe){}
			catch (IOException ioe) {}
		LoadJobQueue(allProcs);
	}
	
	void schedule(){		// public ??
		switch(algorithm){
			case RoundRobin : 
				RunRoundRobin(readyQueue);
				break;
			case FCFS:
				RunFCFS(readyQueue);
				break;
		}
		Dispatch();
	}
	
	void Dispatch(){		// Run the acvtiveJob and wait the other jobs
		Process p= null;
		
		activeJob.executing(currentTime);
		
		for(int i=0; i< readyQueue.size(); ++i){
			p = (Process) readyQueue.get(i);
			if( p.getPID() != activeJob.getPID()){
				p.waiting(currentTime);
			}
		}
	}
	
	
	void RunRoundRobin(Vector jq){
		Process p = null;
		
		try{
			if( busy==0 || activeJob.isFinished() || quantumCounter==0 ){
				activeJob = findNextJob(jq);
				activeIndex = jq.indexOf(activeJob);
				
				quantumCounter = quantum;
			}
				quantumCounter--; 
		}   catch(NullPointerException npe){}
	}
	
	Process findNextJob(Vector que){
		Process p = null, nextJob = null;
		int index = 0;
		
		if(activeIndex >= (que.size() - 1) )
				index = 0;
		else if(activeJob!= null && activeJob.isFinished()){
			index = activeIndex;		// doubt ??
		}
		else{
			index = activeIndex+1;
		}
		
		nextJob = (Process) que.get(index);
		
		return nextJob;
	}

	void RunFCFS(Vector jq){
		Process p;
		
		try{
			if(busy==0 || activeJob.getBurstTime() == 0){
				activeJob = findEarliestJob(jq);
				activeIndex = jq.indexOf(activeJob);
			}
		} catch(NullPointerException npe){}
	}
	
	
	Process findEarliestJob(Vector que){
		Process p = null, earliest = null;
		long time = 0, arrTime=0;
		
		for(int i=0; i<que.size(); i++){
			p = (Process) que.get(i);
			time = p.getArrivalTime();
			if( (time < arrTime) || (i==0) ){
				arrTime = time;
				earliest = p; 
			}
		}
		return earliest;
	}
	
	
	public void LoadJobQueue(Vector jobs){
	Process p;
	//long arTime = 0;
	for(int i = 0 ; i < jobs.size() ; i++ ){
	    p  = (Process) jobs.get(i);
			/*arTime += p.getDelayTime();
			p.setArrivalTime(arTime);*/
	    jobQueue.add(p);
 	}
    }
	
	void PurgeJobQueue(){
	Process p;
	for(int i=0; i < jobQueue.size(); i++){
	    p = (Process) jobQueue.get(i);
	    if( p.isFinished() == true ){
		jobQueue.remove(i);
	    }
	}
    }
	
	void LoadReadyQueue(){		//Check for New Jobs
		Process p;
		
		for(int i=0; i< jobQueue.size(); i++){
			p = (Process) jobQueue.get(i);
			if(p.getArrivalTime() == currentTime){
				readyQueue.add(p);
				procsIn++;
			}
		}
		
	}
	
	void PurgeReadyQueue(){
		Process p;
		for(int i=0; i< readyQueue.size(); i++){
			p = (Process) readyQueue.get(i);
			if(p.isFinished() == true){
				readyQueue.remove(i);
				procsOut++;
			}
		}
		
	}
	
	/*public void simulate(){
		while( nextCycle() );
	}*/
	
	public boolean nextCycle(){
		boolean moreCycles = false;
		if(jobQueue.isEmpty()){
			moreCycles = false;
		} 
		else{
			
				// to add process at each cycle if arrived
			moreCycles = true;
			if(readyQueue.isEmpty()){
				idle++;
			}
			else{
				schedule();
				busy++;
				cleanUp();	// to check whether the has been finished or not
			}
			
			currentTime++;
			LoadReadyQueue();
		}
		return moreCycles;
	}
	
	void cleanUp(){
		PurgeJobQueue();
		PurgeReadyQueue();
	}
	
	
	public void setAlgorithm(int algo){
		algorithm = algo;
	}
	public int getAlgorithm()	{ return algorithm;}
	
	public long getIdleTime()	{ return idle;}
	
	public long getCurrentTime()	{ return currentTime;}		// i.e. total time
	
	public long getBusyTime()	{ return busy;}
	
	public long getQuantum()	{ return quantum;}
	
	public long getQuantumCounter() { return quantumCounter;}
	
	public long getProcsIn()	{ return procsIn;}
	
	public long getProcsOut()	{ return procsOut;}
	
	public Process getActiveProcess()	{ return activeJob;}
	
	public Vector getJobs()	{ return allProcs;}
	public Vector getReadyJobs()	{ return readyQueue;}
	
	public String getAlgorithmName(){
		String s="";
		
		switch(algorithm){
			case RoundRobin:
				s = "Round Robin";
				break;
			case FCFS:
				s="First Come First Serve";
				break;
			default:
				break;
		}
		return s;
	}
}