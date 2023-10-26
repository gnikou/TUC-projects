package PLH512.client;

import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import PLH512.server.Board;
import PLH512.server.City;

public class Client  
{
    final static int ServerPort = 64240;
    final static String username = "myName";
  
    public static void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException  
    { 
    	int numberOfPlayers;
    	int myPlayerID;
    	String myUsername;
    	String myRole;
    	//////////////////////
    	int evaluatePlayers[];
    	/////////////////////
        
        // Getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // Establish the connection 
        Socket s = new Socket(ip, ServerPort); 
        System.out.println("\nConnected to server!");
        
        // Obtaining input and out streams 
        ObjectOutputStream dos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream dis = new ObjectInputStream(s.getInputStream());  
        
        // Receiving the playerID from the Server
        myPlayerID = (int)dis.readObject();
        myUsername = "User_" + myPlayerID;
        System.out.println("\nHey! My username is " + myUsername);
        
        // Receiving number of players to initialize the board
        numberOfPlayers = (int)dis.readObject();
        
        ///////////////////////////////////////////
        evaluatePlayers = new int[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers;i++)
        	evaluatePlayers[i] = 0;
        ///////////////////////////////////////////
        
        
        // Receiving my role for this game
        myRole = (String)dis.readObject();
        System.out.println("\nHey! My role is " + myRole);
        
        // Sending the username to the Server
        dos.reset();
        dos.writeObject(myUsername);
        
        // Setting up the board
        Board[] currentBoard = {new Board(numberOfPlayers)};
        
        // Creating sendMessage thread 
        Thread sendMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() {
            	
            	boolean timeToTalk = false;
            	
            	//MPOREI NA GINEI WHILE  TRUE ME BREAK GIA SINTHIKI??
                while (currentBoard[0].getGameEnded() == false) 
                { 	
                	timeToTalk = ((currentBoard[0].getWhoIsTalking() == myPlayerID)  && !currentBoard[0].getTalkedForThisTurn(myPlayerID));
                	
                	try {
						TimeUnit.MILLISECONDS.sleep(15);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	
                    try { 
                        // Executing this part of the code once per round
                        if (timeToTalk)
                        {
                        	
                        	// Initializing variables for current round
                        	
                        	Board myBoard = currentBoard[0];
                        	
                        	String myCurrentCity = myBoard.getPawnsLocations(myPlayerID);
                        	City myCurrentCityObj = myBoard.searchForCity(myCurrentCity);
                        	
                        	ArrayList<String> myHand = myBoard.getHandOf(myPlayerID);
                        	
                        	int[] myColorCount = {0, 0, 0, 0};
                        	
                        	for (int i = 0 ; i < 4 ; i++)
                        		myColorCount[i] =  cardsCounterOfColor(myBoard, myPlayerID, myBoard.getColors(i));
                        	
                        	ArrayList<citiesWithDistancesObj> distanceMap = new ArrayList<citiesWithDistancesObj>();
                        	distanceMap = buildDistanceMap(myBoard, myCurrentCity, distanceMap);
                        	
                        	
                        	String myAction = "";
                        	String mySuggestion = "";
                        	
                        	
                        	// Printing out my current hand
                        	System.out.println("\nMy current hand...");
                        	printHand(myHand);
                        	
                        	// Printing out current color count
                        	System.out.println("\nMy hand's color count...");
                        	for (int i = 0 ; i < 4 ; i++)
                        		System.out.println(myBoard.getColors(i) + " cards count: " + myColorCount[i]);
                        	
                        	// Printing out distance map from current city
                        	//System.out.println("\nDistance map from " + myCurrentCity);
                        	//printDistanceMap(distanceMap);
                        	
                        	// ADD YOUR CODE FROM HERE AND ON!! 
                        	
                        	Board suggestionBoards[] = new Board[numberOfPlayers]; 
                        	String suggestions[] = new String[numberOfPlayers];
                        	
                        	String newMyAction = "";
                        	//////////////////////////
                        	if(myBoard.getWhoIsPlaying() == myPlayerID) {
                        		myAction = "";
                        		for(int i = 0; i < numberOfPlayers; i++) {
                            		suggestionBoards[i] = copyBoard(myBoard);
                        			if(i != myPlayerID) {
                        				suggestions[i] = myBoard.getActions(i);
                        				suggestionBoards[i] = decodeActions(myBoard.getActions(i), suggestionBoards[i]);
                        			}
                        		}
                        		
                        		//selecting my action
                        		if(myBoard.getRoleOf(myPlayerID).equals("Operations Expert") && myBoard.getRound() == myPlayerID + 1) {
                            		if(!myBoard.searchForCity("Bogota").getHasReseachStation()) {
                                		myAction = myAction + toTextDriveTo(myPlayerID, "Miami");
                                		myAction = myAction + toTextDriveTo(myPlayerID, "Bogota");
                                		myAction = myAction + toTextBuildRS(myPlayerID, "Bogota");
                                		myAction = myAction + toTextOET(myPlayerID, "Instabul", myHand.get(0));
                            		}else if(!myBoard.searchForCity("Instabul").getHasReseachStation()) {
                            			myAction = myAction +  toTextOET(myPlayerID, "Instabul", myHand.get(0));
                                		myAction = myAction + toTextBuildRS(myPlayerID, "Instabul");
                                		myAction = myAction + toTextDriveTo(myPlayerID, "Baghdad");
                                		myAction = myAction + toTextDriveTo(myPlayerID, "Karachi");
                            		}else if(!myBoard.searchForCity("Hong Kong").getHasReseachStation()) {
                            			myAction = myAction +  toTextOET(myPlayerID, "Hong Kong", myHand.get(0));
                                		myAction = myAction + toTextBuildRS(myPlayerID, "Hong Kong");
                                		distanceMap = buildDistanceMap(myBoard, "Hong Kong", distanceMap);
                                		String destination = getMostInfectedInRadius(2, distanceMap, myBoard);
                                		if(destination!=null) {
                                			String cityTo = getDirectionToMove("Hong Kong", destination, distanceMap, myBoard);
                            				myAction = myAction + toTextDriveTo(myPlayerID, cityTo);
                                    		distanceMap = buildDistanceMap(myBoard, cityTo, distanceMap);
                                    		cityTo = getDirectionToMove("Hong Kong", destination, distanceMap, myBoard);
                                			if(destination!= cityTo) {
                                				myAction = myAction + toTextDriveTo(myPlayerID, cityTo);
                                			}else {
                                				myAction = myAction + toTextTreatDisease(myPlayerID, cityTo, myBoard.searchForCity(cityTo).getMaxCubeColor());
                                			}
                                		}
                                		
                            		}else
                            			myAction = MonteCarloTreeSearch.findNextMove(myBoard, myPlayerID, numberOfPlayers);
                            	}else if(myBoard.getRoleOf(myPlayerID).equals("Operations Expert") && myBoard.getRound() == myPlayerID + 1 + numberOfPlayers) {
                            		
                        			
                        			if(myCurrentCity.equals("Instabul") && !myBoard.searchForCity("Hong Kong").getHasReseachStation()) {
                        				myAction = myAction + toTextBuildRS(myPlayerID, "Instabul");
                                		myAction = myAction + toTextOET(myPlayerID, "Hong Kong", myHand.get(0));
                                		myAction = myAction + toTextBuildRS(myPlayerID, "Hong Kong");
                                		String shuttleFlightCity = getDestinationFromAnotherRS(4, myCurrentCity, distanceMap, myBoard);
                            			if(shuttleFlightCity!= null) {
                            				myAction = myAction + toTextShuttleFlight(myPlayerID, shuttleFlightCity);
                            			}else {
                            				myAction = myAction + toTextShuttleFlight(myPlayerID, "Atlanta");
                            			}
                            		}else if(myCurrentCity.equals("Karachi") && !myBoard.searchForCity("Hong Kong").getHasReseachStation()) {

                                		myAction = myAction + toTextDriveTo(myPlayerID, "Delhi");
                                		myAction = myAction + toTextDriveTo(myPlayerID, "Kolkata");
                                		myAction = myAction + toTextDriveTo(myPlayerID, "Hong Kong");
                                		myAction = myAction + toTextBuildRS(myPlayerID, "Hong Kong");
                            		}else
                            			myAction = MonteCarloTreeSearch.findNextMove(myBoard, myPlayerID, numberOfPlayers);
                        			
                            	}else
                            		myAction = MonteCarloTreeSearch.findNextMove(myBoard, myPlayerID, numberOfPlayers);
                        		suggestions[myPlayerID] = myAction;
                				suggestionBoards[myPlayerID] = decodeActions(myAction, suggestionBoards[myPlayerID]);
                				
                				double avgFinal[] = new double[numberOfPlayers];
                				double avgNext[] = new double[numberOfPlayers];
                				
                				for(int i = 0; i < numberOfPlayers; i++) {
                					avgNext[i] = evaluateBoardNext(suggestionBoards[i], numberOfPlayers);
                					avgFinal[i] = 0;
                					for(int j = 0; j < 100; j++) {
                						State state = new State(suggestions[i], suggestionBoards[i], i, 0, numberOfPlayers);
                						ActionNode node = new ActionNode(state);
                						avgFinal[i] += MonteCarloTreeSearch.simulateRandomPlayout(node, numberOfPlayers);
                					}
                					avgFinal[i] = avgFinal[i]/100;
                					
                        		}
                				//assume we chose the best actions out of all the players
                				newMyAction = myAction;
                				double newAvgNext =  avgNext[myPlayerID];
                				double newAvgFinal = avgFinal[myPlayerID];
                				for(int i = 0; i < numberOfPlayers; i++) {
                					if(myBoard.getRound()<3* numberOfPlayers) {
                						if(avgNext[i] > avgNext[myPlayerID]) {
                							evaluatePlayers[i] += 10;
                						}else if(avgNext[i] < avgNext[myPlayerID]) {
                							evaluatePlayers[i] -= 10;
                						}
                						
                						if(avgFinal[i] > avgFinal[myPlayerID]) {
                							evaluatePlayers[i] += 5;
                						}else if(avgFinal[i] < avgFinal[myPlayerID]) {
                							evaluatePlayers[i] -= 5;
                						}
                						
                						
                					}else {
                						if(avgNext[i] > avgNext[myPlayerID]) {
                							evaluatePlayers[i] += 5;
                						}else if(avgNext[i] < avgNext[myPlayerID]) {
                							evaluatePlayers[i] -= 5;
                						}
                						
                						if(avgFinal[i] > avgFinal[myPlayerID]) {
                							evaluatePlayers[i] += 10;
                						}else if(avgFinal[i] < avgFinal[myPlayerID]) {
                							evaluatePlayers[i] -= 10;
                						}
                						
                					}
                					//select best action sequence out of suggestions
            						if(avgNext[i] > newAvgNext && avgFinal[i] > newAvgFinal) {
            							newMyAction = suggestions[i];
            							newAvgNext = avgNext[i];
            							newAvgFinal = avgFinal[i];
            						}
            						
            						if(evaluatePlayers[i] > 50)
            							newMyAction = suggestions[i];
                        		}
                				
                				
                        	}
                        	else {
                        		int suggestionPlayer = myBoard.getWhoIsPlaying();
                        		ArrayList<String> sugHand = myBoard.getHandOf(suggestionPlayer);
                        		//selecting my suggestion
                        		if(myBoard.getRoleOf(suggestionPlayer).equals("Operations Expert") && myBoard.getRound() == suggestionPlayer + 1) {
                            		if(!myBoard.searchForCity("Bogota").getHasReseachStation()) {
                            			mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Miami");
                                		mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Bogota");
                                		mySuggestion = mySuggestion + toTextBuildRS(suggestionPlayer, "Bogota");
                                		mySuggestion = mySuggestion + toTextOET(suggestionPlayer, "Instabul", sugHand.get(0));
                            		}else if(!myBoard.searchForCity("Instabul").getHasReseachStation()) {
                            			mySuggestion = mySuggestion +  toTextOET(suggestionPlayer, "Instabul", sugHand.get(0));
                            			mySuggestion = mySuggestion + toTextBuildRS(suggestionPlayer, "Instabul");
                                		mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Baghdad");
                                		mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Karachi");
                            		}else if(!myBoard.searchForCity("Hong Kong").getHasReseachStation()) {
                            			mySuggestion = mySuggestion +  toTextOET(suggestionPlayer, "Hong Kong", sugHand.get(0));
                            			mySuggestion =mySuggestion + toTextBuildRS(suggestionPlayer, "Hong Kong");
                                		distanceMap = buildDistanceMap(myBoard, "Hong Kong", distanceMap);
                                		String destination = getMostInfectedInRadius(2, distanceMap, myBoard);
                                		if(destination!=null) {
                                			String cityTo = getDirectionToMove("Hong Kong", destination, distanceMap, myBoard);
                                			mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, cityTo);
                                    		distanceMap = buildDistanceMap(myBoard, cityTo, distanceMap);
                                    		cityTo = getDirectionToMove("Hong Kong", destination, distanceMap, myBoard);
                                			if(destination!= cityTo) {
                                				mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, cityTo);
                                			}else {
                                				mySuggestion = mySuggestion + toTextTreatDisease(suggestionPlayer, cityTo, myBoard.searchForCity(cityTo).getMaxCubeColor());
                                			}
                                		}
                                		
                            		}else
                            			mySuggestion = MonteCarloTreeSearch.findNextMove(myBoard, suggestionPlayer, numberOfPlayers);
                            	}else if(myBoard.getRoleOf(suggestionPlayer).equals("Operations Expert") && myBoard.getRound() == suggestionPlayer + 1 + numberOfPlayers) {
                            		
                        			
                        			if(myCurrentCity.equals("Instabul") && !myBoard.searchForCity("Hong Kong").getHasReseachStation()) {
                        				mySuggestion = mySuggestion + toTextBuildRS(suggestionPlayer, "Instabul");
                        				mySuggestion = mySuggestion + toTextOET(suggestionPlayer, "Hong Kong", myHand.get(0));
                                		mySuggestion = mySuggestion + toTextBuildRS(suggestionPlayer, "Hong Kong");
                                		String shuttleFlightCity = getDestinationFromAnotherRS(4, myCurrentCity, distanceMap, myBoard);
                            			if(shuttleFlightCity!= null) {
                            				mySuggestion = mySuggestion + toTextShuttleFlight(suggestionPlayer, shuttleFlightCity);
                            			}else {
                            				mySuggestion = mySuggestion + toTextShuttleFlight(suggestionPlayer, "Atlanta");
                            			}
                            		}else if(myCurrentCity.equals("Karachi") && !myBoard.searchForCity("Hong Kong").getHasReseachStation()) {

                            			mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Delhi");
                            			mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Kolkata");
                                		mySuggestion = mySuggestion + toTextDriveTo(suggestionPlayer, "Hong Kong");
                                		mySuggestion = mySuggestion + toTextBuildRS(suggestionPlayer, "Hong Kong");
                            		}else
                            			mySuggestion = MonteCarloTreeSearch.findNextMove(myBoard, suggestionPlayer, numberOfPlayers);
                        			
                            	}else
                            		mySuggestion = MonteCarloTreeSearch.findNextMove(myBoard, suggestionPlayer, numberOfPlayers);
                        		
                        	}
                        	
                        	
                        	
                        	
                        	// UP TO HERE!! DON'T FORGET TO EDIT THE "msgToSend"
                        	
                        	// Message type 
                        	// toTextShuttleFlight(0,Atlanta)+"#"+etc
                        	String msgToSend;
                        	if (myBoard.getWhoIsPlaying() == myPlayerID)
                        		msgToSend = myAction;
                        		
                        		//msgToSend = "AP,"+myPlayerID+"#AP,"+myPlayerID+"#AP,"+myPlayerID+"#C,"+myPlayerID+",This was my action#AP,"+myPlayerID+"#C,"+myPlayerID+",This should not be printed..";//"Action";
                            else 
                        		msgToSend = mySuggestion;// "#C,"+myPlayerID+",This was my recommendation"; //"Recommendation"
                        	
                        	// NO EDIT FROM HERE AND ON (EXEPT FUNCTIONS OUTSIDE OF MAIN() OF COURSE)
                        	
                        	// Writing to Server
                        	dos.flush();
                        	dos.reset();
                        	if (msgToSend != "")
                        		msgToSend = msgToSend.substring(1); // Removing the initial delimeter
                        	dos.writeObject(msgToSend);
                        	System.out.println(myUsername + " : I've just sent my " + msgToSend);
                        	currentBoard[0].setTalkedForThisTurn(true, myPlayerID);
                        }
                    } catch (IOException e) { 
                        e.printStackTrace(); 
					}
                } 
            }
        }); 
          
        // Creating readMessage thread 
        Thread readMessage = new Thread(new Runnable()  
        { 
            @Override
            public void run() { 
            	
            	
                while (currentBoard[0].getGameEnded() == false) { 
                    try { 
                        
                    	// Reading the current board
                    	//System.out.println("READING!!!");
                    	currentBoard[0] = (Board)dis.readObject();
                    	//System.out.println("READ!!!");
                    	
                    	// Read and print Message to all clients
                    	String prtToScreen = currentBoard[0].getMessageToAllClients();
                    	if (!prtToScreen.equalsIgnoreCase(""))
                    		System.out.println(prtToScreen);
                    	
                    	// Read and print Message this client
                    	prtToScreen = currentBoard[0].getMessageToClient(myPlayerID);
                    	if (!prtToScreen.equalsIgnoreCase(""))
                    		System.out.println(prtToScreen);
                    	
                    } catch (IOException e) { 
                        e.printStackTrace(); 
                    } catch (ClassNotFoundException e) {
						e.printStackTrace();
					} 
                } 
            } 
        }); 
        
        // Starting the threads
        readMessage.start();
        sendMessage.start(); 
        
        // Checking if the game has ended
        while (true) 
        {
        	if (currentBoard[0].getGameEnded() == true) {
        		System.out.println("\nGame has finished. Closing resources.. \n");
        		//scn.close();
            	s.close();
            	System.out.println("Recources closed succesfully. Goodbye!");
            	System.exit(0);
            	break;
        }
        
        }
    } 
    
    //new functions
    /*
     * Find the closest RS from the current city within radius
     */
    
	// Useful functions
	
	public static Board decodeActions(String toRead, Board board)
	{
		String delimiterActions = "#";
		String delimiterVariables = ",";
		
		String[] actions;
		String[] variables;
		
		int actionCounter = 0;
		
		actions = toRead.split(delimiterActions);
		
		for (int i = 0 ; i < actions.length; i++)
		{
			variables = actions[i].split(delimiterVariables);
			
			if (variables[0].equals("DT"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " drives to " + variables[2]);
				board.driveTo(Integer.parseInt(variables[1]), variables[2]);
				actionCounter++;
			}
				
			else if (variables[0].equals("DF"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " takes a direct flight to " + variables[2]);
				board.directFlight(Integer.parseInt(variables[1]), variables[2]);
				actionCounter++;
			}
				
			else if (variables[0].equals("CF"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " takes a charter flight to " + variables[2]);
				board.charterFlight(Integer.parseInt(variables[1]), variables[2]);
				actionCounter++;
			}
				
			else if (variables[0].equals("SF"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " takes a shuttle flight to " + variables[2]);
				board.shuttleFlight(Integer.parseInt(variables[1]), variables[2]);
				actionCounter++;
			}
				
			else if (variables[0].equals("BRS"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " is building a Research Station to " + variables[2]);
				board.buildRS(Integer.parseInt(variables[1]), variables[2]);
				actionCounter++;
			}
			else if (variables[0].equals("RRS"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " is removing a Reseaerch Station from " + variables[2]);
				board.removeRS(Integer.parseInt(variables[1]), variables[2]);
			}
			else if (variables[0].equals("TD"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " is treating the " + variables[3] + " disease from " + variables[2]);
				board.treatDisease(Integer.parseInt(variables[1]), variables[2], variables[3]);
				actionCounter++;
			}
			else if (variables[0].equals("CD1"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " is curing the " + variables[2] + " disease");
				board.cureDisease(Integer.parseInt(variables[1]), variables[2]);
				actionCounter++;
			}
			else if (variables[0].equals("CD2"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " is curing the " + variables[2] + " disease and throws " + variables[3] + variables[4] + variables[5] + variables[6] );
				board.cureDisease(Integer.parseInt(variables[1]), variables[2], variables[3], variables[4], variables[5], variables[6]);
				actionCounter++;
			}
			else if (variables[0].equals("AP"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " decided to pass this action");
				board.actionPass(Integer.parseInt(variables[1]));
				actionCounter++;
			}
			else if (variables[0].equals("C"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " sends the following message: " + variables[2]);
				board.chatMessage(Integer.parseInt(variables[1]), variables[2]);
			}
			else if (variables[0].equals("PA"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " plays Airlift. Moving player " + Integer.parseInt(variables[2]) + " to " + variables[3]);
				
			}
			else if (variables[0].equals("OET"))
			{
				//System.out.println("Player " + Integer.parseInt(variables[1]) + " travels to " + variables[2] + " as the Operations Expert");
				board.operationsExpertTravel(Integer.parseInt(variables[1]), variables[2], variables[3]);
				actionCounter++;
			}
			
			
			if (actionCounter >= 4)
			{
				System.out.println("\nYou reached the maximum actions for this turn..");
				break;
			}
		}
		return board;
	}
	
    
    
    public static String findClosestRS(ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard)
    {
    	String closestRS = null;
    	int min = 1000;
    	
    	ArrayList<String> RSs = myBoard.getRSLocations();
    	
    	for(int RS = 0; RS < RSs.size(); RS++) {
    		if( distanceFrom(RSs.get(RS), distanceMap) <= min ) {
    			min = distanceFrom(RSs.get(RS), distanceMap);
    			closestRS = RSs.get(RS);
    		}
    	}
    	return closestRS;
    }
    
    
    
    public static boolean shouldTravelThroughRS(ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, String goalCity) {
    	String ClosestRS = findClosestRS(distanceMap, myBoard);
    	ArrayList<citiesWithDistancesObj> RSdmap = new ArrayList<citiesWithDistancesObj>();
    	RSdmap = buildDistanceMap(myBoard, ClosestRS, RSdmap);
    	
    	int distanceThroughRS = distanceFrom( ClosestRS , distanceMap) + 1 + distanceFrom( goalCity , RSdmap);
    	
    	int distanceByFoot =  distanceFrom( goalCity , distanceMap);
    	
    	if(distanceThroughRS < distanceByFoot) return true;
    	else
    		return false;
    }

    /*
     * Build a RS as a non-OPEX player
     */
    public static String simpleRSBuild(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, ArrayList<String> hand)
    {
    	String RSToBuild = "";
    	ArrayList<String> allRSs = myBoard.getRSLocations();
    	
    	for(int j=0; j < hand.size(); j++) {
    		boolean shouldbuild = true;
    		
    		for(int RS = 0; RS < allRSs.size(); RS++) {

    	    	ArrayList<citiesWithDistancesObj> RSdistanceMap;
    	    	RSdistanceMap = new ArrayList<citiesWithDistancesObj>();
    			RSdistanceMap = buildDistanceMap(myBoard, allRSs.get(RS), RSdistanceMap);
    			
    			
        		if(distanceFrom(hand.get(j), RSdistanceMap) < 3 ) {
        			shouldbuild = false;
        		}
    			
    		}
    		
    		if(shouldbuild)
    			RSToBuild = hand.get(j);
			
		}
	
    	return RSToBuild;
    }
    

    public static String getMostDangerous(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, int numberOfPlayers)
    {
    	int maxCubes = -1;
    	String mostInfectedWithMostNeighbours = null;
    	int mostNeighbours = -1;
    	
    	ArrayList<String> playerPositions = new ArrayList<String>();
    	
    	for(int i =0; i < numberOfPlayers; i++) {
    		playerPositions.add(myBoard.getPawnsLocations(i));
    	}
    	
    	
    	for (int i = 1 ; i < distanceMap.size() ; i++)
    	{
    		if (distanceMap.get(i).getDistance() <= radius)
    		{
    			String cityName = distanceMap.get(i).getName();
    			City cityToCheck = myBoard.searchForCity(cityName);
    			
    			if (cityToCheck.getMaxCube() > maxCubes && !playerPositions.contains(cityName) && !myBoard.getCured(cityToCheck.getColour()) )
    			{
    				mostInfectedWithMostNeighbours = cityToCheck.getName();
    				maxCubes = cityToCheck.getMaxCube();
    				mostNeighbours = cityToCheck.getNeighboursNumber();
    			}
    			else if (cityToCheck.getMaxCube() == maxCubes && !playerPositions.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) ) {
    				if(cityToCheck.getNeighboursNumber() > mostNeighbours) {
    					mostInfectedWithMostNeighbours = cityToCheck.getName();
        				maxCubes = cityToCheck.getMaxCube();
        				mostNeighbours = cityToCheck.getNeighboursNumber();
    				}
    			}
    		}
    	}
    	String ClosestRS = findClosestRS(distanceMap, myBoard);
		int distanceFromRS = distanceFrom(ClosestRS, distanceMap) + 1;
    	if(distanceFromRS < radius) {
    		for(int RS = 0; RS < myBoard.getRSLocations().size();RS++) {
    			
    			ArrayList<citiesWithDistancesObj> RSdmap = new ArrayList<citiesWithDistancesObj>();
        		RSdmap = buildDistanceMap(myBoard,myBoard.getRSLocations().get(RS), RSdmap);
        		
        		for (int i = 0 ; i < RSdmap.size() ; i++)
            	{
            		if (RSdmap.get(i).getDistance() <= radius - distanceFromRS)
            		{
            			String cityName = distanceMap.get(i).getName();
            			City cityToCheck = myBoard.searchForCity(cityName);
            			if (cityToCheck.getMaxCube() > maxCubes && !playerPositions.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) )
            			{
            				mostInfectedWithMostNeighbours = cityToCheck.getName();
            				maxCubes = cityToCheck.getMaxCube();
            				mostNeighbours = cityToCheck.getNeighboursNumber();
            			}
            			else if (cityToCheck.getMaxCube() == maxCubes && !playerPositions.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) ) {
            				if(cityToCheck.getNeighboursNumber() > mostNeighbours) {
            					mostInfectedWithMostNeighbours = cityToCheck.getName();
                				maxCubes = cityToCheck.getMaxCube();
                				mostNeighbours = cityToCheck.getNeighboursNumber();
            				}
            			}
            		}
            	}
    			
    		}
    		
    		
    	}
    	
    	return mostInfectedWithMostNeighbours;
    }
    

    public static String getMostDangerous3(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, int numberOfPlayers)
    {
    	int maxCubes = 3;
    	String mostInfectedWithMostNeighbours = null;
    	int mostNeighbours = -1;
    	
    	ArrayList<String> playerPositions = new ArrayList<String>();
    	
    	for(int i =0; i < numberOfPlayers; i++) {
    		playerPositions.add(myBoard.getPawnsLocations(i));
    	}
    	
    	
    	for (int i = 0 ; i < distanceMap.size() ; i++)
    	{
    		if (distanceMap.get(i).getDistance() <= radius)
    		{
    			String cityName = distanceMap.get(i).getName();
    			City cityToCheck = myBoard.searchForCity(cityName);
    			
    			if (cityToCheck.getMaxCube() == maxCubes && !playerPositions.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) ) {
    				if(cityToCheck.getNeighboursNumber() > mostNeighbours) {
    					mostInfectedWithMostNeighbours = cityToCheck.getName();
        				maxCubes = cityToCheck.getMaxCube();
        				mostNeighbours = cityToCheck.getNeighboursNumber();
    				}
    			}
    		}
    	}
    	String ClosestRS = findClosestRS(distanceMap, myBoard);
		int distanceFromRS = distanceFrom(ClosestRS, distanceMap) + 1;
    	if(distanceFromRS < radius) {
    		for(int RS = 0; RS < myBoard.getRSLocations().size();RS++) {
    			
    			ArrayList<citiesWithDistancesObj> RSdmap = new ArrayList<citiesWithDistancesObj>();
        		RSdmap = buildDistanceMap(myBoard,myBoard.getRSLocations().get(RS), RSdmap);
        		
        		for (int i = 0 ; i < RSdmap.size() ; i++)
            	{
            		if (RSdmap.get(i).getDistance() <= radius - distanceFromRS)
            		{
            			String cityName = distanceMap.get(i).getName();
            			City cityToCheck = myBoard.searchForCity(cityName);
            			if (cityToCheck.getMaxCube() == maxCubes && !playerPositions.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) ) {
            				if(cityToCheck.getNeighboursNumber() > mostNeighbours) {
            					mostInfectedWithMostNeighbours = cityToCheck.getName();
                				maxCubes = cityToCheck.getMaxCube();
                				mostNeighbours = cityToCheck.getNeighboursNumber();
            				}
            			}
            		}
            	}
    			
    		}
    		
    		
    	}
    	
    	return mostInfectedWithMostNeighbours;
    }
    
    
    
    public static ArrayList<String> getOptimalCitiesForRS(Board myBoard){
        ArrayList<String> OptimalCitiesForRS = new ArrayList<>(Arrays.asList("Atlanta", "Sao Paulo", "Instabul", "Hong Kong"));
        ArrayList<String> RSCities = myBoard.getRSLocations();
        /*check if there is RS in the OptimalCities*/
        OptimalCitiesForRS.removeAll(RSCities);
        return OptimalCitiesForRS;
    }
    
    public static ArrayList<String> getDangerousCities(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, int numberOfPlayers)
    {
    	int maxCubes = 3;
    	ArrayList<String> mostInfected = new ArrayList<String>();
    	
    	ArrayList<String> QSProtections = new ArrayList<String>();
    	
    	for(int i =0; i < numberOfPlayers; i++) {
    		if( myBoard.getRoleOf(i).equals("Quarantine Specialist")){
    			String QSCityName = myBoard.getPawnsLocations(i);
    			City QSCity = myBoard.searchForCity(QSCityName);
    			for(int j = 0; j < QSCity.getNeighboursNumber(); j++) {
    				QSProtections.add(QSCity.getNeighbour(j));
    			}
    			
    			
    		}
    		
    	}
    	
    	
    	for (int i = 0 ; i < distanceMap.size() ; i++)
    	{
    		if (distanceMap.get(i).getDistance() <= radius)
    		{
    			String cityName = distanceMap.get(i).getName();
    			City cityToCheck = myBoard.searchForCity(cityName);
    			
    			if (cityToCheck.getMaxCube() == maxCubes && !QSProtections.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) ) {
    				mostInfected.add(cityToCheck.getName());
    			}
    		}
    	}
    	String ClosestRS = findClosestRS(distanceMap, myBoard);
		int distanceFromRS = distanceFrom(ClosestRS, distanceMap) + 1;
    	if(distanceFromRS < radius) {
    		for(int RS = 0; RS < myBoard.getRSLocations().size();RS++) {
    			
    			ArrayList<citiesWithDistancesObj> RSdmap = new ArrayList<citiesWithDistancesObj>();
        		RSdmap = buildDistanceMap(myBoard,myBoard.getRSLocations().get(RS), RSdmap);
        		
        		for (int i = 0 ; i < RSdmap.size() ; i++)
            	{
            		if (RSdmap.get(i).getDistance() <= radius - distanceFromRS)
            		{
            			String cityName = distanceMap.get(i).getName();
            			City cityToCheck = myBoard.searchForCity(cityName);
            			if (cityToCheck.getMaxCube() == maxCubes  && !QSProtections.contains(cityName)  && !myBoard.getCured(cityToCheck.getColour()) ) {
            				mostInfected.add(cityToCheck.getName());
            			}
            		}
            	}
    			
    		}
    		
    		
    	}
    	
    	return mostInfected;
    }
    
    public static String getMostDangerous(ArrayList<String> mostInfected, Board myBoard) {
    	
    	String mostDangerous = null;
    	int mostNeighbours = -1;
    	for(int i= 0; i< mostInfected.size(); i++) {
    		if(myBoard.searchForCity(mostInfected.get(i)).getNeighboursNumber() > mostNeighbours ) {
    			mostDangerous = mostInfected.get(i);
    			mostNeighbours = myBoard.searchForCity(mostInfected.get(i)).getNeighboursNumber();
    		}
    		
    	}
    	return mostDangerous;
    }
    
    public static String getMostDangerous(ArrayList<String> mostInfected, Board myBoard, String color) {
    	
    	String mostDangerous = null;
    	int mostNeighbours = -1;
    	for(int i= 0; i< mostInfected.size(); i++) {
    		if(myBoard.searchForCity(mostInfected.get(i)).getNeighboursNumber() > mostNeighbours && myBoard.searchForCity(mostInfected.get(i)).getMaxCubeColor().equals(color)) {
    			mostDangerous = mostInfected.get(i);
    			mostNeighbours = myBoard.searchForCity(mostInfected.get(i)).getNeighboursNumber();
    		}
    		
    	}
    	return mostDangerous;
    }
    
    
    public static String getMostDangerousOutOfReach(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, int numberOfPlayers, int remainingActions)
    {
    	ArrayList<String> mostDangerousOnMap = getDangerousCities(20, distanceMap, myBoard, numberOfPlayers);
    	ArrayList<String> mostDangerousInRadius = getDangerousCities(remainingActions, distanceMap, myBoard, numberOfPlayers);
    	mostDangerousOnMap.removeAll(mostDangerousInRadius);
    	
    	return getMostDangerous(mostDangerousOnMap, myBoard);
    }
    
    public static String getMostDangerousOutOfReach(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard, int numberOfPlayers, int remainingActions, String color)
    {
    	ArrayList<String> mostDangerousOnMap = getDangerousCities(20, distanceMap, myBoard, numberOfPlayers);
    	ArrayList<String> mostDangerousInRadius = getDangerousCities(remainingActions, distanceMap, myBoard, numberOfPlayers);
    	mostDangerousOnMap.removeAll(mostDangerousInRadius);
    	
    	return getMostDangerous(mostDangerousOnMap, myBoard, color);
    }
    
    
    public static String getMostInfectedWithMostNeighbours(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard)
        {
        	int maxCubes = -1;
        	String mostInfectedWithMostNeighbours = null;
        	int mostNeighbours = -1;
        	
        	for (int i = 0 ; i < distanceMap.size() ; i++)
        	{
        		if (distanceMap.get(i).getDistance() <= radius)
        		{
        			City cityToCheck = myBoard.searchForCity(distanceMap.get(i).getName());
        			
        			if (cityToCheck.getMaxCube() > maxCubes)
        			{
        				mostInfectedWithMostNeighbours = cityToCheck.getName();
        				maxCubes = cityToCheck.getMaxCube();
        				mostNeighbours = cityToCheck.getNeighboursNumber();
        			}
        			else if (cityToCheck.getMaxCube() == maxCubes) {
        				if(cityToCheck.getNeighboursNumber() > mostNeighbours) {
        					mostInfectedWithMostNeighbours = cityToCheck.getName();
            				maxCubes = cityToCheck.getMaxCube();
            				mostNeighbours = cityToCheck.getNeighboursNumber();
        				}
        			}
        		}
        	}
        	
        	return mostInfectedWithMostNeighbours;
        }
    
    /*
     * 	Find a new City to travel to according the RS already build 
     * 		used if player is on a city with a RS
     * 		but there is no city that needs treatment in appropriate radius
     * 		returns another RS to fly to and the city in need close to that RS
     * 		Note: There is no need to return the city we need to treat
     * 			since we can't save it and the agent will be able to identify
     * 			it on the next iteration
     */
    public static String getDestinationFromAnotherRS(int radius, String myCurrentCity, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard){
		String newRS = null;
    	if(myBoard.getRSLocations().size() > 1) {
			//find all RS on board
			
			ArrayList<String> ResearchStations = myBoard.getRSLocations();
			
			int maxCubesOfOtherRSs = -1;
			
			ArrayList<citiesWithDistancesObj> RSdistanceMap;
			for(int RSs = 0; RSs< ResearchStations.size(); RSs++) {
				//if RS exists
				if(!ResearchStations.get(RSs).equals(myCurrentCity)) {
					RSdistanceMap = new ArrayList<citiesWithDistancesObj>();
					RSdistanceMap = buildDistanceMap(myBoard, ResearchStations.get(RSs), RSdistanceMap);
					//if not try to find the most infected city in radius = remaining actions - 1(move to fly to the RS)
					String temp_destinationCloseOfOtherRSs = getMostInfectedWithMostNeighbours(radius, distanceMap, myBoard);
					if(temp_destinationCloseOfOtherRSs != null) {
						City cityToCheck = myBoard.searchForCity(temp_destinationCloseOfOtherRSs);
						if(cityToCheck.getMaxCube() >  maxCubesOfOtherRSs) {
							maxCubesOfOtherRSs = cityToCheck.getMaxCube();
							newRS = ResearchStations.get(RSs);
						}
					}
				
				}
			
			}
    	}
		return newRS;
    }
    
    
    public static String[] findAllRSs(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard)
    {
    	String allRSs[];
    	
    	//maximum number of RSs
    	//initialize array
    	allRSs = new String[6];
    	for(int j = 0; j<6; j++) {
    		allRSs[j] = "";
    	}
    	int allRSsCount = 0;
    	//check the whole map for RSs
    	for (int i = 0 ; i < distanceMap.size() ; i++)
    	{
    		
    		City cityToCheck = myBoard.searchForCity(distanceMap.get(i).getName());
			
			if (cityToCheck.getHasReseachStation())
			{
				allRSs[allRSsCount++] = cityToCheck.getName();
			}
    	}
    	
    	return allRSs;
    }
    
    
    // --> Useful functions <--
    
    public static Board copyBoard (Board boardToCopy)
    {
    	Board copyOfBoard;
    	
    	try {
    	     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	     ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
    	     outputStrm.writeObject(boardToCopy);
    	     ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    	     ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
    	     copyOfBoard = (Board)objInputStream.readObject();
    	     return copyOfBoard;
    	   }
    	   catch (Exception e) {
    	     e.printStackTrace();
    	     return null;
    	   }
    }
    
    public static String getDirectionToMove (String startingCity, String goalCity, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard)
    {
    	City startingCityObj = myBoard.searchForCity(startingCity);
    	
    	int minDistance = distanceFrom(goalCity, distanceMap);
    	int testDistance = 999;
    	
    	String directionToDrive = null;
    	String testCity = null;
    	
    	for (int i = 0 ; i < startingCityObj.getNeighboursNumber() ; i++)
    	{
    		ArrayList<citiesWithDistancesObj> testDistanceMap = new ArrayList<citiesWithDistancesObj>();
    		testDistanceMap.clear();
    		
    		testCity = startingCityObj.getNeighbour(i);
    		testDistanceMap = buildDistanceMap(myBoard, testCity, testDistanceMap);
    		testDistance = distanceFrom(goalCity, testDistanceMap);
    		
    		if (testDistance < minDistance)
    		{
    			minDistance = testDistance;
    			directionToDrive = testCity;
    		}
    	}
    	return directionToDrive;
    }
    
    
    public static String getMostInfectedInRadius(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard)
    {
    	//maxCubes = 0 instead of -1
    	//if there is no infected city in radius return null
    	int maxCubes = 0;
    	String mostInfected = null;
    	
    	for (int i = 0 ; i < distanceMap.size() ; i++)
    	{
    		if (distanceMap.get(i).getDistance() <= radius)
    		{
    			City cityToCheck = myBoard.searchForCity(distanceMap.get(i).getName());
    			
    			if (cityToCheck.getMaxCube() > maxCubes)
    			{
    				mostInfected = cityToCheck.getName();
    				maxCubes = cityToCheck.getMaxCube();
    			}
    		}
    	}
    	
    	return mostInfected;
    }
    

    
    
    // Count how many card of the color X player X has
    public static int cardsCounterOfColor(Board board, int  playerID, String color)
    {
    	int cardsCounter = 0;
    	
    	for (int i = 0 ; i < board.getHandOf(playerID).size() ; i++)
    		if (board.searchForCity(board.getHandOf(playerID).get(i)).getColour().equals(color))
    			cardsCounter++;
    	
    	return cardsCounter;
    }
    
    public static void printHand(ArrayList<String> handToPrint)
    {
    	for (int i = 0 ; i < handToPrint.size() ; i++)
    		System.out.println(handToPrint.get(i));
    }
    
    public static boolean alredyInDistanceMap(ArrayList<citiesWithDistancesObj> currentMap, String cityName)
    {
    	for (int i = 0 ; i < currentMap.size() ; i++)
    		if (currentMap.get(i).getName().equals(cityName))
    			return true;
    	
    	return false;
    }
    
    public static boolean isInDistanceMap (ArrayList<citiesWithDistancesObj> currentMap, String cityName)
    {
    	for (int i = 0 ; i < currentMap.size() ; i++)
    	{
    		if (currentMap.get(i).getName().equals(cityName))
    			return true;
    	}
    	return false;
    }
    
    public static void printDistanceMap(ArrayList<citiesWithDistancesObj> currentMap)
    {
    	for (int i = 0 ; i < currentMap.size() ; i++)
    		System.out.println("Distance from " + currentMap.get(i).getName() + ": " + currentMap.get(i).getDistance());
    }
    
    public static int distanceFrom(String cityToFind, ArrayList<citiesWithDistancesObj> currentDistanceMap)
    {
    	int result = -1;
    	
    	for (int i = 0 ; i < currentDistanceMap.size() ; i++)
    		if (currentDistanceMap.get(i).getName().equals(cityToFind))
    			result = currentDistanceMap.get(i).getDistance();
    	
    	return result;
    }
    
    public static int numberOfCitiesWithDistance(int distance, ArrayList<citiesWithDistancesObj> currentDistanceMap)
    {
    	int count = 0;
    	
    	for (int i = 0 ; i < currentDistanceMap.size() ; i++)
    		if (currentDistanceMap.get(i).getDistance() == distance)
    			count++;
    	
    	return count;
    }
    
    public static ArrayList<citiesWithDistancesObj> buildDistanceMap(Board myBoard, String currentCityName, ArrayList<citiesWithDistancesObj> currentMap)
    {
    	currentMap.clear();
    	currentMap.add(new citiesWithDistancesObj(currentCityName, myBoard.searchForCity(currentCityName), 0));

    	for (int n = 0 ; n < 15 ; n++)
    	{
        	for (int i = 0 ; i < currentMap.size() ; i++)
        	{
        		if (currentMap.get(i).getDistance() == (n-1))
        		{
        			for (int j = 0 ; j < currentMap.get(i).getCityObj().getNeighboursNumber() ; j++)
        			{
        				String nameOfNeighbor = currentMap.get(i).getCityObj().getNeighbour(j);
        				
        				if (!(alredyInDistanceMap(currentMap, nameOfNeighbor)))
        					currentMap.add(new citiesWithDistancesObj(nameOfNeighbor, myBoard.searchForCity(nameOfNeighbor), n));
        			}
        		}
        	}
    	}
    	
    	return currentMap;
    }
    
    public static double getModifiedInfectionsInRadius(int radius, ArrayList<citiesWithDistancesObj> distanceMap, Board myBoard)
    {
    	double cubes = 0;

    	for (int i = 0 ; i < distanceMap.size() ; i++)
    	{
    		if (distanceMap.get(i).getDistance() <= radius)
    		{
    			
    			City cityToCheck = myBoard.searchForCity(distanceMap.get(i).getName());
    			
    			cubes += (1 - distanceMap.get(i).getDistance() * 0.2) * cityToCheck.getMaxCube();
    			
    		
    		}
    	}
    	
    	return cubes;
    }
    
    
    
    public static double evaluateBoardNext(Board myBoard, int numberOfPlayers)
    {
    	double score = 0;
    	for(int i = 0; i < 3; i++)
    		//-100 for every disease not cured
    		score += !myBoard.getCured(i)? -100: 0;
    	

    	ArrayList<String> playersDeck = myBoard.getPlayersDeck();
    	int epidemicCardsLeft = 0;
    	for (int i = 0 ; i < playersDeck.size() ; i++)
			if (playersDeck.get(i).equals("Epidemic"))
				epidemicCardsLeft++;
    	double epidemicProbabilty = 0; 
    	if(playersDeck.size()>0) epidemicProbabilty = epidemicCardsLeft / playersDeck.size();
    	
    	ArrayList<citiesWithDistancesObj> distanceMap = new ArrayList<citiesWithDistancesObj>();
    	distanceMap = buildDistanceMap(myBoard, myBoard.getPawnsLocations(0), distanceMap);
    	
    	for (int i = 0 ; i < distanceMap.size() ; i++)
    	{
    		//-1 for every cube in the board
    		City cityToCheck = myBoard.searchForCity(distanceMap.get(i).getName());
			
			score -= cityToCheck.getCubes("Red");
			score -= cityToCheck.getCubes("Black");
			score -= cityToCheck.getCubes("Blue");
			score -= cityToCheck.getCubes("Yellow");
			
			if(cityToCheck.getMaxCube() == 3) {
				score -= cityToCheck.getNeighboursNumber() * 1;
				score -= cityToCheck.getMaxCube() ;
			}
			
    	}
    	//-10 for every outbreak
    	score -= myBoard.getOutbreaksCount() * 20;
    	
    	
    	//positive contributions
    	
    	//the cubes close to a player have less weight
    	for(int i = 0; i < numberOfPlayers; i ++) {
    		String playerCity = myBoard.getPawnsLocations(i);
    		ArrayList<citiesWithDistancesObj> dmap;
    		dmap = new ArrayList<citiesWithDistancesObj>();
    		dmap = buildDistanceMap(myBoard, playerCity, dmap);
    		score += getModifiedInfectionsInRadius(3,dmap, myBoard);
    		
    		if(myBoard.getRoleOf(i).equals("Quarantine Specialist")) {
    			City cityToCheck = myBoard.searchForCity(playerCity);
    			if(cityToCheck.getMaxCube() == 3) {
    				score += cityToCheck.getNeighboursNumber() * (1+epidemicProbabilty);
    				
    			}
    		}
    		String closestRS = findClosestRS(dmap, myBoard);
    		if(distanceFrom(closestRS, dmap) <= 3)
    			score += 1;
    		

    		ArrayList<String> playerHand = myBoard.getHandOf(i);
    		int blackCount = 0, redCount = 0, yellowCount = 0, blueCount = 0;
    		for (int j = 0 ; j < playerHand.size() ; j++)
			{
				City cityToCheck = myBoard.searchForCity(playerHand.get(j));
				
				if (cityToCheck.getColour().equals("Black") && !myBoard.getCured("Black"))
					blackCount ++;
				else if(cityToCheck.getColour().equals("Yellow")  && !myBoard.getCured("Yellow"))
					redCount++;
				else if (cityToCheck.getColour().equals("Blue")  && !myBoard.getCured("Blue"))
					blueCount ++;
				else if(cityToCheck.getColour().equals("Red") && !myBoard.getCured("Red"))
					redCount++;
			}
    		int maxCards = max(blackCount, redCount, yellowCount, blueCount);
    		score += maxCards * 10;
    		
    	}
    	
    	//+5 for every RS build up to 4 +2.5 for each for more
    	//additional +5 for RS in optimal positions
    	score += myBoard.getRSLocations().size() <= 4 ? myBoard.getRSLocations().size() * 5 : (20 + (myBoard.getRSLocations().size() - 4) * 2.5); 
    	ArrayList<String> RSLocations = myBoard.getRSLocations();
    	
    	ArrayList<citiesWithDistancesObj> HongKongMap;
    	HongKongMap = new ArrayList<citiesWithDistancesObj>();
    	HongKongMap = buildDistanceMap(myBoard, "Hong Kong", HongKongMap);
    	
    	ArrayList<citiesWithDistancesObj> AtlantaMap;
    	AtlantaMap = new ArrayList<citiesWithDistancesObj>();
    	AtlantaMap = buildDistanceMap(myBoard, "Atlanta", AtlantaMap);
    	
    	ArrayList<citiesWithDistancesObj> InstabulMap;
    	InstabulMap = new ArrayList<citiesWithDistancesObj>();
    	InstabulMap = buildDistanceMap(myBoard, "Instabul", InstabulMap);
    	
    	ArrayList<citiesWithDistancesObj> SaoPauloMap;
    	SaoPauloMap = new ArrayList<citiesWithDistancesObj>();
    	SaoPauloMap = buildDistanceMap(myBoard, "Sao Paulo", SaoPauloMap);
    	
    	score += myBoard.getRSLocations().contains("Hong Kong") ? 10 : 0;
    	score += myBoard.getRSLocations().contains("Sao Paulo") ? 10 : 0;
    	score += myBoard.getRSLocations().contains("Instabul") ? 10 : 0;
    	score += myBoard.getRSLocations().contains("Atlanta") ? 10 : 0;
    	
    	for(int i = 0; i < myBoard.getRSLocations().size(); i++) {	
			int HongKongDist = distanceFrom(RSLocations.get(i), HongKongMap);
	    	int SaoPauloDist = distanceFrom(RSLocations.get(i), SaoPauloMap);
	    	int InstabulDist = distanceFrom(RSLocations.get(i), InstabulMap);
	    	int AtlantaDist = distanceFrom(RSLocations.get(i), AtlantaMap);

			score += max( 
					(HongKongDist ==1 && !myBoard.getRSLocations().contains("Hong Kong")) ? 5 : 0,
					SaoPauloDist == 1 && !myBoard.getRSLocations().contains("Sao Paulo") ? 5: 0,
					InstabulDist == 1 && !myBoard.getRSLocations().contains("Instabul") ? 5: 0,
					AtlantaDist == 1 && !myBoard.getRSLocations().contains("Atlanta") ? 5 : 0
							);
    		
    	}
    	
    	return score;
    }
    
    
    
    
    public static double evaluateBoard(Board myBoard, int numberOfPlayers)
    {
    	double cures = 0;
    	for(int i = 0; i < 3; i++)
    		//-100 for every disease not cured
    		cures += myBoard.getCured(i)? 0.25 : 0; 
    	if(cures == 1) return 1;
    	
    	double cards = 0;
    	
    	for(int i = 0; i < numberOfPlayers; i ++) {

    		ArrayList<String> playerHand = myBoard.getHandOf(i);
    		int blackCount = 0, redCount = 0, yellowCount = 0, blueCount = 0;
    		for (int j = 0 ; j < playerHand.size() ; j++)
			{
				City cityToCheck = myBoard.searchForCity(playerHand.get(j));
				
				if (cityToCheck.getColour().equals("Black") && !myBoard.getCured("Black"))
					blackCount ++;
				else if(cityToCheck.getColour().equals("Yellow")  && !myBoard.getCured("Yellow"))
					redCount++;
				else if (cityToCheck.getColour().equals("Blue")  && !myBoard.getCured("Blue"))
					blueCount ++;
				else if(cityToCheck.getColour().equals("Red") && !myBoard.getCured("Red"))
					redCount++;
			}
    		int maxCards = max(blackCount, redCount, yellowCount, blueCount);
    		cards += maxCards == 3 ? 0.125 : 0;
    		
    	}
    	
    	double cubes = max(24- myBoard.getCubesLeft(0), 24- myBoard.getCubesLeft(1), 24- myBoard.getCubesLeft(2), 24- myBoard.getCubesLeft(3)) / 24;
    	
    	double outbreaks = myBoard.getOutbreaksCount() / 8;
    	
    	if(cubes == 1 || outbreaks == 1) return -1 + cures;
    	
    	double score = cubes > outbreaks ? cubes + cures + cards : outbreaks + cures + cards;
    	
    	
    		
    	
    	return score;
    }
    
    public static double evaluateFinal(Board myBoard)
    {
    	double score = 0;
    	
    	if(myBoard.getCured(0) && myBoard.getCured(1) && myBoard.getCured(2) && myBoard.getCured(3))
    		score = 10;
    	else if(myBoard.getOutbreaksCount() == 8 || myBoard.getCubesLeft(0) == 0 || myBoard.getCubesLeft(1) == 0 ||  myBoard.getCubesLeft(2) == 0 || myBoard.getCubesLeft(3) == 0)
    		score = - 20;
    	else
    		score = -10;
    	return score;
    }
    
    
    public static int max(int a, int b, int c, int d) {

        int max = a;

        if (b > max)
            max = b;
        if (c > max)
            max = c;
        if (d > max)
            max = d;

         return max;
    }
    
    
    public static String findRSToBuild(ArrayList<String> playerHand, ArrayList<String> optimalCitiesForRS, Board myBoard) {
    	ArrayList<String> RSToBuild = new ArrayList<String>();
    	
    	for(int i=0; i <optimalCitiesForRS.size();i++) {
    		if(playerHand.contains(optimalCitiesForRS.get(i)))
    				RSToBuild.add(optimalCitiesForRS.get(i));
    				
    	}
    	for(int i=0; i <optimalCitiesForRS.size();i++) {
    		City optimalCity = myBoard.searchForCity(optimalCitiesForRS.get(i));

    		for(int k = 0; k < optimalCity.getNeighboursNumber(); k++) {
    			
    			String neighbourName = optimalCity.getNeighbour(k);
    			City neighbourCity =  myBoard.searchForCity(neighbourName);
    			
    			
    			if(playerHand.contains(optimalCity.getNeighbour(k)) && optimalCity.getColour().equals(neighbourCity.getColour()))
    				RSToBuild.add(optimalCity.getNeighbour(k));
    			
    		}
    		
    		
    	}
    	
    	if(RSToBuild.size() > 0)
    		return RSToBuild.get(0);
    	else
    		return null;
    }
    
    public static ArrayList<String> removeOptimalRS(Board myBoard){
        ArrayList<String> optimalCitiesForRS = new ArrayList<>(Arrays.asList("Atlanta", "Sao Paulo", "Instabul", "Hong Kong"));
        optimalCitiesForRS.removeAll(myBoard.getRSLocations());
        for(int i = 0; i < optimalCitiesForRS.size();i++) {
        	String optimalCityName = optimalCitiesForRS.get(i);
        	City optimalCity = myBoard.searchForCity(optimalCityName);
        	for(int j = 0; j < optimalCity.getNeighboursNumber(); j++) {
        		String neighbourName = optimalCity.getNeighbour(j);
        		City neighbourCity = myBoard.searchForCity(neighbourName);
        		if(myBoard.getRSLocations().contains(neighbourName)) {
        			optimalCitiesForRS.remove(optimalCityName);
        			i--;
        		}
        	}
        }
        
        
	    return optimalCitiesForRS;
    }
    
    
    public static ArrayList<ActionNode> generateNodes(Board myBoard, int myPlayerID, int numberOfPlayers, ActionNode parent){
    	ArrayList<ActionNode> generatedNodes = new ArrayList<ActionNode>();
    	int remainingActions = parent.getState().getRemainingActions() - 1;
    	
     	//find player's role
    	boolean playerIsMedic = myBoard.getRoleOf(myPlayerID).equals("Medic");
    	
    	boolean playerIsOPEX = myBoard.getRoleOf(myPlayerID).equals("Operations Expert");
    	
    	boolean playerIsQS = myBoard.getRoleOf(myPlayerID).equals("Quarantine Specialist");
    	
    	boolean playerIsScientist = myBoard.getRoleOf(myPlayerID).equals("Scientist");
    	
    	//check which roles are covered(useful for game with less than 4 players)
    	boolean gameHasMedic = false, gameHasOPEX = false , gameHasQS= false , gameHasScientist = false;
    	
    	for(int i = 0; i< numberOfPlayers;i++) {
    		gameHasMedic =  gameHasMedic ? true : myBoard.getRoleOf(i).equals("Medic");
        	
    		gameHasOPEX =  gameHasOPEX ? true : myBoard.getRoleOf(i).equals("Operations Expert");
        	
    		gameHasQS = gameHasQS ? true : myBoard.getRoleOf(i).equals("Quarantine Specialist");
        	
    		gameHasScientist = gameHasScientist ? true : myBoard.getRoleOf(i).equals("Scientist");
    	}
    	//initialize player's current position on the board before deciding on our actions
		String myCurrentCity = myBoard.getPawnsLocations(myPlayerID);
    	City myCurrentCityObj = myBoard.searchForCity(myCurrentCity);
    	ArrayList<citiesWithDistancesObj> distanceMap = new ArrayList<citiesWithDistancesObj>();
    	distanceMap = buildDistanceMap(myBoard, myCurrentCity, distanceMap);
    	
    	ArrayList<String> optimalCitiesForRS = new ArrayList<>(Arrays.asList("Atlanta", "Sao Paulo", "Instabul", "Hong Kong"));
    	
    	
	
		ArrayList<String> myHand = myBoard.getHandOf(myPlayerID);
		
    	String colorToCure = null;
    	
    	
    	
    	//check if player is in a city with a Research Station
    	boolean playerInRS = myCurrentCityObj.getHasReseachStation() || myBoard.getRSLocations().contains(myCurrentCity);
		boolean canCure = false;
    	
		boolean saveBlackCards = false;
		
		boolean saveYellowCards = false;
		
		boolean saveBlueCards = false;
		
		boolean saveRedCards = false;
		
		myHand = myBoard.getHandOf(myPlayerID);
		
		int[] myColorCount = {0, 0, 0, 0};
    	
    	for (int i = 0 ; i < 4 ; i++)
    		myColorCount[i] =  cardsCounterOfColor(myBoard, myPlayerID, myBoard.getColors(i));
		
    	int ScientistDiscount = playerIsScientist? 1 : 0;
    	
		//check if we can cure
    	if (myColorCount[0] >= 4 - ScientistDiscount && !myBoard.getCured("Black") ) {
			canCure = true;
			colorToCure = "Black";
		}
		else if (myColorCount[1] >= 4 - ScientistDiscount  && !myBoard.getCured("Yellow")) {
			canCure = true;
			colorToCure = "Yellow";
		}
		else if (myColorCount[2] >= 4 - ScientistDiscount && !myBoard.getCured("Blue") ){
			canCure = true;
			colorToCure = "Blue";
		}
		else if (myColorCount[3] >= 4 - ScientistDiscount && !myBoard.getCured("Red") ){
			canCure = true;
			colorToCure = "Red";
		}
		
		//check which colors we should be trying to save
		saveBlackCards = (cardsCounterOfColor(myBoard, myPlayerID, "Black") >= 3 - ScientistDiscount && !myBoard.getCured("Black"));
		saveYellowCards = (cardsCounterOfColor(myBoard, myPlayerID, "Yellow") >= 3 - ScientistDiscount && !myBoard.getCured("Yellow"));
		saveBlueCards = (cardsCounterOfColor(myBoard, myPlayerID, "Blue") >= 3 - ScientistDiscount && !myBoard.getCured("Blue"));
		saveRedCards = (cardsCounterOfColor(myBoard, myPlayerID, "Red") >= 3 - ScientistDiscount && !myBoard.getCured("Red"));                       
		
		
		//if there are no infected cities in radius we may try to treat next round
		
		
		System.out.println("We currently are in " + myCurrentCity);
    	System.out.println("Are we in RS? " + playerInRS);
		System.out.println("RS build: " + myBoard.getRSLocations().size());
    	
    	if(!myBoard.checkIfWon()) {
		//CURE DISEASE CHILD
		if(canCure && playerInRS) {
			String myAction = "";
			myAction = myAction + toTextCureDisease(myPlayerID, colorToCure);
			Board myBoardCopy = copyBoard(myBoard);
			myBoardCopy.cureDisease(myPlayerID, colorToCure);
			State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
			ActionNode child = new ActionNode(parent, state);
			generatedNodes.add(child);
		}else if(canCure && !playerInRS) {
    		//Start moving to the closest Research Station
    		String closestRS = findClosestRS(distanceMap, myBoard);
    		
    		if(closestRS!= null) {
    			System.out.println("Closest RS is" + closestRS);
    			String driveFirstTo = getDirectionToMove(myCurrentCity, closestRS, distanceMap, myBoard);
    			String myAction = "";
    			myAction = myAction + toTextDriveTo(myPlayerID, driveFirstTo);
    			System.out.println("We are thinking of driving to "+ driveFirstTo + ", because we are seeking a RS");
    			Board myBoardCopy = copyBoard(myBoard);
    			myBoardCopy.driveTo(myPlayerID, driveFirstTo);
    			State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
    			ActionNode child = new ActionNode(parent, state);
    			generatedNodes.add(child);
    			
    		}else {

        		System.out.println("Closest RS is null");
    		}
		}else {
		
		//TREAT DISEASE CHILD
		if(myCurrentCityObj.getMaxCube() > 0) {
			String myAction = "";
			myAction = myAction + toTextTreatDisease(myPlayerID, myCurrentCity, myCurrentCityObj.getMaxCubeColor());
			System.out.println("Trying to treat "+ myCurrentCityObj.getMaxCube()+ " cubes");
			Board myBoardCopy = copyBoard(myBoard);
			myBoardCopy.treatDisease(myPlayerID, myCurrentCity, myCurrentCityObj.getMaxCubeColor());
			State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
			ActionNode child = new ActionNode(parent, state);
			generatedNodes.add(child);
		}
		
		//DIRECT FLIGHT CHILD
		int maxCubesInArea = -1;
		String directFlightCity = null;
		
		for(int i = 0; i < myHand.size();i++) {
			//no point in wasting a card to move to city where you can Drive/Ferry to in 1 action
			if(distanceFrom(myHand.get(i), distanceMap) > 1) {
				int cubes = cubesInArea(myHand.get(i), myBoard);
				if(cubes > maxCubesInArea) {
					maxCubesInArea = cubes;
					directFlightCity =  myHand.get(i);
				}
			}
			
		}
		if(directFlightCity != null && myBoard.getRound() > numberOfPlayers * 3 ) {
			if((myBoard.searchForCity(directFlightCity).getColour().equals("Black") && !saveBlackCards) 
					|| (myBoard.searchForCity(directFlightCity).getColour().equals("Red") && !saveRedCards) 
					|| (myBoard.searchForCity(directFlightCity).getColour().equals("Blue") && !saveBlueCards)
					|| (myBoard.searchForCity(directFlightCity).getColour().equals("Yellow") && !saveYellowCards) ) {
				String myAction = "";
				myAction = myAction + toTextDirectFlight(myPlayerID, directFlightCity);
				Board myBoardCopy = copyBoard(myBoard);
				myBoardCopy.directFlight(myPlayerID, directFlightCity);
				State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
				ActionNode child = new ActionNode(parent, state);
				generatedNodes.add(child);
				
			}
		}
		
		//CHARTER FLIGHT CHILD
		if(myHand.contains(myCurrentCity)) {
			String charterFlightCity = null;
			if(myBoard.getCubesLeft(0) < 6 ) {
				charterFlightCity = getMostDangerousOutOfReach(remainingActions - 1, distanceMap, myBoard, numberOfPlayers, remainingActions - 1, "Black");
			}else if(myBoard.getCubesLeft(1) < 6 ) {
				charterFlightCity = getMostDangerousOutOfReach(remainingActions - 1, distanceMap, myBoard, numberOfPlayers, remainingActions - 1, "Yellow");
			}else if(myBoard.getCubesLeft(2) < 6 ) {
				charterFlightCity = getMostDangerousOutOfReach(remainingActions - 1, distanceMap, myBoard, numberOfPlayers, remainingActions - 1, "Blue");
			}else if(myBoard.getCubesLeft(3) < 6 ) {
				charterFlightCity = getMostDangerousOutOfReach(remainingActions - 1, distanceMap, myBoard, numberOfPlayers, remainingActions - 1, "Red");
			}else {
				charterFlightCity = getMostDangerousOutOfReach(remainingActions - 1, distanceMap, myBoard, numberOfPlayers, remainingActions - 1);
				
			}
			
			if(charterFlightCity!= null) {
				if((myBoard.searchForCity(charterFlightCity).getColour().equals("Black") && !saveBlackCards) 
						|| (myBoard.searchForCity(charterFlightCity).getColour().equals("Red") && !saveRedCards) 
						|| (myBoard.searchForCity(charterFlightCity).getColour().equals("Blue") && !saveBlueCards)
						|| (myBoard.searchForCity(charterFlightCity).getColour().equals("Yellow") && !saveYellowCards) ) {
					String myAction = "";
					myAction = myAction + toTextCharterFlight(myPlayerID, charterFlightCity);
					Board myBoardCopy = copyBoard(myBoard);
					myBoardCopy.charterFlight(myPlayerID, charterFlightCity);
					State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
					ActionNode child = new ActionNode(parent, state);
					generatedNodes.add(child);
					
				}
			}
			
		}
		
		//SHUTTLE FLIGHT
		if(playerInRS && myBoard.getRSLocations().size() > 1) {
			System.out.println("We are thinking about shuttle flight");
			String shuttleFlightCity = getDestinationFromAnotherRS(remainingActions - 1, myCurrentCity, distanceMap, myBoard);
			if(shuttleFlightCity!= null) {
				System.out.println("Shuttle flight to "+ shuttleFlightCity);
				String myAction = "";
				myAction = myAction + toTextShuttleFlight(myPlayerID, shuttleFlightCity);
				Board myBoardCopy = copyBoard(myBoard);
				myBoardCopy.shuttleFlight(myPlayerID, shuttleFlightCity);
				State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
				ActionNode child = new ActionNode(parent, state);
				generatedNodes.add(child);
			}
			
		}
		
		
		//OPEX FLIGHT CHILD
		if(myHand.size() > 0 && playerInRS && playerIsOPEX && myBoard.getRound() > 4 * numberOfPlayers) {
			String OPEXFlightCity = getMostDangerousOutOfReach(remainingActions - 1, distanceMap, myBoard, numberOfPlayers, remainingActions - 1);
			String cardToDiscard = null;
			for(int i = 0 ; i < myHand.size(); i++) {
				if(myBoard.searchForCity(myHand.get(i)).getColour().equals("Black") && !saveBlackCards)
					cardToDiscard = myHand.get(i);
				if(myBoard.searchForCity(myHand.get(i)).getColour().equals("Red") && !saveRedCards)
					cardToDiscard = myHand.get(i);
				if(myBoard.searchForCity(myHand.get(i)).getColour().equals("Blue") && !saveBlueCards)
					cardToDiscard = myHand.get(i);
				if(myBoard.searchForCity(myHand.get(i)).getColour().equals("Yellow") && !saveYellowCards)
					cardToDiscard = myHand.get(i);
			}
			
			if(OPEXFlightCity!=null && cardToDiscard!=null) {
				if(!myBoard.searchForCity(OPEXFlightCity).getHasReseachStation()) {
					String myAction = "";
					myAction = myAction + toTextOET(myPlayerID, OPEXFlightCity, myHand.get(0));
					Board myBoardCopy = copyBoard(myBoard);
					myBoardCopy.operationsExpertTravel(myPlayerID, OPEXFlightCity, myHand.get(0));
					State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
					ActionNode child = new ActionNode(parent, state);
					generatedNodes.add(child);
				}
			}
		}
		
		
		//BUILD RESEARCH STATION CHILD	
		if((
						( playerIsOPEX && myBoard.getRSLocations().size() < 5 && !myBoard.getRSLocations().contains(myCurrentCity) ) //build up to 5 RSs as OPEX
						|| (myHand.contains(myCurrentCity)) && myBoard.getRSLocations().size() < 5 && !myBoard.getRSLocations().contains(myCurrentCity) // build as another role  
							&& (!gameHasOPEX  || (gameHasOPEX && myBoard.getRSLocations().size() < 4 && myBoard.getRound() > 4* numberOfPlayers)))) { //when the OPEX isn't doing his tasks
			String myAction = "";
			myAction = myAction + toTextBuildRS(myPlayerID, myCurrentCity);
			Board myBoardCopy = copyBoard(myBoard);
			myBoardCopy.buildRS(myPlayerID, myCurrentCity);
			State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions - 1, numberOfPlayers);
			ActionNode child = new ActionNode(parent, state);
			generatedNodes.add(child);
		}
		
		//DRIVE/FERRY MOVEMENT CHILD
		String destinationClose = null;
		//Looking for a city to treat
		//FOR QS WE SHOULD BE MOVING TO CITIES WITH 3 CUBES(?)
		//WITH A RADIUS OF REMAININGACTIONS SINCE WE CAN TREAT
		//NEXT ROUND WITHOUT RISKING AN OUTBREAK
		//OTHERWISE SPECIAL ABILITY IS WASTED
		if(playerIsQS)
			destinationClose = getMostDangerous3(remainingActions, distanceMap, myBoard, numberOfPlayers);
		else if(playerIsMedic)
			//FOR MEDIC WE SHOULD BE MOVING TO CITIES WITH 3 CUBES(?)
			//OTHERWISE SPECIAL ABILITY IS WASTED
			destinationClose = getMostDangerous3(remainingActions - 1, distanceMap, myBoard, numberOfPlayers);
		else
			destinationClose = getMostDangerous(remainingActions - 1, distanceMap, myBoard, numberOfPlayers);

		System.out.println("Destination close is "+ destinationClose);
		if(myCurrentCityObj.getMaxCube() != 3) {
			if(destinationClose != null && !destinationClose.equals(myCurrentCity)) {
				//move to destinationClose
				//check if the most dangerous city is outside our range
				boolean travelThroughRS = shouldTravelThroughRS(distanceMap, myBoard, destinationClose);
				
				if(!travelThroughRS) {
					String driveFirstTo = getDirectionToMove(myCurrentCity, destinationClose, distanceMap, myBoard);
					String myAction = "";
					myAction = myAction + toTextDriveTo(myPlayerID, driveFirstTo);
					System.out.println("We are thinking of driving to "+ driveFirstTo + ", because there is a city nearby");
					Board myBoardCopy = copyBoard(myBoard);
					myBoardCopy.driveTo(myPlayerID, driveFirstTo);
					State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions, numberOfPlayers);
					ActionNode child = new ActionNode(parent, state);
					generatedNodes.add(child);
				}
				//if it is, that means we have to use shuttle flight to get there
				//so we start moving to the closest Research Station
				else {
					//Start moving to the closest Research Station
		    		String closestRS = findClosestRS(distanceMap, myBoard);
		    		if(closestRS != null && !closestRS.equals(myCurrentCity) && !(myCurrentCityObj.getMaxCube() == 3)) {
	
						String driveFirstTo = getDirectionToMove(myCurrentCity, closestRS, distanceMap, myBoard);
						String myAction = "";
						myAction = myAction + toTextDriveTo(myPlayerID, driveFirstTo);
						System.out.println("We are thinking of driving to "+ driveFirstTo + ", because we are going through a RS");
						Board myBoardCopy = copyBoard(myBoard);
						myBoardCopy.driveTo(myPlayerID, driveFirstTo);
						State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions, numberOfPlayers);
						ActionNode child = new ActionNode(parent, state);
						generatedNodes.add(child);
		    		}
				}
				
				
			}
			//there is no infected city in radius
			//we shall start moving to the most dangerous city outside our range
			else {
				int radiusFar = remainingActions - 1  + 4;
				String destinationFar = null;
				
				if(playerIsQS)
					destinationFar = getMostDangerous3(radiusFar + 1, distanceMap, myBoard, numberOfPlayers);
				else if(playerIsMedic)
					//FOR MEDIC WE SHOULD BE MOVING TO CITIES WITH 3 CUBES(?)
					//OTHERWISE SPECIAL ABILITY IS WASTED
					destinationFar = getMostDangerous3(radiusFar, distanceMap, myBoard, numberOfPlayers);
				else
					destinationFar = getMostDangerous(radiusFar, distanceMap, myBoard, numberOfPlayers);
				while(destinationFar == null) {
					radiusFar = radiusFar + 4;
					destinationFar = getMostDangerous(radiusFar, distanceMap, myBoard, numberOfPlayers);
				}
				if(!destinationFar.equals(myCurrentCity)) {
	
					String driveFirstTo = getDirectionToMove(myCurrentCity, destinationFar, distanceMap, myBoard);
					String myAction = "";
					myAction = myAction + toTextDriveTo(myPlayerID, driveFirstTo);
					System.out.println("Destination far is "+ destinationFar);
					System.out.println("We are thinking of driving to "+ driveFirstTo + ", because there is a city far away");
					Board myBoardCopy = copyBoard(myBoard);
					myBoardCopy.driveTo(myPlayerID, driveFirstTo);
					State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions, numberOfPlayers);
					ActionNode child = new ActionNode(parent, state);
					generatedNodes.add(child);
				}
				
			}
		}
		}
    	}else {
    		String myAction = "";
			myAction = myAction + toTextActionPass(myPlayerID);
			Board myBoardCopy = copyBoard(myBoard);
			myBoardCopy.actionPass(myPlayerID);
			State state = new State(myAction, myBoardCopy, myPlayerID, remainingActions, numberOfPlayers);
			ActionNode child = new ActionNode(parent, state);
			generatedNodes.add(child);
    	}
    		
    	
		
    	
		System.out.println(generatedNodes.size()+ " children Generated for user_"+myPlayerID);
		System.out.println();
		System.out.println();
		System.out.println();
    	
    	return generatedNodes;
    }
    
    public static int cubesInArea(String city, Board myBoard) {
    	int cubes = 0;
    	
    	ArrayList<citiesWithDistancesObj> dmap = new ArrayList<citiesWithDistancesObj>();
    	
    	dmap = buildDistanceMap(myBoard, city, dmap);
    	
    	int radius = 3;
    	
    	for (int i = 0 ; i < dmap.size() ; i++)
    	{
    		if (dmap.get(i).getDistance() <= radius)
    		{
    			City cityToCheck = myBoard.searchForCity(dmap.get(i).getName());
    			cubes += cityToCheck.getMaxCube();
    		}
    	}
    	return cubes;
    }
    
    // --> Actions <--
    
    
    // --> Coding functions <--
    
    public static String toTextDriveTo(int playerID, String destination)
    {
    	return "#DT,"+playerID+","+destination;
    }
    
    public static String toTextOET(int playerID, String destination, String card)
    {
    	return "#OET,"+playerID+","+destination+","+card;
    }
    
    
    public static String toTextDirectFlight(int playerID, String destination)
    {
    	return "#DF,"+playerID+","+destination;
    }
    
    public static String toTextCharterFlight(int playerID, String destination)
    {
    	return "#CF,"+playerID+","+destination;
    }
    
    public static String toTextShuttleFlight(int playerID, String destination)
    {
    	return "#SF,"+playerID+","+destination;
    }
    
    public static String toTextBuildRS(int playerID, String destination)
    {
    	return "#BRS,"+playerID+","+destination;
    }
    
    public static String toTextRemoveRS(int playerID, String destination)
    {
    	return "#RRS,"+playerID+","+destination;
    }
    
    public static String toTextTreatDisease(int playerID, String destination, String color)
    {
    	return "#TD,"+playerID+","+destination+","+color;
    }
    
    public static String toTextCureDisease(int playerID, String color)
    {
    	return "#CD1,"+playerID+","+color;
    }
    
    public static String toTextCureDisease(int playerID, String color, String card1, String card2, String card3, String card4, String card5)
    {
    	return "#CD2,"+playerID+","+color+","+card1+","+card2+","+card3+","+card4+","+card5;
    }
    
    public static String toTextShareKnowledge(boolean giveOrTake, String cardToSwap, int myID, int playerIDToSwap)
    {
    	return "#SK,"+giveOrTake+","+cardToSwap+","+myID+","+playerIDToSwap;
    }
    
    public static String toTextActionPass(int playerID)
    {
    	return "#AP,"+playerID;
    }
    
    public static String toTextChatMessage(int playerID, String messageToSend)
    {
    	return "#C,"+playerID+","+messageToSend;
    }
    
    public static String toTextPlayGG(int playerID, String cityToBuild)
    {
    	return "#PGG,"+playerID+","+cityToBuild;
    }
    
    public static String toTextPlayQN(int playerID)
    {
    	return "#PQN,"+playerID;
    }
    public static String toTextPlayA(int playerID, int playerToMove, String cityToMoveTo)
    {
    	return "#PA,"+playerID+","+playerToMove+","+cityToMoveTo;
    }
    public static String toTextPlayF(int playerID)
    {
    	return "#PF,"+playerID;
    }
    public static String toTextPlayRP(int playerID, String cityCardToRemove)
    {
    	return "#PRP,"+playerID+","+cityCardToRemove;
    }

} 