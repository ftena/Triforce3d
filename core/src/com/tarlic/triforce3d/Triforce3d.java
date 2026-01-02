package com.tarlic.triforce3d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

public class Triforce3d implements ApplicationListener {
	// Constant useful for logging
	public static final String LOG = Triforce3d.class.getSimpleName();
	
	// These attributes are used to manage the app state
	public final static int RUNNING = 0;
	public final static int PAUSED = 1;
	
	// a libgdx helper class that logs the current FPS each second
	private FPSLogger fpsLogger;

	public AssetManager assets;
	private PerspectiveCamera cam;
	private AnimationController controller;
	public Environment environment;
	public ModelInstance instance;
	public boolean loading;
	public Model model;
	public ModelBatch modelBatch;
	/*
	 * Control game state
	 */
	private int mState;	
	private String prefColor;
	private String prefRotation;
	private ShapeRenderer shapeRenderer;
	
	private Array<ModelInstance> instances = new Array<ModelInstance>();

	@Override
	public void create() {
		//Gdx.app.setLogLevel(Gdx.app.LOG_NONE);
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		
		Gdx.graphics.getWidth();
		Gdx.graphics.getHeight();

		Gdx.app.log(Triforce3d.LOG, "Creating game");
		fpsLogger = new FPSLogger();
		
		/* 
		 * Default values for color and rotation.
		 */
		prefColor = "11"; // WHITE
		prefRotation = "0"; // STOP rotation if screen is touched
		
		// Start the app as running
		mState = RUNNING;
		
		// Create ModelBatch that will render all models using a camera
		modelBatch = new ModelBatch();

		// Create a camera and point it to our model
		cam = new PerspectiveCamera(60, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 9f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();
		
		/* Set the input processor */
		TriforceInputProcessor triforceInputProcessor = new TriforceInputProcessor(this);
		Gdx.input.setInputProcessor(triforceInputProcessor);
		
		/* Create ShapeRenderer to draw a square */
		shapeRenderer = new ShapeRenderer();
		
		// Create an asset manager that lets us dispose all assets at once.
		assets = new AssetManager();
		assets.load("data/triforce.g3db", Model.class);
		assets.finishLoading();

		// Create an instance of our crate model and put it in an array
		Model model = assets.get("data/triforce.g3db", Model.class);
		ModelInstance inst = new ModelInstance(model);
		instances.add(inst);

		// Set up environment with simple lighting
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -0.8f,
				0.3f, -1f));
		
		// You use an AnimationController to um, control animations.  Each control is tied to the model instance
		controller = new AnimationController(inst);  
		// Pick the current animation by name
		controller.setAnimation(inst.animations.get(0).id, 1, new AnimationListener(){

            @Override
            public void onEnd(AnimationDesc animation) {
                // this will be called when the current animation is done. 
                // queue up another animation. 
                // Passing a negative to loop count loops forever.  1f for speed is normal speed.
                controller.queue(animation.animation.id,-1,1f,null,0f);
            }

            @Override
            public void onLoop(AnimationDesc animation) {
                // TODO Auto-generated method stub
            }
            
        });
	}

	@Override
	public void dispose() {
		Gdx.app.log(Triforce3d.LOG, "Disposing game");

		modelBatch.dispose();
		instances.clear();
		assets.dispose();
	}

	@Override
	public void render() {
		// output the current FPS
		// fpsLogger.log();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		
		// For some flavor, lets spin our camera around the Y axis by 1 degree each time render is called
        // cam.rotateAround(Vector3.Zero, new Vector3(0,1,0),1f);
        // When you change the camera details, you need to call update();
        // Also note, you need to call update() at least once.
        cam.update();
        
        if ( mState == RUNNING ) { 
	        /* You need to call update on the animation controller so it will
	         * advance the animation.
	         */
	        // Pass in frame delta.
	        controller.update(Gdx.graphics.getDeltaTime());
	    }
		
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		
		shapeRenderer.setColor(getColor(prefColor));
		
        shapeRenderer.rect(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
        		Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        shapeRenderer.end();		
        
		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	private Color getColor(String prefColor) {
		
		switch (Integer.parseInt(prefColor)) {
		case 0:
			return Color.BLACK;
		case 1:
			return Color.BLUE;
		case 2:
			return Color.CYAN;
		case 3:
			return Color.DARK_GRAY;
		case 4:
			return Color.GRAY;
		case 5:
			return Color.GREEN;
		case 6:
			return Color.LIGHT_GRAY;
		case 7:
			return Color.MAGENTA;
		case 8:
			return Color.ORANGE;
		case 9:
			return Color.PINK;
		case 10:
			return Color.RED;
		case 11:
			return Color.WHITE;
		case 12:
			return Color.YELLOW;
		default:
			return Color.BLACK;
		}
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(Triforce3d.LOG, "Resizing game to: " + width + " x "
				+ height);
		
		// Update viewport size and refresh camera matrices
	      cam.viewportWidth = Gdx.graphics.getWidth();
	      cam.viewportHeight = Gdx.graphics.getHeight();
	      cam.update(true);
	}

	@Override
	public void pause() {		
		Gdx.app.log(Triforce3d.LOG, "Pausing game");
		
		mState = PAUSED;
	}

	@Override
	public void resume() {
		Gdx.app.log(Triforce3d.LOG, "Resuming game");
		
		mState = RUNNING;
		
		Preferences prefs = Gdx.app.getPreferences("com.tarlic.triforce3d.prefs");
		
		// The color number 11 means white
		prefColor = prefs.getString("color", "11");
		
		/*
		 *  The option 0 means to stop rotating
		 *  when the user clicks on screen.  
		 */
		prefRotation = prefs.getString("rotation", "0");
	}
	
	public void gameState(int state)
	{
	    this.mState = state;
	}
	
	public int gameState()
	{
	    return this.mState;
	}
	
	public boolean rotation()
	{	
		/*
		 * 0 means to stop rotation when the user touches the screen.
		 * 1 means to continue with the rotation.
		 */
		switch (Integer.parseInt(prefRotation)) {
		case 0:
			return false;
		case 1:
		default:
			return true;
		}
	}
}