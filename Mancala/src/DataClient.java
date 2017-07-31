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

public class DataClient {
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private ClientFunctions clientHelper;
	boolean isPlayerOne;
	boolean whoMoved; //T = 1, F =2
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
	public DataClient() throws UnknownHostException, IOException {
		//setup network
		String serverAddress = "127.0.0.1";
        socket = new Socket(serverAddress, 9090);
        input = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        
		
        clientHelper = new ClientFunctions(); 
	}

	/**
	 * Main thread will listen for messages from the server
	 * @throws IOException 
	 * @throws Exception
	 */
	public void play() throws IOException{
		String response;
		String moveToSend = "";
		String tempMessage = "";
		String[] tokens;
		
		try {
			if(!isAI){
				clientHelper.launchStartGUI();
			}
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
				System.out.println("recieved info");
    			if(clientHelper.init(response)){
    				clientHelper.receiveWelcome(tempMessage);
    				output.println("READY");
        		}
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
	            			//recieves moves from server
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
		
	            			
	            			//clientHelper.endMyTurn();
	            			clientHelper.toggleTurn();
	            			//output.println(moveToSend);
	            			output.println(moveToSend);
	            			clientHelper.toggleTurn();
	            			//clientHelper.endMyTurn();
	      
	            			System.out.println("MOVE:" + moveToSend);
	            			
	            			break;
	            		case "P":
	            			//execute pie move
	            			System.out.println("recieved pie move");
	            			output.println("OK");
	            			clientHelper.myTurn();
	            			
	            			if(isPlayerOne){ // if I am player one, player two moved
	            				whoMoved = false;
	            			}
	            			else{
	            				whoMoved = true;
	            			}
	            			
	            			clientHelper.updateBoard(response, whoMoved, true);
	            		
	            			moveToSend = clientHelper.createMoves(isPlayerOne);
	            			
	            			clientHelper.endMyTurn();
	            			clientHelper.toggleTurn();
	            			output.println(moveToSend);
	            
	            			
	            			break;
	            		case "OK":
	            			//wait to receive moves
	            			break;
	            		case "TIME":
	            			System.out.println("recieved time");
	            			//wait to see if loser or winner
	            			break;
	            		case "LOSER":
	            			clientHelper.endGame("You Lose!");
	            			
	            			break;
	            		case "WINNER":
	            			clientHelper.endGame("You Won!");
	            			
	            			break;
	            		case "ILLEGAL":
	            			//wait for winner / loser
	            			System.out.println("recieved illegal");
	            			output.println(moveToSend);
	            			break;
	            		default:
	            			System.out.println("Doesn't recognize response");
	            			break;
	            	}
	            	System.out.println("RESPONSE: " + response + " TO PLAYER " + isPlayerOne);
	            }
            	
            	response = "";
            }
		}finally {
            socket.close();
        }
	}

    public static void main(String[] args) throws Exception {
    	while(true){
    		DataClient client = new DataClient();
    		
    		client.play();
    	}
   }
	
}