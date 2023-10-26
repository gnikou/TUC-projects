package PLH512.client;

import java.util.ArrayList;
import java.util.Collections;

import PLH512.server.Board;

public class State {

    String action;
    double evaluation;
    Board myBoard;
    int visits;
	int playerID;
	int remainingActions;
	int numberOfPlayers;
    
	public State(String action, Board myBoard, int playerID, int remainingActions, int numberOfPlayers) {
		super();
		this.action = action;
		this.evaluation = 0;
		this.myBoard = myBoard;
		this.visits = 0;
		this.playerID = playerID;
		this.remainingActions = remainingActions;
		this.numberOfPlayers = numberOfPlayers;
		
	}
	
    
    public State(String action, double evaluation, Board myBoard, int visits, int playerID, int remainingActions, int numberOfPlayers) {
		super();
		this.action = action;
		this.evaluation = evaluation;
		this.myBoard = myBoard;
		this.visits = visits;
		this.playerID = playerID;
		this.remainingActions = remainingActions;
		this.numberOfPlayers = numberOfPlayers;
	}
    
	public State(State state) {
		// TODO Auto-generated constructor stub
		this.action = state.action;
		this.evaluation = state.evaluation;
		this.myBoard = Client.copyBoard(state.myBoard);
		this.visits = state.visits;
		this.playerID = state.playerID;
		this.remainingActions = state.remainingActions;
		this.numberOfPlayers = state.numberOfPlayers;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public double getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}
	public Board getMyBoard() {
		return myBoard;
	}
	public void setMyBoard(Board myBoard) {
		this.myBoard = myBoard;
	}
	public int getVisits() {
		return visits;
	}
	public void setVisits(int visits) {
		this.visits = visits;
	}
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public int getRemainingActions() {
		return remainingActions;
	}
	public void setRemainingActions(int remainingActions) {
		this.remainingActions = remainingActions;
	}
	public int getNumberOfPlayers() {
		return this.numberOfPlayers;
	}
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public void incrementVisits() {
		this.visits ++;
	}
	

	public void togglePlayer() {
		//temporary work around
		//WE NEED TO SAVE THE CARDS OF EACH PLAYER & THE (SHUFFLED) DECKS
		//draw cards
		

		
		
		this.myBoard.drawCards(playerID, 2);
		this.myBoard.infectCities(this.myBoard.getInfectionRate(),1);
		this.myBoard.setRound(this.myBoard.getRound() + 1);
		if(this.playerID < numberOfPlayers - 1) {
			this.playerID++;
			this.remainingActions = 5;
			
		}else {
			this.playerID = 0;
			this.remainingActions = 5;
		}
	}


	public void addEvaluation(double newEvaluation) {
		this.evaluation += newEvaluation;
		
	}
	
	
	
	
}
