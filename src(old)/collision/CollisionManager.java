package collision;

import box.Main;
import entities.BorgVessel;
import entities.Entity;
import entities.projectiles.Projectile;

public class CollisionManager {
	
	private static int ENTITIES = 0;
	private static int ENEMIES = 1;
	private static int PLAYER_PROJECTILES = 2;
	
	public static void checkCollisions() {
		
		for (Entity projectile : Main.scene.get(PLAYER_PROJECTILES)) {
			
			BoundingBox bb1 = projectile.getBoundingBox();
			
			for (Entity enemy : Main.scene.get(ENEMIES)) {
				
				BoundingBox bb2 = enemy.getBoundingBox();
				
				if (bb1.intersects(bb2)) {
					
					projectile.respondToCollision();
					((BorgVessel) enemy).respondToCollision(((Projectile) projectile).getDamage());
				}
				
			}
			
		}
		
	}
	
}