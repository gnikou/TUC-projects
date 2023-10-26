package PLH512.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ActionNode {
    ActionNode parent;
    State state;
    ArrayList<ActionNode> childArray;
    int level;
   
    
    
	public ActionNode(State state) {
		super();
		this.state = state;
		this.parent = null;
		this.childArray = new ArrayList<ActionNode>();
		this.level = 0;
	}
	
	
	public ActionNode(ActionNode actionNode) {
		super();
		this.state = actionNode.getState();
		this.parent = actionNode.getParent();
		this.childArray = actionNode.getChildArray();
	}
	
	public ActionNode(ActionNode parent, State state) {
		super();
		this.parent = parent;
		this.state = state;
		this.childArray = new ArrayList<ActionNode>();
		this.level = parent.getLevel() + 1;
	}
	public ActionNode(ActionNode parent, State state, ArrayList<ActionNode> childArray) {
		super();
		this.parent = parent;
		this.state = state;
		this.childArray = childArray;
		this.level = parent.parent.getLevel() + 1;
	}
	public ActionNode getParent() {
		return parent;
	}
	public void setParent(ActionNode parent) {
		this.parent = parent;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public ArrayList<ActionNode> getChildArray() {
		return childArray;
	}
	public void setChildArray(ArrayList<ActionNode> childArray) {
		this.childArray = childArray;
	}
	
	public void randomPlayEarly() {
		ActionNode parent = this;
		
		int maxRounds = parent.getState().getNumberOfPlayers() + 1;
		
		while(parent.getState().getMyBoard().getGameEnded() || parent.getState().getMyBoard().getRound() == maxRounds) {
			for(int i = 0; i < 4; i++) {
				ArrayList<ActionNode> Children = Client.generateNodes(parent.getState().getMyBoard(), parent.getState().getPlayerID(), parent.getState().getNumberOfPlayers(), this);
				Collections.shuffle(Children);
				parent = Children.get(0);
			}
			parent.getState().togglePlayer();
			parent.getState().setRemainingActions(4);
		}
		
	}
	
	
	//generate single child for random simulation play
	public ActionNode randomPlay() {
		ActionNode parent = this;
		ArrayList<ActionNode> Children = Client.generateNodes(parent.getState().getMyBoard(), parent.getState().getPlayerID(), parent.getState().getNumberOfPlayers(), this);
		Collections.shuffle(Children);
		ActionNode child  = Children.get(0);
		return child;
	}
	

	public void randomPlayLate() {
		ActionNode parent = this;
		while(parent.getState().getMyBoard().getGameEnded()) {
			for(int i = 0; i < 4; i++) {
				ArrayList<ActionNode> Children = Client.generateNodes(parent.getState().getMyBoard(), parent.getState().getPlayerID(), parent.getState().getNumberOfPlayers(), this);
				Collections.shuffle(Children);
				parent = Children.get(0);
			}
			parent.getState().togglePlayer();
			parent.getState().setRemainingActions(5);
		}	
	}
	
	public ActionNode getRandomChildNode() {
		Random rand = new Random();
		return this.childArray.get(rand.nextInt(getChildArray().size()));
		
	}
	
	public ActionNode getChildWithMaxScore() {
		double maxEvaluation = - 100000000;
		ActionNode childWithMaxScore = null;
		for(int i = 0; i < this.getChildArray().size(); i++) {
			if(this.getChildArray().get(i).getState().getEvaluation() > maxEvaluation) {
				maxEvaluation = this.getChildArray().get(i).getState().getEvaluation();
				childWithMaxScore = this.getChildArray().get(i);
			}
		}
		return childWithMaxScore;
	}

	
	public ActionNode getGreatGreatGrandChildWithMaxScore() {
		double maxEvaluation = - 100000000;
		ActionNode greatGreatGrandChildWithMaxScore = null;
		for(int i = 0; i < this.getChildArray().size(); i++) {
			ActionNode child = this.getChildArray().get(i);
			for(int j = 0; j < child.getChildArray().size(); j++) {
				ActionNode grandChild = child.getChildArray().get(j);
				for(int k = 0; k < grandChild.getChildArray().size(); k++) {
					ActionNode greatGrandChild = grandChild.getChildArray().get(k);
					for(int l = 0; l < greatGrandChild.getChildArray().size(); l++) {
						if(greatGrandChild.getChildArray().get(l).getState().getEvaluation() > maxEvaluation) {
							maxEvaluation = greatGrandChild.getChildArray().get(l).getState().getEvaluation();
							greatGreatGrandChildWithMaxScore = greatGrandChild.getChildArray().get(l);
						}
					}
				}
			}
			
		}
		return greatGreatGrandChildWithMaxScore;
	}
	
	
	
	
	

	public int getLevel() {
		// TODO Auto-generated method stub
		return this.level;
	}
	

}