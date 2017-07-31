
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
/*
 * The TimerGUI class acts as if the decrement of the timer is an Action and updates the timer accordingly
 */
public class TimerGUI extends JPanel implements ActionListener {

  /**
	 * 
	 */
private static final long serialVersionUID = 1L;
private boolean init = false;
  public int time;
  public int initialTime;
  Timer timer;
  public boolean gameOver = false;
  private boolean pause;
  public boolean wait;
  boolean isServer = false;
  boolean serverEndTurn=false;
  boolean timeUp = false;
  
  /**
   * update() is called if a player takes their turn before time is up
   * This sets up the timer for the next player
   */
  public void update(){
	  time = initialTime;
  }

/**
 * Constructor sets up the timer
 */
  public TimerGUI() {
    timer = new Timer(800, this);
    timer.setInitialDelay(1000);
    timer.start();
  }

  public void startTimer(){
	  
  }
  public void pause(boolean pauseToggle){
	 pause = pauseToggle; 
  }
  public void wait(boolean waitToggle){
	  wait = waitToggle;
  }

/**
 * Painting the timer
 * (non-Javadoc)
 * @see javax.swing.JComponent#paint(java.awt.Graphics)
 */
  public void paint(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    	g2.setColor(Color.BLACK);
    	if(initialTime == 0){
	    	g2.drawString("No Time Limit", 50, 70);
	    }
    	else{
		    if(wait){
		    	g2.drawString("Opponents Turn", 50, 70);
		    }
		    
		    else if(!timeUp){
			    if (init) {
			      time = initialTime;
			      init = false;
			      g2.drawString("Time Left on Turn: "+Integer.toString(time), 50, 70);
			    }
			    if(time==1){
			    	timeUp=true;
			    }
			    if(time<=5 && time > 0){
			    	time--;
			    	g2.setColor(Color.RED);
			    	g2.drawString("Time Left on Turn: "+Integer.toString(time), 50, 70);
			    }
			    else{
			    	time--;
			    	g2.drawString("Time Left on Turn: "+Integer.toString(time), 50, 70);
			    }
			    
			}
		}
	 
 
  }
  /*
   * Every time the "time" object is decremented, the GUI is updated
   * (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

}