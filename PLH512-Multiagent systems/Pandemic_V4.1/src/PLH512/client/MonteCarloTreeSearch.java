package PLH512.client;

import java.util.ArrayList;
import java.util.Collections;

import PLH512.server.Board;

public class MonteCarloTreeSearch {
    static final int WIN_SCORE = 10;
    int level;
    int opponent;

    public static String findNextMove(Board myBoard, int playerID, int numberOfPlayers) {
        // define an end time which will act as a terminating condition

        State state = new State(null, myBoard, playerID, 5, numberOfPlayers); //5
        ActionTree tree = new ActionTree(state);
        ActionNode rootNode = tree.getRoot();
        
        //setting the root back to tree root
        rootNode = tree.getRoot();

        for(int i = 0; i < 3500; i++) {
        	ActionNode promisingNode = selectPromisingNode(rootNode);
        	
        	
        	//we are only expanding if we haven't reached level 4
            if (promisingNode.getLevel() < 4 *(numberOfPlayers + 1) && promisingNode.getChildArray().size() ==0){
            	promisingNode = expandNode(promisingNode);
            }
            
            ActionNode nodeToExplore = promisingNode;
            if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            
            if(promisingNode.getLevel() % 4 == 1) {
                double playoutResult = simulateRandomPlayout(nodeToExplore, numberOfPlayers);
                backPropogation(nodeToExplore, playoutResult);
            }
        	
        }
        String winnerActions = "";

        ActionNode greatGreatGrandChild = rootNode.getGreatGreatGrandChildWithMaxScore();
        ActionNode greatGrandChild = greatGreatGrandChild.getParent();
        ActionNode grandChild = greatGrandChild.getParent();
        ActionNode child = grandChild.getParent();
        
        winnerActions = winnerActions + child.getState().getAction();
        winnerActions = winnerActions + grandChild.getState().getAction();
        winnerActions = winnerActions + greatGrandChild.getState().getAction();
        winnerActions = winnerActions + greatGreatGrandChild.getState().getAction();
        
        /*
        winnerActions = winnerActions + rootNode.getState().getAction();
        rootNode = rootNode.getGreatGreatGrandChildWithMaxScore();
        winnerActions = winnerActions + rootNode.getState().getAction();
        rootNode = rootNode.getGreatGreatGrandChildWithMaxScore();
        winnerActions = winnerActions + rootNode.getState().getAction();
        rootNode = rootNode.getGreatGreatGrandChildWithMaxScore();
        winnerActions = winnerActions + rootNode.getState().getAction();
        rootNode = rootNode.getGreatGreatGrandChildWithMaxScore();
        winnerActions = winnerActions + rootNode.getState().getAction();
        */
        return winnerActions;
    }
    
    private static ActionNode selectPromisingNode(ActionNode rootNode) {
    	ActionNode node = rootNode;
        while (node.getChildArray().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }
    
    private static ActionNode expandNode(ActionNode node) {
    	if(node.getLevel() < 4 * (node.getState().getPlayerID() + 1) && !node.getState().getMyBoard().getGameEnded()) {
    		State tempState = new State(node.getState());
        	ArrayList<ActionNode> childrenNodes = Client.generateNodes( tempState.getMyBoard(), tempState.getPlayerID(), tempState.getNumberOfPlayers(), node);
        	node.setChildArray (childrenNodes);
        	return node;
    	}else if(!node.getState().getMyBoard().getGameEnded()){
    		State tempState = new State(node.getState() );
    		if(!tempState.getMyBoard().getGameEnded()) 
     			tempState.togglePlayer();
    		ArrayList<ActionNode> childrenNodes = Client.generateNodes( tempState.getMyBoard(), tempState.getPlayerID(), tempState.getNumberOfPlayers(), node);
        	node.setChildArray (childrenNodes);
        	return node;
    	}
    	return node;
    }
    
    
    public static double simulateRandomPlayout(ActionNode node, int numberOfPlayers) {
    	//set up the current state of the game
    	ActionNode tempNode = new ActionNode(node);
        State tempState = tempNode.getState();
        Board myBoard = Client.copyBoard(tempState.getMyBoard());
        int startingRound = myBoard.getRound();
    	//shuffle decks
		Collections.shuffle(myBoard.getInfectedDeck());
		Collections.shuffle(myBoard.getPlayersDeck());
		Collections.shuffle(myBoard.getDiscardedPile());
        //repeat while the game has not ended
        if(startingRound > 0 ) { // 4 * numberOfPlayers) {
        	 while (!myBoard.getGameEnded()) {
             	//4 random actions for each player
             	for(int i = 0; i < 4; i++) {
             		tempNode = tempNode.randomPlay();
             		//state changes
             		tempState = tempNode.getState();
             	}
             	//once the 4 actions are completed we change player
             	if(!myBoard.getGameEnded()) 
             		tempState.togglePlayer();
             	tempNode.setState(tempState);
             	myBoard = tempState.getMyBoard();
             }
        }else {
            while (!myBoard.getGameEnded() && myBoard.getRound() <= startingRound + 5 ) {
            	//4 random actions for each player
            	for(int i = 0; i < 4; i++) {
            		tempNode = tempNode.randomPlay();
            		//state changes
            		tempState = tempNode.getState();
            	}
            	//once the 4 actions are completed we change player
            	if(!myBoard.getGameEnded()) 
             		tempState.togglePlayer();
            	tempNode.setState(tempState);
            	myBoard = tempState.getMyBoard();
            }
        	
        }
        return Client.evaluateBoard(myBoard, numberOfPlayers);
    }
    
    private static void backPropogation(ActionNode nodeToExplore, double playoutResult) {
    	ActionNode tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisits();
            tempNode.getState().addEvaluation(playoutResult);
            tempNode = tempNode.getParent();
        }
    }
    
    
    
}