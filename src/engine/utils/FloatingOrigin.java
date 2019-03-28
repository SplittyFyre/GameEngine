package engine.utils;

import org.lwjgl.util.vector.Vector3f;

import engine.scene.entities.TREntity;

public class FloatingOrigin {
	
	private static TREntity player;
	
	public static float gridLen;
	private static float halfGrid;
	
	private static int gridX = 0;
	private static int gridZ = 0;

	public static void init(TREntity player, float gridSideLen) {
		FloatingOrigin.player = player;
		gridLen = gridSideLen;
		halfGrid = gridLen / 2;
	}
	
	public static Vector3f update() {
		
		float transX = 0;
		float transZ = 0;
		
		boolean changed = false;
		
		if (player.getPosition().x > halfGrid) {
			transX = -gridLen;
			gridX++;
			changed = true;
		}
		else if (player.getPosition().x < -halfGrid) {
			transX = gridLen;
			gridX--;
			changed = true;
		}
		
		if (player.getPosition().z > halfGrid) {
			transZ = -gridLen;
			gridZ++;
			changed = true;
		}
		else if (player.getPosition().z < -halfGrid) {
			transZ = gridLen;
			gridZ--;
			changed = true;
		}
		
		return changed ? new Vector3f(transX, 0, transZ) : null;
		
	}
	
	public static int getGridX() {
		return gridX;
	}

	public static int getGridZ() {
		return gridZ;
	}

}
