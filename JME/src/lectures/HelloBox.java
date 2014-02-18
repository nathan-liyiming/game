package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;

public class HelloBox extends SimpleGame {

	
	protected void simpleInitGame() {
		Box b = new Box("MyBox", Vector3f.ZERO, 1, 1, 1);
		rootNode.attachChild(b);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloBox app = new HelloBox();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
