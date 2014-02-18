package lectures;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;



public class HelloBounce extends SimpleGame {
	Vector3f force = new Vector3f(0,0,0);
	Vector3f speed = new Vector3f(3,0,0);
	Vector3f acceleration = new Vector3f(0,0,0);
	
	float  mass = 1f;
	float viscosity = 0.1f;
	float energyLoss = .9f;
	
	Vector3f gravity = new Vector3f(0, -9.8f, 0);
	Sphere s;
	Box floor1, floor2, floor3;
	Vector3f floor2Normal = new Vector3f(-1f,2f,0).normalize();
	Vector3f floor3Normal = new Vector3f(1f,2f,0).normalize();

		
	protected Box boxFromNormal(String name, Vector3f n){
		Box b = new Box(name, Vector3f.ZERO, 10f, 1f, 10f);
		Quaternion q = new Quaternion();
		q.fromAxes(n.cross(Vector3f.UNIT_Z), n, Vector3f.UNIT_Z);
		b.setLocalRotation(q);
		return b;
	}
	
	protected void simpleInitGame() {
		floor1 = boxFromNormal("floor1",  Vector3f.UNIT_Y);
		floor1.setModelBound(new BoundingBox());
		floor1.updateModelBound();
		
		floor2 = boxFromNormal("floor2", floor2Normal); 
		floor2.setLocalTranslation(18.5f, 4.5f, 0);
		floor2.setModelBound(new BoundingBox());
		floor2.updateModelBound();
		

		
		floor3 = boxFromNormal("floor3", floor3Normal); 
		floor3.setLocalTranslation(-18.5f, 4.5f, 0);
		floor3.setModelBound(new BoundingBox());
		floor3.updateModelBound();
				
		s = new Sphere("mySphere", Vector3f.ZERO, 30, 30, 1);
		s.setModelBound(new BoundingSphere());
		s.updateModelBound();
		s.setLocalTranslation(-8,15,0);
		
	
		rootNode.attachChild(floor1);
		rootNode.attachChild(floor2);
		rootNode.attachChild(floor3);
		rootNode.attachChild(s);
		
		cam.setLocation(new Vector3f(0, 5, 60));
	    cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
	 	cam.update();
    }
	
	protected void simpleUpdate() {
		force = speed.mult(-viscosity);
		acceleration = gravity.add(force.divide(mass));
		speed = speed.add(acceleration.mult(tpf));
		
		if(s.hasCollision(floor1, false)) {
				speed.setY(FastMath.abs(energyLoss*speed.getY()));
		}
		
		else if(s.hasCollision(floor2, true)) {
			float projVal = speed.dot(floor2Normal);
			Vector3f projection = floor2Normal.mult(projVal);
			Vector3f parall = speed.subtract(projection);
			speed = parall.add(floor2Normal.mult(energyLoss*FastMath.abs(projVal)));
		}
		
		else if(s.hasCollision(floor3, true)) {
			float projVal = speed.dot(floor3Normal);
			Vector3f projection = floor3Normal.mult(projVal);
			Vector3f parall = speed.subtract(projection);
			speed = parall.add(floor3Normal.mult(energyLoss*FastMath.abs(projVal)));
		}

		s.setLocalTranslation(s.getLocalTranslation().add(speed.mult(2*tpf)));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloBounce app = new HelloBounce();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
