package codes.blitz.game.bot;

import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.Command;
import codes.blitz.game.message.game.commands.CommandActionBuild;
import codes.blitz.game.message.game.commands.CommandActionSell;
import codes.blitz.game.message.game.commands.CommandActionSendReinforcements;
import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;

import java.util.List;

public class Bot
{
    public Bot()
    {
        System.out.println("Initializing your super duper mega bot.");
        // initialize some variables you will need throughout the game here

        // get the game state and the list of free tiles
        GameMessage gameMessage = GameMessage.getGameMessage();
        List<Point> freeTiles = gameMessage.getFreeTiles();

    }

    /*
     * Here is where the magic happens. I bet you can do better ;)
     */
    public Command getCommand(GameMessage gameMessage)
    {
        Command command =  new Command();

        // 3 possible commands: BUILD, SELL OR SEND_REINFORCEMENT ! Here are a few examples.

        // build a tower on a free tile (if you have enough money)
        if (gameMessage.getMoney() >= TowerType.TOWER_1.getCost())
        {
            command.setAction(new CommandActionBuild(TowerType.TOWER_1, gameMessage.getFreeTiles().get(0)));
        }

        // Send a reinforcement to the enemy
        command.addAction(new CommandActionSendReinforcements(EnemyType.LVL1,
                List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));

        return command;
    }
}