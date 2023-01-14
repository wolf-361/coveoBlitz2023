package codes.blitz.game.message.game;

import codes.blitz.game.message.game.enemies.Enemy;
import codes.blitz.game.message.game.towers.Tower;

public record Tile(Tower[] towers, Enemy[] enemies, String[] paths, Boolean hasObstacle)
{
}
