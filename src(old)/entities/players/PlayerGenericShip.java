package entities.players;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.projectiles.Bolt;
import entities.projectiles.Torpedo;
import models.TexturedModel;
import particles.ParticleSystem;
import renderEngine.DisplayManager;
import utils.RaysCast;

public class PlayerGenericShip extends Entity {
	
	private List<Torpedo> torpedoes = new ArrayList<Torpedo>();
	private List<Bolt> bolts = new ArrayList<Bolt>();
	public List<Entity> projectiles = new ArrayList<Entity>();
	
	private ParticleSystem particalSystem = new ParticleSystem(1000, 1900, 0, 4);
	
	private static final float MOVE_SPEED = 400;
	private static final float TURN_SPEED = 160;
	private static final float VERT_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean hover = false;
	private boolean toggle = true;
	
	public PlayerGenericShip(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void update(RaysCast caster, TexturedModel torpedoModel, TexturedModel boltModel) {
		
		projectiles.clear();
		projectiles.addAll(torpedoes);
		projectiles.addAll(bolts);
		move();
		updateTorpedoes(caster, torpedoModel);
		updateBolts(boltModel);
	}
	
	private void move() {
		
		checkInputs();
		super.rotate(0, currentTurnSpeed * DisplayManager.getFrameTime(), 0);	
		float distanceMoved = currentSpeed * DisplayManager.getFrameTime();
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(super.getRotY())));
		super.move(dx, 0, dz);
		if (hover)
			upwardsSpeed = 0;
		super.move(0, upwardsSpeed * DisplayManager.getFrameTime(), 0);
	}
	
	private void checkInputs() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) //move forwards
			this.currentSpeed = MOVE_SPEED;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_S)) //move backwards
			this.currentSpeed = -MOVE_SPEED;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = MOVE_SPEED * 7;
			particalSystem.generateParticles(new Vector3f(super.getPosition()));
		}
		else 
			this.currentSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) { //yaw left and rotate left
			this.currentTurnSpeed = TURN_SPEED;
			if (this.getRotZ() > -45)
				super.rotate(0, 0, -0.6f);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D)) { //yaw right and rotate right
			this.currentTurnSpeed = -TURN_SPEED;
			if (this.getRotZ() < 45)
				super.rotate(0, 0, 0.6f);
		}
		else {											
			this.currentTurnSpeed = 0; 				//return to rotZ = 0
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			this.upwardsSpeed = VERT_POWER;
			//isMidAir = true;
			if (super.getRotX() > -15)
				super.rotate(-0.2f, 0, 0);
			
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.upwardsSpeed = -VERT_POWER;
			if (super.getRotX() < 15)
				super.rotate(0.2f, 0, 0);
		}
		else {
			if (super.getRotX() < 0) {
				super.rotate(0.3f, 0, 0);
				
				if (super.getRotX() > 0)
					super.setRotX(0);
				
				if (this.upwardsSpeed > 0)
					upwardsSpeed--;
			}
			else if (super.getRotX() > 0) {
				super.rotate(-0.3f, 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);

				if (this.upwardsSpeed < 0)
					upwardsSpeed++;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SEMICOLON)) 
			System.out.println(this.getRotZ());	
		
		if (Keyboard.isKeyDown(Keyboard.KEY_H) && toggle) {
			hover = !hover;
			toggle = false;
		}
		
		if (!(Keyboard.isKeyDown(Keyboard.KEY_H)) && !toggle)
			toggle = true;
	}
	
	private void updateTorpedoes(RaysCast caster, TexturedModel torpedo) {
		
		float ex = caster.getCurrentRay().getX();
		float why = caster.getCurrentRay().getY() + 0.035f;
		float zed = caster.getCurrentRay().getZ();
		
		float dx;
		float dy;
		float dz;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			Vector3f playerPos = new Vector3f(super.getPosition());
			torpedoes.add(new Torpedo(torpedo, 0, playerPos, 0, 0, 0, 1.5f, 1.5f, ex * 10f, why * 10f, zed * 10f));
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			Vector3f planet = new Vector3f(100, 25, -100);
			Vector3f start = new Vector3f(super.getPosition());
			
			dx = planet.x - start.x;
			dy = planet.y - start.y;
			dz = planet.z - start.z;
			
			float distance = (float) Math.sqrt((dx * dx + (dy * dy) + (dz * dz)));
			if (distance <= 3000)
				torpedoes.add(new Torpedo(torpedo, 0, new Vector3f(super.getPosition()), 0, 0, 0, 1.5f, 1, dx * DisplayManager.getFrameTime(),
						(dy + 20) * DisplayManager.getFrameTime(), dz * DisplayManager.getFrameTime()));
		}
		
		Iterator<Torpedo> iter = torpedoes.iterator();
		
		for (Entity torp : torpedoes) {
			if (!( (Torpedo) torp).isDead()) 
				((Torpedo) torp).update();
		}
		
		while (iter.hasNext()) {
			Entity next = iter.next();
			if (((Torpedo) next).isDead()) 
				iter.remove();	
		}
		
	}
	
	float cooldown = 0;
	
	private void updateBolts(TexturedModel boltModel) {
		
		cooldown += DisplayManager.getFrameTime();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && cooldown > 0.5) {
			Vector3f playerPos = new Vector3f(super.getPosition());
			bolts.add(new Bolt(boltModel, new Vector3f((float) (playerPos.x + Math.sin(Math.toRadians(super.getRotY() + 90)) * 7), playerPos.y + 5, (float) (playerPos.z + Math.cos(Math.toRadians(super.getRotY() + 90)) * 7)), 0, super.getRotY(), 0, 0.5f, 5, this.currentSpeed));
			bolts.add(new Bolt(boltModel, new Vector3f((float) (playerPos.x - Math.sin(Math.toRadians(super.getRotY() + 90)) * 7), playerPos.y + 5, (float) (playerPos.z - Math.cos(Math.toRadians(super.getRotY() + 90)) * 7)), 0, super.getRotY(), 0, 0.5f, 5, this.currentSpeed));
			cooldown = 0;
	}
		
		Iterator<Bolt> iter = bolts.iterator();
		
		for (Bolt bolt : bolts) {
			if (!bolt.isDead()) 
				bolt.update();
		}
		
		while (iter.hasNext()) {
			Bolt next = iter.next();
			if (next.isDead())
				iter.remove();
		}
		
	}

	@Override
	public void respondToCollision() {
		System.exit(0);
	}
	
}