package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Sphere;

public class HelloCamera extends SimpleGame {
	Sphere s;
	
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("myRods");
		rootNode.attachChild(rods);        

	    cam.setFrustumPerspective(45.0f, (float) display.getWidth()
	                / (float) display.getHeight(), 1f, 1000f);
        cam.setLocation(new Vector3f(10, 10, 10));
        cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
 		cam.update();
 		      
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloCamera app = new HelloCamera();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
