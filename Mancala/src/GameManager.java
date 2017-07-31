
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Game Manager coordinates screen launches
 *
 */
public class GameManager {
	Board gameBoard = null;
	static DataClient client;
	public GameScreen game = null;
	int startHoles;
	private int move;
	StartGUI startScreen;
	OptionGUI optScreen;
	boolean paused = false;
	int[] randomSeedArray;
    /**
     * Empty constructor Game Manager
     */
	public GameManager(){
    }
	
	/**
	 * creates start screen
	 */
    public void launchStartGUI(){
    	//start GUI should decide if it is an AI client or human CLIENT
        startScreen = new StartGUI();
        while (startScreen.Wait()==true){
            System.out.print("");
        }
        System.out.println(startScreen.getMode());//Is the mode person to AI? Will return T/F
        
        //Testing Option Screen: It will get launched with Server
        optScreen = new OptionGUI();
        while (optScreen.Wait()==true){
            System.out.print("");
        }
        
        System.out.println(optScreen.getINFO());
    	}
   
    /**
     * Initializes the GameScreen and Board
     * @param _holes The number of holes in the board. 
     * @param _randomSeeds True to randomly distribute the seeds else false.
     * @param _timer True if you want to have a timer.
     * @param _timerLength The length of time.
     */
    public void initGame(int _holes, boolean _randomSeeds, boolean _timer, int _timerLength){
        gameBoard = new Board(_holes, 4 , _randomSeeds); //need to update later
        game = new GameScreen(gameBoard, _holes);
        
        runningGame(game, _holes, _randomSeeds);
    }
    

    /**
     * watches the running game and takes care of clicks accordingly
     * @param game, the current GameScreen
     * @param board, the current board
     * @param randomSeeds DOESN"t GET USED
     */
    public void runningGame(GameScreen game, int _numHoles,Boolean randomSeeds){
        while(!gameBoard.checkEndState() && !game.endGame){
            if(game.clickOccurred()){
                setMove(game.getRow(), game.getColumn());
                gameBoard.movePiece(game.getRow(), game.getColumn(), game.boardGUI.getpieRule());
                game.createAndShowGUI(gameBoard, _numHoles);
                game.boardGUI.falsepieRule();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("");
          	}
       // if replay then reset & relaunch
        	
        	game.endGame = false;
        	game.frame.dispose();
        	game.resetVariables();
        	game = null;
        	gameBoard = null;
        	launchStartGUI();
    }
    
    
    
 //------------------------------------------------------------------ 
    /**
     * @throws IOException 
     * @throws UnknownHostException 
     * 
     */
    public void clientLaunchStartGUI() throws Exception{
    	//start GUI should decide if it is an AI client or human CLIENt
        startScreen = new StartGUI();
        while (startScreen.Wait()==true){
            System.out.print("");
        }
        if(startScreen.getMode().equals("PP")){ //True = human
        	//just connect & wait for other client
        }
        else if(startScreen.getMode().equals("PAI")){
        	//This means that the first player is always assumed to be the human
        	while(true){
        		AIClient client = new AIClient();
        		client.play();
        	}
        	//launch an AI client
        	
        }
        else if(startScreen.getMode().equals("AIAI")){
        	//make this client an AI, then make another AI client
        }
    }
    
    /** Initializes the game for use in the client move.  
     * 
     * @param _holes The number of holes in the board. 
     * @param _randomSeeds True to randomly distribute the seeds else false.
     * @param _timer True if you want to have a timer.
     * @param _timerLength The length of time.
     */
    public void clientInitGame(int holes, Boolean randomSeeds, Boolean timer, int timerLength, boolean isAI, int numSeeds){
    	if(randomSeeds){
    		gameBoard = new Board(holes, 4,true, randomSeedArray);
    	}
    	else{
    		gameBoard = new Board(holes, numSeeds, randomSeeds); //need to update later to take in variable seeds
    	}
    	
    	game = new GameScreen(gameBoard, holes);
    }
    
    /** Used for running the game in createMoves.  
     * 
     * @param game The gamescreen of the client
     * @param numHoles The number of holes in the board
     * @param _isPlayerOne The client's player.
     * @return
     */
    public String clientRunningGame(GameScreen game, int numHoles, boolean _isPlayerOne){
    	String moveReturn = "";
    	StringBuilder moveBuilder = new StringBuilder();
    	int move=-1;
    	boolean turnSwitch = false;
    	boolean executedPie = false;
    	while(!gameBoard.checkEndState() && !game.endGame){
            if(game.clickOccurred()){
            	move = game.getColumn();
                setMove(game.getRow(), game.getColumn());
                gameBoard.movePiece(game.getRow(), game.getColumn(), game.boardGUI.getpieRule());
                if(game.boardGUI.getpieRule()){
                	game.player1 = true;
                	executedPie = true;
                }
                game.createAndShowGUI(gameBoard, numHoles);
                game.boardGUI.falsepieRule();
                if(_isPlayerOne){ 
                	move += 1;
					moveBuilder.append(move);
					moveBuilder.append(" ");
					if(!game.boardGUI.player1){
						turnSwitch = true;
					}
					
				}
				else{
					if(executedPie){
						return "P";
					}
					move = numHoles - (numHoles - move);
					moveBuilder.append(move);
					moveBuilder.append(" ");
					if(game.boardGUI.player1){
						turnSwitch = true;
					}
				}
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("");
            if(turnSwitch){
        		return moveBuilder.toString();
        	}
    	}
    	return moveReturn;
    }
    
    /**
     * 
     */
    public void clientEndGame(){
    	game.endGame = false;
    	game.frame.dispose();
    	game.resetVariables();
    	game = null;
    	gameBoard = null;
    	launchStartGUI();
    }
    
    
    //Used for client
    /**
     * 
     * @return
     */
    public int getMove(){
    	return move;
    }
    
    /**
     * 
     * @param _row
     * @param _column
     */
    private void setMove(int _row, int _column){
    	if(_row == 1){
    		move = _column*2;
    	}
    	else{
    		move = _column;
    	}
    }
    public void setRandomArray(int[] _inArray){
    	randomSeedArray = _inArray;
    }
       
    public static void main(String[] args) throws Exception {
    	GameManager gm = new GameManager();
        gm.launchStartGUI();     
    }
    
}