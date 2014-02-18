package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;

public class HelloText extends SimpleGame {

	
	protected void simpleInitGame() {
		Box b = new Box("MyBox", Vector3f.ZERO, 1, 1, 1);
		rootNode.attachChild(b);
		
        Text t = Text.createDefaultTextLabel("myText", "Hi Boris");
        t.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        t.setLightCombineMode(Spatial.LightCombineMode.Off);
        t.setLocalTranslation(new Vector3f(display.getWidth()/2, display.getHeight()/2, 1));
        rootNode.attachChild(t);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloText app = new HelloText();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
