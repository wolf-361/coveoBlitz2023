package codes.blitz.game.message.game;

public record GameMap(String name, Integer width, Integer height, Path[] paths, Point[] obstacles) {
}