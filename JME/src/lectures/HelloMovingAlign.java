package lectures;

import java.util.Random;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.AxisRods;
import com.jme.util.Timer;

public class HelloMovingAlign extends SimpleGame {
	Random r = new Random();
	Arrow a;
	Vector3f targetV, axisV;
	float startAngle, targetAngle;
	float startT;
	final float timeoutR = 10f;
	Logger logger = Logger.getLogger("Test");
	Timer t;
	
	@Override
	protected void simpleInitGame() {
		float x = 10*(r.nextFloat()-1);
		float y = 10*(r.nextFloat()-1);
		float z = 10*(r.nextFloat()-1);
		targetV = new Vector3f(x, y, z).normalize();
		
		logger.info("\n\nX = " + x + " Y = " + y + " Z = " + z + "\n\n");

		a = new Arrow("myArrow", 10, 1); 

		axisV = Vector3f.UNIT_Y.cross(targetV);
		targetAngle = FastMath.acos(Vector3f.UNIT_Y.dot(targetV));
		startAngle = 0;
		
		
		AxisRods rods = new AxisRods("myRods", true, 5);
		rootNode.attachChild(a);
		rootNode.attachChild(rods);
		t = Timer.getTimer();
		startT = t.getTimeInSeconds();
	}

	protected void simpleUpdate() {
		float curT = t.getTimeInSeconds();
		if (curT < (startT + timeoutR))
		{
			float currentAngle = (startAngle + ((curT-startT)/timeoutR)*targetAngle);
			Quaternion q = new Quaternion();
			logger.info("\n\ntargetAngle = " + targetAngle + " currenAngle = " + currentAngle + "\n\n");
			currentAngle += tpf;
			q.fromAngleAxis(currentAngle, axisV);
			a.setLocalRotation(q);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloMovingAlign app = new HelloMovingAlign();
		app.start();
	}

}
