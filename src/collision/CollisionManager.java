package collision;

import java.util.List;

import box.Main;
import scene.entities.entityUtils.ITakeDamage;
import scene.entities.hostiles.Enemy;
import scene.entities.players.Player;
import scene.entities.projectiles.Projectile;
import utils.RaysCast;

public class CollisionManager {
	
	public static void checkCollisions(List<Projectile> playerProjectiles, List<Enemy> enemies, Player player, RaysCast caster) {
		
		player.choreCollisions(enemies, caster);
		
		for (Projectile projectile : Main.foeprojectiles) {
			if (projectile.getBoundingBox().intersects(player.getBoundingBox())) {
				projectile.respondToCollision();
				((ITakeDamage) player).respondToCollisioni(projectile.getDamage());
			}
		}
		
	}
	
}