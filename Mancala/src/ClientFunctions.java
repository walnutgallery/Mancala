

public class ClientFunctions {
	boolean isAI; //if the current client is a ai or human
	boolean isReady; //if the player is ready
	boolean hasBegun; //if the game has begun
	Board board; //the board that the clients are playing on
	int row; //the row that the client uses.  Player 1 is 0 and player 2 is 1
	public GameManager gameManager;
	private int numHoles;
	AI ai;
	//boolean internalIsPlayerOne;
	public boolean isFirst;
	boolean isTurn;
	int[] randomSeedArray = new int[9];
	int timerLength;
	
	/**
	 * Client functions run differently per human or AI
	 */
	public ClientFunctions(){
		gameManager = new GameManager();
	}
	/**
	 * Takes in an info string and creates an AI based on the information given.
	 * @param info The info string.
	 */
	public ClientFunctions(String[] info){
		gameManager = new GameManager();
		isAI = true;
		boolean isPlayerOne = info[4].equals("F");
		int timer = Integer.parseInt(info[3]);
		//internalIsPlayerOne = _isPlayerOne;
		ai = new AI(5, isPlayerOne);
	}
	
	/**
	 * Launches the game manager's GUI
	 */
	public void launchStartGUI(){
		try {
			gameManager.clientLaunchStartGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Initializes a gameManager/other things you might need
	 * Delete if not needed.
	 */
	public boolean init(String response) {
		String[] tokens = response.split("\\s+");
		boolean timer;
		
		boolean randomSeeds;
		int numSeeds;
		//info,holes/side,num seeds, timer, who goes first, standard or random, nums aft = random
		numHoles = Integer.parseInt(tokens[1]);
		if(tokens[3] == "0"){
			timer = false;
			timerLength = 0;
		}
		else{
			timer = true;
			timerLength = Integer.parseInt(tokens[3]);
		}
		
		if(tokens[4].equals("F")){
			isFirst = true;
			isTurn = true; //this client goes first
		}
		else{
			isFirst = false;
			isTurn = false;
		}
		numSeeds = Integer.parseInt(tokens[2]);
		randomSeeds = false;
		if(tokens[5].equals("R")){
			for(int i = 6; i<tokens.length; i++){
				randomSeedArray[i-6] = Integer.parseInt(tokens[i]);
			}
			
			randomSeeds = true;
		}
		
		gameManager = new GameManager();
		gameManager.setRandomArray(randomSeedArray);
		gameManager.clientInitGame(numHoles, randomSeeds, timer, timerLength, isAI,numSeeds);
		
		gameManager.game.timer.initialTime = timerLength/1000;
		
        
        return true;
	}
	
	public String createMoves(boolean isPlayerOne){
		int move = -1;
		String moveToSend = "";
		if(!isAI){
			//wait for isclick, move = column
			
			if(isTurn && isPlayerOne){ 
				moveToSend = gameManager.clientRunningGame(gameManager.game, numHoles, isPlayerOne);	
				gameManager.game.messageLabel.setText("Opponents Turn");
				//System.out.println("Move:" + moveToSend);
				return moveToSend;
			}
			else if(isTurn && !isPlayerOne){
				moveToSend = gameManager.clientRunningGame(gameManager.game, numHoles, isPlayerOne);	
				gameManager.game.messageLabel.setText("Opponents Turn");
				System.out.println("Move:" + moveToSend);
				return moveToSend;
			}
		
		}
		
		else{
			//get AI calculated moves
			boolean extraTurn = true;
			String moves = "";
			while(extraTurn){
				System.out.println(isPlayerOne);
				ai.eval(gameManager.gameBoard, isPlayerOne);
				move = ai.getBestMove();
				
				System.out.println("EVAL MOVE:" + move);
				if(move == -1){
					gameManager.gameBoard.movePiece(-1, -1, true);
					return "P";
				}
				int row = isPlayerOne? 0 : 1;
				extraTurn = gameManager.gameBoard.movePiece(row , move, false);
				gameManager.game.createAndShowGUI(gameManager.gameBoard, numHoles);
				if(isPlayerOne){ // multiplied by two to symbolize top or bottom row
					//player 1
					move +=1;
				}
				else{
					//this means it was player two
					move = numHoles - (numHoles - move);
					
				}
				moves += Integer.toString(move) + " ";
				if(gameManager.gameBoard.checkEndState()){
					break;
				}
			}
			return moves;
		}
		return "FAILED";
	}

	/** 
	 * Updates the board on the screen after receiving a response from the server
	 * @param response The response string from server
	 * @param whoMoved The last person to move
	 * @param pieMove Whether or not to use the pie move.
	 */
	public void updateBoard(String response, boolean whoMoved, boolean pieMove){
		//acts as if clicks happen and automatically updates board
		String[] tokens = response.split("\\s+");
		if(tokens[0].equals("P")){
			gameManager.gameBoard.movePiece(-1, -1, true);
			
			gameManager.game.createAndShowGUI(gameManager.gameBoard, numHoles);
		
		}
		else{
			int opponentMove;
			int row=1;
			int column=0;
			if(whoMoved){
				row = 0;
			}
			for(int i =0; i<tokens.length; i++){
				opponentMove = Integer.parseInt(tokens[i]);
				if(whoMoved){
					column = opponentMove-1;
				}
				else{
					column = numHoles - (numHoles-opponentMove);
					
				}
				gameManager.gameBoard.movePiece(row, column, false);
				
				gameManager.game.createAndShowGUI(gameManager.gameBoard, numHoles);
				
			}
		}
		System.out.println("updated board : \n" + gameManager.gameBoard.toString());
	}
		
	/**
	 *  Used to end the game and update the text
	 * @param winOrLose If the person won, lost or drew.
	 */
	public void endGame(String winOrLose){
		//send this string to the end screen
		gameManager.game.setClientGameOver(true);
		gameManager.game.messageLabel.setText(winOrLose);
	}
	
	/**Takes in the board info and sets the correct parameters
	 * 
	 * @param boardInfo INFO <int holes per side><int seeds per side><long int time for timer><F|S><S|R hole_config>
	 */
	public void recieveBoardInfo(String boardInfo) {
	}
	
	/**Acknowledges that the game has begun.
	 * calls sendMove if it's player 1. 
	 */
	public void recieveBegin() {
		//gameManager.game.timer.startTimer();
		if(isTurn){
			gameManager.game.timer.pause(false);
			gameManager.game.timer.wait=false;
			gameManager.game.messageLabel.setText("Your Turn!");
		}
		else{
			
			gameManager.game.timer.pause(false);
			gameManager.game.timer.wait=true;
			gameManager.game.messageLabel.setText("Opponents Turn");
		}
	}
	
	/**
	 * Toggle the turn to display who's turn it is.
	 */
	public void toggleTurn(){
		//System.out.println("DOING: toggle turn");
		GameScreen.player1 = !GameScreen.player1; 
	}
	
	/**
	 * Start the game once it's received a welcome and to update the bottom text
	 * @param welcome Should just be "welcome"
	 */
	public void receiveWelcome(String welcome) {
		gameManager.game.messageLabel.setText(welcome);
	}
	
	/**
	 * Update the turn indicator
	 */
	public void myTurn(){
		gameManager.game.timer.wait = false;
		if(isAI){
			gameManager.game.messageLabel.setText("THIS AN AI");
		}
		else{
			gameManager.game.messageLabel.setText("Your Turn!");
		}
		isTurn = true;
		gameManager.game.timer.pause(false);
		//gameManager.game.timer.wait(false);
		gameManager.game.timer.update();
		
	}
	
	/**
	 * Updates the bottom text
	 */
	public void endMyTurn(){
		//toggleTurn();
		gameManager.game.timer.wait = true;
		if(isAI){
			gameManager.game.messageLabel.setText("AI SCREEN");
		}
		else{
			gameManager.game.messageLabel.setText("Opponents Turn");
		}
		
		isTurn = false;
		
	}
	
	
}