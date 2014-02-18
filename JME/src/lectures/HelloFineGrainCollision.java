package lectures;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;
import com.jme.scene.shape.Tube;

public class HelloFineGrainCollision extends SimpleGame {
	Sphere ball;	
	Tube tube;
	Vector3f direction = new Vector3f(0f,0f,-1f);
	protected void simpleInitGame() {      
        tube = new Tube("Tube", 5, 4, 5); 
        Quaternion q = new Quaternion();
        q.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
        tube.setLocalRotation(q);
        tube.setModelBound(new BoundingBox());
        tube.updateModelBound();
        rootNode.attachChild(tube);
             
        ball = new Sphere("ball", 10, 10, 1);
        ball.setModelBound(new BoundingSphere());
        ball.updateModelBound();

        ball.setLocalTranslation(0, 0, 10);
        rootNode.attachChild(ball);
        
        cam.setLocation(new Vector3f(-15, 0, 30));
        cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
 		cam.update();
   
	}
	
	protected void simpleUpdate() {
		if(ball.hasCollision(tube, true)) {
			direction.setZ(-direction.getZ());
		}
		ball.setLocalTranslation(ball.getLocalTranslation().add(direction.mult(2*tpf)));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloFineGrainCollision app = new HelloFineGrainCollision();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
