package scheduler;

import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.net.*;

public class SchedulerFrame extends JFrame implements ActionListener{
	Scheduler cpu;
	
	JCheckBox startCB;
	
	JLabel statusBar;
	
	JMenuBar menuBar;
	JMenu algorithmMenu, optionsMenu;
	JMenuItem rr,fcfs,speed;
	
	ClockPanel cpuTimePanel;
	
	int delay = 250; //miliseconds
	Timer timer;
	
	boolean frozen = true;
	
	JPanel queuePanel;
	
	public SchedulerFrame(){
		cpu = new Scheduler("jobQueue.txt");
		timer = new Timer(delay, this);
		timer.setCoalesce(false);
		timer.setInitialDelay(0);
		
		//setup frame
		setTitle("Scheduler Simulation");
		setSize(790,390);
		
		buildButtons();
		
		queuePanel = new JPanel();
		
		buildMenus();
		
		buildStatusPanels();
		
		fillQueuePanel();
		
		updateReadouts();
		
		Container masterPanel = getContentPane();
		masterPanel.setLayout(new BoxLayout(masterPanel,BoxLayout.Y_AXIS));
		
		JPanel topRow = new JPanel();
		topRow.setLayout(new BorderLayout());
		topRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel middleRow = new JPanel();
		middleRow.setLayout(new FlowLayout(FlowLayout.CENTER));
		middleRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel bottomRow = new JPanel();
		bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.Y_AXIS));
		bottomRow.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		topRow.add(queuePanel,"North");
		
		middleRow.add(cpuTimePanel);
		
		bottomRow.add(middleRow,"Center");
		bottomRow.add(startCB, "South");
		
		masterPanel.add(topRow);
		masterPanel.add(bottomRow);
		
		addWindowListener( 
				new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						System.exit(0);
					}
				}
			);
		
		setVisible(true);
		
	}
	
	void buildButtons(){
		startCB = new JCheckBox();
		startCB.addActionListener(this);
		startCB.setBorder(new EmptyBorder(0,0,0,0));
		startCB.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	
	void buildMenus(){
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//build Algorithm Menu
		algorithmMenu = new JMenu("Algorithm");
		
		rr = new JMenuItem("Round Robin");
		rr.addActionListener(this);
		algorithmMenu.add(rr);
		
		fcfs = new JMenuItem("FCFS");
		fcfs.addActionListener(this);
		algorithmMenu.add(fcfs);
		
		menuBar.add(algorithmMenu);
		
		//build Options Menu
		optionsMenu = new JMenu("Options");
		
		speed = new JMenuItem("Speed");
		optionsMenu.add(speed);
		
		menuBar.add(optionsMenu);
	}
	
	void buildStatusPanels(){
		statusBar = new JLabel("");
		statusBar.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		statusBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		cpuTimePanel = new ClockPanel("CPU");
		cpuTimePanel.setStats(0,0,0);
		cpuTimePanel.setToolTipText("The real time on CPU clock");
	}
	
	void fillQueuePanel(){
		//Vector v = cpu.getJobs();
		queuePanel.setBackground(Color.white);
		queuePanel.setOpaque(true);
		queuePanel.setSize(140,340);
		queuePanel.setPreferredSize(new Dimension(640,140));
		queuePanel.setMinimumSize(new Dimension(640,130));
		FlowLayout flay = new FlowLayout(FlowLayout.LEFT);
		queuePanel.setLayout(flay);
	}
	
	void updateReadouts(){
		cpuTimePanel.setStats( (int) cpu.getCurrentTime(), (int) cpu.getBusyTime(), (int) cpu.getIdleTime());
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == startCB){
			if(frozen == false){
				frozen = true;
				stopAnimation();
				startCB.setSelected(false);
			} else{
				frozen = false;
				startAnimation();
				startCB.setSelected(true);
			}
		}
		else if(e.getSource() == timer){
			if(cpu.nextCycle() == true){
				updateReadouts();
				
				if(cpu.getQuantumCounter()==9){
					ProcessPanel p = new ProcessPanel( cpu.getActiveProcess());
					queuePanel.add(p,"Left");
					p.setVisible(true);
				}
			}
			queuePanel.revalidate();
			resetQueuePanel();
		}
	}
	
	public void resetQueuePanel(){
	ProcessPanel p;
	int num = queuePanel.getComponentCount();
	for(int i=0; i < num;i++){
	    p = (ProcessPanel) queuePanel.getComponent(i);
	    p.setVisible(true);
	}
    }

	public synchronized void startAnimation(){
			if(!timer.isRunning()){
				timer.start();
			}
	}
	
	public synchronized void stopAnimation(){
		if(timer.isRunning()){
			timer.stop();
		}
	}
}