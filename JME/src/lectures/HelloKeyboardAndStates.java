package lectures;

import com.jme.app.SimpleGame;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;
import com.jme.scene.shape.Teapot;


public class HelloKeyboardAndStates extends SimpleGame {
	Vector3f direction = new Vector3f(0,0,0);
	float speed = 0;
	Sphere s;
	Teapot t;

	enum State {user, auto};
	State state = State.auto;
	
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 2);
		t = new Teapot("myTeapot");
		rootNode.attachChild(rods);
		rootNode.attachChild(s);
        KeyBindingManager.getKeyBindingManager().set("TURN_LEFT", KeyInput.KEY_1);
        KeyBindingManager.getKeyBindingManager().set("TURN_RIGHT", KeyInput.KEY_0);
        KeyBindingManager.getKeyBindingManager().set("GO", KeyInput.KEY_SPACE);
	}
	
	protected void simpleUpdate() {
		switch (state) {
		case user:
			System.out.println("user");
			if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_RIGHT", false)) {
				direction = Vector3f.UNIT_X.mult(1);
			}
			
			if(KeyBindingManager.getKeyBindingManager().isValidCommand("TURN_LEFT", false)) {
				direction = Vector3f.UNIT_X.mult(-1);
			}

			if(KeyBindingManager.getKeyBindingManager().isValidCommand("GO", false)) {
				speed=10;
				rootNode.detachChild(t);
				state = State.auto;
			}
			break;
		case auto:
			System.out.println("auto");
			speed -= 2*tpf;
			if(speed < 0.01f) {
				speed = 0;
				t.setLocalTranslation(s.getLocalTranslation().add(new Vector3f(0,3,0)));
				rootNode.attachChild(t);
				state = State.user;
			}
			s.setLocalTranslation(s.getLocalTranslation().add(direction.mult(speed*tpf)));
			break;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloKeyboardAndStates app = new HelloKeyboardAndStates();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
