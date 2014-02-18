package lectures;

import com.jme.app.SimpleGame;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;

public class TestRotateAndShift extends SimpleGame {
  private Quaternion rotQuat = new Quaternion();
  private float angle = 0;
  private Vector3f axis = new Vector3f(0, 1, 0);
  private Sphere s;
  private Sphere moon;
  private Node pivotNode;

  
  public static void main(String[] args) {
    TestRotateAndShift app = new TestRotateAndShift();
    app.setConfigShowMode(ConfigShowMode.AlwaysShow);
    app.start();
  }

  protected void simpleUpdate() {
    if (tpf < 1) {
      angle = angle + (tpf * 1);
      if (angle > 360) {
        angle = 0;
      }
    }
    rotQuat.fromAngleAxis(angle, axis);
    pivotNode.setLocalRotation(rotQuat);
    moon.setLocalTranslation(moon.getLocalTranslation().add(tpf*5,0,0));    
  }

  protected void simpleInitGame() {
    display.setTitle("jME - Rotation About a Point");
    //Planet
    s = new Sphere("Planet", 25, 25, 25);
    rootNode.attachChild(s);
    //Moon
    moon = new Sphere("Moon 1",25, 25, 10);
    moon.setLocalTranslation(40, 0, 0);
    pivotNode = new Node("PivotNode 1");
    pivotNode.attachChild(moon);
    rootNode.attachChild(pivotNode);
  }
}
