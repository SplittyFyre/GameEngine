package scene.entities.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import box.TM;
import fontMeshCreator.GUIText;
import renderEngine.models.TexturedModel;
import renderEngine.textures.GUITexture;
import scene.entities.Camera;
import scene.entities.Entity;
import scene.entities.entityUtils.ITakeDamage;
import scene.entities.entityUtils.StatusText;
import scene.entities.hostiles.Enemy;
import scene.entities.projectiles.Projectile;
import utils.RaysCast;
import utils.SFMath;

public abstract class Player extends Entity implements ITakeDamage {
	
	public abstract void update(RaysCast caster);
	public abstract void choreCollisions(List<Enemy> enemies, RaysCast caster);
	public abstract Vector3f getPlayerPos();
	protected abstract void initWeapons();
	
	protected float HEALTH;
	protected float SHIELD;
	protected boolean shieldsOn = true;
	
	protected int ENERGY;
	
	protected float IMPULSE_MOVE_SPEED_VAR = 0;
	protected float WARP_SPEED_VAR = 0;
	
	protected float currentSpeed = 0;
	protected float currentTurnSpeed = 0;
	
	protected GUIText healthText;
	protected GUIText shieldsText;
	protected GUIText energyText;
	protected GUIText coordsX = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0), 0.5f, false);
	protected GUIText coordsY = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.05f), 0.5f, false);
	protected GUIText coordsZ = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.1f), 0.5f, false);
	protected GUIText gridText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0, 0.15f), 0.5f, false);
	
	public float tracingX, tracingY, tracingZ, distMoved;
	
	public Camera camera;
	
	protected Enemy target;
	
	public Enemy getTarget() {
		return target;
	}
	
	public boolean cloaked = false;
	
	protected Matrix4f tmat;
	
	protected List<StatusText> statusQueue = new ArrayList<StatusText>();
	protected List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<Projectile>());
	protected List<GUITexture> guis;
	protected HashMap<String, Runnable> weapons = new HashMap<String, Runnable>();
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, List<GUITexture> guis) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	protected void initsuper(float health, float shield, int energy) {
		this.HEALTH = health;
		this.SHIELD = shield;
		this.ENERGY = energy;
		
		healthText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.20f), 1, false);
		healthText.setColour(1, 0, 0);
		
		shieldsText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.25f), 1, false);
		shieldsText.setColour(0, 1, 0.75f);
		
		energyText = new GUIText("Loading...", 1.7f, TM.font, new Vector2f(0.64f, 0.15f), 1, false);
		energyText.setColour(1, 0.75f, 0);
		
		coordsX.setColour(0, 1, 0);
		coordsY.setColour(0, 1, 0);
		coordsZ.setColour(0, 1, 0);
		gridText.setColour(1, 0, 0);
		
	}
	
	protected void prerequisite() {
		tmat = SFMath.createTransformationMatrix(super.getPosition(),
			super.getRotX(), super.getRotY(), super.getRotZ(), 1);
	}
	
	public List<Projectile> getProjectiles() {
		return projectiles;
	}

}
