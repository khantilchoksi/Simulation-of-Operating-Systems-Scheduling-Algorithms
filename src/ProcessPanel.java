package scheduler;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
//import java.net.*;

public class ProcessPanel extends JPanel{
	Process proc;
	
	static final int PPWIDTH = 10;
	static final int PPHEIGHT = 115;
	static final int BARHEIGHT = 100;
	
	Color lblColor = Color.black, busyColor = Color.blue, idleColor = Color.red;
	Color burstColor,
	initBurstColor=Color.darkGray;
	
	JLabel procIdLabel;
	
	boolean showHidden = false;
	
	ProcessPanel(Process p){
		proc = p;
		initPanel();
	}
	
	void initPanel(){
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setLayout(new BorderLayout());
		
		procIdLabel = new JLabel(""+ (int)proc.getPID());
		procIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		setSize(PPWIDTH,PPHEIGHT);
		setBackground(Color.white);
		setOpaque(true);
		add(procIdLabel,"South");
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int width =0;
		width = (int) PPWIDTH-2; 	//why??
		
		procIdLabel.setForeground(Color.black);
		g.setColor(Color.black);
		g.drawRect(0,BARHEIGHT-50,width,50);
		//System.out.println("\n Rect Drawn");
		g.setColor(Color.red);
		
		if(proc==null){		// think
			g.setColor(idleColor);	
		} else{
			g.setColor(busyColor);
		}
		g.fillRect(1,BARHEIGHT-50+1,width-1,50-1);
		
		setVisible(true);
	}
   
   public Dimension getPreferredSize(){
	return ( new Dimension(PPWIDTH,PPHEIGHT));
    }
}