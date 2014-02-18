package lectures;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;


public class HelloBSP extends SimpleGame {
	Sphere ball;	

	
	
	class Plain {
		private Vector3f myPosition, myDirection;
		public Plain(Vector3f position, Vector3f direction) {
			myPosition = position;
			myDirection = direction;
		}
		public boolean isInFront(Vector3f pos) {
			if(pos.subtract(myPosition).dot(myDirection)>0) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	enum NodeType {solid, empty, internal};

	class BSPTree {
		NodeType myType;
		Plain myPlain;
		BSPTree myInfront, myBehind;
		public BSPTree(NodeType t) {
			// if((t != NodeType.empty) || (t != NodeType.solid)) throw new Exception();
			myType = t;
			myPlain = null;
			myInfront = null;
			myBehind = null;
		}
		public BSPTree(Plain p, BSPTree infront, BSPTree behind) {
			myPlain = p;
			myType = NodeType.internal;
			myInfront = infront;
			myBehind = behind;
		}
		public boolean isSolid(Vector3f pos) {
			if(myType == NodeType.solid) {
				return true;
			}
			if(myType == NodeType.empty) {
				return false;
			}
			
			// else
			if(myPlain.isInFront(pos)) {
				return myInfront.isSolid(pos);
			}
			else {
				return myBehind.isSolid(pos);
			}
		}
	}
	
	BSPTree t;
	
	protected void simpleInitGame() {      

             
        ball = new Sphere("ball", 30, 30, 1);
        ball.setModelBound(new BoundingSphere());
        ball.updateModelBound();
        ball.setLocalTranslation(-1, 1, 0);
        rootNode.attachChild(ball);
        
        BSPTree solidT = new BSPTree(NodeType.solid);
        BSPTree emptyT = new BSPTree(NodeType.empty);
        
         
        t = new BSPTree(new Plain(new Vector3f(0,0,0), new Vector3f(0,1,0)),
        				new BSPTree(new Plain(new Vector3f(0,0,0), new Vector3f(-2,1,0)),
        							new BSPTree(new Plain(new Vector3f(0,5,0), new Vector3f(-1,2,0)),
        										new BSPTree(new Plain(new Vector3f(0,5,0), new Vector3f(-1,1,0)), 
        													emptyT, 
        													solidT),
        										solidT			
        								),
        							emptyT
        					), 
        				emptyT
        		);
        
        KeyBindingManager.getKeyBindingManager().set("TURN_LEFT", KeyInput.KEY_1);
        KeyBindingManager.getKeyBindingManager().set("TURN_RIGHT", KeyInput.KEY_2);
        KeyBindingManager.getKeyBindingManager().set("TURN_UP", KeyInput.KEY_9);
        KeyBindingManager.getKeyBindingManager().set("TURN_DOWN", KeyInput.KEY_0);


	}
	
	protected void simpleUpdate() {
		
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_RIGHT", true)) {
			Vector3f newPos = (ball.getLocalTranslation().add(Vector3f.UNIT_X.mult(10*tpf)));
			if(t.isSolid(newPos)) {
				ball.setLocalTranslation(newPos);
			}
		}
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_LEFT", true)) {
			Vector3f newPos = (ball.getLocalTranslation().subtract(Vector3f.UNIT_X.mult(10*tpf)));
			if(t.isSolid(newPos)) {
				ball.setLocalTranslation(newPos);
			}
		}
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_UP", true)) {
			Vector3f newPos = (ball.getLocalTranslation().add(Vector3f.UNIT_Y.mult(10*tpf)));
			if(t.isSolid(newPos)) {
				ball.setLocalTranslation(newPos);
			}
		}
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_DOWN", true)) {
			Vector3f newPos = (ball.getLocalTranslation().subtract(Vector3f.UNIT_Y.mult(10*tpf)));
			if(t.isSolid(newPos)) {
				ball.setLocalTranslation(newPos);
			}
		}
		
	}

	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloBSP app = new HelloBSP();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
