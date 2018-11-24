package box;

import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.Loader;

public class TaskManager {
	
	static Loader loader = new Loader();
	
	private static ParticleTexture tex1 = new ParticleTexture(loader.loadTexture("cosmic"), 4);
	public static ParticleSystem warpParticleSystem = new ParticleSystem(tex1, 250, 30, 0, 20, 10);
	//texture, pps, speed, gravity, lifelength, scale
	
	private static ParticleTexture tex2 = new ParticleTexture(loader.loadTexture("fire"), 8);
	public static ParticleSystem burnParticleSystem = new ParticleSystem(tex2, 250, 21, 0, 0.5f, 7);

}
