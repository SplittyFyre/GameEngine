package scene.entities.players;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import audio.AudioEngine;
import audio.AudioSrc;
import box.TM;
import collision.BoundingBox;
import fontMeshCreator.GUIText;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.guis.IButton;
import renderEngine.guis.IGUI;
import renderEngine.guis.SFAbstractButton;
import renderEngine.models.TexturedModel;
import renderEngine.textures.GUITexture;
import scene.entities.Entity;
import scene.entities.hostiles.BorgVessel;
import scene.entities.hostiles.Enemy;
import scene.entities.projectiles.Bolt;
import scene.entities.projectiles.Projectile;
import scene.entities.projectiles.Torpedo;
import scene.particles.Particle;
import scene.particles.ParticleTexture;
import utils.RaysCast;
import utils.SFMath;

public class PlayerBirdOfPrey extends Player {
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private float mainGunTimer = 0;
	
	private AudioSrc lclsrc = new AudioSrc();
	
	private List<IGUI> elements = new ArrayList<IGUI>();
	
	private SFAbstractButton grabmouse = new SFAbstractButton(elements, "image", new Vector2f(0, 0.5f), TM.sqr4) {
		
		@Override
		public void whileHovering(IButton button) {
			
		}
		
		@Override
		public void whileHolding(IButton button) {
			
		}
		
		@Override
		public void onStopHover(IButton button) {
			
		}
		
		@Override
		public void onStartHover(IButton button) {
			
		}
		
		@Override
		public void onClick(IButton button) {
			Mouse.setGrabbed(true);
			for (IGUI el : elements) {
				el.hide(guis);
			}
		}
	};
	
	@Override
	public Enemy getTarget() {
		return target;
	}

	public void setTarget(Enemy target) {
		this.target = target;
	}
	
	private Particle retical;
	
	public void setRetical() {
		this.retical = new Particle(new ParticleTexture(Loader.loadTexture("retical2"), 1), target.getPosition(),
				new Vector3f(0, 0, 0), 0, Float.POSITIVE_INFINITY, 0, 500, true);
	}
	
	public void dropRetical() {
		if (this.retical != null)
			this.retical.setLife(0);
	}
	
	private float HEALTH = 15000;
	private float SHIELD = 37500;
	
	private boolean shieldsOn = true;
	
	private static final float FULL_SHIELDS = 37500;
	
	private static final float FULL_IMPULSE_SPEED = 3000;
	private static final float BOOST_SPEED = 10000;
	
	private float IMPULSE_MOVE_SPEED_VAR = 5000;
	private static final float TURN_SPEED = 200;
	private static final float VERT_POWER = 60;
	
	private static final float SIDE_ROT_CAP = 90;
	private static final float UP_ROT_CAP = 30;
	
	private static final int MAX_ENERGY = 1000000;
	private int energy = MAX_ENERGY;
	
	private float energyCounter = 0;
	
	private GUIText healthText;
	private GUIText shieldsText;
	private GUIText energyText;
	
	private GUIText coordsX = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0), 0.5f, false);
	private GUIText coordsY = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.05f), 0.5f, false);
	private GUIText coordsZ = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.1f), 0.5f, false);

	public PlayerBirdOfPrey(Vector3f position, float rotX, float rotY, float rotZ, float scale,
			List<GUITexture> guis) {
		
		super(new TexturedModel(TM.BOPModel), position, rotX, rotY, rotZ, scale, guis);
		initGUIS();
	}
	
	private void initGUIS() {
		
		healthText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.20f), 1, false);
		healthText.setColour(1, 0, 0);
		
		shieldsText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.25f), 1, false);
		shieldsText.setColour(0, 1, 0.75f);
		
		energyText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.15f), 1, false);
		energyText.setColour(1, 0.75f, 0);
		
		GUITexture gui_panel = new GUITexture(Loader.loadTexture("klingonpanel"), new Vector2f(0.725f, -0.3f), new Vector2f(0.275f, 0.7f));
		guis.add(gui_panel);
		
		coordsX.setColour(0, 1, 0);
		coordsY.setColour(0, 1, 0);
		coordsZ.setColour(0, 1, 0);
		
		for (IGUI el : elements) {
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
		
		for (IGUI el : elements) {
			el.update();
		}
		
	}

	@Override
	public void respondToCollisioni(float damage) {
		if (shieldsOn && this.SHIELD > 0) {
			if (this.SHIELD - damage > 0) {
				this.SHIELD -= damage;
				TM.redShieldSystem.generateParticles(super.getPosition(), super.getPosition());
			}
			else {
				this.SHIELD = 0;
				TM.redShieldSystem.generateParticles(super.getPosition(), super.getPosition());
				//trmText.setText("Warning: Shields Down");
				///trmText.setColour(1, 0, 0);
				//shieldWarning = true;
				AudioEngine.playTempSrc(AudioEngine.loadSound("res/critical_stereo.wav"), 200);
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
	public void update(RaysCast caster) {
		
		lclsrc.setPosition(super.getPosition().x, 
							super.getPosition().y, 
							super.getPosition().z);
		
		if (this.target != null && this.target.isDead()) {
			this.setTarget(null);
			this.dropRetical();
		}
		
		move();
		fireAllWeapons();
		updateGUIS();
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
		//super.move(0, upwardsSpeed * DisplayManager.getFrameTime(), 0);
		this.setRotY(this.getRotY() % 360);
		if (this.getRotY() < 0) {
			this.setRotY(Math.abs(360 - this.getRotY()));
		}
		if (Mouse.isGrabbed()) {
			float mx = Mouse.getDX();
			this.rotate(-Mouse.getDY() * 0.025f, -mx * 0.1f, Math.signum(mx) * 0.9f);
		}
		
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
			if (this.getRotZ() > -SIDE_ROT_CAP)
				super.rotate(0, 0, -0.6f);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D)) { 
			this.currentTurnSpeed = -TURN_SPEED;
			if (this.getRotZ() < SIDE_ROT_CAP)
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
		
		if (!Mouse.isGrabbed()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				if (super.getRotX() > -UP_ROT_CAP)
					super.rotate(-0.2f, 0, 0);
				
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if (super.getRotX() < UP_ROT_CAP)
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
		}
		
		
		if (Mouse.isGrabbed() && Keyboard.isKeyDown(Keyboard.KEY_GRAVE)) {
			Mouse.setGrabbed(false);
			
			for (IGUI el : elements) {
				el.show(guis);
			}
			
			/*gui_panel.setTexture(Loader.loadTexture("LCARSpanel"));
			trmText.show();
			
			for (IGUI el : miscElements) {
				el.show(guis);
			}*/
			
		}
		
	}
	
	private void fireAllWeapons() {
		
		mainGunTimer += DisplayManager.getFrameTime();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P) || (Mouse.isGrabbed() && Mouse.isButtonDown(0))) {
			
			if (mainGunTimer > 0.1f) {
				
				Vector3f rots;
				//System.out.println(Math.abs(this.getRotY() - SFMath.rotateToFaceVector(getPosition(), target.getPosition()).y));
				if (this.target != null) {
					rots = SFMath.rotateToFaceVector(super.getPosition(), target.getPosition());
				}
				else {
					rots = new Vector3f(-super.getRotX(), super.getRotY(), super.getRotZ());
				}
				
				projectiles.add(new Bolt(TM.disruptorBolt, new Vector3f(
						
						super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 28.125f),
						super.getPosition().y + 3.75f,
						super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_LEFT, super.getRotY(), 28.125f)
						
						), 
						-rots.x, rots.y, rots.z, 1.5f, 1.5f, 25, 500, this.currentSpeed, 10000,
						TM.bigexplosionParticleSystem));
				
				projectiles.add(new Bolt(TM.disruptorBolt, new Vector3f(
						
						super.getPosition().x + SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, super.getRotY(), 28.125f),
						super.getPosition().y + 3.75f,
						super.getPosition().z + SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, super.getRotY(), 28.125f)
						
						), 
						-rots.x, rots.y, rots.z, 1.5f, 1.5f, 25, 500, this.currentSpeed, 10000,
						TM.bigexplosionParticleSystem));
				
				//AudioEngine.playTempSrc(TM.disruptorsnd, 300, super.getPosition().x, super.getPosition().y, super.getPosition().z);
				AudioEngine.playTempSrc(TM.disruptorsnd, 300, super.getPosition().x, super.getPosition().y, super.getPosition().z);
				
				mainGunTimer = 0;
			}
			
		}	
		else if (Keyboard.isKeyDown(Keyboard.KEY_MINUS) || (Mouse.isGrabbed() && Mouse.isButtonDown(1))) {
			projectiles.add(Bolt.greenphaser(getPosition(), 0, 3.75f, 26, 50, super.getRotX(), super.getRotY(), super.getRotZ(), this.currentSpeed));
		}
		
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
				Vector3f vec;
				
				if (this.target != null) {
					vec = SFMath.rotateToFaceVector(getPosition(), target.getPosition());
					vec.x = -vec.x;
				}
				else {
					vec = new Vector3f(super.getRotX(), super.getRotY(), 0);
				}
				projectiles.add(Torpedo.klingonTorpedo(super.getPosition(), 4500 + (this.currentSpeed < 0 ? 0 : this.currentSpeed),
						0, 3.75f, 26f, vec.y, vec.x));
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
				projectiles.add(Torpedo.klingonTorpedo(super.getPosition(), 4500 - (this.currentSpeed > 0 ? 0 : this.currentSpeed),
						0, 5.625f, -11.25f, super.getRotY() + 180, -super.getRotX()));
			} 
			else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				
				if (cloaked) {
					cloaked = false;
					lclsrc.play(AudioEngine.loadSound("decloaking"), 150);
					//this.getModel().setTexture(TM.BOPModel.getTexture());
					
					translucent = false;
					
				}
				else {
					cloaked = true;
					lclsrc.play(AudioEngine.loadSound("cloaking"), 150);
					//this.getModel().setTexture(new ModelTexture(Loader.loadTexture("tryalpha")));
					translucent = true;
					
				}
				
			}
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
	
	@Deprecated
	@Override
	public void respondToCollision() {
		
	}
	
	@Override
	public Vector3f getPlayerPos() {

		return cloaked ? 
		new Vector3f(super.getPosition().x + (TM.rng.nextFloat() - 0.5f) * 100000,
				super.getPosition().y + (TM.rng.nextFloat() - 0.5f) * 1000,
				super.getPosition().z + (TM.rng.nextFloat() - 0.5f) * 100000)
		:
			super.getPosition();
		
	}

	@Override
	protected void initWeapons() {
		// FIXME Auto-generated method stub
		
	}

}
