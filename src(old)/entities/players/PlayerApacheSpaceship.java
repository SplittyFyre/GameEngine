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

public class PlayerApacheSpaceship extends Entity {
	
	private List<Torpedo> torpedoes = new ArrayList<Torpedo>();
	private List<Bolt> bolts = new ArrayList<Bolt>();
	public List<Entity> projectiles = new ArrayList<Entity>();
	
	private ParticleSystem particalSystem = new ParticleSystem(1000, 1900, 0, 4);
	
	private static final float MOVE_SPEED = 469;
	private static final float TURN_SPEED = 160;
	private static final float VERT_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	public PlayerApacheSpaceship(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void update(TexturedModel bulletModel, TexturedModel boltModel) {
		
		projectiles.clear();
		projectiles.addAll(torpedoes);
		projectiles.addAll(bolts);
		move();
		fireMainGun(bulletModel);
		fireBolts(boltModel);
	}
	
	private void move() {
		
		checkInputs();
		super.rotate(0, currentTurnSpeed * DisplayManager.getFrameTime(), 0);	
		float distanceMoved = currentSpeed * DisplayManager.getFrameTime();
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(super.getRotY())));
		super.move(dx, 0, dz);
		super.move(0, upwardsSpeed * DisplayManager.getFrameTime(), 0);
	}
	
	private void checkInputs() {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			 this.currentSpeed = MOVE_SPEED;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
			this.currentSpeed = -MOVE_SPEED;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
			this.currentSpeed = MOVE_SPEED * 7;
			particalSystem.generateParticles(new Vector3f(super.getPosition()));
		}
		else
			this.currentSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			this.currentTurnSpeed = TURN_SPEED;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_D))
			this.currentTurnSpeed = -TURN_SPEED;
		
		else
			this.currentTurnSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			this.upwardsSpeed = VERT_POWER;
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			this.upwardsSpeed = -VERT_POWER;
		
		else 
			this.upwardsSpeed = 0;
		
	}
	
	private void fireMainGun(TexturedModel bulletModel) {
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			Vector3f playerPos = new Vector3f(super.getPosition());
			float distanceToMove = (this.currentSpeed + 569) * DisplayManager.getFrameTime();
			torpedoes.add(new Torpedo(bulletModel, 0, playerPos, 0, super.getRotY(), 0, 1.5f, 2, (float) (distanceToMove * 
					Math.sin(Math.toRadians(super.getRotY()))), 0, (float) (distanceToMove * Math.cos(Math.toRadians(super.getRotY())))));
		}
			/*Iterator<Torpedo> iter = torpedoes.iterator();
			
			for (Entity torp : torpedoes) {
				if (!( (Torpedo) torp).isDead()) 
					((Torpedo) torp).update();
			}
			
			while (iter.hasNext()) {
				Entity next = iter.next();
				if (((Torpedo) next).isDead()) 
					iter.remove();*/
		
		for (int i = 0; i < torpedoes.size(); i++) {
			
			Torpedo torpedo = torpedoes.get(i);
			if (torpedo.isDead())
				torpedoes.remove(i);
			else
				torpedo.update();
				
		}
		
	}
	
	float cooldown = 0;
	
	private void fireBolts(TexturedModel boltModel) {
		
		if (cooldown < 3)
			cooldown += DisplayManager.getFrameTime();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q) && cooldown > 0.5) {
			Vector3f playerPos = new Vector3f(super.getPosition());
			bolts.add(new Bolt(boltModel, new Vector3f((float) (playerPos.x + Math.sin(Math.toRadians(super.getRotY() + 90)) * 3), playerPos.y + 1, (float) (playerPos.z + Math.cos(Math.toRadians(super.getRotY() + 90)) * 3)), 0, super.getRotY(), 0, 0.5f, 5, this.currentSpeed));
			bolts.add(new Bolt(boltModel, new Vector3f((float) (playerPos.x - Math.sin(Math.toRadians(super.getRotY() + 90)) * 3), playerPos.y + 1, (float) (playerPos.z - Math.cos(Math.toRadians(super.getRotY() + 90)) * 3)), 0, super.getRotY(), 0, 0.5f, 5, this.currentSpeed));
			bolts.add(new Bolt(boltModel, new Vector3f((float) ((float) (playerPos.x + Math.sin(Math.toRadians(super.getRotY() + 90)) * 8) - 10 * Math.sin(Math.toRadians(super.getRotY()))), playerPos.y + 1, (float) ((float) (playerPos.z + Math.cos(Math.toRadians(super.getRotY() + 90)) * 8) - 10 * Math.cos(Math.toRadians(super.getRotY())))), 0, super.getRotY(), 0, 0.5f, 5, this.currentSpeed));
			bolts.add(new Bolt(boltModel, new Vector3f((float) ((float) (playerPos.x - Math.sin(Math.toRadians(super.getRotY() + 90)) * 8) - 10 * Math.sin(Math.toRadians(super.getRotY()))), playerPos.y + 1, (float) ((float) (playerPos.z - Math.cos(Math.toRadians(super.getRotY() + 90)) * 8) - 10 * Math.cos(Math.toRadians(super.getRotY())))), 0, super.getRotY(), 0, 0.5f, 5, this.currentSpeed));
			
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
