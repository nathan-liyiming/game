package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;

public class HelloBlueBox extends SimpleGame {

	
	protected void simpleInitGame() {
		Box b = new Box("MyBox", Vector3f.ZERO, 1, 4, 1);
		b.setDefaultColor(ColorRGBA.blue);
		rootNode.attachChild(b);

		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloBlueBox app = new HelloBlueBox();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
