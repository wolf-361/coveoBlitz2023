package codes.blitz.game.bot;

import codes.blitz.game.message.game.GameMap;
import codes.blitz.game.message.game.PlayArea;
import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.*;
import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;

import java.util.ArrayList;
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
        boolean placed = false;
        Command command =  new Command();
        while ((int)gameMessage.teamInfos().get(gameMessage.teamId()).money() >= 200) {
            do {
                placed = false;
                int x = (int) (Math.random() * (gameMessage.map().width() - 1));
                int y = (int) (Math.random() * (gameMessage.map().height() - 1));
                Point point = new Point(x, y);
                if (gameMessage.playAreas().get(gameMessage.teamId()).grid().get(point).isEmpty()) {
                    command.addAction((new CommandActionBuild(TowerType.SPEAR_SHOOTER, point)));
                    placed = true;
                }
            } while (!placed);
        }

        // 3 possible commands: BUILD, SELL OR SEND_REINFORCEMENT ! Here are a few examples.
//        command.addAction(new CommandActionBuild(TowerType.SPIKE_SHOOTER, new Point(10,10)));
//        command.addAction(new CommandActionBuild(TowerType.SPEAR_SHOOTER, new Point(5, 4)));
//        command.addAction(new CommandActionSell(new Point(10,10)));
//        command.addAction(new CommandActionSendReinforcements(EnemyType.LVL1,
//                List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
        return command;
    }
}