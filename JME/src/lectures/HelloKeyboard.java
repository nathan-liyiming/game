package lectures;

import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;

public class HelloKeyboard extends SimpleGame {
	Sphere s;
	
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		rootNode.attachChild(rods);
		rootNode.attachChild(s);
        KeyBindingManager.getKeyBindingManager().set("TURN_LEFT", KeyInput.KEY_1);
        KeyBindingManager.getKeyBindingManager().set("TURN_RIGHT", KeyInput.KEY_0);

	}
	
	protected void simpleUpdate() {
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_RIGHT", false)) {
			s.setLocalTranslation(s.getLocalTranslation().add(Vector3f.UNIT_X));
		}
		
		if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_LEFT", false)) {
			s.setLocalTranslation(s.getLocalTranslation().subtract(Vector3f.UNIT_X));
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloKeyboard app = new HelloKeyboard();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
