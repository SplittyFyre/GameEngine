package entities.players;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import box.TaskManager;
import entities.Entity;
import entities.projectiles.Bolt;
import entities.projectiles.Torpedo;
import models.TexturedModel;
import renderEngine.DisplayManager;
import utils.Maths;
import utils.RaysCast;

public class PlayerWarshipVoyager extends Entity {
	
	private List<Bolt> bolts = new ArrayList<Bolt>();
	public List<Entity> projectiles = new ArrayList<Entity>();
	private List<Torpedo> torpedoes = new ArrayList<Torpedo>();
	
	private static final float MOVE_SPEED = 100;
	private static final float TURN_SPEED = 180;
	private static final float VERT_POWER = 60;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;

	private boolean hover = false;
	private boolean toggle = true;
	private boolean toggle1 = false;
	
	public PlayerWarshipVoyager(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void update(TexturedModel phaserBeamModel, RaysCast caster) {
		move();
		fireFrontPhasers(phaserBeamModel, caster);
		projectiles.clear();
		projectiles.addAll(bolts);
		projectiles.addAll(torpedoes);
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
		
		else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = MOVE_SPEED * 7;
			TaskManager.warpParticleSystem.generateParticles(new Vector3f(super.getPosition()));
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
			this.upwardsSpeed = VERT_POWER;
			
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
				
				while (this.upwardsSpeed > 0)
					upwardsSpeed--;
			}
			else if (super.getRotX() > 0) {
				super.rotate(-0.3f, 0, 0);
				
				if (super.getRotX() < 0)
					super.setRotX(0);

				while (this.upwardsSpeed < 0)
					upwardsSpeed++;
			}
		}
	
	}
	
	private void fireFrontPhasers(TexturedModel phaserBeamModel, RaysCast caster) {
		
		Vector3f ray = new Vector3f(caster.getCurrentRay());
		Vector3f target = new Vector3f(caster.getPointOnRay(ray, 600));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			Vector3f playerPos = new Vector3f(super.getPosition());
			
			bolts.add(new Bolt(phaserBeamModel, new Vector3f((float) (playerPos.x + (Math.sin
					(Math.toRadians(super.getRotY() - 90)) / 1.35) + Math.sin
					(Math.toRadians(super.getRotY())) * 32), playerPos.y + 20.9f, (float) (playerPos.z + (Math.cos
							(Math.toRadians(super.getRotY() - 90)) / 1.35) + Math.cos
							(Math.toRadians(super.getRotY())) * 32)), super.getRotX(), super.getRotY(), 0, 1.5f, 5, this.currentSpeed));
			
			bolts.add(new Bolt(phaserBeamModel, new Vector3f((float) (playerPos.x + (Math.sin(Math.toRadians(super.getRotY() + 90)) * 4) + Math.sin
					(Math.toRadians(super.getRotY())) * 19), playerPos.y + 22.35f, (float) (playerPos.z + (Math.cos
							(Math.toRadians(super.getRotY() + 90)) * 4) + Math.cos
							(Math.toRadians(super.getRotY())) * 19) ), 0, super.getRotY(), 0, 1.5f, 1, this.currentSpeed));
			
			bolts.add(new Bolt(phaserBeamModel, new Vector3f((float) (playerPos.x + (Math.sin(Math.toRadians(super.getRotY() - 90)) * 5) + Math.sin
					(Math.toRadians(super.getRotY())) * 19), playerPos.y + 22.35f, (float) (playerPos.z + (Math.cos
							(Math.toRadians(super.getRotY() - 90)) * 5) + Math.cos
							(Math.toRadians(super.getRotY())) * 19) ), 0, super.getRotY(), 0, 1.5f, 1, this.currentSpeed));
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			
			Vector3f rotations = new Vector3f(Maths.rotateToFaceVector(super.getPosition(), target));
			
			bolts.add(new Bolt(phaserBeamModel, new Vector3f(super.getPosition()), -rotations.x, rotations.y, rotations.z, 1.5f, 1, this.currentSpeed));
			
		}
		
		for (int i = 0; i < bolts.size(); i++) {
			Bolt bolt = bolts.get(i);
			if (!bolt.isDead())
				bolt.update();
			else
				bolts.remove(i);
		}
		
		for (int i = 0; i < torpedoes.size(); i++) {
			Torpedo torp = torpedoes.get(i);
			if (!torp.isDead())
				torp.update();
			else
				torpedoes.remove(i);
		}
		
	}
	
	@Override
	public void respondToCollision() {
		System.exit(0);
	}
	
}
