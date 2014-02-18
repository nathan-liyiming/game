package lectures;

import com.jme.app.SimpleGame;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;

public class HelloKeyboardHandler extends SimpleGame {
	Sphere s;
	
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		rootNode.attachChild(rods);
		rootNode.attachChild(s);
        input.addAction(new MoveRight(), "moveright", KeyInput.KEY_0, false);
        input.addAction(new MoveLeft(), "moveleft", KeyInput.KEY_1, false);


	}
	
	class MoveRight extends KeyInputAction {
		public void performAction(InputActionEvent arg0) {
			s.setLocalTranslation(s.getLocalTranslation().add(Vector3f.UNIT_X));
		}
	}
	
	class MoveLeft extends KeyInputAction {
		public void performAction(InputActionEvent arg0) {
			s.setLocalTranslation(s.getLocalTranslation().subtract(Vector3f.UNIT_X));
		}	
	}
	
	protected void simpleUpdate() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloKeyboardHandler app = new HelloKeyboardHandler();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
