package scheduler;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
//import java.net.*;

public class ClockPanel extends JPanel{
	final static int width = 170, height = 80;
	
	JLabel timeLabel, idleLabel, busyLabel, timet, idle, busy;
	
	ClockPanel(String title){
		TitledBorder tBorder = BorderFactory.createTitledBorder(title);
		setBorder(tBorder);
		setLayout( new GridLayout(0,2));
		
		timeLabel = new JLabel("Time");
		timet = new JLabel(""+0);
		
		busyLabel = new JLabel("Busy");
		busy = new JLabel(""+0);
		
		idleLabel = new JLabel("Idle");
		idle = new JLabel(""+0);
		
		add(timeLabel);
		add(timet);
		
		add(busyLabel);
		add(busy);
		
		add(idleLabel);
		add(idle);
		
		setSize(width,height);
		setMinimumSize(new Dimension(width, height));
	}
	
	public void setStats(int t, int b, int i){
		timet.setText(Integer.toString(t));
		busy.setText(Integer.toString(b));
		idle.setText(Integer.toString(i));
	}
	
}