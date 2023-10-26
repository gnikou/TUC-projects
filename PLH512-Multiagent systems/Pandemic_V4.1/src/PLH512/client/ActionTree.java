package PLH512.client;


public class ActionTree {

	public ActionNode root;
	
	public ActionTree(State state) {
		this.root = new ActionNode(state);
		
	}
	
	public void setRoot(ActionNode root) {
		this.root = root;
	}
	
	public ActionNode getRoot() {
		return this.root;
	}
	
}