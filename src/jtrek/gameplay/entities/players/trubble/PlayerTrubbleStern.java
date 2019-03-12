package jtrek.gameplay.entities.players.trubble;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engine.utils.RaysCast;
import jtrek.box.TM;
import jtrek.gameplay.entities.hostiles.Enemy;
import jtrek.gameplay.entities.players.Player;

public class PlayerTrubbleStern extends Player {

	private PlayerTrubble stardrive;

	public PlayerTrubbleStern(Vector3f position, PlayerTrubble stardrive) {
		super(TM.stern_model, position, stardrive.getRotX(), stardrive.getRotY(), stardrive.getRotZ(),
				stardrive.getScale().x, null);
		this.stardrive = stardrive;
		this.useCustomRotationAxis = true;
	}

	@Override
	public void update(RaysCast caster) {
		
	}

	@Override
	public void choreCollisions(List<Enemy> enemies, RaysCast caster) {
		
	}

	@Override
	@Deprecated
	public void respondToCollision() {
		
	}
	
	@Override
	public void respondToCollisioni(float damage, Vector3f hit) {
		
	}
	
	@Override
	public Vector3f getPlayerPos() {
		return super.getPosition();
	}

	@Override
	protected void initWeapons() {
		// FIXME Auto-generated method stub
		
	}

}
