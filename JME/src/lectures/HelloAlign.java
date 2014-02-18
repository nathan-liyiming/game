package lectures;

import java.util.Random;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.AxisRods;

public class HelloAlign extends SimpleGame {
	Random r = new Random();
	Arrow a;
	Logger logger = Logger.getLogger("Test");

	@Override
	protected void simpleInitGame() {
		float x = 1;//10*(r.nextFloat()-1);
		float y = 1;//10*(r.nextFloat()-1);
		float z = 1;//10*(r.nextFloat()-1);
		//Vector3f u = new Vector3f(x, y, z).normalize();
		Vector3f u = new Vector3f(1, 1, 1).normalize();
		
		logger.info("\n\nX = " + x + " Y = " + y + " Z = " + z + "\n\n");
		//Vector3f v = new Vector3f(10*(r.nextFloat()-1), 10*(r.nextFloat()-1), 10*(r.nextFloat()-1));

		a = new Arrow("myArrow", 10, 1); 
		// by default, an arrow always points at Vector3f.UNIT_Y
		// Let's allign it with u
		
		Vector3f axis = Vector3f.UNIT_Y.cross(u);
		float angle = FastMath.acos(Vector3f.UNIT_Y.dot(u));
		
		Quaternion q = new Quaternion();
		q.fromAngleAxis(angle, axis);
		
		a.setLocalRotation(q);
		
		AxisRods rods = new AxisRods("myRods", true, 5);
		rootNode.attachChild(a);
		rootNode.attachChild(rods);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloAlign app = new HelloAlign();
		app.start();
	}

}
