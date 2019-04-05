package engine.scene.particles;

import org.lwjgl.util.vector.Vector3f;

import engine.utils.TRUtils;

public abstract class TRParticleSystem {
	
    protected float pps, averageSpeed, gravityComplient, averageLifeLength, averageScale;
    
    protected float speedError, lifeError, scaleError = 0;
    protected boolean randomRotation = false;
    protected Vector3f direction;
    protected float directionDeviation = 0;
    
    protected ParticleTexture texture;
 
    
    
    
    public void setDirection(Vector3f direction, float deviation) {
        this.direction = new Vector3f(direction);
        this.directionDeviation = (float) (deviation * Math.PI);
    }
 
    public void randomizeRotation() {
        randomRotation = true;
    }
    
    public ParticleTexture getTexture() {
    	return this.texture;
    }
    
    public void setPPS(float pps) {
    	this.pps = pps;
    }
    
    public void setSpeedError(float error) {
        this.speedError = error * averageSpeed;
    }
 
    public void setLifeError(float error) {
        this.lifeError = error * averageLifeLength;
    }
 
    public void setScaleError(float error) {
        this.scaleError = error * averageScale;
    }
    
    protected float generateValue(float average, float errorMargin) {
        float offset = (TRUtils.rng.nextFloat() - 0.5f) * 2f * errorMargin;
        return average + offset;
    }
 
    protected float generateRotation() {
        if (randomRotation) {
            return TRUtils.rng.nextFloat() * 360f;
        } else {
            return 0;
        }
    }
    
    
    
    public TRParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityComplient = gravityComplient;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
        this.texture = texture;
    }
    
    
    
	public abstract void generateParticles(Vector3f sysCenter);

}
