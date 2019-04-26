package jtrek.gameplay.entities.hostiles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engine.objStuff.OBJParser;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.utils.TRMath;
import jtrek.box.Main;
import jtrek.box.TM;
import jtrek.gameplay.entities.players.Player;
import jtrek.gameplay.entities.projectiles.Bolt;
import jtrek.gameplay.entities.projectiles.HomingTorpedo;
import jtrek.gameplay.entities.projectiles.Torpedo;

public class BorgVessel extends Enemy {
	
	RawModel pretorpedo = OBJParser.loadObjModel("photon");
	TexturedModel privateTorpedoTexture = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("allGlow")));
	
	RawModel prephaser = OBJParser.loadObjModel("bolt");
	TexturedModel privatePhaserTexture = new TexturedModel(prephaser, new ModelTexture(Loader.loadTexture("allGlow")));
	
	private Player player;
	private float HEALTH = 50000;
	private float movX, movY = 0, movZ;
	private float counter = 0, counter1 = 0, counter2 = 0, counter3 = 0;
	private boolean flag = false, flag1 = false, flag2 = false;
	private float beamcounter = 0;
	private boolean beaming = false;
	
	public float getHealth() {
		return HEALTH;
	}
	
	private List<Torpedo> seeking = new ArrayList<Torpedo>();

	public BorgVessel(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Player player) {
		super(model, position, rotX, rotY, rotZ, scale);
		movX = (TM.rng.nextFloat() * 2 - 1) * 25;
		movY = (TM.rng.nextFloat() * 2 - 1) * 10;
		movZ = (TM.rng.nextFloat() * 2 - 1) * 25;
		privateTorpedoTexture.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		privateTorpedoTexture.getTexture().setBrightDamper(1);
		
		privatePhaserTexture.getTexture().setUseFakeLighting(true);
		privatePhaserTexture.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		privatePhaserTexture.getTexture().setBrightDamper(1);
		
		this.player = player;
	}
	
	@Override
	public void update() {
		//WARNING: EXPERIMENTAL
		float dist = TRMath.distance(player.getPlayerPos(), super.getPosition());
		
		float coeff = (dist / (3200 * TRDisplayManager.getFrameDeltaTime())) * 1.8f;
		coeff = 0;
		
		Vector3f vec = TRMath.rotateToFaceVector(super.getPosition(), Vector3f.add(new Vector3f(player.getPlayerPos().x, player.getPlayerPos().y, player.getPlayerPos().z),
				new Vector3f(player.tracingX * coeff, player.tracingY * coeff, player.tracingZ * coeff), null));
		
		//System.out.printf("%f, %f, %f\n", player.tracingX, player.tracingY, player.tracingZ);
		
		if (dist > 2000) {
			//super.move(homingX, homingY, homingZ);
		}
		
		//super.move(movX, movY, movZ);
		super.rotate(0, 0.15f, 0);
		this.counter += TRDisplayManager.getFrameDeltaTime();
		this.counter1 += TRDisplayManager.getFrameDeltaTime();
		
		if (dist <= 10000) {
			Vector3f torpmv = TRMath.moveToVector(player.getPlayerPos(), 
					super.getPosition(), 10000);
			if (counter > 1 && !flag) {
				Main.foeprojectiles.add(new Torpedo(privateTorpedoTexture, 
					new Vector3f(super.getPosition()),
					0, 0, 0, 3, 3, 6.5f, 250, 
					torpmv.x, torpmv.y, 
					torpmv.z, TM.smlexplosionParticleSystem));
				flag = true;
			}
			else if (counter > 1.25f) {
				
				Main.foeprojectiles.add(new Torpedo(privateTorpedoTexture, 
						new Vector3f(super.getPosition()),
						0, 0, 0, 3, 3, 6.5f, 250, 
						torpmv.x, torpmv.y, 
						torpmv.z, TM.smlexplosionParticleSystem));
				
				/*if (!player.cloaked) { 
					Main.foeprojectiles.add(new HomingTorpedo(privateTorpedoTexture,
							new Vector3f(super.getPosition().x, super.getPosition().y, super.getPosition().z), 
							3, 3, 6.5f, 300, 3100, 15, player, 
							0, 20, 0, TM.smlexplosionParticleSystem));
				}*/
				
				counter = 0;
				flag = false;
			}
			
			if (beaming) {
				beamcounter += TRDisplayManager.getFrameDeltaTime();

				if (beamcounter > 1) {
					beaming = false;
				}

				Main.foeprojectiles.add(new Bolt(privatePhaserTexture, 
						new Vector3f(super.getPosition()), 
						-(vec.x), vec.y, 0, 
						1.5f, 1.5f, 15, 40, 0));
			}
			else {
				beamcounter += TRDisplayManager.getFrameDeltaTime();
				if (beamcounter >= 3) {
					beaming = true;
					beamcounter = 0;
				}
			}
			
			vec = TRMath.rotateToFaceVector(new Vector3f(super.getPosition().x, super.getPosition().y + 400, super.getPosition().z),
					player.getPlayerPos());
			
			/*if (TaskManager.rng.nextInt(225) < 2) {
			    Main.foeprojectiles.add(new Torpedo(privateTorpedoTexture, 
						new Vector3f(super.getPosition().x, super.getPosition().y + 400, super.getPosition().z),
						0, 0, 0, 3, 3, 6.5f, 250, 
						(float) (f * Math.sin(Math.toRadians(vec.y))), (float) (f * Math.sin(Math.toRadians(vec.x))), 
						(float) (f * Math.cos(Math.toRadians(vec.y)))));
			}*/
			
			if (!player.cloaked && dist <= 3000 && counter1 > 0.5f) {
				Main.foeprojectiles.add(new HomingTorpedo(privateTorpedoTexture,
						new Vector3f(super.getPosition().x, super.getPosition().y + 400, super.getPosition().z), 
						3, 3, 6.5f, 300, 3100, 15, player, 
						0, -5, 0, TM.smlexplosionParticleSystem));
				counter1 = 0;
			}

		}
		
	}
	
	public List<Torpedo> getHomingMissiles() {
		return seeking;
	}

	@Override
	public void respondToCollisioni(float damage, Vector3f hit) {
		HEALTH -= damage;
		TM.greenShieldSystem.setPPS(Math.min(2000, 15 * damage));
		TM.greenShieldSystem.generateParticles(hit, hit);
		if (HEALTH <= 0) {
			this.setDead();
			TM.borgExplosionSystem.generateParticles(this.getPosition());
		}
	}

	/**
	 * @deprecated
	 * **/
	@Deprecated
	@Override
	public void respondToCollision() {
		
	}
	
	public void takeDamage(float damage) {
		HEALTH -= damage;
		if (HEALTH <= 0)
			this.setDead();
	}
	


}
