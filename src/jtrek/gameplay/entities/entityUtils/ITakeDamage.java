package jtrek.gameplay.entities.entityUtils;

import org.lwjgl.util.vector.Vector3f;

public abstract interface ITakeDamage {
	
	public abstract void respondToCollisioni(float damage, Vector3f hit);

}
