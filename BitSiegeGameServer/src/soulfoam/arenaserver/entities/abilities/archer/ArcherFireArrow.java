package soulfoam.arenaserver.entities.abilities.archer;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import soulfoam.arenaserver.entities.Entity;
import soulfoam.arenaserver.entities.abilities.Ability;
import soulfoam.arenaserver.main.game.Game;
import soulfoam.arenashared.main.abilityinfo.AbilityInfo;
import soulfoam.arenashared.main.abilityinfo.archer.ArcherFireArrowInfo;
import soulfoam.arenashared.main.entityinfo.EntityInfo;


public class ArcherFireArrow extends Ability{
	
	public ArcherFireArrow(int gameID, float dx, float dy, Entity player){
		
		this.abilityID = AbilityInfo.ARCHERFIREARROW;
		this.tag = AbilityInfo.PROJECTILE;
		
		this.gameID = gameID;
		this.dx = dx;
		this.dy = dy;

		this.player = player;
		
		
		this.coolDown = ArcherFireArrowInfo.COOLDOWN;
		this.damage = ArcherFireArrowInfo.DAMAGE;
		this.moveSpeed = ArcherFireArrowInfo.MOVE_SPEED;
		this.destroyTimer = ArcherFireArrowInfo.DESTROY_TIMER;
		
		this.width = ArcherFireArrowInfo.WIDTH;
		this.height = ArcherFireArrowInfo.HEIGHT;
		

		this.hitBoxWidth = 8;
		this.hitBoxHeight = 8;
		this.x = player.getX() + 1;
		this.y = player.getY() + 6;
		
		
	}

	public void update(int delta) {
		
		handleMovement(delta);
		handleCollision();	
		
		if (destroyTimer > 0) {
			destroyTimer -= delta;
		}
		
		if (destroyTimer <= 0)
		{
			removeThis(true);
		}

	}
	
	public void handleMovement(int delta){
		
		if (!move(delta)){
			removeThis(false);
		}
		
	}

	public void handleCollision(){
		
		for (Entity p: Game.getGame().getPlayers()){

			if (getBounds().intersects(p.getBounds()) || getBounds().contains(p.getBounds())){
				if (!collision.getPlayersHit().contains(p) && !p.isDead() && p.getTeam() != player.getTeam()){
					collision.getPlayersHit().add(p);
				}
			}
			
		}
		
		if (collision.getPlayersHit().size() > 0){
			for (Entity p: collision.getPlayersHit()){
				if (!collision.getPlayersCycled().contains(p) && collision.getPlayersCycled().size() < playersToDamage){
					p.takeDamage(damage, player);
					collision.getPlayersCycled().add(p);
				}
			}
			removeThis(true);
		}
			
	}

	public Shape getBounds() {
		return new Rectangle(x, y, hitBoxWidth, hitBoxHeight);
	}

}
