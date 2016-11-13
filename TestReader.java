package scheduler;

import java.io.*;
import java.util.*;


public class TestReader{
		
		//Vector allProcs = new Vector(10);
		String filename = "jobQueue.txt";
			
		String s="";
		long b=0,a=0,p=0;
		
		public void printProcesses(){
			
		try{
			BufferedReader input = new BufferedReader( new FileReader(filename));
				while( (s = input.readLine()) != null){
					StringTokenizer st = new StringTokenizer(s);
					b = Long.parseLong(st.nextToken());
					a = Long.parseLong(st.nextToken());
					p = Long.parseLong(st.nextToken());
					System.out.println("\n "+b+"	"+a+"	"+p);
					/*Process proc = new Process(b,a,p);
					allProcs.add(proc);*/
			}
		}	catch (FileNotFoundException fe){
			fe.printStackTrace();
		}	catch(IOException ie){
			ie.printStackTrace();
		}
		
		
		/*for(int i=0; i<allProcs.size(); i++){
				proc = (Process) allProcs.get(i);
				System.out.println("\n "+proc.getPID()+"	"+proc.getBurstTime()+"	"+proc.getArrivalTime()+"	"+proc.getPriority());
			}*/
			
		
		}
	
	public static void main(String args[]){
	
			TestReader tr = new TestReader();
			tr.printProcesses();
			
			System.out.println("\n Thank You! ");
	}
}