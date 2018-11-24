package scene.particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	public void setLife(float f) {
		this.lifeLength = f;
	}
	
	public boolean priority = false;
	
	private ParticleTexture texture;
	
	private Vector2f texOffSet1 = new Vector2f();
	private Vector2f texOffSet2 = new Vector2f();
	
	private Vector3f trace = null;
	private Vector3f ttlChange;
	
	private float blend;
	
	protected Vector2f getTexOffSet1() {
		return texOffSet1;
	}

	protected Vector2f getTexOffSet2() {
		return texOffSet2;
	}

	protected float getBlend() {
		return blend;
	}

	private float elapsedTime = 0;

	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.texture = texture;
		ParticleWatcher.addParticle(this);
	}
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale, boolean prir) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.texture = texture;
		this.priority = prir;
		ParticleWatcher.addParticle(this);
	}
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale, Vector3f trace) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.texture = texture;
		this.trace = trace;
		this.ttlChange = new Vector3f(0, 0, 0);
		ParticleWatcher.addParticle(this);
	}

	protected ParticleTexture getTexture() {
		return texture;
	}

	protected Vector3f getPosition() {
		return position;
	}

	protected float getRotation() {
		return rotation;
	}

	protected float getScale() {
		return scale;
	}
	
	protected boolean update() {
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTime());
		
		if (this.trace != null) {
			Vector3f.add(change, ttlChange, ttlChange);
			Vector3f.add(ttlChange, trace, position);
		}
		else {
			Vector3f.add(change, position, position);
		}
		
		updateTextureCoordsInfo();
		elapsedTime += DisplayManager.getFrameTime();
		
		return elapsedTime < lifeLength;
	}
	
	private void updateTextureCoordsInfo() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffSet1, index1);
		setTextureOffset(texOffSet2, index2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int numrows = texture.getNumberOfRows();
		int column = index % numrows;
		int row = index / numrows;
		offset.x = (float) column / numrows;
		offset.y = (float) row / numrows;
	}
	
}
