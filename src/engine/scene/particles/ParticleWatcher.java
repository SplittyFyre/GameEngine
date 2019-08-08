package engine.scene.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import engine.scene.entities.camera.TRCamera;

public class ParticleWatcher {
	
	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer renderer;
	
	public static void init(Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(projectionMatrix);
	}
	
	public static void setProjectionMatrix(Matrix4f pmat) {
		renderer.setProjectionMatrix(pmat);
	}
	
	public static void update() {
		
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iter = list.iterator();
			while(iter.hasNext()) {
				Particle particle = iter.next();
				boolean alive = particle.update();
				if (!alive) {
					iter.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
		}
	}
	
	public static void shiftParticles(Vector3f trans) {
		
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iter = list.iterator();
			while(iter.hasNext()) {
				Particle particle = iter.next();
				Vector3f.add(particle.getPosition(), trans, particle.getPosition());
			}
		}
	}

	public static void renderParticles(TRCamera camera) {
		renderer.render(particles, camera);
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	public static void addParticle(Particle p) {
		List<Particle> list = particles.get(p.getTexture());
		if (list == null) {
			list = new ArrayList<Particle>();
			particles.put(p.getTexture(), list);
		}
		list.add(p);
	}
	
}
