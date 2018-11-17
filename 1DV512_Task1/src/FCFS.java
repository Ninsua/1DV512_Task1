/*
 * File:	Process.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti & Elise Anjel
 * Date: 	November, 2018
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;

public class FCFS{

	// The list of processes to be scheduled
	public ArrayList<Process> processes;

	// Class constructor
	public FCFS(ArrayList<Process> processes) {
		this.processes = processes;
	}

	public void run() {
		//Sorts the list on Arrival Time.
		sortProcessOnAt();
		
		//FCFS simulation
		int listIndex = 0; //Used to keep track of incoming processes, SIMULATION ONLY
		ArrayDeque<Process> queue = new ArrayDeque<Process>();	//Process queue for incoming processes
		int cpuTime = 0;
		boolean running = true;
		while (running) {
			
			//Simulates incoming processes into the queue. This part is for simulation only.
			int nextProcessArrivalTime = processes.get(listIndex).arrivalTime;
			if (cpuTime >= nextProcessArrivalTime) {
				queue.add(processes.get(listIndex));
				listIndex++;
			}
			
			//Check queue for incoming processes. Services first process if queue is not empty.
			if (!queue.isEmpty()) {
				int at, bt, ct, tat, wt;
				Process p = queue.remove();
				
				at = p.getArrivalTime();
	
				//Simulates executing a process
				bt = runProcess(p); //Runs process, saves it's run time as burst time.
				cpuTime += bt;	//Simulates the CPU having run the process.
				
				//Calculates and sets times on process
				ct = cpuTime;
				tat = ct - at;
				wt = tat-bt;
				p.setCompletedTime(ct);
				p.setTurnaroundTime(tat);
				p.setWaitingTime(wt);
			}
			
			//If no process is in the queue.
			else {
				cpuTime++;
			}
			
			//Ends the simulation when there are no more simulated incoming processes
			if (listIndex == processes.size() && cpuTime > processes.get(listIndex-1).getArrivalTime()) {
				running = false;
			}
		}
		
		printGanttChart();
		printTable();
	}
	
	//Simulates running a process. Returns the process burst time for simulation purposes only.
	private int runProcess(Process p) {
		return p.getBurstTime();
	}

	//Prints the table
	public void printTable() {
		System.out.println("----------------------------------------");
		System.out.format("%-6s %-6s %-6s %-6s %-6s %-6s \n","PID","AT","BT","CT","TAT","WT");
		
		for (Process p : processes) {
			System.out.format("%-6s %-6s %-6s %-6s %-6s %-6s \n",
					p.getProcessId(),p.getArrivalTime(),p.getBurstTime(),p.getCompletedTime(),
					p.getTurnaroundTime(),p.getWaitingTime());
		}
		
		System.out.println("----------------------------------------");
	}

	//Prints the gantt chart
	public void printGanttChart() {
		int totalTime = processes.get(processes.size()-1).getCompletedTime();
		int multiplier = 4;
		
		String line = "====================";
		String space = "                    ";
		String idle = "**********************";
		line = line+line+line+line+line;
		space = space+space+space+space+space;
		line = line+line+line+line+line;
		space = space+space+space+space+space;
		idle = idle+idle+idle+idle+idle;
		
		int lineLength = totalTime*multiplier+processes.size()*4;
		System.out.format("%."+lineLength+"s\n",line);
		
		int prevCt = 0;
		for (Process p : processes) {
			int bt = p.getBurstTime();
			
			//Prints idle time
			if (p.getArrivalTime() > prevCt) {
				System.out.format("|%."+(((p.getArrivalTime()-prevCt)*(multiplier))-2)+"s|", idle);
			}
			
			//Prints process burst time
			System.out.format("|%."+(bt*multiplier/2)+"s", space);
			System.out.print("P"+p.getProcessId());
			System.out.format("%."+(bt*multiplier/2)+"s|", space);
			
			prevCt = p.getCompletedTime();
		}
		
		System.out.format("\n%."+lineLength+"s\n",line);
		
		//Prints first time
		System.out.print("0");
		
		prevCt = 0;
		int prevCtLength = 2;
		for (Process p : processes) {
			int bt = p.getBurstTime();
			int ct = p.getCompletedTime();
			int at = p.getArrivalTime();
			int atLength = 0;
			
			if (at > prevCt) {
				atLength = (int)(Math.log10(at)+1);
				System.out.format("%."+((at-prevCt)*(multiplier)+2-prevCtLength-atLength)+"s%d", space,at);
				atLength = (int)(Math.log10(at)-2);
			}
			
			System.out.format("%."+(bt*multiplier+4-prevCtLength+(atLength))+"s", space);
			System.out.format("%d", ct);
			
			prevCt = ct;
			prevCtLength = (int)(Math.log10(prevCt)+1);
		}
		
		System.out.print("\n");		
	}
	
	private void sortProcessOnAt() {
		processes.sort(new Comparator<Process>() {
			public int compare(Process p1, Process p2) {
				return p1.getArrivalTime() - p2.getArrivalTime();
			}
		});
	}
}
