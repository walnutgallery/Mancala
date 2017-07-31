
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartGUI {
    private JFrame newScreen;
    private JLabel headerLabel;
    private JLabel promptLabel;
    private JPanel buttonPanel;
    private JPanel optionPanel;
    static JButton enterButton;
    JButton startButton = new JButton("Start Game");
    JButton ppButton = new JButton("Person - Person");
    JButton paiButton = new JButton("Person - AI");
    JButton aiaiButton = new JButton("AI - AI");
    public static JTextField input;
    private Boolean wait = true;
    String Mode;
    
    /**
     * Get the mode from the start GUI
     * @return
     */
    public String getMode(){ return Mode; }
    
    /**
     * Create the start screen
     */
    public StartGUI(){
        prepareGUI();
    }
    Boolean Wait(){//So GameManager knows when the start button is pressed
        return wait;
    }
    private void prepareGUI(){
        
    	newScreen = new JFrame("Mancala Start Screen");

        newScreen.setSize(450,480);
        newScreen.setLayout(new GridLayout(3, 1));
        
        headerLabel = new JLabel("",JLabel.CENTER );
        promptLabel = new JLabel("",JLabel.CENTER);

        
        newScreen.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        optionPanel = new JPanel();
        optionPanel.setLayout(new FlowLayout());
        
        
        newScreen.add(headerLabel);
      
        newScreen.add(optionPanel);
        optionPanel.setLayout(new GridLayout(5,0));
        
        newScreen.setVisible(true);
        createScreen();
    }
    
    /**
     * Creates all the fields and contents
     */
    void createScreen(){//Create Start Screen
            headerLabel.setText("<html><h1><font color='red'>MANCALA</h1></html>");
            promptLabel.setText("");
            
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            
            JButton quitButton = new JButton("Quit");
            quitButton.setActionCommand("Quit");
   
            startButton.addActionListener(new ButtonClickListener());
            quitButton.addActionListener(new ButtonClickListener());
            
            buttonPanel.add(startButton);
            startButton.setLayout(new BoxLayout(startButton,BoxLayout.LINE_AXIS));
            quitButton.setLayout(new BoxLayout(quitButton,BoxLayout.LINE_AXIS));
            buttonPanel.add(quitButton);
            optionPanel.add(buttonPanel);
            optionPanel.add(promptLabel);
            
            promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            //Set up Input for number of holes
            JPanel inputpanel = new JPanel();
            inputpanel.setLayout(new FlowLayout());
            input = new JTextField(20);
            enterButton = new JButton("Enter");
            enterButton.setActionCommand("Enter");
            enterButton.addActionListener(new ButtonClickListener());
            input.setVisible(false);
            enterButton.setVisible(false);
            input.setActionCommand("Enter");
            input.addActionListener(new ButtonClickListener());
            
            
            inputpanel.add(input);
            inputpanel.add(enterButton);
            optionPanel.add(inputpanel);
            
            ppButton.setActionCommand("PP");
            paiButton.setActionCommand("PAI");
            aiaiButton.setActionCommand("AIAI");
            ppButton.setVisible(false);
            paiButton.setVisible(false);
            aiaiButton.setVisible(false);
            ppButton.addActionListener(new ButtonClickListener());
            paiButton.addActionListener(new ButtonClickListener());
            aiaiButton.addActionListener(new ButtonClickListener());
            
            inputpanel.add(ppButton);
            inputpanel.add(paiButton);
            inputpanel.add(aiaiButton);

        newScreen.setVisible(true);
    }
    private class ButtonClickListener implements ActionListener{
    	/**
    	 * Implements functionality for each of the buttons
    	 */
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            if( command.equals( "Start Game" ))  {
                promptLabel.setText("Select Gameplay Mode:");
                ppButton.setVisible(true);
                paiButton.setVisible(true);
                aiaiButton.setVisible(true);
                startButton.setEnabled(false);
               
            }
            else if( command.equals( "PP" )){
            	Mode="PP";
            	wait=false;
            	newScreen.dispose();
            }
            else if( command.equals( "PAI" )){
            	Mode="PAI";
            	wait=false;
            	newScreen.dispose();
            }
            else if( command.equals( "AIAI" )){
            	Mode="AIAI";
            	wait=false;
            	newScreen.dispose();
            }
            else if( command.equals( "Quit" )){//Quit Button
                System.exit(0);
            }
        }
        
    }
}
