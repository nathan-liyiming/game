/**
 * This is a two player game. The game is played with three balls (red, blue and white) on
 * a rectangular billiards table without holes. Player one (Blue Player) controls the blue ball,
 * player two (Red Player) controls the red ball. Players play in turns. Players keep turn as
 * long as their shots are successful.
 * There are some nice feature in my game:
 * <ol>
 * <li>There are some sounds, such as background, collision or kick successfully sound.</li>
 * <li>There is a background picture.</li>
 * <li>More realistic physics: lost energy with collision and rotation of balls.</li>
 * <li>Not only keyboard can control the direction and force of ball, but also mouse can control it.</li>
 * <li>Set two kinds of virtual players(AI player).</li>
 * </ol>
 * 
 * @author <a href="mailto:Y.Li38@student.liverpool.ac.uk">Li Yiming</a>
 * @version 1.0
 * 
 */

package assignment1;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.FirstPersonHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.scene.shape.Tube;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.ColorMaterial;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
import com.jme.util.resource.SimpleResourceLocator;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class BilliardsGame extends SimpleGame {

	/* ------------------ Fields ------------------------ */

	// create objects for sound
	private AudioSystem audio;
	private AudioTrack backgroudSound;
	private AudioTrack collideSound;
	private AudioTrack collideEdgeSound;
	private AudioTrack scoreSound;

	// choose the number of players
	// 1--two players
	// 2--one player VS AI1
	// 3--one player VS AI2
	private int choose = 1;

	// This will be my mouse
	private AbsoluteMouse am;
	private int mouseForce = 1;

	// lost energy
	private float lossForSide = 0.9f;
	private float lossForBall = 0.9f;

	// creage room background by Skybox
	private Skybox sb;

	// nodes for table, sides, legs and arrow(directionNode)
	private Node table = new Node("Table");;
	private Node sides = new Node("Sides");
	private Node legs = new Node("Legs");
	private Node directionNode = new Node("Direction");

	// create sides for table
	private Box side1;
	private Box side2;
	private Box side3;
	private Box side4;

	// create for Balls
	private Sphere redBall;
	private float redSpeed = 0;
	private Vector3f redDirection = new Vector3f(0, 0, 0);
	private float redAngle = 0;
	private int redScore = 0;
	private Text redScoreText;

	private Sphere blueBall;
	private float blueSpeed = 0;
	private Vector3f blueDirection = new Vector3f(0, 0, 0);
	private float blueAngle = 0;
	private int blueScore = 0;
	private Text blueScoreText;

	private Sphere whiteBall;
	private float whiteSpeed = 0;
	private Vector3f whiteDirection = new Vector3f(0, 0, 0);
	private float whiteAngle = 0;

	// AIBall is a 'cheating'(AI2 player)
	private Sphere AIBall;
	private float AISpeed;
	private Vector3f AIDirection;
	private float AIAngle = 0;
	private float time = 2;
	private int AIScore = 0;
	private int addAI = 0;

	// create for arrow(direction)
	private Arrow direction;
	private float angle;
	private float length;

	// create for camera and sound
	private int camera = 1;
	private int sound = 1;
	private int initialSound = 1;

	// create for scoring mechanism
	private enum Kick {
		nothing, opponent, white, whiteAndOpponent, opponentAndWhite
	};

	private Kick play = Kick.whiteAndOpponent;
	private int count = 0;
	private Text round;

	// create for switching roles
	private enum State {
		red, blue, auto
	};

	private State state = State.auto;
	private State lastPlayer = State.blue;

	/* ------------------ Methods ------------------------ */

	/**
	 * Default constructor
	 */
	public BilliardsGame() {

	}

	/**
	 * 
	 * @param initialSound
	 *            1--turn on 0--turn off
	 * @param choose
	 *            1--two players 2--Player VS AI1 3--Player VS AI2
	 */
	public BilliardsGame(int initialSound, int choose) {
		this.initialSound = initialSound;
		this.choose = choose;
	}

	/**
	 * Initialize the game and set all the features.
	 */
	protected void simpleInitGame() {
		display.setTitle("Carom Billiards Game");

		setCamera();
		setSound();
		setBackground();
		setBalls();
		setTable();
		setKeyBoard();
		setMouse();
		setText();

		// Make the object default colors shine through
		MaterialState ms = display.getRenderer().createMaterialState();
		ms.setColorMaterial(ColorMaterial.AmbientAndDiffuse);
		rootNode.setRenderState(ms);
	}

	/**
	 * Initialize camera.
	 */
	private void setCamera() {
		cam.setFrustumPerspective(45.0f, (float) display.getWidth()
				/ (float) display.getHeight(), 1f, 1000f);
		cam.setLocation(new Vector3f(-150, 200, 80));
		cam.lookAt(new Vector3f(-30, 0, 10), Vector3f.UNIT_Y);
		cam.update();
	}

	/**
	 * Initialize sound.
	 */
	private void setSound() {
		audio = AudioSystem.getSystem();

		backgroudSound = audio.createAudioTrack(
				"/assignment1/data/sound/Background.ogg", false);
		collideSound = audio.createAudioTrack(
				"/assignment1/data/sound/Collide.wav", false);
		collideEdgeSound = audio.createAudioTrack(
				"/assignment1/data/sound/CollideEdge.wav", false);
		scoreSound = audio.createAudioTrack(
				"/assignment1/data/sound/Score.wav", false);

		MusicTrackQueue queue = AudioSystem.getSystem().getMusicQueue();
		queue.setCrossfadeinTime(0);
		queue.setRepeatType(RepeatType.ONE);
		queue.addTrack(backgroudSound);
		queue.play();
		// if at the beginning, the user want to turn off
		if (initialSound == 0) {
			backgroudSound.pause();
			collideSound.stop();
			collideEdgeSound.stop();
			scoreSound.stop();

			backgroudSound.setEnabled(false);
			collideSound.setEnabled(false);
			collideEdgeSound.setEnabled(false);
			scoreSound.setEnabled(false);
			sound = 0;
		}
	}

	/**
	 * Initialize background.
	 */
	private void setBackground() {
		sb = new Skybox("skybox", 200, 200, 200);

		// get the location of resources
		try {
			ResourceLocatorTool.addResourceLocator(
					ResourceLocatorTool.TYPE_TEXTURE,
					new SimpleResourceLocator(getClass().getResource(
							"/assignment1/data/texture/")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		sb.setTexture(Skybox.Face.North, TextureManager.loadTexture(
				"around.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.West, TextureManager.loadTexture(
				"around.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.South, TextureManager.loadTexture(
				"around.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.East, TextureManager.loadTexture(
				"around.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.Up, TextureManager.loadTexture("around.jpg",
				Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.setTexture(Skybox.Face.Down, TextureManager.loadTexture(
				"around.jpg", Texture.MinificationFilter.BilinearNearestMipMap,
				Texture.MagnificationFilter.Bilinear));
		sb.preloadTextures();

		rootNode.attachChild(sb);
	}

	/**
	 * Initialize balls.
	 */
	private void setBalls() {
		// Create ball
		redBall = new Sphere("Red Ball", 20, 20, 3.5f);
		redBall.setModelBound(new BoundingSphere());
		redBall.updateModelBound();
		redBall.setDefaultColor(ColorRGBA.red);
		redBall.getLocalTranslation().set(50, 4.5f, 0);
		rootNode.attachChild(redBall);

		blueBall = new Sphere("Blue Ball", 20, 20, 3.5f);
		blueBall.setModelBound(new BoundingSphere());
		blueBall.updateModelBound();
		blueBall.setDefaultColor(ColorRGBA.blue);
		blueBall.getLocalTranslation().set(-50, 4.5f, 25);
		rootNode.attachChild(blueBall);

		whiteBall = new Sphere("White Ball", 20, 20, 3.5f);
		whiteBall.setModelBound(new BoundingSphere());
		whiteBall.updateModelBound();
		whiteBall.setDefaultColor(ColorRGBA.white);
		whiteBall.getLocalTranslation().set(-50, 4.5f, -25);
		rootNode.attachChild(whiteBall);

		AIBall = new Sphere("AI Ball", 20, 20, 3.5f);
		AIBall.setModelBound(new BoundingSphere());
		AIBall.updateModelBound();
		AIBall.getLocalTranslation().set(50, 4.5f, 0);
		// cheating for AIBall with transparency
		setBallTransparency();
		rootNode.attachChild(AIBall);

	}

	/**
	 * Set AIBall transparency if choose==3.
	 */
	private void setBallTransparency() {
		// the sphere material that will be modified to make the sphere
		// look opaque then transparent then opaque and so on
		MaterialState materialState = display.getRenderer()
				.createMaterialState();
		materialState.setAmbient(new ColorRGBA(0.0f, 0.0f, 0.0f, 0));
		materialState.setDiffuse(new ColorRGBA(0.1f, 0.5f, 0.8f, 0));
		materialState.setSpecular(new ColorRGBA(1.0f, 1.0f, 1.0f, 0));
		materialState.setShininess(128.0f);
		materialState.setEmissive(new ColorRGBA(0.0f, 0.0f, 0.0f, 0));
		materialState.setEnabled(true);

		// this is used to handle the internal sphere faces when
		// setting them to transparent
		materialState.setMaterialFace(MaterialState.MaterialFace.FrontAndBack);

		AIBall.setRenderState(materialState);
		AIBall.updateRenderState();

		// to handle transparency: a BlendState
		// an other tutorial will be made to deal with the possibilities of this
		// RenderState
		final BlendState alphaState = DisplaySystem.getDisplaySystem()
				.getRenderer().createBlendState();
		alphaState.setBlendEnabled(true);
		alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaState
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaState.setTestEnabled(true);
		alphaState.setTestFunction(BlendState.TestFunction.GreaterThan);
		alphaState.setEnabled(true);

		AIBall.setRenderState(alphaState);
		AIBall.updateRenderState();

		// since the sphere will be transparent, place it
		// in the transparent render queue!
		AIBall.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
	}

	/**
	 * Initialize table adding legs.
	 */
	private void setTable() {
		Box tableBottom = new Box("Bottom", new Vector3f(), 100, 1, 50);
		tableBottom.setModelBound(new BoundingBox());
		tableBottom.updateModelBound();
		tableBottom.setDefaultColor(new ColorRGBA(0.4f, 1, 0.3f, 0));

		side1 = new Box("Side1", new Vector3f(), 102, 3, 2);
		side1.setModelBound(new BoundingBox());
		side1.updateModelBound();
		side1.getLocalTranslation().set(0, 0, 50);
		side1.setDefaultColor(ColorRGBA.brown);
		sides.attachChild(side1);

		side2 = new Box("Side2", new Vector3f(), 102, 3, 2);
		side2.setModelBound(new BoundingBox());
		side2.updateModelBound();
		side2.getLocalTranslation().set(0, 0, -50);
		side2.setDefaultColor(ColorRGBA.brown);
		sides.attachChild(side2);

		side3 = new Box("Side3", new Vector3f(), 2, 3, 50);
		side3.setModelBound(new BoundingBox());
		side3.updateModelBound();
		side3.getLocalTranslation().set(100, 0, 0);
		side3.setDefaultColor(ColorRGBA.brown);
		sides.attachChild(side3);

		side4 = new Box("Side4", new Vector3f(), 2, 3, 50);
		side4.setModelBound(new BoundingBox());
		side4.updateModelBound();
		side4.getLocalTranslation().set(-100, 0, 0);
		side4.setDefaultColor(ColorRGBA.brown);
		sides.attachChild(side4);

		sides.setLocalTranslation(sides.getLocalTranslation().add(
				new Vector3f(0, 2f, 0)));

		Tube leg1 = new Tube("Leg1", 5, 0, 40);
		leg1.setModelBound(new BoundingBox());
		leg1.updateModelBound();
		leg1.getLocalTranslation().set(90, 0, 40);
		leg1.setDefaultColor(ColorRGBA.brown);
		legs.attachChild(leg1);

		Tube leg2 = new Tube("Leg2", 5, 0, 40);
		leg2.setModelBound(new BoundingBox());
		leg2.updateModelBound();
		leg2.getLocalTranslation().set(-90, 0, 40);
		leg2.setDefaultColor(ColorRGBA.brown);
		legs.attachChild(leg2);

		Tube leg3 = new Tube("Leg3", 5, 0, 40);
		leg3.setModelBound(new BoundingBox());
		leg3.updateModelBound();
		leg3.getLocalTranslation().set(90, 0, -40);
		leg3.setDefaultColor(ColorRGBA.brown);
		legs.attachChild(leg3);

		Tube leg4 = new Tube("Leg4", 5, 0, 40);
		leg4.setModelBound(new BoundingBox());
		leg4.updateModelBound();
		leg4.getLocalTranslation().set(-90, 0, -40);
		leg4.setDefaultColor(ColorRGBA.brown);
		legs.attachChild(leg4);

		legs.setLocalTranslation(legs.getLocalTranslation().add(
				new Vector3f(0, -21f, 0)));

		table.attachChild(tableBottom);
		table.attachChild(sides);
		table.attachChild(legs);
		rootNode.attachChild(table);
	}

	/**
	 * Initialize mouse for changing the direction and force of ball.
	 */
	private void setMouse() {
		// Create a new mouse. Restrict its movements to the display screen.
		am = new AbsoluteMouse("The Mouse", display.getWidth(),
				display.getHeight());

		// Get a picture for my mouse.
		TextureState ts = display.getRenderer().createTextureState();
		ts.setTexture(TextureManager.loadTexture("cursor.png",
				Texture.MinificationFilter.NearestNeighborNoMipMaps,
				Texture.MagnificationFilter.Bilinear));
		am.setRenderState(ts);

		// Make the mouse's background blend with what's already there
		BlendState as = display.getRenderer().createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		as.setTestEnabled(true);
		as.setTestFunction(BlendState.TestFunction.GreaterThan);
		am.setRenderState(as);

		// Get the mouse input device and assign it to the AbsoluteMouse
		// Move the mouse to the middle of the screen to start with
		am.setLocalTranslation(new Vector3f(display.getWidth() / 2, display
				.getHeight() / 2, 0));
		// Assign the mouse to an input handler
		am.registerWithInputHandler(input);
		rootNode.attachChild(am);
		((FirstPersonHandler) input).getMouseLookHandler().setEnabled(false);
	}

	/**
	 * Initialize test for scoring and hints.
	 */
	private void setText() {
		// Create score showing items
		redScoreText = Text.createDefaultTextLabel("RedScoreText",
				"Red Player Score: 0");
		redScoreText.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		redScoreText.setLightCombineMode(Spatial.LightCombineMode.Off);
		redScoreText.setLocalTranslation(new Vector3f(20,
				display.getHeight() - 20, 1));
		statNode.attachChild(redScoreText);

		blueScoreText = Text.createDefaultTextLabel("BlueScoreText",
				"Blue Player Score: 0");
		blueScoreText.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		blueScoreText.setLightCombineMode(Spatial.LightCombineMode.Off);
		blueScoreText.setLocalTranslation(new Vector3f(20,
				display.getHeight() - 40, 1));
		statNode.attachChild(blueScoreText);

		round = Text.createDefaultTextLabel("Round", "Round: 1");
		round.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		round.setLightCombineMode(Spatial.LightCombineMode.Off);
		round.setLocalTranslation(new Vector3f(20, 20, 1));
		statNode.attachChild(round);

		Text hint1 = Text.createDefaultTextLabel("Hint1", "1/2:    direction");
		hint1.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		hint1.setLightCombineMode(Spatial.LightCombineMode.Off);
		hint1.setLocalTranslation(new Vector3f(display.getWidth() / 2 + 100,
				100, 1));
		statNode.attachChild(hint1);

		Text hint2 = Text.createDefaultTextLabel("Hint2", "9/0:        force");
		hint2.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		hint2.setLightCombineMode(Spatial.LightCombineMode.Off);
		hint2.setLocalTranslation(new Vector3f(display.getWidth() / 2 + 100,
				80, 1));
		statNode.attachChild(hint2);

		Text hint3 = Text.createDefaultTextLabel("Hint3", "Space:       kick");
		hint3.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		hint3.setLightCombineMode(Spatial.LightCombineMode.Off);
		hint3.setLocalTranslation(new Vector3f(display.getWidth() / 2 + 100,
				60, 1));
		statNode.attachChild(hint3);

		Text hint4 = Text.createDefaultTextLabel("Hint4", "F5: change camera");
		hint4.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		hint4.setLightCombineMode(Spatial.LightCombineMode.Off);
		hint4.setLocalTranslation(new Vector3f(display.getWidth() / 2 + 100,
				40, 1));
		statNode.attachChild(hint4);

		Text hint5 = Text.createDefaultTextLabel("Hint5", "F6:  sound on/off");
		hint5.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		hint5.setLightCombineMode(Spatial.LightCombineMode.Off);
		hint5.setLocalTranslation(new Vector3f(display.getWidth() / 2 + 100,
				20, 1));
		statNode.attachChild(hint5);
	}

	/**
	 * Initialize keyboard.
	 */
	private void setKeyBoard() {
		// Assign key bindings
		KeyBindingManager.getKeyBindingManager().set("LEFT", KeyInput.KEY_1);
		KeyBindingManager.getKeyBindingManager().set("RIGHT", KeyInput.KEY_2);
		KeyBindingManager.getKeyBindingManager().set("LARGE_FORCE",
				KeyInput.KEY_9);
		KeyBindingManager.getKeyBindingManager().set("SMALL_FORCE",
				KeyInput.KEY_0);
		KeyBindingManager.getKeyBindingManager()
				.set("KICK", KeyInput.KEY_SPACE);
		KeyBindingManager.getKeyBindingManager().set("CAMERA", KeyInput.KEY_F5);
		KeyBindingManager.getKeyBindingManager().set("SOUND", KeyInput.KEY_F6);
	}

	/**
	 * Update for each frame.
	 */
	protected void simpleUpdate() {
		updataCameraAndSound();
		switch (state) {
		case blue:
			updataDirectionAndForce();
			if (KeyBindingManager.getKeyBindingManager().isValidCommand("KICK",
					false)) {
				// set the speed and direction
				blueSpeed = 12 * length;
				blueDirection = new Vector3f(FastMath.cos(angle) * length, 0,
						FastMath.sin(-angle) * length).normalize();
				directionNode.detachChild(direction);
				state = State.auto;
			}
			break;
		case red:
			if (choose == 1) {
				updataDirectionAndForce();
				if (KeyBindingManager.getKeyBindingManager().isValidCommand(
						"KICK", false)) {
					// set the speed and direction
					redSpeed = 12 * length;
					redDirection = new Vector3f(FastMath.cos(angle) * length,
							0, FastMath.sin(-angle) * length).normalize();
					directionNode.detachChild(direction);
					state = State.auto;
				}
			} else if (choose == 2) {
				// wait for 2 seconds
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AIPlayer1();
				state = State.auto;
			} else {
				AIPlayer2();
			}
			break;
		case auto:
			// decrease the speed
			blueSpeed -= 30 * tpf;
			redSpeed -= 30 * tpf;
			whiteSpeed -= 30 * tpf;

			// until stop, then update the round
			if (blueSpeed < 0.01f && redSpeed < 0.01f && whiteSpeed < 0.01f) {
				blueSpeed = 0;
				redSpeed = 0;
				whiteSpeed = 0;
				updateRound();
			}

			// check the collision with sides
			updateCollisionSides(blueBall, blueDirection);
			updateCollisionSides(redBall, redDirection);
			updateCollisionSides(whiteBall, whiteDirection);

			// check the collisions with other balls
			updateCollisionBalls();

			// update the moving
			if (blueSpeed >= 0.01f) {
				blueBall.setLocalTranslation(blueBall.getLocalTranslation()
						.add(blueDirection.mult(blueSpeed * tpf)));

				// compute the moving angle to simulate the rotation
				// blueAngle=(float)(((blueSpeed*tpf)/(2*FastMath.PI*3.5))*2*FastMath.PI)
				blueAngle += (float) (blueSpeed * tpf / 3.5);
				if (blueAngle > 2 * FastMath.PI) {
					blueAngle -= 2 * FastMath.PI;
				}

				Vector3f n = blueDirection.cross(Vector3f.UNIT_Y);
				Quaternion rotQuat = new Quaternion();
				rotQuat.fromAngleAxis(blueAngle, n);
				blueBall.setLocalRotation(rotQuat);
			}

			// update the moving
			if (redSpeed >= 0.01f) {
				redBall.setLocalTranslation(redBall.getLocalTranslation().add(
						redDirection.mult(redSpeed * tpf)));

				// compute the moving angle to simulate the rotation
				// redAngle=(float)(((redSpeed*tpf)/(2*FastMath.PI*3.5))*2*FastMath.PI)
				redAngle += (float) (redSpeed * tpf / 3.5);
				if (redAngle > 2 * FastMath.PI) {
					redAngle -= 2 * FastMath.PI;
				}

				Vector3f n = redDirection.cross(Vector3f.UNIT_Y);
				Quaternion rotQuat = new Quaternion();
				rotQuat.fromAngleAxis(redAngle, n);
				redBall.setLocalRotation(rotQuat);
			}

			// update the moving
			if (whiteSpeed >= 0.01f) {
				whiteBall.setLocalTranslation(whiteBall.getLocalTranslation()
						.add(whiteDirection.mult(whiteSpeed * tpf)));

				// compute the moving angle to simulate the rotation
				// whiteAngle=(float)(((whiteSpeed*tpf)/(2*FastMath.PI*3.5))*2*FastMath.PI)
				whiteAngle += (float) (whiteSpeed * tpf / 3.5);
				if (whiteAngle > 2 * FastMath.PI) {
					whiteAngle -= 2 * FastMath.PI;
				}

				Vector3f n = whiteDirection.cross(Vector3f.UNIT_Y);
				Quaternion rotQuat = new Quaternion();
				rotQuat.fromAngleAxis(whiteAngle, n);
				whiteBall.setLocalRotation(rotQuat);
			}

			// update the sound
			AudioSystem.getSystem().update();
			break;
		}
	}

	/**
	 * Update the camera and sound.
	 */
	private void updataCameraAndSound() {
		// always in the middle of box
		sb.getLocalTranslation().set(cam.getLocation().x, cam.getLocation().y,
				cam.getLocation().z);

		// switch the camera
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("CAMERA",
				false)) {
			if (camera == 1) {
				changeSecondCamera();
				camera = 2;
			} else if (camera == 2) {
				changeThirdCamera();
				camera = 3;
			} else {
				setCamera();
				camera = 1;
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("SOUND",
				false)) {
			if (sound == 1) {
				backgroudSound.pause();
				collideSound.stop();
				collideEdgeSound.stop();
				scoreSound.stop();

				backgroudSound.setEnabled(false);
				collideSound.setEnabled(false);
				collideEdgeSound.setEnabled(false);
				scoreSound.setEnabled(false);
				sound = 0;
			} else {
				backgroudSound.setEnabled(true);
				collideSound.setEnabled(true);
				collideEdgeSound.setEnabled(true);
				scoreSound.setEnabled(true);

				backgroudSound.play();
				sound = 1;
			}
		}
	}

	/**
	 * Another camera of location.
	 */
	private void changeSecondCamera() {
		cam.setFrustumPerspective(45.0f, (float) display.getWidth()
				/ (float) display.getHeight(), 1f, 1000f);
		cam.setLocation(new Vector3f(0, 200, 0));
		cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Z.mult(-1));
		cam.update();
	}

	/**
	 * Another camera of location.
	 */
	private void changeThirdCamera() {
		cam.setFrustumPerspective(45.0f, (float) display.getWidth()
				/ (float) display.getHeight(), 1f, 1000f);
		cam.setLocation(new Vector3f(0, 100, 200));
		cam.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
		cam.update();
	}

	/**
	 * Update the direction and force of ball by keyboard.
	 */
	private void updataDirectionAndForce() {

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("LEFT",
				true)
				|| MouseInput.get().isButtonDown(0)) {
			if (tpf < 1) {
				angle += (tpf * 1);
				if (angle > 2 * FastMath.PI) {
					angle -= 2 * FastMath.PI;
				}
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("RIGHT",
				true)) {
			if (tpf < 1) {
				angle -= (tpf * 1);
				if (angle < -2 * FastMath.PI) {
					angle += 2 * FastMath.PI;
				}
			}
		}

		Quaternion qDirection = new Quaternion();
		qDirection.fromAngleAxis(angle, Vector3f.UNIT_Y);
		directionNode.setLocalRotation(qDirection);

		if (MouseInput.get().isButtonDown(1)) {
			if (mouseForce == 1) {
				if (tpf < 1 && length < 26) {
					length += (tpf * 10);
				} else {
					mouseForce = 0;
				}
			} else {
				if (tpf < 1 && length > 6) {
					length -= (tpf * 10);
				} else {
					mouseForce = 1;
				}
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"LARGE_FORCE", true)) {
			// there is a limit for force
			if (tpf < 1 && length < 26) {
				length += (tpf * 10);
			}
		}

		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"SMALL_FORCE", true)) {
			// there is a limit for force
			if (tpf < 1 && length > 6) {
				length -= (tpf * 10);
			}
		}

		direction.getLocalTranslation().set(6 + length / 2, 0, 0);
		direction.updateGeometry(length, 0.5f);
	}

	/**
	 * Place a arrow on the ball by initialization.
	 * 
	 * @param ball
	 *            red or blue ball on turn
	 */
	private void updataArrow(Sphere ball) {
		angle = 0f;
		length = 12f;
		direction = new Arrow("direction", 12, 0.5f);
		direction.setModelBound(new BoundingBox());
		direction.updateModelBound();
		direction.getLocalTranslation().set(12, 0, 0);
		Quaternion q = new Quaternion();
		q.fromAngleAxis(FastMath.PI * 3 / 2, Vector3f.UNIT_Z);
		direction.setLocalRotation(q);

		directionNode.setLocalTranslation(ball.getLocalTranslation());
		directionNode.attachChild(direction);
		rootNode.attachChild(directionNode);
	}

	/**
	 * Update the collision with sides. For each collision, there is a loss
	 * energy. And update the sound.
	 * 
	 * @param ball
	 *            red, blue or white ball
	 * @param direct
	 *            the direction of ball
	 */
	private void updateCollisionSides(Sphere ball, Vector3f direct) {
		// loss the energy for each time and always keep the reverse direction
		// there is a sound for each collision

		if (ball.hasCollision(side1, false)) {
			direct.z = -lossForSide * FastMath.abs(direct.z);
			collideEdgeSound.play();
		}

		if (ball.hasCollision(side2, false)) {
			direct.z = lossForSide * FastMath.abs(direct.z);
			collideEdgeSound.play();
		}

		if (ball.hasCollision(side3, false)) {
			direct.x = -lossForSide * FastMath.abs(direct.x);
			collideEdgeSound.play();
		}

		if (ball.hasCollision(side4, false)) {
			direct.x = lossForSide * FastMath.abs(direct.x);
			collideEdgeSound.play();
		}
	}

	/**
	 * Update the collision with ball and for each time there is a lost energy.
	 * And for each time, there is a sound.
	 */
	private void updateCollisionBalls() {
		if (redBall.hasCollision(blueBall, false)) {
			Vector3f n = blueBall.getLocalTranslation()
					.subtract(redBall.getLocalTranslation()).normalize();
			float proj1V = redDirection.mult(redSpeed).dot(n);
			float proj2V = blueDirection.mult(blueSpeed).dot(n);
			Vector3f tan1 = redDirection.mult(redSpeed)
					.subtract(n.mult(proj1V));
			Vector3f tan2 = blueDirection.mult(blueSpeed).subtract(
					n.mult(proj2V));
			if (proj1V - proj2V > 0) {
				Vector3f velocity1 = tan1.add(n.mult(lossForBall * proj2V));
				Vector3f velocity2 = tan2.add(n.mult(lossForBall * proj1V));
				redDirection = velocity1.normalize();
				blueDirection = velocity2.normalize();
				redSpeed = velocity1.length();
				blueSpeed = velocity2.length();

				// keep the track for scoring
				if (play == Kick.nothing) {
					play = Kick.opponent;
				} else if (play == Kick.white) {
					play = Kick.whiteAndOpponent;
				}
				// sound of collision
				collideSound.play();
			}
		}
		if (redBall.hasCollision(whiteBall, false)) {
			Vector3f n = whiteBall.getLocalTranslation()
					.subtract(redBall.getLocalTranslation()).normalize();
			float proj1V = redDirection.mult(redSpeed).dot(n);
			float proj2V = whiteDirection.mult(whiteSpeed).dot(n);
			Vector3f tan1 = redDirection.mult(redSpeed)
					.subtract(n.mult(proj1V));
			Vector3f tan2 = whiteDirection.mult(whiteSpeed).subtract(
					n.mult(proj2V));
			if (proj1V - proj2V > 0) {
				Vector3f velocity1 = tan1.add(n.mult(lossForBall * proj2V));
				Vector3f velocity2 = tan2.add(n.mult(lossForBall * proj1V));
				redDirection = velocity1.normalize();
				whiteDirection = velocity2.normalize();
				redSpeed = velocity1.length();
				whiteSpeed = velocity2.length();

				// keep the track for scoring
				if (lastPlayer == State.red) {
					if (play == Kick.nothing) {
						play = Kick.white;
					} else if (play == Kick.opponent) {
						play = Kick.opponentAndWhite;
					}
				}
				// sound of collision
				collideSound.play();
			}
		}
		if (whiteBall.hasCollision(blueBall, false)) {
			Vector3f n = blueBall.getLocalTranslation()
					.subtract(whiteBall.getLocalTranslation()).normalize();
			float proj1V = whiteDirection.mult(whiteSpeed).dot(n);
			float proj2V = blueDirection.mult(blueSpeed).dot(n);
			Vector3f tan1 = whiteDirection.mult(whiteSpeed).subtract(
					n.mult(proj1V));
			Vector3f tan2 = blueDirection.mult(blueSpeed).subtract(
					n.mult(proj2V));
			if (proj1V - proj2V > 0) {
				Vector3f velocity1 = tan1.add(n.mult(lossForBall * proj2V));
				Vector3f velocity2 = tan2.add(n.mult(lossForBall * proj1V));
				whiteDirection = velocity1.normalize();
				blueDirection = velocity2.normalize();
				whiteSpeed = velocity1.length();
				blueSpeed = velocity2.length();

				// keep the track for scoring
				if (lastPlayer == State.blue) {
					if (play == Kick.nothing) {
						play = Kick.white;
					} else if (play == Kick.opponent) {
						play = Kick.opponentAndWhite;
					}
				}
				// sound of collision
				collideSound.play();
			}
		}
	}

	/**
	 * Update the round and change the text on the screen.
	 */
	private void updateRound() {
		// it depends on the track of collision
		if (play == Kick.opponentAndWhite) {
			if (lastPlayer == State.red) {
				state = State.red;
				redScoreText.getText().replace(0,
						redScoreText.getText().length(),
						"Red Player Score: " + (++redScore));
			} else {
				state = State.blue;
				blueScoreText.getText().replace(0,
						blueScoreText.getText().length(),
						"Blue Player Score: " + (++blueScore));
			}
			scoreSound.play();
		} else if (play == Kick.whiteAndOpponent) {
			if (lastPlayer == State.red) {
				state = State.red;
			} else {
				state = State.blue;
			}
		} else {
			if (lastPlayer == State.red) {
				state = State.blue;
				lastPlayer = State.blue;
			} else {
				state = State.red;
				lastPlayer = State.red;
			}
		}

		// choose==2 or choose==3 is controlled by AI without updating arrow
		if (state == State.red && choose == 1) {
			updataArrow(redBall);
		} else if (state == State.blue) {
			updataArrow(blueBall);
		}

		// round count
		count++;
		round.getText().replace(0, round.getText().length(), "Round: " + count);
		// update to nothing
		play = Kick.nothing;
	}

	/**
	 * AI player1: each time, it always kicks to the direction of blue ball
	 * without thinking about white one.
	 */
	private void AIPlayer1() {
		Vector3f redToBlue = blueBall.getLocalTranslation()
				.subtract(redBall.getLocalTranslation()).normalize();
		float tmp = redToBlue.dot(Vector3f.UNIT_X);
		float tmpAngle = FastMath.asin(tmp);

		// for each time, kick the wall firstly and then kick the blue ball,
		// which side of wall will be kicked depending on the location of white
		// ball. Because I want to avoid to kick the white ball firstly
		if (tmpAngle <= FastMath.HALF_PI / 2
				|| tmpAngle >= -FastMath.HALF_PI / 2) {
			float z = blueBall.getLocalTranslation().z
					+ (whiteBall.getLocalTranslation().x - blueBall
							.getLocalTranslation().x)
					* (blueBall.getLocalTranslation().z - redBall
							.getLocalTranslation().z)
					/ (blueBall.getLocalTranslation().x - redBall
							.getLocalTranslation().x);
			if (z < whiteBall.getLocalTranslation().z) {
				redDirection = new Vector3f(blueBall.getLocalTranslation().x,
						0, -blueBall.getLocalTranslation().z - 96).normalize();
				redSpeed = 12 * 20;
			} else {
				redDirection = new Vector3f(blueBall.getLocalTranslation().x,
						0, -blueBall.getLocalTranslation().z + 96).normalize();
				redSpeed = 12 * 20;
			}
		} else {
			float x = blueBall.getLocalTranslation().x
					+ (whiteBall.getLocalTranslation().z - blueBall
							.getLocalTranslation().z)
					* (blueBall.getLocalTranslation().x - redBall
							.getLocalTranslation().x)
					/ (blueBall.getLocalTranslation().z - redBall
							.getLocalTranslation().z);
			if (x < whiteBall.getLocalTranslation().x) {
				redDirection = new Vector3f(
						-blueBall.getLocalTranslation().x - 196, 0,
						blueBall.getLocalTranslation().z).normalize();
				redSpeed = 12 * 20;
			} else {
				redDirection = new Vector3f(
						-blueBall.getLocalTranslation().x + 196, 0,
						blueBall.getLocalTranslation().z).normalize();
				redSpeed = 12 * 20;
			}
		}
	}

	/**
	 * AI player2: it is a cheating and there is a transparent AIBall to kick
	 * firstly. When the AIBall can successfully kick the blue one and then
	 * white one, the red ball can be kicked. There is a time limit with 1.9. If
	 * the ball can not successfully kick, then change the direction by
	 * rotation.
	 */
	private void AIPlayer2() {
		time += tpf;

		// successfully to kick the blue and white balls
		if (AIScore == 3) {
			AIAngle = 0;
			time = 2;
			AISpeed = 0;
			rootNode.detachChild(AIBall);
			addAI = 0;
			state = State.auto;
		}

		if (time > 1.9) {
			// set the initial values again
			// change the direction by rotation

			// if AIBall has been added into rootNode, ignore it
			if (addAI == 0) {
				rootNode.attachChild(AIBall);
				addAI = 1;
			}
			AIAngle += 0.1;
			time = 0;
			AIScore = 0;
			AISpeed = 3000;
			AIDirection = new Vector3f(FastMath.cos(AIAngle), 0,
					FastMath.sin(-AIAngle)).normalize();
			AIBall.setLocalTranslation(redBall.getLocalTranslation());
			redSpeed = 12 * 26;
			redDirection = AIDirection;
		} else {
			// check the collision with other balls or sides
			// without a loss energy, decreasing speed and playing sound,
			// because it is a cheating with simulating

			if (AIBall.hasCollision(side1, false)) {
				AIDirection.z = -FastMath.abs(AIDirection.z);
			}

			if (AIBall.hasCollision(side2, false)) {
				AIDirection.z = FastMath.abs(AIDirection.z);
			}

			if (AIBall.hasCollision(side3, false)) {
				AIDirection.x = -FastMath.abs(AIDirection.x);
			}

			if (AIBall.hasCollision(side4, false)) {
				AIDirection.x = FastMath.abs(AIDirection.x);
			}

			if (AIBall.hasCollision(blueBall, false)) {
				Vector3f n = blueBall.getLocalTranslation()
						.subtract(AIBall.getLocalTranslation()).normalize();
				float proj1V = AIDirection.mult(AISpeed).dot(n);
				float proj2V = blueDirection.mult(blueSpeed).dot(n);
				Vector3f tan1 = AIDirection.mult(AISpeed).subtract(
						n.mult(proj1V));
				if (proj1V - proj2V > 0) {
					Vector3f velocity1 = tan1.add(n.mult(proj2V));
					AIDirection = velocity1.normalize();
					AISpeed = velocity1.length();
				}
				if (AIScore == 0) {
					AIScore = 2;
				}
			}

			if (AIBall.hasCollision(whiteBall, false)) {
				if (AIScore == 2) {
					AIScore++;
				}
			}

			AIBall.setLocalTranslation(AIBall.getLocalTranslation().add(
					AIDirection.mult(AISpeed * tpf)));
		}
	}

	/**
	 * Test for only player to player.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BilliardsGame app = new BilliardsGame();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

}
