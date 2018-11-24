package utils;

import org.lwjgl.util.vector.Vector3f;

import scene.entities.players.Player;

public class FloatingOrigin {
	
	private static Player player;
	
	public static float gridLen;
	private static float part;
	
	private static int gridX = 0;
	private static int gridZ = 0;

	public static void init(Player player, float gridSideLen) {
		FloatingOrigin.player = player;
		gridLen = gridSideLen;
		part = gridLen / 2;
	}
	
	public static Vector3f update() {
		
		float transX = 0;
		float transZ = 0;
		
		if (player.getPosition().x > part) {
			transX = -gridLen;
			gridX++;
		}
		else if (player.getPosition().x < -part) {
			transX = gridLen;
			gridX--;
		}
		
		if (player.getPosition().z > part) {
			transZ = -gridLen;
			gridZ++;
		}
		else if (player.getPosition().z < -part) {
			transZ = gridLen;
			gridZ--;
		}
		
		return new Vector3f(transX, 0, transZ);
		
	}
	
	public static int getGridX() {
		return gridX;
	}

	public static int getGridZ() {
		return gridZ;
	}

}
