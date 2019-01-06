package gameplay.entities.hostiles;

import org.lwjgl.util.vector.Vector3f;

import box.TM;
import gameplay.entities.players.Player;
import objStuff.OBJParser;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.models.RawModel;
import renderEngine.models.TexturedModel;
import renderEngine.textures.ModelTexture;

public class RogueVessel extends Enemy {
	
	private float HEALTH = 2500;
	private float movetimer = 0;
	
	private Player player;
	
	private static RawModel pretorpedo = OBJParser.loadObjModel("photon");
	private static TexturedModel privateTorpedoTexture = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("allGlow")));

	public RogueVessel(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Player player) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.player = player;
	}

	@Override
	public void update() {
		move();
		movetimer += DisplayManager.getFrameTime();
		if (movetimer > 5) {
			int rand = TM.rng.nextInt(360);
			super.setRotY(rand);
			movetimer = 0;
		}
		
	}
	
	private void move() {
		
		float distanceMoved = 300 * DisplayManager.getFrameTime();
		
		float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		
		float l = (float) Math.cos(Math.toRadians(super.getRotX()));
		
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotY()))) * l;
		//float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(super.getRotY()))) * l;
		super.move(dx, -dy, dz);
	}
	
	@Override
	public void respondToCollisioni(float damage, Vector3f hit) {
		HEALTH -= damage;
		if (HEALTH < 0) {
			this.setDead();
		}
	}

	@Override
	public void respondToCollision() {
		
	}

}
