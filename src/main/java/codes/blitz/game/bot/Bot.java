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
    }

    /*
     * Here is where the magic happens. I bet you can do better ;)
     */
    public Command getCommand(GameMessage gameMessage)
    {
        Command command =  new Command();

        // 3 possible commands: BUILD, SELL OR SEND_REINFORCEMENT ! Here are a few examples.
        command.addAction(new CommandActionBuild(TowerType.SPIKE_SHOOTER, new Point(10,10)));
        command.addAction(new CommandActionSell(new Point(10,10)));
        command.addAction(new CommandActionSendReinforcements(EnemyType.LVL1,
                List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));

        return command;
    }
}