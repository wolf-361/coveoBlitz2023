package codes.blitz.game.message.game.shop;

import java.util.Map;

import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;

public record Shop(Map<TowerType,TowerEntry> towers, Map<EnemyType, ReinforcementsEntry> reinforcements)
{
}
