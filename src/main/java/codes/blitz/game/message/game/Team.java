package codes.blitz.game.message.game;

import codes.blitz.game.message.game.enemies.EnemyReinforcements;

public record Team(String id, Number money, Number hp, Boolean isAlive, Number payoutBonus, EnemyReinforcements[] sentReinforcements)
{
}
