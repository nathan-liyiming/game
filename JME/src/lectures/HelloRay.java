package lectures;

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

public class HelloRay extends SimpleGame {

	
	protected void simpleInitGame() {
		for (int i = 0; i< 10; i++)	{
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 5; k++) {
					Box b = new Box("myBox"+i +":"+j+":"+k, Vector3f.ZERO, 1,1,1);
					b.setModelBound(new BoundingBox());
					b.updateModelBound();
					b.setLocalTranslation((i-4.5f)*3, (j-4.5f)*3, (k-4.5f)*3-30);
					b.setDefaultColor(ColorRGBA.gray);
					rootNode.attachChild(b);
				}
			}
		}
		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);
        Text cross = Text.createDefaultTextLabel("Crosshairs", "+");
        cross.setLocalTranslation(new Vector3f(display.getWidth() / 2f - 8f,
                display.getHeight() / 2f - 8f, 0));
        rootNode.attachChild(cross);
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
		HelloRay app = new HelloRay();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
