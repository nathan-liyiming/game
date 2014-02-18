package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.AxisRods;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;

public class HelloTwoBoxNormalisedTranslation extends SimpleGame {

	Box b,c; 
	protected void simpleInitGame() {
		AxisRods rods = new AxisRods("MyRods");
		b = new Box("WBox", Vector3f.ZERO, 1, 1, 1);
		
		c = new Box("BBox", Vector3f.ZERO, 1, 1, 1);
		c.setDefaultColor(ColorRGBA.blue);
		
		rootNode.attachChild(rods);
		rootNode.attachChild(b);
		rootNode.attachChild(c);
		
		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);

	}
	
	protected void simpleUpdate() {
		b.setLocalTranslation(b.getLocalTranslation().add((new Vector3f(1,0,0)).mult(.01f)));
		
		Vector3f v = new Vector3f(1,2,3);
		float l = v.length();
		Vector3f u = v.clone().mult(1/l);
		
		//u = v.normalize();
		
		c.setLocalTranslation(c.getLocalTranslation().add((u.mult(0.01f))));

		
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloTwoBoxNormalisedTranslation app = new HelloTwoBoxNormalisedTranslation();
		app.start();

	}

}
