package codes.blitz.game.message.game;

import java.util.Map;

import codes.blitz.game.message.game.shop.Shop;

public record GameMessage(String teamId, Number tick, GameMap map, String[] teams, Map<String, Team> teamInfos, Map<String, PlayArea> playAreas, Number round, Number ticksUntilPayout, String[] lastTickErrors, Shop shop, Constants constants) {
}
