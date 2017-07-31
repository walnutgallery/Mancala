
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class EndGUI extends JPanel implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    private final JButton playButton = new JButton( "Play Again" );
    JButton quitButton = new JButton("Quit");
    private JLabel scoreLabel;
    private JPanel buttonPanel;
    private JPanel optionPanel;
    private Boolean replay = false;
    int score1;
    int score2;
    
    /** Builds the GUI for the ending screen
     * 
     * @param player1 the score of player 1
     * @param player2 the score of player 2
     */
    public EndGUI(int _player1, int _player2) {
        
        super();

        playButton.setBounds(0, 0, getWidth(), getHeight());
        playButton.setActionCommand("Start Game");
        quitButton.setActionCommand("Quit");
         score1 = _player1;
         score2 = _player2;
        
        playButton.addActionListener(new ButtonClickListener());
        quitButton.addActionListener(new ButtonClickListener());
        
        optionPanel = new JPanel();
        optionPanel.setLayout(new FlowLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        scoreLabel = new JLabel("",JLabel.CENTER);
        scoreLabel.setSize(350,100);
        scoreLabel.setText("Player 1 Score:"+score1+"        Player 2 Score:"+score2);
        optionPanel.add(new JLabel("",JLabel.CENTER));//Spacing
        optionPanel.add(new JLabel("",JLabel.CENTER));//Spacing
        optionPanel.add(new JLabel("",JLabel.CENTER));//Spacing
        optionPanel.add(scoreLabel);
        
        
        playButton.setSize(60,20);
        playButton.setLocation(100, -140);
        playButton.setVisible(true);
        playButton.requestFocusInWindow();
        buttonPanel.add(playButton);
        buttonPanel.add(quitButton);
        optionPanel.add(buttonPanel);
        
        optionPanel.setLayout(new GridLayout(5,0));
        add(optionPanel);
    }
    
    
    /**
     * (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        //Background
        g2.setColor(Color.WHITE);
        
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.RED);
        g2.drawString("GAME OVER", getWidth()/2 -61, 100);
        
        setVisible(true);
        
    }
    Boolean getreplay(){return replay;}
    
    /*
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    private class ButtonClickListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            if( command.equals( "Start Game" ))  {//New Game
            	scoreLabel.setText("Loading New Game...");
            	replay = true;
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
    
}