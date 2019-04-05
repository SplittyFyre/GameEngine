package jtrek.box;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.audio.AudioEngine;
import engine.fontMeshCreator.FontType;
import engine.objStuff.OBJParser;
import engine.renderEngine.TRDisplayManager;
import engine.renderEngine.Loader;
import engine.renderEngine.models.RawModel;
import engine.renderEngine.models.TexturedModel;
import engine.renderEngine.textures.ModelTexture;
import engine.scene.particles.ParticleTexture;
import engine.scene.particles.systems.SimpleParticleSystem;

public class TM {
	
	public static DecimalFormat df = new DecimalFormat("#.###");
	
	public static int photonsnd = AudioEngine.loadSound("photon_torpedo");
	public static int quantumsnd = AudioEngine.loadSound("quantum_torpedo");
	public static int disruptorsnd = AudioEngine.loadSound("klingon_shot");
	
	public static RawModel prephaser = OBJParser.loadObjModel("boltaxis");
	public static TexturedModel phaserBolt = new TexturedModel(prephaser, new ModelTexture(Loader.loadTexture("orange")));
	public static TexturedModel phaserBoltBlue = new TexturedModel(prephaser, new ModelTexture(Loader.loadTexture("quantum")));
	public static TexturedModel greenPhaser = new TexturedModel(prephaser, new ModelTexture(Loader.loadTexture("allGlow")));
	
	public static RawModel pretorpedo = OBJParser.loadObjModel("photon");
	public static TexturedModel photonTorpedo = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("photon")));
	public static TexturedModel quantumTorpedo = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("quantum")));
	public static TexturedModel klingonTorpedo = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("red")));
	
	//public static RawModel prebolt = OBJParser.loadObjModel("");
	public static TexturedModel disruptorBolt = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("allGlow")));
	public static TexturedModel phaserCannon = new TexturedModel(pretorpedo, new ModelTexture(Loader.loadTexture("photon")));
	
	public static Vector3f vec31 = new Vector3f();
	public static Vector3f vec32 = new Vector3f();
	public static Vector3f vec33 = new Vector3f();
	public static Vector3f vec34 = new Vector3f();
	public static Random rng = new SecureRandom();
	
	public static float f1 = 0;
	public static float f2 = 0;
	public static float f3 = 0;
	public static float f4 = 0;
	
	public static RawModel preBOPModel= OBJParser.loadObjModel("birdOP");
	public static TexturedModel BOPModel = new TexturedModel(preBOPModel, new ModelTexture(Loader.loadTexture("try")));
	
	private static ParticleTexture tex1 = new ParticleTexture(Loader.loadTexture("plasma"), 1);
	public static SimpleParticleSystem warpParticleSystem = new SimpleParticleSystem(tex1, 200, 30, 0, 20, 10);
	//texture, pps, speed, gravity, lifelength, scale
	
	private static ParticleTexture tex2 = new ParticleTexture(Loader.loadTexture("fire"), 8);
	public static SimpleParticleSystem burnParticleSystem = new SimpleParticleSystem(tex2, 250, 21, 20, 0.5f, 14);
	public static SimpleParticleSystem smlexplosionParticleSystem = new SimpleParticleSystem(tex2, 250, 21, 0, 1, 50);
	public static SimpleParticleSystem explosionParticleSystem = new SimpleParticleSystem(tex2, 250, 21, 0, 1, 150);
	public static SimpleParticleSystem bigexplosionParticleSystem = new SimpleParticleSystem(tex2, 250, 21, 0, 1, 350);
	private static ParticleTexture tex3 = new ParticleTexture(Loader.loadTexture("greenfire"), 8);
	public static SimpleParticleSystem borgExplosionSystem = new SimpleParticleSystem(tex3, 250, 21, 0, 4, 5000);
	
	private static ParticleTexture tex4 = new ParticleTexture(Loader.loadTexture("shieldBlue"), 1);
	public static SimpleParticleSystem blueShieldSystem = new SimpleParticleSystem(tex4, 1000, 125, 0, 0.5f, 20);
	
	private static ParticleTexture tex5 = new ParticleTexture(Loader.loadTexture("shieldRed"), 1);
	public static SimpleParticleSystem redShieldSystem = new SimpleParticleSystem(tex5, 2500, 150, 0, 0.5f, 20);
	
	private static ParticleTexture tex6 = new ParticleTexture(Loader.loadTexture("shieldGreen"), 1);
	public static SimpleParticleSystem greenShieldSystem = new SimpleParticleSystem(tex6, 2000, 300, 0, 0.5f, 35);
	
	
	public static SimpleParticleSystem blueShieldSystemBig = new SimpleParticleSystem(tex4, 10000, 300, 0, 0.5f, 25);
	
	public static FontType font = new FontType(Loader.loadTexture("segoeUI"), "segoeUI");
	
	public static Vector2f sqr2 = new Vector2f(0.01f, 0.01f * TRDisplayManager.getAspectRatio());
	public static Vector2f sqr4 = new Vector2f(0.025f, 0.025f * TRDisplayManager.getAspectRatio());
	public static Vector2f sqr8 = new Vector2f(0.045f, 0.045f * TRDisplayManager.getAspectRatio());
	
	public static Vector2f sqrgui(float scale) {
		return new Vector2f(scale, scale * TRDisplayManager.getAspectRatio());
	}
	
	public static Vector2f coordtext(Vector2f pos) {
		return coordtext(pos.x, pos.y);
	}
	
	public static Vector2f coordtextcenter(Vector2f pos, float scaleX, float scaleY) {
		Vector2f retval = coordtext(pos.x, pos.y);
		retval.x -= (scaleX / 2f);
		retval.y -= (scaleY / 2f);
		return retval;
	}
	
	public static Vector2f coordtextcenter(Vector2f pos, float scaleX, float scaleY, float yscalediv) {
		Vector2f retval = coordtext(pos.x, pos.y);
		retval.x -= (scaleX / 2f);
		retval.y -= (scaleY / yscalediv);
		return retval;
	}
	
	public static Vector2f coordtext(float x, float y) {	
		return new Vector2f(((x / 2) + 0.5f), -(y / 2) + 0.5f);
	}
	
	public static float coordtextx(float x) {
		return (x / 2) + 0.5f;
	}
	
	public static float coordtexty(float y) {
		return -(y / 2) + 0.5f;
	}
	
	public static RawModel dm = OBJParser.loadObjModel("TRUBBLEDeck");
	public static TexturedModel deck_model = new TexturedModel(dm, new ModelTexture(Loader.loadTexture("uss")));
	
	public static RawModel tm = OBJParser.loadObjModel("TRUBBLEStarDrive");
	public static TexturedModel main_model = new TexturedModel(tm, new ModelTexture(Loader.loadTexture("trubblestardrivepic")));
	
	public static RawModel sm = OBJParser.loadObjModel("TRUBBLESternDriveobj");
	public static TexturedModel stern_model = new TexturedModel(sm, new ModelTexture(Loader.loadTexture("trubblesternpic")));
	
	static {
		main_model.getTexture().setSpecularMap(Loader.loadTexture("trubblestardriveglow"));
		main_model.getTexture().setBrightDamper(3);
		
		stern_model.getTexture().setSpecularMap(Loader.loadTexture("trubblesternglow"));
		stern_model.getTexture().setBrightDamper(3);
		
		blueShieldSystem.randomizeRotation();
		blueShieldSystem.setScaleError(1);
		blueShieldSystem.setSpeedError(0.5f); 
		blueShieldSystem.setLifeError(0.25f);
		
		redShieldSystem.randomizeRotation();
		redShieldSystem.setScaleError(1);
		redShieldSystem.setSpeedError(0.5f);
		redShieldSystem.setLifeError(0.25f);
		
		greenShieldSystem.randomizeRotation();
		greenShieldSystem.setScaleError(1);
		greenShieldSystem.setSpeedError(0.5f);
		greenShieldSystem.setLifeError(0.25f);
		
		blueShieldSystemBig.randomizeRotation();
		blueShieldSystemBig.setScaleError(1);
		blueShieldSystemBig.setSpeedError(0.5f);
		blueShieldSystemBig.setLifeError(0.25f);
		
		phaserBolt.getTexture().setUseFakeLighting(true);
		phaserBolt.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		phaserBolt.getTexture().setBrightDamper(0.4f);
		
		phaserBoltBlue.getTexture().setUseFakeLighting(true);
		phaserBoltBlue.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		phaserBoltBlue.getTexture().setBrightDamper(2);
		
		greenPhaser.getTexture().setUseFakeLighting(true);
		greenPhaser.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		greenPhaser.getTexture().setBrightDamper(2);
		
		photonTorpedo.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		photonTorpedo.getTexture().setBrightDamper(1);
		quantumTorpedo.getTexture().setUseFakeLighting(true);
		quantumTorpedo.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		quantumTorpedo.getTexture().setBrightDamper(1);
		
		disruptorBolt.getTexture().setUseFakeLighting(true);
		disruptorBolt.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		disruptorBolt.getTexture().setBrightDamper(1);
		
		phaserCannon.getTexture().setUseFakeLighting(true);
		phaserCannon.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		phaserCannon.getTexture().setBrightDamper(1);
		
		BOPModel.getTexture().setSpecularMap(Loader.loadTexture("tempGlowMap"));
		BOPModel.getTexture().setBrightDamper(4);
		
		klingonTorpedo.getTexture().setUseFakeLighting(true);
		klingonTorpedo.getTexture().setSpecularMap(Loader.loadTexture("allGlow"));
		klingonTorpedo.getTexture().setBrightDamper(1);
		
	}
	
}
