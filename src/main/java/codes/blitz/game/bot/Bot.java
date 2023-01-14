package codes.blitz.game.bot;

import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.Command;
import codes.blitz.game.message.game.commands.CommandActionBuild;
import codes.blitz.game.message.game.commands.CommandActionSell;
import codes.blitz.game.message.game.commands.CommandActionSendReinforcements;
import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;
import codes.blitz.game.message.game.Path;

import java.util.List;

public class Bot {

    public int i; //Current tick;
    public Point[] towers; //Current towers

    /**
     * Called once when the game starts
     */
    public Bot() {
        i = 0;
    }

    /**
     * Return the best tower positions sorted by descending priority
     * The first Point in the array will be the best position, the second the second best and so on
     * @param gameMessage
     * @param towerRange
     * @return Point[] of best tower positions
     */
    public Point[] getBestTowerPositions(GameMessage gameMessage, int towerRange) {
    
        int mapHeight = gameMessage.map().height();
        int mapWidth = gameMessage.map().width();

        Point[] tiles = new Point[mapHeight * mapWidth];
        int[] score = new int[mapHeight * mapWidth];

        for(int y = 0; y < mapHeight; y++) {
            for(int x = 0; x < mapWidth; x++) {
            //For each tile of the map

                for(Path p: gameMessage.map().paths()) {
                    for(Point pathTile: p.tiles()) {
                        if(x == pathTile.x() && y == pathTile.y()) {
                            continue; //Skip this tile, it's a path so we can put a tower in it
                        }
                    }
                }


            }
        }

        return null;
    }

    /**
     * Runned every tick
     */
    public Command getCommand(GameMessage gameMessage) {

        Command command =  new Command();

        // // 3 possible commands: BUILD, SELL OR SEND_REINFORCEMENT ! Here are a few examples.
        // command.addAction(new CommandActionBuild(TowerType.SPIKE_SHOOTER, new Point(10,10)));
        // command.addAction(new CommandActionSell(new Point(10,10)));
        // command.addAction(new CommandActionSendReinforcements(EnemyType.LVL1,
        //         List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));

        getBestTowerPositions(gameMessage, 2);

        return command;
    }
}