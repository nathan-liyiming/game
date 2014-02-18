package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Teapot;

public class HelloTable extends SimpleGame {
	
	protected void simpleInitGame() {
		Box tableTop = new Box("Top", Vector3f.ZERO, 10, .3f, 10);
		Box leg1 = new Box("Leg1", Vector3f.ZERO, 1,5,1);
		Box leg2 = new Box("Leg2", Vector3f.ZERO, 1,5,1);
		Box leg3 = new Box("Leg2", Vector3f.ZERO, 1,5,1);
		Box leg4 = new Box("Leg2", Vector3f.ZERO, 1,5,1);
				
		leg1.setLocalTranslation( 7, 0, 7);
		leg2.setLocalTranslation(-7, 0, 7);
		leg3.setLocalTranslation( 7, 0,-7);
		leg4.setLocalTranslation(-7, 0,-7);
		
		Node legs = new Node("Legs");
		legs.attachChild(leg1);
		legs.attachChild(leg2);
		legs.attachChild(leg3);
		legs.attachChild(leg4);
				
		legs.setLocalTranslation(legs.getLocalTranslation().add(new Vector3f(0,-5f,0)));
		
		Node table = new Node("Table");
		table.attachChild(tableTop);
		table.attachChild(legs);
		
		Teapot t = new Teapot("Teepot");
		t.setLocalTranslation(4, .15f, 5);
		rootNode.attachChild(t);
		
		rootNode.attachChild(table);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloTable app = new HelloTable();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();

	}

}
