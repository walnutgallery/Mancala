
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;



public class BoardGUI extends JPanel implements ActionListener{
    //global variables needed

    public boolean player1 = true;
    private static final long serialVersionUID = 1L;
    public static final java.awt.Color BURLY_WOOD= new Color(222, 184, 135);
    public static final Color BlanchedAlmond = new Color(255, 235, 205);
    private static int playerOneStore = 0;
    private static int playerTwoStore = 0;
    public static int newBoard[][];
    public boolean timeUp = false;
    public boolean endGame = false;
    public boolean pieRule = false;
    public boolean secondTurnHappened = false;
    private static JButton pieButton = new JButton( "Pie Rule" );
    JPanel board,seeds, turnIndicator;
    
    TimerGUI timer = new TimerGUI();

    /**
     * Primary Constructor
     * @param _inBoard The board you want to display
     * @param _numHoles The number of holes in the board.
     */
	BoardGUI(Board _inBoard, int _numHoles){
		newBoard = _inBoard.getBoardArray();
		   
	    
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JLayeredPane layers = new JLayeredPane();
		
	
		board=new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponents(g);
				
				//Background
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				
				
				//board
				g.setColor(BURLY_WOOD);
				for(int i = 0; i<_numHoles+2; i++){
					//fill board by pieces
					g.fillRect(50 + 105*i, 140, 120, 230);		
				}
				
				//pits and stores
				g.setColor(BlanchedAlmond);
				for(int j = 0; j<_numHoles+2; j++){
					int x = 150;
					if (j==0){
						//first store
						g.fillRoundRect(70, 145, 70, 210,50,50);
					}
					else if(j==_numHoles+1){
						//second store
						g.fillRoundRect(x+(107)*(j-1), 145, 70, 210,50,50);
					}
					else{
						//pits
						g.fillRoundRect(x+(110)*(j-1),145, 80, 100, 80, 80);
						g.fillRoundRect(x+(110)*(j-1),255, 80, 100, 80, 80);
					}
				}
			
			}
		};
		
		//painting the seeds
		seeds=new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			//painting seeds in pots
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				int numSeeds;
				int stackCount;
				int newCount=0;
				int newX=0;
				
				
				for(int x = 0; x<newBoard[0].length; x++){//6
					for(int y=0; y<newBoard.length; y++){//2
						stackCount=0;
						numSeeds = newBoard[y][x];
						
						for(int count =0; count<numSeeds; count++){
							if((count%5)==0 && count!=0){
								stackCount++;
								newCount=0;
								newX = 0;
							}
							
							if(stackCount>0){
								g.setColor(Color.BLUE);
								g.fillOval(160+110*(x-newX)+10*newCount, 165+110*y+10*stackCount, 10, 10);
							}
							else{
								g.setColor(Color.BLUE);
								g.fillOval(160+110*x+10*count, 165+110*y+10*stackCount, 10, 10);
							}
							newCount++;
						}
						g.setColor(Color.BLACK);
						g.drawString(Integer.toString(numSeeds), 185+110*x, 240+110*y);
					}
					newX++;
				}
				//painting seeds in stores FIX THIS
				g.setColor(Color.MAGENTA);
				for(int z1 = 0; z1 <playerOneStore; z1++ ){
					g.fillOval(80, 185+10*z1, 10, 10);
				}
				g.setColor(Color.MAGENTA);
				for(int z2 = 0; z2 <playerTwoStore; z2++ ){
					g.fillOval(150+(109)*(_numHoles), 185+10*z2, 10, 10);
				}
				g.setColor(Color.BLACK);
				g.drawString(Integer.toString(playerOneStore), 100, 350);
				g.drawString(Integer.toString(playerTwoStore), 150+(110)*(_numHoles), 350);
			}
			
		};
		
		//painting turn indicator
		turnIndicator = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			//painting seeds in pots
			public void paintComponent(Graphics g) {
				if(player1==true){
					g.setColor(Color.YELLOW);
					g.fillRect(70, 100, 130, 30);
					g.setColor(Color.BLACK);
					g.drawString("Player One's turn", 80, 120);
					if(!pieRule&&!secondTurnHappened){
                        pieButton.setVisible(false);
                    }
                    else{
                        pieButton.setEnabled(false);
                        pieRule=false;
                    }
				}
				else{
					g.setColor(Color.YELLOW);
					g.fillRect(110*(_numHoles+1)-10, 380, 130, 30);
					g.setColor(Color.BLACK);
					g.drawString("Player Two's turn", (_numHoles+1)*110, 400);
					if(!secondTurnHappened){
                        pieButton.setVisible(true);
                        secondTurnHappened=true;
                    }
				}
			}
		
		};
		
		//title
		JLabel label= new JLabel();
		label.setText("<html><h1><font color='red'>MANCALA</h1></html>");
		board.add(label);
		 pieButton.setSize(60,20);
		 pieButton.addActionListener(new ButtonClickListener());
		 pieButton.setActionCommand("PIE");
	        pieButton.setVisible(true);
	        pieButton.requestFocusInWindow();
	        
		board.setBounds(0,0,170*_numHoles,800);
		seeds.setBounds(0,0,170*_numHoles,800);
		turnIndicator.setBounds(0,0,170*_numHoles,800);
		
		board.setOpaque(false);
		seeds.setOpaque(false);
		pieButton.setOpaque(false);
		turnIndicator.setOpaque(false);
		
		//Identifying layers
		layers.setLayer(board,JLayeredPane.DEFAULT_LAYER); //default layer
		layers.setLayer(seeds,new Integer(1));
		layers.setLayer(turnIndicator,new Integer(1));
		
		//combining layers
		layers.add(board);
		layers.add(seeds);
		layers.add(turnIndicator);
		
		
		
		//adding layer to the frame
		add(layers);
		board.add(pieButton);
	}
	
	/**
	 * Sets current players turn
	 * @param _inBool, sets player
	 */
	public void setTurn( Boolean _inBool){
		player1 = _inBool;
	}
	
	/**
	 * updates the Board BUI
	 * @param _inBoard, board used to paint
	 */
	public void update(Board _inBoard){
		playerOneStore = _inBoard.getPlayerOneStore();
		playerTwoStore = _inBoard.getPlayerTwoStore();
		repaint();
	}
	
	private class ButtonClickListener implements ActionListener{
		
		/** mouse clicked to turn on/off pie rule.
		 * 
		 */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            if( command.equals( "PIE" ))  {//Pie Button
            	pieRule=true;
            }
            else if( command.equals( "Quit" )){//Quit Button
                System.exit(0);
            }  	
        }
        
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Returns if the pie rule has been used.
	 * @return
	 */
	public boolean getpieRule() {
		return pieRule;
	}
	
	/**
	 * Turns the pie rule off.
	 * @return Returns false after the pie rule has be turned off.
	 */
	public boolean falsepieRule() {
		pieRule=false;
		return pieRule;
	}

}