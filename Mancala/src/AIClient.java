import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * This Client class represents a start on the functions needed to continue sprint 3
 * Based on code given in lab
 */

public class AIClient {
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private ClientFunctions clientHelper;
	boolean isPlayerOne;
	boolean whoMoved;
	boolean isTurn = true;
	boolean isPlayerTwo;
	boolean isAI = false;
	/**
	 * Constructs clients by connecting to server and laying out the start GUI
	 * Start GUI tells whether it is person - person or person - AI game.
	 * This determines how the client runs
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws Exception
	 */
	public AIClient() throws UnknownHostException, IOException {
		//setup network
		String serverAddress = "127.0.0.1";
        socket = new Socket(serverAddress, 9090);
        input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        
        isAI = true;
	}


	/**
	 * Main thread will listen for messages from the server
	 * @throws IOException If it fails in sending/receiving
	 * @throws InterruptedException If the timer stops
	 * @throws Exception other exceptions
	 */
	public void play() throws IOException{
		String response;
		String moveToSend = "";
		String tempMessage = "";
		String[] tokens;
		
		try {
			
			//WELCOME
			response = input.readLine();
			if(response.startsWith("WELCOME")){
				System.out.println("recieved welcome");
			}
						
			//INFO
			response = input.readLine();
			System.out.println(response);
			if(response.startsWith("INFO")){
				tokens = response.split("\\s+");
				if(tokens[4].equals("F")){
					isPlayerOne = true;
					tempMessage = "WELCOME Player One!";
				}
				else{
					isPlayerOne = false;
					tempMessage = "WELCOME Player Two!";
				}
				
				clientHelper = new ClientFunctions(tokens); 
				
				System.out.println("recieved info");
    			if(clientHelper.init(response)){
    				if(isAI){
    					tempMessage = "WELCOME AI";
    				}
    				clientHelper.receiveWelcome(tempMessage);
    				output.println("READY");
        		}
			}
		
			//BEGIN
			response = input.readLine();
			if(response.equals("BEGIN")){
				if(clientHelper.isFirst){
    				System.out.println("recieved begin");
        			clientHelper.recieveBegin();
    				clientHelper.myTurn();
        			moveToSend = clientHelper.createMoves(isPlayerOne);
        			clientHelper.endMyTurn();
        			output.println(moveToSend);
    			}
				clientHelper.toggleTurn();
			}
			
			
			//layout GUI
            while(true){
            	response = input.readLine();
            	if(response!= null){
	            	tokens = response.split("\\s+");
	            	
	            	switch(tokens[0]){
	            	case "BEGIN":
	            		if(clientHelper.isFirst){
	        				System.out.println("recieved begin");
	            			clientHelper.recieveBegin();
	        				clientHelper.myTurn();
	            			moveToSend = clientHelper.createMoves(isPlayerOne);
	            			clientHelper.endMyTurn();
	            			output.println(moveToSend);
	        			}
	    				clientHelper.toggleTurn();
	            		break;
	            		case "0": case "1": case "2": case "3":
	            		case "4": case "5": case "6": case "7": 
	            		case "8": case "9":
	            			output.println("OK");
	            			clientHelper.myTurn();
	            			if(isPlayerOne){ // if I am player one, player two moved
	            				whoMoved = false;
	            			}
	            			else{
	            				whoMoved = true;
	            			}
	            			clientHelper.updateBoard(response, whoMoved ,false);
	            			moveToSend = clientHelper.createMoves(isPlayerOne);
	            			System.out.println("AI MOVE: " + moveToSend);
	            			clientHelper.toggleTurn();
	            			clientHelper.endMyTurn();
	            			output.println(moveToSend);
	            			break;
	            		case "P":
	            			output.println("OK");
	            			clientHelper.myTurn();
	            			//if I am player one, player two moved
	            			if(isPlayerOne){ 
	            				whoMoved = false;
	            			}
	            			else{
	            				whoMoved = true;
	            			}
	            			
	            			clientHelper.updateBoard(response, whoMoved, true);
	            			moveToSend = clientHelper.createMoves(isPlayerOne);
	            			clientHelper.endMyTurn();
	            			output.println(moveToSend);
	            			break;
	            		case "OK":
	            			//shouldn't do anything
	            			break;
	            		case "TIME":
	            			//shouldn't do anything
	            			break;
	            		case "LOSER":
	            			clientHelper.endGame("You Lose!");
	            			break;
	            		case "WINNER":
	            			clientHelper.endGame("You Won!");
	            			break;
	            		case "ILLEGAL":
	            			System.out.println("recieved illegal");
	            			break;
	            		default:
	            			System.out.println("Doesn't recognize response");
	            			break;
	            	}
	            }
            	
            	response = "";
            }
		}finally {
            socket.close();
        }
	}

    public static void main(String[] args) throws Exception {
    	while(true){
    		AIClient client = new AIClient();
    		client.play();
    	}
   }
	
}