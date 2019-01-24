package gameplay.entities.hostiles;

import org.lwjgl.util.vector.Vector3f;

import box.Main;
import box.TM;
import gameplay.entities.players.Player;
import gameplay.entities.projectiles.Torpedo;
import objStuff.OBJParser;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.models.RawModel;
import renderEngine.models.TexturedModel;
import renderEngine.textures.ModelTexture;
import utils.SFMath;

public class RogueVessel extends Enemy {
	
	private float HEALTH = 2500;
	private float movetimer = 0, avoidtimer = 0, guntimer = 0, swingtimer = 0;
	private float neededswing = 0;
	private boolean allowInitSwing = true;
	
	private float currSpeed = 0;
	private float currentTurnSpeed = 0;
	
	private int ATTACK_STAGE = 0;
	private static final int NEUTRAL = 0;
	private static final int CHARGING = 1, SWINGING = 2;
	
	private boolean flagLeft = false, flagRight = false;
	private boolean flagUp = false, flagDown = false;
	private static final float UPWARDS_ROT_CAP = 50.0f;
	
	private static final float TURN_SPEED = 180.0f;
	
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
		float dist = SFMath.distance(super.getPosition(), player.getPosition());
		Vector3f rot = SFMath.rotateToFaceVector(super.getPosition(), player.getPosition());
		
		if (dist < 600) {
			ATTACK_STAGE = SWINGING;
		}
		else if (dist < 12000) {
			if (ATTACK_STAGE == NEUTRAL) {
				ATTACK_STAGE = CHARGING;
			}
		}
		else {
			ATTACK_STAGE = NEUTRAL;
			currSpeed = 0;
		}
		
		switch (ATTACK_STAGE) {
		
		case CHARGING:
			currSpeed = 1500;
			super.setRotX(-rot.x);
			super.setRotY(rot.y);
			
			guntimer += DisplayManager.getFrameTime();
			
			if (guntimer > 0.1f) {
				Vector3f torpmv = SFMath.moveToVector(player.getPlayerPos(), 
						super.getPosition(), 10000);
				Main.foeprojectiles.add(new Torpedo(privateTorpedoTexture, 
						new Vector3f(super.getPosition()),
						0, 0, 0, 3, 3, 6.5f, 250, 
						torpmv.x, torpmv.y, 
						torpmv.z, TM.smlexplosionParticleSystem));
				guntimer = 0;
			}
			
			break;
			
		case SWINGING:
			currSpeed = 1500;

			if (avoidtimer < 0.5f) {
				flagUp = true;
				avoidtimer += DisplayManager.getFrameTime();
			}
			
			if (dist > 4000) {
				
				if (allowInitSwing) {
					allowInitSwing = false;
					neededswing = TM.rng.nextFloat() + 0.5f;
					swingtimer = 0;
				}
				
				swingtimer += DisplayManager.getFrameTime();
				
				if (swingtimer < neededswing) {
					flagRight = true;
				}
				else {
					allowInitSwing = true;
					ATTACK_STAGE = CHARGING;
				}
				
				avoidtimer = 0;
			}
			
			break;
		
		}
		
	}
	
	private void calculateRotations() {
		
		if (flagLeft) { 
			flagLeft = false;
			this.currentTurnSpeed = TURN_SPEED;
			if (this.getRotZ() > -45)
				super.rotate(0, 0, -60 * DisplayManager.getFrameTime());
		}
		else if (flagRight) { 
			flagRight = false;
			this.currentTurnSpeed = -TURN_SPEED;
			if (this.getRotZ() < 45)
				super.rotate(0, 0, 60 * DisplayManager.getFrameTime());
		}
		else {											
			this.currentTurnSpeed = 0;
			
			if (this.getRotZ() < 0) {
				super.rotate(0, 0, 70 * DisplayManager.getFrameTime());
				
				if (this.getRotZ() > 0)
					super.setRotZ(0);
			}
			else if (this.getRotZ() > 0) {
				super.rotate(0, 0, -70 * DisplayManager.getFrameTime());
				
				if (this.getRotZ() < 0)
					super.setRotZ(0);
			}
					
		}
		
		if (flagUp) {
			
			flagUp = false;
			
			if (super.getRotX() > -UPWARDS_ROT_CAP)
				super.rotate(-20 * DisplayManager.getFrameTime(), 0, 0);
			
			if (super.getRotX() > 0) {
				super.rotate(-60 * DisplayManager.getFrameTime(), 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);
			}
			
		} 
		else if (flagDown) {
			
			flagDown = false;
			
			if (super.getRotX() < UPWARDS_ROT_CAP)
				super.rotate(20 * DisplayManager.getFrameTime(), 0, 0);
			
			if (super.getRotX() < 0) {
				super.rotate(60 * DisplayManager.getFrameTime(), 0, 0);
				
				if (super.getRotX() > 0)
					super.setRotX(0);
			}
		}	
		else {
			if (super.getRotX() < 0) {
				super.rotate(30 * DisplayManager.getFrameTime(), 0, 0);
				
				if (super.getRotX() > 0)
					super.setRotX(0);
			}
			else if (super.getRotX() > 0) {
				super.rotate(-30 * DisplayManager.getFrameTime(), 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);
			}
		}
		
	}
	
	private void move() {
		calculateRotations();
		super.rotate(0, currentTurnSpeed * DisplayManager.getFrameTime(), 0);
		
		if (currSpeed == 0) {
			return;
		}
		
		float distanceMoved = currSpeed * DisplayManager.getFrameTime();
		
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
