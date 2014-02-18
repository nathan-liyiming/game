package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;



public class HelloAiming extends SimpleGame {
//world constants
	final float g = 9.8f;
	final Vector3f gravity = new Vector3f(0, -g, 0);

//distance to the target
	final float distance = 100f;

// bullet velocity
 	Vector3f velocity = new Vector3f(0,0,0);
//objects	
	Sphere bullet, target;

	protected void simpleInitGame() {
		bullet = new Sphere("mySphere",  30, 30, 1);
		bullet.setDefaultColor(ColorRGBA.red);
		bullet.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(bullet);
		
		target = new Sphere("myTarget", 30, 30, 1);
		target.setLocalTranslation(distance, 0, 0);
		target.setDefaultColor(ColorRGBA.green);
		rootNode.attachChild(target);
		
		
		cam.setLocation(new Vector3f(-25, 5, 15));
	    cam.lookAt(new Vector3f(distance/2f, 0, 0), Vector3f.UNIT_Y);
	 	cam.update();

		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);

	 	
     // horizontal component of the bullet speed
    	float vx = 20f;	 
    // computing the vertical component
	 	float vy = (g*distance) / (2*vx);
	 	velocity = new Vector3f(vx,vy,0);
		

    }
	
	
	protected void simpleUpdate() {
		
		if(bullet.getLocalTranslation().getY() >= 0 ) {	
			velocity = velocity.add(gravity.mult(tpf));
			bullet.setLocalTranslation(bullet.getLocalTranslation().add(velocity.mult(tpf)));
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloAiming app = new HelloAiming();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
