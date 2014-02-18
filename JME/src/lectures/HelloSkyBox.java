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

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Skybox;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;

/**
 *Based on code by Jack Lindamood
 */
public class HelloSkyBox extends SimpleGame {
  
    /** A sky box for our scene. */
    Skybox sb;

    public static void main(String[] args) {
        HelloSkyBox app = new HelloSkyBox();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleInitGame() {
        sb = new Skybox("skybox", 200, 200, 200);

        try {
            ResourceLocatorTool.addResourceLocator(
                    ResourceLocatorTool.TYPE_TEXTURE,
                    new SimpleResourceLocator(getClass().getResource(
                            "/lectures/data/texture/")));
        } catch (Exception e) {
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
        
        rootNode.attachChild(sb);
        
        Box b = new Box("myBox", Vector3f.ZERO,10,10,10);
        // Get a TextureState
        TextureState ts=display.getRenderer().createTextureState();
        // Use the TextureManager to load a texture
        Texture t=TextureManager.loadTexture("monkey.png",Texture.MinificationFilter.Trilinear,Texture.MagnificationFilter.Bilinear);
        // Assign the texture to the TextureState
        ts.setTexture(t);
        b.setRenderState(ts);

        TextureState ts2=display.getRenderer().createTextureState();
        Sphere s = new Sphere("mySphere", new Vector3f(40,0,0), 10, 30, 25);
        Texture t2=TextureManager.loadTexture("monkey.jpg",Texture.MinificationFilter.Trilinear,Texture.MagnificationFilter.Bilinear);
        ts2.setTexture(t2);
        s.setRenderState(ts2);
        
        rootNode.attachChild(b);
        rootNode.attachChild(s);
    }


    /**
     * Called every frame for updating
     */
    protected void simpleUpdate() {
        // Let the programmable sound update itself.
        // Move the skybox into position
        sb.getLocalTranslation().set(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z);
    }

    @Override
    protected void cleanup() {
        super.cleanup();
         
    }
}