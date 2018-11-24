package scene.entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import box.TM;
import renderEngine.DisplayManager;
import renderEngine.models.TexturedModel;
import scene.particles.ParticleSystem;
import utils.SFMath;

public class Bolt extends Projectile {
	
	private float moveCoff = 0;
	private float mcX, mcY, mcZ;
	private float elapsedTime = 0;
	private boolean split = false;
	private int SPEED = 3200;
	
	private ParticleSystem sys = null;
	
	@Override
	public boolean isDead() {
		return super.isDead;
	}

	public Bolt(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, float damage, float movementCoefficient) {
		super(model, position, rotX, rotY, rotZ, scale, damage);
		this.moveCoff = movementCoefficient;
	}
	
	public Bolt(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float 
			scaleX, float scaleY, float scaleZ, float damage, float mcX, float mcY, float mcZ, boolean split) {
		super(model, position, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, damage, mcX, mcY, mcZ);
		//this.mcX = mcX;
		//this.mcY = mcY;
		//this.mcZ = mcZ;
		super.setScale(scaleX, scaleY, scaleZ); 
		this.split = split;
	}
	
	public Bolt(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ
			, float damage, float movCoff) {
		super(model, position, rotX, rotY, rotZ, 0, damage);
		this.moveCoff = movCoff < 0 ? 0 : movCoff;
		super.setScale(scaleX, scaleY, scaleZ);
	}
	
	public Bolt(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ
			, float damage, float movCoff, int speed, ParticleSystem sys) {
		super(model, position, rotX, rotY, rotZ, 0, damage);
		this.moveCoff = movCoff < 0 ? 0 : movCoff;
		this.SPEED = speed;
		this.sys = sys;
		super.setScale(scaleX, scaleY, scaleZ);
	}

	@Override
	public void update() {
		
		float distanceMoved = (SPEED + this.moveCoff) * DisplayManager.getFrameTime();
		float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		
		//distanceMoved -= Math.abs(dy);
		
		float l = (float) Math.cos(Math.toRadians(super.getRotX()));
		
		float dx = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotY()))) * l;
		//float dy = (float) (distanceMoved * Math.sin(Math.toRadians(super.getRotX())));
		float dz = (float) (distanceMoved * Math.cos(Math.toRadians(super.getRotY()))) * l;
		dx += mcX;
		dy += mcY;
		dz += mcZ;
		super.move(dx, -dy, dz);
		elapsedTime += DisplayManager.getFrameTime();
		if (elapsedTime > 5f)
			super.isDead = true;
		
	}

	@Override
	public float getDamage() {
		return this.damage;
	}

	@Override
	public void respondToCollision() {
		this.setDead();
		if (this.sys != null) 
			sys.generateParticles(this.getPosition());
		else 
			TM.burnParticleSystem.generateParticles(this.getPosition());
	}
	
	public static Bolt phaser(Vector3f position, float damage, float rotX, float rotY, float rotZ, float movCoff) {
		return new Bolt(TM.phaserBolt, new Vector3f(position), rotX, rotY, rotZ, 1.5f, 1.5f, 10, damage, movCoff);
	}
	
	public static Bolt phaser(Vector3f position, float magSide, float magHeight, float magFront, float damage, float rotX, float rotY, float rotZ, float movCoff) {
		return new Bolt(TM.phaserBolt, 
				new Vector3f(
						
						position.x 
						+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						,
						position.y + magHeight
						,
						position.z
						+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						
						), rotX, rotY, rotZ, 1.5f, 1.5f, 10, damage, movCoff);
	}
	
	public static Bolt phaser(Vector3f position, float magSide, float magHeight, float magFront, float damage, float rotX, float rotY, float rotZ, float movCoff, 
			float scaleMul) {
		return new Bolt(TM.phaserBolt, 
				new Vector3f(
						
						position.x 
						+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						,
						position.y + magHeight
						,
						position.z
						+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						
						), rotX, rotY, rotZ, 1.5f * scaleMul, 1.5f * scaleMul, 10, damage, movCoff);
	}
	
	public static Bolt greenphaser(Vector3f position, float magSide, float magHeight, float magFront, float damage, float rotX, float rotY, float rotZ, float movCoff) {
		return new Bolt(TM.greenPhaser, 
				new Vector3f(
						
						position.x 
						+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						,
						position.y + magHeight
						,
						position.z
						+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						
						), rotX, rotY, rotZ, 1.5f, 1.5f, 10, damage, movCoff);
	}
	
	public static Bolt bluephaser(Vector3f position, float magSide, float magHeight, float magFront, float damage, float rotX, float rotY, float rotZ, float movCoff) {
		return new Bolt(TM.phaserBoltBlue, 
				new Vector3f(
						
						position.x 
						+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						,
						position.y + magHeight
						,
						position.z
						+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
						- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
						
						), rotX, rotY, rotZ, 1.5f, 1.5f, 10, damage, movCoff);
	}
	
}