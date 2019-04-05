package jtrek.gameplay.entities.hostiles;

import org.lwjgl.util.vector.Vector3f;

import engine.audio.AudioEngine;
import engine.objStuff.OBJParser;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.utils.SFMath;
import jtrek.box.Main;
import jtrek.box.TM;
import jtrek.gameplay.entities.players.Player;
import jtrek.gameplay.entities.projectiles.Bolt;

public class RogueVessel extends Enemy {
	
	private float HEALTH = 5000;
	private float movetimer = 0, avoidtimer = 0, guntimer = 0, stagetimer = 0;
	private float neededswing = 0, swingspeed = 0, hasswung = 0;
	private boolean allowInitSwing = true;
	private boolean odd = true;
	
	private float currSpeed = 0;
	private float currentTurnSpeed = 0;
	
	private int ATTACK_STAGE = 0;
	private static final int NEUTRAL = 0;
	private static final int CHARGING = 1, SWINGING = 2;
	
	private boolean flagLeft = false, flagRight = false;
	private boolean flagUp = false, flagDown = false;
	private static final float UPWARDS_ROT_CAP = 50.0f;
	private static final float UP_ROT_SPEED = 35;
	
	private static final float TURN_SPEED = 90.0f;
	
	private Player player;
	
	private static RawModel pretorpedo = OBJParser.loadObjModel("photon");
	private static TexturedModel privateTorpedoTexture = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("allGlow")));

	public RogueVessel(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Player player) {
		super(model, position, rotX, rotY, rotZ, scale);
		this.player = player;
	}
	
	private void fireGuns() {
		
		float f = 28.125f;
		
		float ex = SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), f);
		float zed = SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), f);
		
		// shouldn't these be reversed? like the negatives...
		Vector3f rots = SFMath.rotateToFaceVector(super.getPosition(), SFMath.vecoffset(player.getPlayerPos(), -ex, 0, -zed));
		float rot2 = SFMath.Y_rotateToFaceVector(super.getPosition(), SFMath.vecoffset(player.getPlayerPos(), ex, 0, zed));
		
		Main.foeprojectiles.add(new Bolt(TM.disruptorBolt, new Vector3f(
				
				super.getPosition().x + ex,
				super.getPosition().y + 3.75f,
				super.getPosition().z + zed
				
				), 
				-rots.x, rots.y, 0, 1.5f, 1.5f, 25, 500, this.currSpeed, 10000));
		
		Main.foeprojectiles.add(new Bolt(TM.disruptorBolt, new Vector3f(
				
				super.getPosition().x - ex,
				super.getPosition().y + 3.75f,
				super.getPosition().z - zed
				
				), 
				-rots.x, rot2, 0, 1.5f, 1.5f, 25, 500, this.currSpeed, 10000));
		
		//AudioEngine.playTempSrc(TM.disruptorsnd, 300, super.getPosition().x, super.getPosition().y, super.getPosition().z);
		AudioEngine.playTempSrc(TM.disruptorsnd, 300, super.getPosition().x, super.getPosition().y, super.getPosition().z);
	}

	@Override
	public void update() {
		move();
		float dist = SFMath.distance(super.getPosition(), player.getPosition());
		Vector3f rot = SFMath.rotateToFaceVector(super.getPosition(), player.getPosition());
			
		if (dist < 15000) {
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
			
			if (dist < 1000 || stagetimer > 5) {
				stagetimer = 0;
				ATTACK_STAGE = SWINGING;
			}
			
			currSpeed = 1500;
			
			super.setRotX(-rot.x);
			super.setRotY(rot.y);
			
			if (dist < 3750) {
				stagetimer += TRDisplayManager.getFrameDeltaTime();
				guntimer += TRDisplayManager.getFrameDeltaTime();
				
				if (guntimer > 0.1f) {
					fireGuns();
					guntimer = 0;
				}	
			}
			
			break;
			
		case SWINGING:
			currSpeed = 2500;
			stagetimer += TRDisplayManager.getFrameDeltaTime();

			if (avoidtimer < 0.5f) {
				flagUp = true;
				avoidtimer += TRDisplayManager.getFrameDeltaTime();
			}
			else if (avoidtimer < 0.75f) {
				flagDown = true;
				avoidtimer += TRDisplayManager.getFrameDeltaTime();
			}
			
			if (dist > 5000 || stagetimer > 10) {
				
				if (allowInitSwing) {
					allowInitSwing = false;
					neededswing = super.getRotY() - rot.y;
					if (Math.abs(neededswing) > 180) {
						neededswing = Math.abs(neededswing + (neededswing > 0 ? -360 : 360));
					}
					hasswung = 0;
					swingspeed = 2600 + TM.rng.nextFloat() * 1000;
					if (dist < 2000) {
						swingspeed += player.requestCurrentSpeed() + 1500;
					}
				}
				
				currSpeed = swingspeed;
				
				neededswing = Math.abs(neededswing);
				
				// INSTA TURN BUG, neededswing is negative for some reason
				if (hasswung < neededswing) {
					flagRight = odd;
					flagLeft = !odd;
					hasswung += TURN_SPEED * TRDisplayManager.getFrameDeltaTime();
				}
				else {
					allowInitSwing = true;
					ATTACK_STAGE = CHARGING;
					stagetimer = 0;
					avoidtimer = 0;
					odd = !odd;
				}
				
			}
			
			break;
		
		}
		
	}
	
	private void calculateRotations() {
		
		if (flagLeft) { 
			flagLeft = false;
			this.currentTurnSpeed = TURN_SPEED;
			if (this.getRotZ() > -45)
				super.rotate(0, 0, -60 * TRDisplayManager.getFrameDeltaTime());
		}
		else if (flagRight) { 
			flagRight = false;
			this.currentTurnSpeed = -TURN_SPEED;
			if (this.getRotZ() < 45)
				super.rotate(0, 0, 60 * TRDisplayManager.getFrameDeltaTime());
		}
		else {											
			this.currentTurnSpeed = 0;
			
			if (this.getRotZ() < 0) {
				super.rotate(0, 0, 70 * TRDisplayManager.getFrameDeltaTime());
				
				if (this.getRotZ() > 0)
					super.setRotZ(0);
			}
			else if (this.getRotZ() > 0) {
				super.rotate(0, 0, -70 * TRDisplayManager.getFrameDeltaTime());
				
				if (this.getRotZ() < 0)
					super.setRotZ(0);
			}
					
		}
		
		if (flagUp) {
			
			flagUp = false;
			
			if (super.getRotX() > -UPWARDS_ROT_CAP)
				super.rotate(-UP_ROT_SPEED * TRDisplayManager.getFrameDeltaTime(), 0, 0);
			
			if (super.getRotX() > 0) {
				super.rotate(-60 * TRDisplayManager.getFrameDeltaTime(), 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);
			}
			
		} 
		else if (flagDown) {
			
			flagDown = false;
			
			if (super.getRotX() < UPWARDS_ROT_CAP)
				super.rotate(UP_ROT_SPEED * TRDisplayManager.getFrameDeltaTime(), 0, 0);
			
			if (super.getRotX() < 0) {
				super.rotate(60 * TRDisplayManager.getFrameDeltaTime(), 0, 0);
				
				if (super.getRotX() > 0)
					super.setRotX(0);
			}
		}	
		else {
			if (super.getRotX() < 0) {
				super.rotate(30 * TRDisplayManager.getFrameDeltaTime(), 0, 0);
				
				if (super.getRotX() > 0)
					super.setRotX(0);
			}
			else if (super.getRotX() > 0) {
				super.rotate(-30 * TRDisplayManager.getFrameDeltaTime(), 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);
			}
		}
		
	}
	
	private void move() {
		calculateRotations();
		super.rotate(0, currentTurnSpeed * TRDisplayManager.getFrameDeltaTime(), 0);
		
		if (currSpeed == 0) {
			return;
		}
		
		float distanceMoved = currSpeed * TRDisplayManager.getFrameDeltaTime();
		
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
