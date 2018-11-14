/*
 * File:	Process.java
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti
 * Date: 	November, 2018
 */

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
		int currentTime;
		
		//Sorts the list on Arrival Time.
		sortProcessOnAt();
		
		currentTime = 0;
		for (Process p : processes) {
			if (p.getArrivalTime() > currentTime)
				currentTime = p.getArrivalTime();
			
			currentTime += p.getBurstTime();
			
			//Calculates Sets CT, TAT and WT
			p.setCompletedTime(currentTime);
			p.setTurnaroundTime(p.getCompletedTime() - p.getArrivalTime());
			p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
		}
		
		
		printGanttChart();
		printTable();
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
