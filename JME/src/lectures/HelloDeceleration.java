package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;



public class HelloDeceleration extends SimpleGame {
	Vector3f direction = new Vector3f(1,0,0);
	float speed = 5;
	Sphere s;
	
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 2);
		rootNode.attachChild(rods);
		rootNode.attachChild(s);
    }
	
	protected void simpleUpdate() {
			speed -= 2*tpf;
			if(speed < 0.01f) {
				speed = 0;
			}
			s.setLocalTranslation(s.getLocalTranslation().add(direction.mult(speed*tpf)));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloDeceleration app = new HelloDeceleration();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
