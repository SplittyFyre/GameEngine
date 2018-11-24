package scene.entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import audio.AudioEngine;
import box.TM;
import renderEngine.DisplayManager;
import renderEngine.models.TexturedModel;
import scene.particles.ParticleSystem;
import utils.SFMath;

public class Torpedo extends Projectile {
	
	public static final float PT = 250;
	public static final float QT = 400;
	
	public static final float PE = 40;
	public static final float QE = 70 ;
	
	private float elapsedTime = 0;
	private float lifeLength = 4;
	
	private ParticleSystem sys = null;
	
	@Override
	public boolean isDead() {
		return super.isDead;
	}

	public Torpedo(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, 
			float scaleX, float scaleY, float scaleZ, float damage, float dx, float dy, float dz) {
		super(model, position, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, damage, dx, dy, dz);
	}
	
	public Torpedo(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, 
			float scaleX, float scaleY, float scaleZ, float damage, float dx, float dy, float dz, ParticleSystem sysin) {
		super(model, position, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, damage, dx, dy, dz);
		this.sys = sysin;
	}
	
	public Torpedo(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, 
			float scaleX, float scaleY, float scaleZ, float damage, float dx, float dy, float dz, float life) {
		super(model, position, rotX, rotY, rotZ, scaleX, scaleY, scaleZ, damage, dx, dy, dz);
		this.lifeLength = life;
	}

	public void setMovement(float dx, float dy, float dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	
	public void setLifeLength(float arg) {
		this.lifeLength = arg;
	}

	@Override
	public void update() {
		elapsedTime += DisplayManager.getFrameTime();
		super.move(dx, dy, dz);
		super.rotate(30f, 18f, -12.6f);
		if (elapsedTime > lifeLength) 
			respondToCollision();
	}

	@Override
	public float getDamage() {
		return this.damage;
	}

	@Override
	public void respondToCollision() {
		this.setDead();
		if (sys != null) 
			sys.generateParticles(getPosition());
		else
			TM.explosionParticleSystem.generateParticles(this.getPosition());
		
		/*AudioEngine.playTempSrc(AudioEngine.loadSound("explosion"), 150, 
				super.getPosition().x, 
				super.getPosition().y, 
				super.getPosition().z);*/
	}
	
	
	public static Torpedo photonTorpedo(Vector3f position, float dx, float dy, float dz) {
		AudioEngine.playTempSrc(TM.photonsnd, 100, position.x, position.y, position.z);
		return new Torpedo(TM.photonTorpedo, new Vector3f(position), 0, 0, 0, 2, 2, 5, PT, dx, dy, dz, 10);
	}
	
	public static Torpedo quantumTorpedo(Vector3f position, float dx, float dy, float dz) {
		return new Torpedo(TM.quantumTorpedo, new Vector3f(position), 0, 0, 0, 2, 2, 5, QT, dx, dy, dz, 10);
	}
	
	public static Torpedo photonTorpedo(Vector3f position, float dx, float dy, float dz, 
			float magSide, float magHeight, float magFront, float rotY) {
		
		return new Torpedo(TM.photonTorpedo, new Vector3f(
				
				position.x 
				+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				position.y + magHeight
				,
				position.z
				+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				),
				0, 0, 0, 2, 2, 5, PT, dx, dy, dz);
	}
	
	public static Torpedo quantumTorpedo(Vector3f position, float dx, float dy, float dz, 
			float magSide, float magHeight, float magFront, float rotY) {
		
		return new Torpedo(TM.quantumTorpedo, new Vector3f(
				
				position.x 
				+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				position.y + magHeight
				,
				position.z
				+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				),
				0, 0, 0, 2, 2, 5, QT, dx, dy, dz);
	}
	
	public static Torpedo photonTorpedo(Vector3f position, float speed, 
			float magSide, float magHeight, float magFront, float rotY, float rotX) {
		
		float move = DisplayManager.getFrameTime() * speed;
		
		AudioEngine.playTempSrc(TM.photonsnd, 100, position.x, position.y, position.z);
		
		return new Torpedo(TM.photonTorpedo, new Vector3f(
				
				position.x 
				+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				position.y + magHeight
				,
				position.z
				+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				),
				0, 0, 0, 2, 2, 5, PT, 
				
				(float) ((float) Math.sin(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				,
				(float) Math.sin(Math.toRadians(-rotX)) * move
				,
				(float) ((float) Math.cos(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				);
	}
	
	public static Torpedo photonTorpedo(float speed, float rotY, float rotX, Vector3f position) {
		
		float move = DisplayManager.getFrameTime() * speed;
		
		AudioEngine.playTempSrc(TM.photonsnd, 100, position.x, position.y, position.z);
		
		return new Torpedo(TM.photonTorpedo, new Vector3f(position),
				0, 0, 0, 2, 2, 5, PT, 
				
				(float) ((float) Math.sin(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				,
				(float) Math.sin(Math.toRadians(-rotX)) * move
				,
				(float) ((float) Math.cos(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				);
	}
	
	public static Torpedo quantumTorpedo(float speed, float rotY, float rotX, Vector3f position) {
		
		float move = DisplayManager.getFrameTime() * speed;
		
		AudioEngine.playTempSrc(TM.quantumsnd, 100, position.x, position.y, position.z);
		
		return new Torpedo(TM.quantumTorpedo, new Vector3f(position),
				0, 0, 0, 2, 2, 5, QT, 
				
				(float) ((float) Math.sin(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				,
				(float) Math.sin(Math.toRadians(-rotX)) * move
				,
				(float) ((float) Math.cos(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				);
	}
	
	public static Torpedo photonTorpedo(float damage, Vector3f position, float speed, 
			float magSide, float magHeight, float magFront, float rotY, float rotX) {
		
		float move = DisplayManager.getFrameTime() * speed;
		
		AudioEngine.playTempSrc(TM.photonsnd, 100, position.x, position.y, position.z);
		
		return new Torpedo(TM.photonTorpedo, new Vector3f(
				
				position.x 
				+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				position.y + magHeight
				,
				position.z
				+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				),
				0, 0, 0, 2, 2, 5, damage, 
				
				(float) ((float) Math.sin(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				,
				(float) Math.sin(Math.toRadians(-rotX)) * move
				,
				(float) ((float) Math.cos(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				);
	}
	
	public static Torpedo klingonTorpedo(Vector3f position, float speed, 
			float magSide, float magHeight, float magFront, float rotY, float rotX) {
		
		float move = DisplayManager.getFrameTime() * speed;
		
		AudioEngine.playTempSrc(TM.photonsnd, 100, position.x, position.y, position.z);
		
		return new Torpedo(TM.klingonTorpedo, new Vector3f(
				
				position.x 
				+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				position.y + magHeight
				,
				position.z
				+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				),
				0, 0, 0, 2, 2, 5, PT, 
				
				(float) ((float) Math.sin(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				,
				(float) Math.sin(Math.toRadians(-rotX)) * move
				,
				(float) ((float) Math.cos(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				);
	}
	
	public static Torpedo quantumTorpedo(Vector3f position, float speed, 
			float magSide, float magHeight, float magFront, float rotY, float rotX) {
		
		float move = DisplayManager.getFrameTime() * speed;
		
		AudioEngine.playTempSrc(TM.quantumsnd, 100, position.x, position.y, position.z);
		
		return new Torpedo(TM.quantumTorpedo, new Vector3f(
				
				position.x 
				+ SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftX(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				,
				position.y + magHeight
				,
				position.z
				+ SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_RIGHT, rotY, magSide)
				- SFMath.relativePosShiftZ(SFMath.SF_DIRECTION_AZIMUTH_NEUTRAL, rotY, -magFront)
				
				),
				0, 0, 0, 2, 2, 5, QT, 
				
				(float) ((float) Math.sin(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				,
				(float) Math.sin(Math.toRadians(-rotX)) * move
				,
				(float) ((float) Math.cos(Math.toRadians(rotY)) * move * Math.cos(Math.toRadians(rotX)))
				);
	}
	
}
