/*
 * Copyright (c) 2003-2009 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package lectures;

import java.util.Random;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

/**
 *Based on code by Jack Lindamood
 */
public class HelloIntersection extends SimpleGame {
    private static final Logger logger = Logger
            .getLogger(HelloIntersection.class.getName());

    /** Target you're trying to hit */
    Sphere target;

    /** Used to move target location on a hit */
    Random r = new Random();

    /** A sky box for our scene. */
    Skybox sb;

    public static void main(String[] args) {
        HelloIntersection app = new HelloIntersection();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleInitGame() {

        /** Create a + for the middle of the screen */
        Text cross = Text.createDefaultTextLabel("Crosshairs", "+");

        // 8 is half the width of a font char
        /** Move the + to the middle */
        cross.setLocalTranslation(new Vector3f(display.getWidth() / 2f - 8f,
                display.getHeight() / 2f - 8f, 0));
        statNode.attachChild(cross);
        target = new Sphere("my sphere", 15, 15, 1);
        target.setModelBound(new BoundingSphere());
        target.updateModelBound();
		target.setDefaultColor(ColorRGBA.red);

        rootNode.attachChild(target);

        /** Create a skybox to suround our world */
        setupSky();

        // Attach the skybox to our root node, and force the rootnode to show
        // so that the skybox will always show
        rootNode.attachChild(sb);
        
        input.addAction(new MousePick());

        
		MaterialState ms = display.getRenderer().createMaterialState();
        ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
        rootNode.setRenderState(ms);

    }

 
    private void setupSky() {
        sb = new Skybox("skybox", 200, 200, 200);

        try {
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE,
                    new SimpleResourceLocator(getClass().getResource(
                            "/lectures/data/texture/")));
        } catch (Exception e) {
            logger.warning("Unable to access texture directory.");
            e.printStackTrace();
        }

        sb.setTexture(Skybox.Face.North, TextureManager.loadTexture("north.jpg",
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.West, TextureManager.loadTexture("west.jpg",
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.South, TextureManager.loadTexture("south.jpg",
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.East, TextureManager.loadTexture("east.jpg",
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.Up, TextureManager.loadTexture("top.jpg",
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));
        sb.setTexture(Skybox.Face.Down, TextureManager.loadTexture("bottom.jpg",
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear));
        sb.preloadTextures();
    }

    class MousePick extends MouseInputAction {
        int numBullets;
		public void performAction(InputActionEvent arg0) {
			// TODO Auto-generated method stub
			if( MouseInput.get().isButtonDown(0)){
	           logger.info("BANG");
	            /** Create bullet */
	            Sphere bullet = new Sphere("bullet" + numBullets++, 8, 8, .25f);
	            bullet.setModelBound(new BoundingSphere());
	            bullet.updateModelBound();
	            /** Move bullet to the camera location */
	            bullet.setLocalTranslation(new Vector3f(cam.getLocation()));
	    		bullet.setDefaultColor(ColorRGBA.blue);
	            /**
	             * Update the new world locaion for the bullet before I add a
	             * controller
	             */
	            bullet.updateGeometricState(0, true);
	            /**
	             * Add a movement controller to the bullet going in the camera's
	             * direction
	             */
	            bullet.addController(new BulletMover(bullet, new Vector3f(cam
	                    .getDirection())));
	            rootNode.attachChild(bullet);
	           // bullet.updateRenderState();
	            /** Signal our sound to play laser during rendering */
			}
			
		}
    	
    }
    
    class BulletMover extends Controller {
        private static final long serialVersionUID = 1L;
        /** Bullet that's moving */
        TriMesh bullet;

        /** Direciton of bullet */
        Vector3f direction;

        /** speed of bullet */
        float speed = 10;

        /** Seconds it will last before going away */
        float lifeTime = 5;

        BulletMover(TriMesh bullet, Vector3f direction) {
            this.bullet = bullet;
            this.direction = direction;
            this.direction.normalizeLocal();
        }

        public void update(float time) {
            lifeTime -= time;
            /** If life is gone, remove it */
            if (lifeTime < 0) {
                rootNode.detachChild(bullet);
                bullet.removeController(this);
                return;
            }
            /** Move bullet */
            Vector3f bulletPos = bullet.getLocalTranslation();
            bulletPos.addLocal(direction.mult(time * speed));
            bullet.setLocalTranslation(bulletPos);
            /** Does the bullet intersect with target? */
            if (bullet.getWorldBound().intersects(target.getWorldBound())) {
                logger.info("OWCH!!!");

                target.setLocalTranslation(new Vector3f(r.nextFloat() * 10, r
                        .nextFloat() * 10, r.nextFloat() * 10));

                lifeTime = 0;
            }
        }
    }

    /**
     * Called every frame for updating
     */
    protected void simpleUpdate() {
        // Let the programmable sound update itself.
        // Move the skybox into position
        sb.getLocalTranslation().set(cam.getLocation().x, cam.getLocation().y,
                cam.getLocation().z);
    }

    @Override
    protected void cleanup() {
        super.cleanup();
         
    }
}