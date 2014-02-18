package lectures;



//  In order to run this example you need to extend the java heap size. Right-click on the class name in the navigation panel, properties -> Run / Debug settings -> Edit -> (x) Arguments -> VM arguments and enter 
//  -Xms32m -Xmx1024m 

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;

public class HelloCrazyRay extends SimpleGame {

	
	protected void simpleInitGame() {
		for (int i = 0; i< 50; i++)	{
			for (int j = 0; j < 50; j++) {
				for (int k = 0; k < 50; k++) {
					Box b = new Box("myBox"+i +":"+j+":"+k, Vector3f.ZERO, 1,1,1);
					b.setModelBound(new BoundingBox());
					b.updateModelBound();
					b.setLocalTranslation((i-4.5f)*3, (j-4.5f)*3, (k-4.5f)*3-150);
					b.setDefaultColor(ColorRGBA.gray);
					rootNode.attachChild(b);
				}
			}
		}
        Text cross = Text.createDefaultTextLabel("Crosshairs", "+");
        cross.setLocalTranslation(new Vector3f(display.getWidth() / 2f - 8f,
                display.getHeight() / 2f - 8f, 0));
        rootNode.attachChild(cross);

		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);
	}
	protected void simpleUpdate() {
		Ray ray = new Ray(cam.getLocation(), cam.getDirection());
		PickResults pr = new BoundingPickResults();
		pr.clear();
		rootNode.findPick(ray, pr);
		for (int i = 0; i < pr.getNumber(); i++) {
			pr.getPickData(i).getTargetMesh().setDefaultColor(ColorRGBA.red);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloCrazyRay app = new HelloCrazyRay();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
