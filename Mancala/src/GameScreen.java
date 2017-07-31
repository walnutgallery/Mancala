

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Game Screen takes care of click events and managing GUI's
 *
 */

public class GameScreen extends JPanel implements MouseListener{
    //global variables needed
    public static boolean didClick = false;
    public static boolean player1 = true;
    private static boolean allowed = false;
    private static final long serialVersionUID = 1L;
    private static boolean init = false;
    private static int row = -1;
    private static int column = -1;
    public static int newBoard[][];
    public boolean timeUp = false;
    private static boolean extraTurn = false;
    public JFrame frame = new JFrame("MANCALA");
    public boolean endGame = false;
    TimerGUI timer;
    Board inBoard;
    BoardGUI boardGUI = null;
    EndGUI endscreen = null;
    public boolean replay;
    private boolean clientGameOver = false;
    public JLabel messageLabel = new JLabel("MESSAGE LABEL",SwingConstants.CENTER);
   
    //Arrays needed for mouse-click comparisons
    private static final ArrayList<Integer> xRangeArray = new ArrayList<Integer>(){
		private static final long serialVersionUID = 1L;
	{
    add(150);add(230); add(260);add(340); add(370);add(450);
    add(480);add(560); add(590);add(670); add(700);add(780);
    add(810);add(890); add(920);add(1000); add(1030);add(1110);
    }};

	private static final ArrayList<Integer> yRangeArray = new ArrayList<Integer>(){
		private static final long serialVersionUID = 1L;
	{
	add(150);add(250);add(260);add(360);
	}};

	/**
	 * Primary Constructor
	 * Constructs the MancalaGUI
	 * @param _inBoard , brings in the board that is being painted
	 */
	GameScreen(Board _inBoard, int _numHoles){
		newBoard = _inBoard.getBoardArray();
		inBoard = _inBoard;
		createAndShowGUI(_inBoard, _numHoles);
	}
	/**
	 * Empty Constructor
	 */
	GameScreen(){
		
	}
	
	/** Resets the variables for redrawing and ending the game
	 * 
	 */
	public void resetVariables(){
		didClick = false;
	    player1 = true;
	    allowed = false;
	    init = false;
	    row = -1;
	    column = -1;
	    timeUp = false;
	    extraTurn = false;
	    frame = null;
	    timer = new TimerGUI();
	    replay = false;
	    boardGUI = null;
	    timer.gameOver = false;
	    clientGameOver = false;
	}
	
	/**
	 * Create the GUI and show it.
	 * @param Board b2 is the board that is being drawn
	 */
	public void createAndShowGUI(Board _inBoard, int numHoles) {
		didClick = false;
		timer = new TimerGUI();
		if(!timer.gameOver){
			if(!init){
				frame.setPreferredSize(new Dimension(170*numHoles,600));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				frame.setSize(200, 100);
				frame.add(timer);
				frame.setVisible(true);
				
			    boardGUI = new BoardGUI(_inBoard, numHoles);
				boardGUI.addMouseListener(this);
				init = true;
				endGame=false;
			}
			else{
				timer.update();
				boardGUI.setTurn(player1);
				boardGUI.update(_inBoard);
			}
			
			frame.add(boardGUI);
			messageLabel.setVisible(true);
			messageLabel.setBorder(new LineBorder(Color.BLACK));
			messageLabel.setPreferredSize(new Dimension(200, 100));
			messageLabel.setForeground(Color.RED);
			frame.add(messageLabel,"South");
			//Display the window.
			frame.pack();
			frame.setVisible(true);
		}
		
		//at end of game remove screen & reset variables for replay
		if(_inBoard.checkEndState() && clientGameOver ){
			frame.getContentPane().removeAll();
			endscreen =new EndGUI(inBoard.getPlayerOneStore(),inBoard.getPlayerTwoStore());
			frame.add(endscreen);
			messageLabel.setVisible(true);
			messageLabel.setBorder(new LineBorder(Color.BLACK));
			messageLabel.setPreferredSize(new Dimension(200, 100));
			messageLabel.setForeground(Color.RED);
			frame.add(messageLabel,"South");
			frame.pack();
		    init = false;
		    timer.update();
		    frame.setVisible( true );
		    while (endscreen.getreplay()==false){
	            System.out.print("");
	        }
			endGame = true;
		}
	}

	/**
	 * checks if a click occurred
	 * @return true if a click occurred, false otherwise
	 */
	Boolean clickOccurred(){
		if(boardGUI.getpieRule()){
			return true;
		}
	return didClick;
	}
	
	/**
	 * determines which row was clicked
	 * @return row of the click
	 */
	public int getRow(){
		return row;
	}
	
	/**
	 * determines which column was clicked
	 * @return column of the click
	 */
	public int getColumn(){
		return column;
	}
	
	/**
	 * Determines if there is an extra turn.
	 * @return
	 */
	public Boolean getExtraTurn(){
		return extraTurn;
	}
	
	/**
	 * Sets if there is an extra turn
	 * @param _inExtra true if there is an extra turn else false
	 */
	public void setExtraTurn(boolean _inExtra){
		extraTurn = _inExtra;
		
	}
	
	/**
	 * Stops the game from the client.
	 * @param endGame Ends the game if true
	 */
	public void setClientGameOver(Boolean endGame){
		clientGameOver = endGame;
	}
	
	/**
	 * Gets the pot number that was clicked so that we can distribute seeds as needed
	 * Also checks if the player gets an extra turn
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int xCor = e.getX();
		int yCor = e.getY();
		int xCount = 0;
		
		
		for(int i = 0; i<2*(newBoard[0].length); i+=2){
			
			if(xCor > xRangeArray.get(i) && xCor < xRangeArray.get(i+1)){
				row = 0;
				if(yCor > yRangeArray.get(0) && yCor < yRangeArray.get(1)){
					column = xCount;
					setExtraTurn(inBoard.extraTurn(row, column, false));
					if(player1==true){//Player 1 clicked on the top row
						if(getExtraTurn()){
							allowed = true;
						}
						else{
							player1=false;
							allowed=true;
						}
					}
					else{//Player 1 clicked on the bottom row
						allowed=false;
					}
				}
				
				
				else if(yCor > yRangeArray.get(2) && yCor < yRangeArray.get(3)){
					column = xCount;
					row = 1;
					setExtraTurn(inBoard.extraTurn(row, column, false));
					if(player1==false){//Player 2 clicked bottom row
						if(getExtraTurn()){
							player1=false;
							allowed=true;
						}
						else{
							player1 = true;
							allowed = true;
						}
					}
					else{//Player 2 clicked on the top row
						allowed=false;
					}
				}
				else{//illegal move
					allowed=false;
				}
			}
			xCount++;
		}
		if(allowed==true){//if the move was legal, the click is acknowledged.
			didClick = true;	
		}
	}
	
	
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}

}

