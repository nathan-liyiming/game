package lectures;

import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;

public class HelloMouse extends SimpleGame {
	Sphere s;
	float shotTime = 0;

	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		rootNode.attachChild(rods);
		rootNode.attachChild(s);
	}
	
			
	protected void simpleUpdate() {
		if (MouseInput.get().isButtonDown(0) ){
			s.setLocalTranslation(s.getLocalTranslation().add(Vector3f.UNIT_X));
		}
		if (MouseInput.get().isButtonDown(1) ){
			s.setLocalTranslation(s.getLocalTranslation().subtract(Vector3f.UNIT_X));
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloMouse app = new HelloMouse();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
