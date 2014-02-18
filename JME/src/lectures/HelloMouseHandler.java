package lectures;

import com.jme.app.SimpleGame;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;

public class HelloMouseHandler extends SimpleGame {
	Sphere s;
	float shotTime = 0;

	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		rootNode.attachChild(rods);
		rootNode.attachChild(s);
        input.addAction(new MyMouse());
	}
	
	class MyMouse extends MouseInputAction {
		public void performAction(InputActionEvent evt) {
	        shotTime += evt.getTime();
			if (MouseInput.get().isButtonDown(0) && shotTime > 0.1f){
				s.setLocalTranslation(s.getLocalTranslation().add(Vector3f.UNIT_X));
				shotTime = 0;
			}
			if (MouseInput.get().isButtonDown(1) && shotTime > 0.1f){
				s.setLocalTranslation(s.getLocalTranslation().subtract(Vector3f.UNIT_X));
				shotTime = 0;
			}
		}
	}
		
	protected void simpleUpdate() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloMouseHandler app = new HelloMouseHandler();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
