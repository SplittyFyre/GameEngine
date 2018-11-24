package scene.entities.players.trubble;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioEngine;
import box.TM;
import collision.BoundingBox;
import fontMeshCreator.GUIText;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.guis.IButton;
import renderEngine.guis.IGUI;
import renderEngine.guis.SFAbstractButton;
import renderEngine.textures.GUITexture;
import scene.entities.Entity;
import scene.entities.hostiles.BorgVessel;
import scene.entities.hostiles.Enemy;
import scene.entities.players.Player;
import scene.entities.projectiles.Bolt;
import scene.entities.projectiles.Projectile;
import scene.entities.projectiles.Torpedo;
import scene.particles.Particle;
import scene.particles.ParticleTexture;
import utils.RaysCast;
import utils.SFMath;

public class PlayerTrubble extends Player {
	
	private List<IGUI> tacticalElements = new ArrayList<IGUI>();
	private List<IGUI> opsElements = new ArrayList<IGUI>();
	private List<IGUI> helmElements = new ArrayList<IGUI>();
	
	private List<IGUI> miscElements = new ArrayList<IGUI>();
	
	/*//BOOKMARK fire phaser tip
	private SFAbstractButton firetipphaser = new SFAbstractButton(tacticalElements, "image", new Vector2f(0.5f, -0.5f), TM.sqr4) {
		
		@Override
		public void whileHovering(IButton button) {
			
		}
		
		@Override
		public void whileHolding(IButton button) {
			fireTipPhaser();
		}
		
		@Override
		public void onStopHover(IButton button) {
			
		}
		
		@Override
		public void onStartHover(IButton button) {
			
		}
		
		@Override
		public void onClick(IButton button) {
			
		}
	};*/
	
	//BOOKMARK transfer button energy to shields
	private SFAbstractButton energyshields = new SFAbstractButton(miscElements, "clear", new Vector2f(0.35f, 0.45f), new Vector2f(0.08f, 0.04f)) {
		
		@Override
		public void whileHovering(IButton button) {
				
		}
			
		@Override
		public void whileHolding(IButton button) {
			if (shieldsOn && SHIELD + 100 <= FULL_SHIELDS) {
				energy -= 500;
				SHIELD += 100;
			}
		}
			
		@Override
		public void onStopHover(IButton button) {
				
		}
			
		@Override
		public void onStartHover(IButton button) {
			
		}
			
		@Override
		public void onClick(IButton button) {
			
		}
		
	};	
	
	//BOOKMARK GUI SETUP
	
	private int currentPanel = 1;
	private static final int TACTICAL_PANEL = 1;
	private static final int OPS_PANEL = 2;
	private static final int HELM_PANEL = 3;
	
	private Vector2f panelpos = new Vector2f(0.65f, -0.3f);
	private GUITexture gui_panel;
	
	private GUIText healthText;
	private GUIText shieldsText;
	private GUIText energyText;
	
	private GUIText coordsX = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0), 0.5f, false);
	private GUIText coordsY = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.05f), 0.5f, false);
	private GUIText coordsZ = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.1f), 0.5f, false);
	
	private Particle retical;
	
	@Override
	public Enemy getTarget() {
		return target;
	}

	public void setTarget(Enemy target) {
		this.target = target;
	}
	
	public void setRetical() {
		this.retical = new Particle(new ParticleTexture(Loader.loadTexture("retical2"), 1), target.getPosition(),
				new Vector3f(0, 0, 0), 0, Float.POSITIVE_INFINITY, 0, 500, true);
	}
	
	public void dropRetical() {
		if (this.retical != null)
			this.retical.setLife(0);
	}
	
	//BOOKMARK TACTICAL VARS
	
	private PlayerTrubbleDeck deck = new PlayerTrubbleDeck(SFMath.vecadd(getPosition(), 50, 0, 0), this);
	private PlayerTrubbleStern stern = new PlayerTrubbleStern(SFMath.vecadd(getPosition(), -50, 0, 0), this);
	
	public boolean isSeperated = false;
	
	private int sepMode = SIDE_BY_SIDE;

	private static final int SIDE_BY_SIDE = 0;
	private static final int ATTACK_WING = 1;
	
	private int sepStage = 0;
	
	private static final float SEP_TIME = 1f;
	
	Vector3f dir = new Vector3f(0, 0, 0);
	Vector3f var1 = new Vector3f(0, 0, 0); 
	Vector3f var2 = new Vector3f(0, 0, 0);
	Vector3f var3 = new Vector3f(0, 0, 0);
	
	private float timer = 0;
	private boolean hold = false;
	private boolean sepseq_flag = false;
	private boolean re_sepseq_flag = false;
	
	private boolean halfway = false;
	
	private float HEALTH = 25000;
	private float SHIELD = 500000;
	
	private boolean shieldsOn = true;
	
	private static final float FULL_SHIELDS = 500000;
	
	private float phaserCannonTimer = 0;
	private float gatlingPCannonTimer = 0;
	private int cannonShots = 0;
	private int gatlingShots = 0;
	
	private float t1 = 2;
	
	private boolean flag1 = false;
	private boolean flag2 = false;
	
	//BOOKMARK NAVIGATION VARS
	
	private boolean flagr = false;
	private Vector3f vec = new Vector3f(0, 0, 0);
	private float alpha = 0, register = 0;
	
	float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private static final float FULL_IMPULSE_SPEED = 3000;
	private static final float BOOST_SPEED = 10000;
	
	private float IMPULSE_MOVE_SPEED_VAR = 2000;
	private static final float TURN_SPEED = 180;
	private static final float VERT_POWER = 60;
	
	public float X_ROT_CAP = 90;
	
	//BOOKMARK OPS VARS
	
	private static final int MAX_ENERGY = 7500000;
	int energy = MAX_ENERGY;
	
	private float energyCounter = 0;
	
	//BOOKMARK MISC VARS
	
	public PlayerTrubble(Vector3f position, float rotX, float rotY, float rotZ, float scale,
			List<GUITexture> guis) {
		super(TM.main_model, position, rotX, rotY, rotZ, scale, guis);
		initGUIS();
	}
	
	public void add(List<Entity> entities) {
		entities.add(deck);
		entities.add(this);
		entities.add(stern);
	}
	
	private void initGUIS() {
		
		gui_panel = new GUITexture(Loader.loadTexture("LCARSpanel"), panelpos, new Vector2f(0.35f, 0.7f));
		guis.add(gui_panel);
		
		healthText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.20f), 1, false);
		healthText.setColour(1, 0, 0);
		
		shieldsText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.25f), 1, false);
		shieldsText.setColour(0, 1, 0.75f);
		
		energyText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.15f), 1, false);
		energyText.setColour(1, 0.75f, 0);
		
		coordsX.setColour(0, 1, 0);
		coordsY.setColour(0, 1, 0);
		coordsZ.setColour(0, 1, 0);
		
		for (IGUI el : miscElements) {
			el.show(guis);
		}
		
		for (IGUI el : tacticalElements) {
			el.show(guis);
		}
		
	}
	
	private void updateGUIS() {
		
		healthText.setText(Float.toString(this.HEALTH));
		
		shieldsText.setText(Integer.toString((int) (this.SHIELD / FULL_SHIELDS * 100)) + '%');
		
		energyText.setText(Integer.toString(this.energy));
		
		coordsX.setText(Float.toString(super.getPosition().x));
		coordsY.setText(Float.toString(super.getPosition().y));
		coordsZ.setText(Float.toString(super.getPosition().z));
		
		for (IGUI el : miscElements) {
			el.update();
		}
		
		switch (currentPanel) {
		
			case TACTICAL_PANEL:
				for (IGUI el : tacticalElements) {
					el.update();
				}
				break;
				
			case HELM_PANEL:
				for (IGUI el : helmElements) {
					el.update();
				}
				break;
		}
	}

	@Override
	public void update(RaysCast caster) {
		
		if (this.target != null) {
			if (this.target.isDead()) {
				this.setTarget(null);
				this.dropRetical();
			}
		}
		
		//deck.customRotationAxis = !isSeperated;
		//stern.customRotationAxis = !isSeperated;
		move();
		updateGUIS();
		fireAllWeapons(caster);
	}
	
	private void move() {
		checkInputs();
		super.rotate(0, currentTurnSpeed * DisplayManager.getFrameTime(), 0);	
		float distanceMoved = currentSpeed * DisplayManager.getFrameTime();
		
		float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		
		if (distanceMoved < 0)
			distanceMoved += Math.abs(dy);
		else
			distanceMoved -= Math.abs(dy);
		
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotY())));
		//float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(super.getRotY())));
		super.move(dx, -dy, dz);
		
		float deckfront = 116;
		
		//deck.matpremul = this;
		
		if (!hold) {
			if (isSeperated) {
				
				switch (sepMode) {
				
				
				case SIDE_BY_SIDE:
					
					deck.customOrigin = null;
					stern.customOrigin = null;
					
					deck.setPosition(new Vector3f(
								
								super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250), 
								super.getPosition().y,
								super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250)
								
							));
					
					stern.setPosition(new Vector3f(
							
							super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, super.getRotY(), 250), 
							super.getPosition().y,
							super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, super.getRotY(), 250)
							
							));
					
					break; //SIDE_BY_SIDE
					
				case ATTACK_WING: 
					
					deck.customOrigin = new Vector3f(super.getPosition());
					stern.customOrigin = new Vector3f(super.getPosition());
					
					deck.setPosition(new Vector3f(
							
							super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, 90, 200), 
							super.getPosition().y,
							super.getPosition().z
							
						));
					
					stern.setPosition(new Vector3f(
							
							super.getPosition().x - SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, 90, 200), 
							super.getPosition().y,
							super.getPosition().z
							
							));
					
					break; //ATTACK_WING
					
					
				}
				
			}
			else {
				
				/*Vector3f volumeCenter = new Vector3f(
						
						super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 100),
						super.getPosition().y,
						super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 100)
						
						);
				
				deck.customRotationAxis = false;
				deck.customOrigin = new Vector3f(volumeCenter);
				super.customOrigin = new Vector3f(volumeCenter);
				super.customRotationAxis = true;
				stern.customOrigin = new Vector3f(volumeCenter);
				
				System.out.println(volumeCenter);*/
				
				deck.customOrigin = super.getPosition();
				stern.customOrigin = super.getPosition();
				
				
				deck.setPosition(new Vector3f(
							
							super.getPosition().x, 
							super.getPosition().y - 23.5f,
							super.getPosition().z + deckfront
							
							));
				
				stern.setPosition(new Vector3f(
							
							super.getPosition().x, 
							super.getPosition().y - 12.5f,
							super.getPosition().z
							
							));
				
			}
		}
		
		
		deck.setRotX(super.getRotX());
		stern.setRotX(super.getRotX());
		
		deck.setRotY(super.getRotY());
		stern.setRotY(super.getRotY());
		
		deck.setRotZ(super.getRotZ());
		stern.setRotZ(super.getRotZ());
		
		//this.mx += dx; this.mx /= 2; this.my += dy; this.my /= 2; this.mz += dz; this.mz /= 2;
		//super.move(0, upwardsSpeed * DisplayManager.getFrameTime(), 0);
		this.setRotY(this.getRotY() % 360);
		if (this.getRotY() < 0) {
			this.setRotY(Math.abs(360 - this.getRotY()));
		}
		
	}
	
	private void rotateAll(float x, float y, float z) {
		super.rotate(x, y, z);
		deck.rotate(x, y, z);
		stern.rotate(x, y, z);
	}
	
	private void setRotations(float x, float y, float z) {
		super.setRotX(x);
		deck.setRotX(x);
		stern.setRotX(x);
		
		super.setRotY(y);
		deck.setRotY(y);
		stern.setRotY(y);
		
		super.setRotZ(z);
		deck.setRotZ(z);
		stern.setRotZ(z);
		
	}
	
	private void checkInputs() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			this.currentSpeed = IMPULSE_MOVE_SPEED_VAR;
			// TaskManager.warpParticleSystem.generateParticles(new Vector3f(super.getPosition()));
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			this.currentSpeed = -IMPULSE_MOVE_SPEED_VAR;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			//this.currentSpeed = IMPULSE_MOVE_SPEED_VAR * 30 * 9.975f * 42;
			TM.warpParticleSystem.generateParticles(new Vector3f(super.getPosition()));
			this.currentSpeed = BOOST_SPEED;
		}
		else 
			this.currentSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) { 
			this.currentTurnSpeed = TURN_SPEED;
			if (this.getRotZ() > -45)
				super.rotate(0, 0, -0.6f);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D)) { 
			this.currentTurnSpeed = -TURN_SPEED;
			if (this.getRotZ() < 45)
				super.rotate(0, 0, 0.6f);
		}
		else {											
			this.currentTurnSpeed = 0; 		
			if (this.getRotZ() < 0) {
				super.rotate(0, 0, 0.7f);
				
				if (this.getRotZ() > 0)
					super.setRotZ(0);
			}
			else if (this.getRotZ() > 0) {
				super.rotate(0, 0, -0.7f);
				
				if (this.getRotZ() < 0)
					super.setRotZ(0);
			}
					
		}
		
		//TODO: Rotate according to vertical speed
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (super.getRotX() > -X_ROT_CAP)
				super.rotate(-0.2f, 0, 0);
			
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			if (super.getRotX() < X_ROT_CAP)
				super.rotate(0.2f, 0, 0);
		}
		else {
			if (super.getRotX() < 0) {
				super.rotate(0.3f, 0, 0);
				
				if (super.getRotX() > 0)
					super.setRotX(0);
			}
			else if (super.getRotX() > 0) {
				super.rotate(-0.3f, 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);
			}
		}  
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Z) && !flagr) {
			flagr = true;
			vec = SFMath.rotateToFaceVector(super.getPosition(), this.target != null ? this.target.getPosition() : new Vector3f(0, 0, 0));
			alpha = vec.y - super.getRotY();
			
			alpha %= 360;
			
		}
		
		if (flagr) {
			
			float toRot = TURN_SPEED * DisplayManager.getFrameTime();
			
			if (register < Math.abs(alpha)) {
				super.rotate(0, toRot * Math.signum(alpha), 0);
				register += toRot;
			}
			else {
				register = 0;
				flagr = false;
			}
			
		}
		
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_Q) &! (sepseq_flag || re_sepseq_flag)) {
				if (!isSeperated) {
					sepseq_flag = true;
					hold = true;
					var1 = new Vector3f(
							
							super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 116), 
							super.getPosition().y - 23.5f,
							super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 116)
							
							);
					
					var2 = new Vector3f(
							
							super.getPosition().x, 
							super.getPosition().y - 12.5f,
							super.getPosition().z
							
							);
					
					deck.setPosition(var1);
					stern.setPosition(var2);
				}
				else {
					re_sepseq_flag = true;
					hold = true;
					var1 = deck.getPosition();
					var2 = stern.getPosition();
				}
				
			}	
			
			if (Keyboard.isKeyDown(Keyboard.KEY_M) && isSeperated &! (sepseq_flag || re_sepseq_flag)) {
				
				sepMode++;
				
				if (sepMode < 0) {
					sepMode = 1;
				}
				else if (sepMode > 1) {
					sepMode = 0;
				}
				
			}

		}
		
		if (sepseq_flag) {
			
			float dists = SFMath.distance(new Vector3f(
					
					super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 116), 
					super.getPosition().y - 23.5f,
					super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 116)
					
				), new Vector3f(

						super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 400), 
						super.getPosition().y,
						super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 400)
							
					))
					
					+
					
					SFMath.distance(new Vector3f(

						super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 400), 
						super.getPosition().y,
						super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 400)
							
					), new Vector3f(
							
							super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250), 
							super.getPosition().y,
							super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250)
							
						))
					;
			
			Vector3f deck_target = !halfway ? 
					
					new Vector3f(

						super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 400), 
						super.getPosition().y,
						super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 400)
							
					)
					 
					:
					
					new Vector3f(
					
					super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250), 
					super.getPosition().y,
					super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250)
					
				);
			
			Vector3f stern_target = !halfway ? 
					
					new Vector3f(
							
						super.getPosition().x - SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 250), 
						super.getPosition().y,
						super.getPosition().z - SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 250)
							
					)
					
					:
					
					new Vector3f(
					
					super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, super.getRotY(), 250), 
					super.getPosition().y,
					super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, super.getRotY(), 250)
					
					);
			
			deck.customRotationAxis = false;
			stern.customRotationAxis = false;
			
			timer += DisplayManager.getFrameTime();
			
			Vector3f tracing = SFMath.rotateToFaceVector(var1, deck_target);
			
			//float dtmove = DisplayManager.getFrameTime() * (700 + this.currentSpeed);

			float dtmove = (1 / ((SEP_TIME) / DisplayManager.getFrameTime())) * dists + this.currentSpeed * DisplayManager.getFrameTime();
			
			float homingX = (float) (dtmove * Math.sin(Math.toRadians(tracing.y)));
			float homingY = (float) (dtmove * Math.sin(Math.toRadians(tracing.x)));
			float homingZ = (float) (dtmove * Math.cos(Math.toRadians(tracing.y)));
			
			deck.move(homingX, homingY, homingZ);
			
			tracing = SFMath.rotateToFaceVector(var2, stern_target);
		
			homingX = (float) (dtmove * Math.sin(Math.toRadians(tracing.y)));
			homingY = (float) (dtmove * Math.sin(Math.toRadians(tracing.x)));
			homingZ = (float) (dtmove * Math.cos(Math.toRadians(tracing.y)));	
			
			stern.move(homingX, homingY, homingZ); 
			
			if (timer > SEP_TIME * 1.25f) {
				hold = false;
				timer = 0;
				sepseq_flag = false;
				isSeperated = true;
				deck.customRotationAxis = true;
				stern.customRotationAxis = true;
				halfway = false;
			}
			else if (timer >= SEP_TIME / 2) {
				halfway = true;
			}
		
		}
		else if (re_sepseq_flag) {
			System.out.println("repseping");
			Vector3f deck_target = new Vector3f(
					
					super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 116), 
					super.getPosition().y - 23.5f,
					super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, super.getRotY(), 116)
					
					);
			
			Vector3f stern_target = new Vector3f(
					
					super.getPosition().x, 
					super.getPosition().y - 12.5f,
					super.getPosition().z
					
					);
			
			deck.customRotationAxis = false;
			stern.customRotationAxis = false;
			
			timer += DisplayManager.getFrameTime();
			
			Vector3f tracing = SFMath.rotateToFaceVector(var1, deck_target);
			
			float dtmove = DisplayManager.getFrameTime() * (275 + this.currentSpeed);
			
			float homingX = (float) (dtmove * Math.sin(Math.toRadians(tracing.y)));
			float homingY = (float) (dtmove * Math.sin(Math.toRadians(tracing.x)));
			float homingZ = (float) (dtmove * Math.cos(Math.toRadians(tracing.y)));
			
			deck.move(homingX, homingY, homingZ);
			
			tracing = SFMath.rotateToFaceVector(var2, stern_target);
			
			homingX = (float) (dtmove * Math.sin(Math.toRadians(tracing.y)));
			homingY = (float) (dtmove * Math.sin(Math.toRadians(tracing.x)));
			homingZ = (float) (dtmove * Math.cos(Math.toRadians(tracing.y)));	
			
			stern.move(homingX, homingY, homingZ); 
			
			if (timer > 1f) {
				hold = false;
				timer = 0;
				re_sepseq_flag = false;
				isSeperated = false;
				deck.customRotationAxis = true;
				stern.customRotationAxis = true;
			}
			
		}
		
	}
	
	public void fireAllWeapons(RaysCast caster) {
		
		if (phaserCannonTimer <= 0.1f) {
			phaserCannonTimer += DisplayManager.getFrameTime();	
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			fireTipPhaser();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			firePhaserCannons();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
			fireMassivePhaserGuns();
		}
	
		if (Keyboard.isKeyDown(Keyboard.KEY_U) && !flag1) {
			fireHighYieldTorpedo();
			flag1 = true;
		}
		else if (!Keyboard.isKeyDown(Keyboard.KEY_U)) {
			flag1 = false;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {

			/*t1 -= DisplayManager.getFrameTime();
			
			if (t1 > 1.75f) {
				fireStarbArrays(3);
				firePortArrays(1);
				
			}
			else if (t1 > 1.5f) {
				firePortArrays(1);
				firePortArrays(2);
			}
			else if (t1 > 1.25f) {
				firePortArrays(2);
				firePortArrays(3);
			}
			else if (t1 > 1) {
				firePortArrays(3);
				fireStarbArrays(1);
				
			}
			else if (t1 > 0.75f) {
				fireStarbArrays(1);
				fireStarbArrays(2);
			}
			else if (t1 > 0.5f) {
				fireStarbArrays(2);
				fireStarbArrays(3);
			}
			else {
				t1 = 2;
			}*/
			
			for (int i = 1; i <= 3; i++) {
				firePortArrays(i);
				fireStarbArrays(i);
			}
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
			fireGatlingPhaserCannons();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Y) && !flag2) {
			fireShotgunTorpedoes();
			flag2 = true;
		}
		else if (!Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			flag2 = false;
		}
		
		for (int i = 0; i < this.projectiles.size(); i++) {
			
			Projectile el = projectiles.get(i);
			
			if (el.isDead()) {
				projectiles.remove(i);
			}
			else {
				el.update();
			}
			
		}
		
	}
	
	private void fireTipPhaser() {
		
		projectiles.add(new Bolt(TM.phaserBolt, 
				SFMath.fullPos(getRotY(), getRotX(), rlDeckPos(), 0, 17.75f, 175, 175 + 116)
				, getRotX(), getRotY(), 0, 1.5f * 3, 1.5f * 3, 10, 125, this.currentSpeed));
	}
	
	// FIXME you can create a method just for firing the two shots
	
	private void firePhaserCannons() {
		if (phaserCannonTimer > 0.1f) {
			cannonShots++;
			Vector3f pos = rlDeckPos();
			projectiles.add(new Bolt(TM.phaserCannon, new Vector3f(
					
					pos.x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, deck.getRotY(), 40),
					pos.y + 45,
					pos.z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, deck.getRotY(), 40)
					
					), 
					deck.getRotX(), deck.getRotY(), 0, 3, 3, 50, 1000, this.currentSpeed, 10000,
					TM.bigexplosionParticleSystem));
			
			projectiles.add(new Bolt(TM.phaserCannon, new Vector3f(
					
					pos.x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, deck.getRotY(), 40),
					pos.y + 45,
					pos.z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, deck.getRotY(), 40)
					
					), 
					deck.getRotX(), deck.getRotY(), 0, 3, 3, 50, 1000, this.currentSpeed, 10000,
					TM.bigexplosionParticleSystem));
			
			phaserCannonTimer = 0;
			AudioEngine.playTempSrc(TM.disruptorsnd, 300, super.getPosition().x, super.getPosition().y, super.getPosition().z);
			
			if (cannonShots == 20) {
				phaserCannonTimer = -5;
				cannonShots = 0;
				energy -= 20000;
			}
			
		}
	}
	
	private void fireGatlingPhaserCannons() {
		
		gatlingPCannonTimer += DisplayManager.getFrameTime();
		
		if (gatlingPCannonTimer > 1) {
		
			gatlingShots++;
			
			//FIRE
			Vector3f pos = rlDeckPos();
			projectiles.add(new Bolt(TM.phaserCannon, new Vector3f(
					
					pos.x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, deck.getRotY(), 40),
					pos.y + 45,
					pos.z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, deck.getRotY(), 40)
					
					), 
					deck.getRotX(), deck.getRotY(), 0, 3, 3, 50, 1000, this.currentSpeed, 10000,
					TM.bigexplosionParticleSystem));
			
			projectiles.add(new Bolt(TM.phaserCannon, new Vector3f(
					
					pos.x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, deck.getRotY(), 40),
					pos.y + 45,
					pos.z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, deck.getRotY(), 40)
					
					), 
					deck.getRotX(), deck.getRotY(), 0, 3, 3, 50, 00, this.currentSpeed, 10000,
					TM.bigexplosionParticleSystem));
			
			AudioEngine.playTempSrc(TM.disruptorsnd, 300, super.getPosition().x, super.getPosition().y, super.getPosition().z);
			
			energy -= 500;
			
			//FIRE
			
			if (gatlingShots == 4) {
				gatlingPCannonTimer = 0;
				gatlingShots = 0;
			}
			else {
				gatlingPCannonTimer = 0.9f;
			}
			
		}
		
	}
	
	private void fireMassivePhaserGuns() {
		
		float dmg = 30;
		
		float rx = deck.getRotX();
		float ry = deck.getRotY();
		
		Vector3f pos = rlDeckPos();
		
		projectiles.add(Bolt.phaser(pos, 4.5f, 34, 120, dmg, rx, ry, 0, this.currentSpeed));
		projectiles.add(Bolt.phaser(pos, 0, 34, 120, dmg, rx, ry, 0, this.currentSpeed));
		projectiles.add(Bolt.phaser(pos, -4.5f, 34, 120, dmg, rx, ry, 0, this.currentSpeed));
		
		projectiles.add(Bolt.phaser(pos, 4.5f, 41, 90, dmg, rx, ry, 0, this.currentSpeed));
		projectiles.add(Bolt.phaser(pos, 0, 41, 90, dmg, rx, ry, 0, this.currentSpeed));
		projectiles.add(Bolt.phaser(pos, -4.5f, 41, 90, dmg, rx, ry, 0, this.currentSpeed));
		
	}
	
	private void fireHighYieldTorpedo() {
		projectiles.add(Torpedo.photonTorpedo(Torpedo.PT * 12, rlDeckPos(), this.currentSpeed + 5500,
				0, 45, -15, deck.getRotY(), deck.getRotX()));
	}
	
	private void fireShotgunTorpedoes() {
		for (int i = 0; i < 80; i++) {
			projectiles.add(Torpedo.photonTorpedo(rlDeckPos(), this.currentSpeed + 5500,
					0, 45, -15, (float) (deck.getRotY() + (Math.random() * 2 - 1) * 2), (float) (deck.getRotX() + (Math.random() * 2 - 1) * 2)));
		}
	}
	
	private void firePortArrays(int mode) {
		
		Vector3f rots;
		Vector3f firing;
		
		switch (mode) {
		
		case 1:
			firing = SFMath.vecShifts(deck.getRotY(), rlDeckPos(), -11, 30, 70);
			break;
		
		case 2:
			firing = SFMath.vecShifts(deck.getRotY(), rlDeckPos(), -22.5f, 40, 5);
			break;
			
		case 3:
			firing = SFMath.vecShifts(deck.getRotY(), rlDeckPos(), -30.5f, 50, -60);
			break;
			
		default:
			throw new IllegalArgumentException("firing phaser array with illegal mode");
		
		}
		
		if (this.target == null) {
			rots = new Vector3f(-deck.getRotX(), deck.getRotY(), 0);
		}
		else {
			rots = SFMath.rotateToFaceVector(firing, this.target.getPosition());
		}
		
		projectiles.add(new Bolt(TM.phaserBolt, firing,
				-rots.x, rots.y, 0,
				1.5f, 1.5f, 35, 15, this.currentSpeed + 10000));
		
	}
	
	private void fireStarbArrays(int mode) {
		
		Vector3f rots;
		Vector3f firing;
		
		switch (mode) {
		
		case 1:
			firing = SFMath.vecShifts(deck.getRotY(), rlDeckPos(), 11, 30, 70);
			break;
		
		case 2:
			firing = SFMath.vecShifts(deck.getRotY(), rlDeckPos(), 22.5f, 40, 5);
			break;
			
		case 3:
			firing = SFMath.vecShifts(deck.getRotY(), rlDeckPos(), 30.5f, 50, -60);
			break;
			
		default:
			throw new IllegalArgumentException("firing phaser array with illegal mode");
		
		}
		
		if (this.target == null) {
			rots = new Vector3f(-deck.getRotX(), deck.getRotY(), 0);
		}
		else {
			rots = SFMath.rotateToFaceVector(firing, this.target.getPosition());
		}
		
		projectiles.add(new Bolt(TM.phaserBolt, firing,
				-rots.x, rots.y, 0,
				1.5f, 1.5f, 35, 15, this.currentSpeed + 10000));
		
	}

	@Override
	public void choreCollisions(List<Enemy> enemies, RaysCast caster) {
		
		boolean virg = true;
		
		for (Enemy enemy : enemies) {
			
			BoundingBox bb1 = enemy.getBoundingBox(); 
			
			if (!Mouse.isGrabbed() && caster.penetrates(bb1)) {
				virg = false;
				try {
					Mouse.setNativeCursor(Loader.loadCursor("bullseye"));
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				
				if (Mouse.isButtonDown(0)) {
					while (Mouse.next()) {
						if (Mouse.getEventButtonState()) {
							if (Mouse.getEventButton() == 0) {
								
								if (enemy == this.getTarget()) {
									this.setTarget(null);
									this.dropRetical();
								}
								else {
									this.setTarget(enemy);
									this.dropRetical();
									this.setRetical();
								}
								
							}
						}
					
					}
					
				}
				
				/*if (Mouse.isButtonDown(1)) {
					fireTurret(enemy.getPosition());
				}
				else {
					turretTimer = 0;
					turretToggle = true;
				}*/
				
			}
			
			for (Entity projectile : this.getProjectiles()) {
				
				BoundingBox bb2 = projectile.getBoundingBox();
				
				if (bb1.intersects(bb2)) {
					projectile.respondToCollision();
					((BorgVessel) enemy).respondToCollisioni(((Projectile) projectile).getDamage());
				}
				
			}
			
		}
		
		if (virg) {
			
			if (DisplayManager.currentCursor != DisplayManager.NORMAL) {
				try {
					Mouse.setNativeCursor(DisplayManager.cursor);
					DisplayManager.currentCursor = DisplayManager.NORMAL;
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
			
			while (Mouse.next()) {
				if (Mouse.getEventButtonState()) {
					if (Mouse.getEventButton() == 1) {
						/*this.fireTurret(caster.getCurrentRay().x,
						caster.getCurrentRay().y, caster.getCurrentRay().z);*/
					}
				}
			}
			
		}
		else {
			if (DisplayManager.currentCursor != DisplayManager.TARGET) {System.out.println("Cursor taregt");
				try {
					Mouse.setNativeCursor(DisplayManager.target);
					DisplayManager.currentCursor = DisplayManager.TARGET;
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void respondToCollisioni(float damage) {
		if (shieldsOn && this.SHIELD > 0) {
			if (this.SHIELD - damage > 0) {
				this.SHIELD -= damage;
				TM.blueShieldSystemBig.generateParticles(super.getPosition(), super.getPosition());
				TM.blueShieldSystemBig.generateParticles(super.getPosition(), super.getPosition());
			}
			else {
				this.SHIELD = 0;
				TM.blueShieldSystemBig.generateParticles(super.getPosition(), super.getPosition());
				TM.blueShieldSystemBig.generateParticles(super.getPosition(), super.getPosition());
			}
		}
		else {
			this.HEALTH -= damage;
			if (this.HEALTH <= 0) {
				System.exit(0);
			}
		}
	}

	@Override
	@Deprecated
	public void respondToCollision() {
		System.err.println("Error: use respondToCollisioni() instead ");
		System.exit(1);
	}

	@Override
	public Vector3f getPlayerPos() {
		return super.getPosition();
	}
	
	private Vector3f rlDeckPos() {
		
		if (isSeperated) {
			return new Vector3f(deck.getPosition());
		}
		else {
			return new Vector3f(
					
					super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, deck.getRotY(), 116), 
					super.getPosition().y - 23.5f,
					super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, deck.getRotY(), 116)
					
					);	
		}
		
		/*return new Vector3f(
				
				super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250), 
				super.getPosition().y,
				super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 250)
				
			);*/
	}

	@Override
	protected void initWeapons() {
		// FIXME Auto-generated method stub
		
	}

}
