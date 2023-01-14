package codes.blitz.game.message.game;

import codes.blitz.game.message.game.enemies.Enemy;
import codes.blitz.game.message.game.enemies.EnemyReinforcements;
import codes.blitz.game.message.game.towers.Tower;

public record PlayArea(String teamId, Enemy[] enemies, EnemyReinforcements[] enemyReinforcementsQueue, Tower[] towers, Grid grid)
{
}
