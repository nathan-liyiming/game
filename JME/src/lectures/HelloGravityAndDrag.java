package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;



public class HelloGravityAndDrag extends SimpleGame {

	Vector3f velocity = new Vector3f();
	Vector3f acceleration = new Vector3f();
	Vector3f force = new Vector3f();

	final Vector3f gravity = new Vector3f(0, -9.8f, 0);
	final float b = 0.9f;
	final float m = 1f;
	
	Vector3f velocity2 = new Vector3f();
	Vector3f acceleration2 = new Vector3f();
	
	Sphere s,s2;

	protected void simpleInitGame() {
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		s.setDefaultColor(ColorRGBA.red);
		
		s2 = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		s2.setDefaultColor(ColorRGBA.blue);
		rootNode.attachChild(s);
		rootNode.attachChild(s2);
		
		restart();
				
		cam.setLocation(new Vector3f(0, 5, 40));
	    cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
	 	cam.update();
	 	
		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);

    }
	
	protected void restart() {
		velocity = new Vector3f(10,10,0);
		s.setLocalTranslation(-8, 0, 0);
		
		velocity2 = new Vector3f(10,10,0);
		s2.setLocalTranslation(-8, 4, 0);
	}
	
	protected void simpleUpdate() {
		
		if(s.getLocalTranslation().getY() < -15f) {
			restart();
		}
		
		force = velocity.mult(-b);
		acceleration = gravity.add(force.divide(m));
		velocity = velocity.add(acceleration.mult(tpf));
		s.setLocalTranslation(s.getLocalTranslation().add(velocity.mult(tpf)));
		
		acceleration2 = gravity;
		velocity2 = velocity2.add(acceleration2.mult(tpf));
		s2.setLocalTranslation(s2.getLocalTranslation().add(velocity2.mult(tpf)));

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloGravityAndDrag app = new HelloGravityAndDrag();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
