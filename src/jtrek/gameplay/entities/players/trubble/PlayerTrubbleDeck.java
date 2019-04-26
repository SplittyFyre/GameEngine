package jtrek.gameplay.entities.players.trubble;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import engine.utils.TRRayCaster;
import jtrek.box.TM;
import jtrek.gameplay.entities.hostiles.Enemy;
import jtrek.gameplay.entities.players.Player;

public class PlayerTrubbleDeck extends Player {
	
	private PlayerTrubble stardrive;

	public PlayerTrubbleDeck(Vector3f position, PlayerTrubble stardrive) {
		super(TM.deck_model, position, stardrive.getRotX(), stardrive.getRotY(), stardrive.getRotZ(),
				stardrive.getScale().x, null);
		this.setScale(stardrive.getScale().x, stardrive.getScale().y, stardrive.getScale().z * 1.5f);
		this.stardrive = stardrive;
		//this.useCustomRotationAxis = true;
	}

	@Override
	public void update(TRRayCaster caster) {
		
	}

	@Override
	public void choreCollisions(List<Enemy> enemies, TRRayCaster caster) {
		
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
