package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Box;

public class HelloSimpleTranslation extends SimpleGame {

	Box b; 
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("MyRods");
		b = new Box("WBox", Vector3f.ZERO, 1, 1, 1);		
		rootNode.attachChild(rods);
		rootNode.attachChild(b);
	}
	
	protected void simpleUpdate() {
		b.setLocalTranslation(b.getLocalTranslation().add((new Vector3f(1,0,0)).mult(0.1f)));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloSimpleTranslation app = new HelloSimpleTranslation();
		app.start();

	}

}
