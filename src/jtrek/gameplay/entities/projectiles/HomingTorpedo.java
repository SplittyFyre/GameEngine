package jtrek.gameplay.entities.projectiles;

import org.lwjgl.util.vector.Vector3f;

import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.models.TexturedModel;
import engine.scene.entities.TREntity;
import engine.scene.particles.Particle;
import engine.scene.particles.ParticleTexture;
import engine.scene.particles.systems.SimpleParticleSystem;
import engine.utils.TRMath;
import jtrek.box.TM;
import jtrek.gameplay.entities.players.Player;

public class HomingTorpedo extends Projectile {

	private float speed;
	private TREntity target;
	
	private float timer = 0;
	private float lifelength = 10;
	private Vector3f tracing;
	private float arcX, arcY, arcZ;
	
	private boolean trail = false;
	private ParticleTexture trailTexture = null;
	
	private float particleLife, particleScale;
	
	private Runnable col = null;
	
	private SimpleParticleSystem sys = null;
	
	public HomingTorpedo(TexturedModel model, Vector3f position, float scaleX, float scaleY, float scaleZ,
			float damage, float speed, float lifelength, TREntity target, float arcX, float arcY, float arcZ) {
		super(model, position, 0, 0, 0, scaleX, scaleY, scaleZ, damage, 0, 0, 0);
		this.speed = speed;
		this.target = target;
		this.lifelength = lifelength;
		this.arcX = arcX;
		this.arcY = arcY;
		this.arcZ = arcZ;
	}
	
	public HomingTorpedo(TexturedModel model, Vector3f position, float scaleX, float scaleY, float scaleZ,
			float damage, float speed, float lifelength, TREntity target, float arcX, float arcY, float arcZ, SimpleParticleSystem sysin) {
		super(model, position, 0, 0, 0, scaleX, scaleY, scaleZ, damage, 0, 0, 0);
		this.speed = speed;
		this.target = target;
		this.lifelength = lifelength;
		this.arcX = arcX;
		this.arcY = arcY;
		this.arcZ = arcZ;
		sys = sysin;
	}
	
	public HomingTorpedo(TexturedModel model, Vector3f position, float scaleX, float scaleY, float scaleZ,
			float damage, float speed, float lifelength, TREntity target, float arcX, float arcY, float arcZ, 
			ParticleTexture trailTexture, float particleLife, float particleScale) {
		super(model, position, 0, 0, 0, scaleX, scaleY, scaleZ, damage, 0, 0, 0);
		this.speed = speed;
		this.target = target;
		this.lifelength = lifelength;
		this.arcX = arcX;
		this.arcY = arcY;
		this.arcZ = arcZ;
		
		this.trail = true;
		
		this.trailTexture = trailTexture;
		this.particleLife = particleLife;
		this.particleScale = particleScale;
	}
	
	public HomingTorpedo(TexturedModel model, Vector3f position, float scaleX, float scaleY, float scaleZ,
			float damage, float speed, float lifelength, TREntity target, float arcX, float arcY, float arcZ, 
			ParticleTexture trailTexture, float particleLife, float particleScale, Runnable col) {
		super(model, position, 0, 0, 0, scaleX, scaleY, scaleZ, damage, 0, 0, 0);
		this.speed = speed;
		this.target = target;
		this.lifelength = lifelength;
		this.arcX = arcX;
		this.arcY = arcY;
		this.arcZ = arcZ;
		
		this.trail = true;
		
		this.trailTexture = trailTexture;
		this.particleLife = particleLife;
		this.particleScale = particleScale;
		this.col = col;
	}
	
	@Override
	public void update() {
		
		if (target == null) {
			this.respondToCollision();
		}
		timer += TRDisplayManager.getFrameDeltaTime();
		float dtmove = TRDisplayManager.getFrameDeltaTime() * this.speed;
		if (target != null) {
			if (target instanceof Player)
				tracing = TRMath.rotateToFaceVector(this.getPosition(), ((Player) target).getPlayerPos());	
			else
				tracing = TRMath.rotateToFaceVector(this.getPosition(), target.getPosition());
		}
		else
			tracing = new Vector3f(0, 0, 0);
		
		//float arch = (float) Math.toRadians(tracing.x + arcY));
		
		float rad = (float) Math.toRadians(tracing.x + arcY);
		float compensate = (float) Math.cos(rad);
		
		float homingX = (float) (dtmove * Math.sin(Math.toRadians(tracing.y + arcX)) * compensate);
		float homingY = (float) (dtmove * Math.sin(rad));
		float homingZ = (float) (dtmove * Math.cos(Math.toRadians(tracing.y + arcZ)) * compensate);
		
		super.move(homingX, homingY, homingZ);
		
		super.rotate(30f, 18f, -12.6f);
		
		if (trail) {
			new Particle(trailTexture, new Vector3f(super.getPosition()), new Vector3f(0, 0, 0), 0, 
					this.particleLife, 0, this.particleScale);
		}
		
		if (timer > lifelength) 
			this.respondToCollision();
	}
	
	public void setTarget(TREntity arg) {
		this.target = arg;
	}
	
	public TREntity getTarget() {
		return this.target;
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
		
		if (this.col != null) {
			col.run();
		}
		
	}

}
