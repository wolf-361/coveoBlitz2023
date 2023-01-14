package codes.blitz.game.message.game.towers;

import codes.blitz.game.message.game.Point;

public record Tower(String id, TowerType type, Point position, Integer width, Integer height, Boolean isShooting)
{
}
