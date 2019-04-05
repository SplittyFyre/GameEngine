
package engine.scene.particles.systems;
 
import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderEngine.TRDisplayManager;
import engine.scene.particles.Particle;
import engine.scene.particles.ParticleTexture;
import engine.scene.particles.TRParticleSystem;
import engine.utils.TRUtils;
 
public class SimpleParticleSystem extends TRParticleSystem {
 
    public SimpleParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient,
			float lifeLength, float scale) {
		super(texture, pps, speed, gravityComplient, lifeLength, scale);
	}

	@Override
	public void generateParticles(Vector3f sysCenter) {
        float delta = TRDisplayManager.getFrameDeltaTime();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for (int i = 0; i < count; i++) {
            emitParticle(sysCenter, null);
        }
        if (Math.random() < partialParticle) {
            emitParticle(sysCenter, null);
        }
    }
    
    public void generateParticles(Vector3f systemCenter, Vector3f trace) {
        float delta = TRDisplayManager.getFrameDeltaTime();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for (int i = 0; i < count; i++) {
            emitParticle(systemCenter, trace);
        }
        if (Math.random() < partialParticle) {
            emitParticle(systemCenter, trace);
        }
    }
 
    private void emitParticle(Vector3f center, Vector3f trace) {
        Vector3f velocity = null;
        if (direction != null) {
            velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
        } else {
            velocity = generateRandomUnitVector();
        }
        velocity.normalise();
        velocity.scale(generateValue(averageSpeed, speedError));
        float scale = generateValue(averageScale, scaleError);
        float lifeLength = generateValue(averageLifeLength, lifeError);
        new Particle(texture, new Vector3f(center), velocity, gravityComplient, lifeLength, generateRotation(), scale, trace);
    }
 
    private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
        float cosAngle = (float) Math.cos(angle);
        Random random = new Random();
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
 
        Vector4f direction = new Vector4f(x, y, z, 1);
        if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1)) {
            Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
            rotateAxis.normalise();
            float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Matrix4f.transform(rotationMatrix, direction, direction);
        } else if (coneDirection.z == -1) {
            direction.z *= -1;
        }
        return new Vector3f(direction);
    }
     
    private Vector3f generateRandomUnitVector() {
        float theta = (float) (TRUtils.rng.nextFloat() * 2f * Math.PI);
        float z = (TRUtils.rng.nextFloat() * 2) - 1;
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
        return new Vector3f(x, y, z);
    }
 
}