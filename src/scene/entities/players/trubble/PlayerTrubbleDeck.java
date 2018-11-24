package scene.entities.players.trubble;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import box.TM;
import scene.entities.hostiles.Enemy;
import scene.entities.players.Player;
import utils.RaysCast;

public class PlayerTrubbleDeck extends Player {
	
	private PlayerTrubble stardrive;

	public PlayerTrubbleDeck(Vector3f position, PlayerTrubble stardrive) {
		super(TM.deck_model, position, stardrive.getRotX(), stardrive.getRotY(), stardrive.getRotZ(),
				stardrive.getScale().x, null);
		this.setScale(stardrive.getScale().x, stardrive.getScale().y, stardrive.getScale().z * 1.5f);
		this.stardrive = stardrive;
		this.customRotationAxis = true;
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
	public void respondToCollisioni(float damage) {
		
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
