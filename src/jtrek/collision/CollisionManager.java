package jtrek.collision;

import java.util.List;

import engine.utils.TRRayCaster;
import jtrek.box.Main;
import jtrek.gameplay.entities.entityUtils.ITakeDamage;
import jtrek.gameplay.entities.hostiles.Enemy;
import jtrek.gameplay.entities.players.Player;
import jtrek.gameplay.entities.projectiles.Projectile;

public class CollisionManager {
	
	public static void checkCollisions(List<Projectile> playerProjectiles, List<Enemy> enemies, Player player, TRRayCaster caster) {
		
		player.choreCollisions(enemies, caster);
		
		for (Projectile projectile : Main.foeprojectiles) {
			if (projectile.getBoundingBox().intersects(player.getBoundingBox())) {
				//projectile.respondToCollision();
				((ITakeDamage) player).respondToCollisioni(projectile.getDamage(), null);
			}
		}
		
	}
	
}