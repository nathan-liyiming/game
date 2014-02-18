package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Box;

public class HelloRotation extends SimpleGame {
	private Box b; 
	private float angle = 0;
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("MyRods");
		b = new Box("WBox", Vector3f.ZERO, 1, 1, 1);	
		rootNode.attachChild(rods);
		rootNode.attachChild(b);
		
	}
	
	protected void simpleUpdate() {
		
		Vector3f axis = new Vector3f(1,2,3);
		Quaternion quat = new Quaternion();
		angle += timer.getTimePerFrame();
		if (angle > 2*FastMath.PI) {
			angle -= 2*FastMath.PI;
	    }

		quat.fromAngleAxis(angle, axis);
		b.setLocalRotation(quat);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloRotation app = new HelloRotation();
		app.start();

	}

}
