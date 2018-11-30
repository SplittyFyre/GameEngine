package gameplay.collision;

import java.util.List;

import box.Main;
import gameplay.entities.entityUtils.ITakeDamage;
import gameplay.entities.hostiles.Enemy;
import gameplay.entities.players.Player;
import gameplay.entities.projectiles.Projectile;
import utils.RaysCast;

public class CollisionManager {
	
	public static void checkCollisions(List<Projectile> playerProjectiles, List<Enemy> enemies, Player player, RaysCast caster) {
		
		player.choreCollisions(enemies, caster);
		
		for (Projectile projectile : Main.foeprojectiles) {
			if (projectile.getBoundingBox().intersects(player.getBoundingBox())) {
				projectile.respondToCollision();
				((ITakeDamage) player).respondToCollisioni(projectile.getDamage(), null);
			}
		}
		
	}
	
}