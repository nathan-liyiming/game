package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Box;

public class HelloRods extends SimpleGame {

	
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("MyRods");
		Box b = new Box("myBox", new Vector3f(0,0,0), .5f,0.5f,0.5f);
		rootNode.attachChild(rods);
		rootNode.attachChild(b);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloRods app = new HelloRods();
		app.start();

	}

}
