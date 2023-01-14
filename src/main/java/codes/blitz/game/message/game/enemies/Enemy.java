package codes.blitz.game.message.game.enemies;

import codes.blitz.game.message.game.Point;

public record Enemy(String id, EnemyType type, Point position, Point position_precise, Boolean isPopped, Boolean hasEndedPath)
{
}
