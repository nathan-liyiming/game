package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;



public class HelloGravity extends SimpleGame {

	Vector3f velocity = new Vector3f(0,0,0);
	Vector3f acceleration = new Vector3f(0,0,0);
	Vector3f gravity = new Vector3f(0, -9.8f, 0);
	Sphere s;

	protected void simpleInitGame() {
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		restart();

		rootNode.attachChild(s);
		
		cam.setLocation(new Vector3f(0, 5, 40));
	    cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
	 	cam.update();
    }
	
	protected void restart() {
		velocity = new Vector3f(10,10,0);
		s.setLocalTranslation(-8, 0, 0);
	}
	
	protected void simpleUpdate() {
		
		if(s.getLocalTranslation().getY() < -15f) {
			restart();
		}
		
		acceleration = gravity;
		velocity = velocity.add(acceleration.mult(tpf));
		s.setLocalTranslation(s.getLocalTranslation().add(velocity.mult(tpf)));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloGravity app = new HelloGravity();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
