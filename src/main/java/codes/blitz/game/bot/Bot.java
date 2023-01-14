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
        System.out.println("Papa ?");
        // initialize some variables you will need throughout the game here
    }

    /*
     * Here is where the magic happens. I bet you can do better ;)
     */
    public Command getCommand(GameMessage gameMessage)
    {
        Command command =  new Command();

        int i = 0;
        int sentNumber = 0;

        double fric = gameMessage.teamInfos().get(gameMessage.teamId()).money().doubleValue();

        if(gameMessage.round().equals(1)) {
            while (fric >= 15 && sentNumber < 8) {
                command.addAction(new CommandActionSendReinforcements(EnemyType.LVL2, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                fric -= 15;
                sentNumber++;
            }
        }

        if((int)gameMessage.round() >= 15){
            while (fric >= 120 && sentNumber < 3) {
                command.addAction(new CommandActionSendReinforcements(EnemyType.LVL9, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                fric -= 120;
                sentNumber++;
            }
        }

        while (fric >= 200) {

            boolean placed = false;

            while (!placed) {
                int x = (int) (Math.random() * (gameMessage.map().width() - 1));
                int y = (int) (Math.random() * (gameMessage.map().height() - 1));
                Point newPoint = new Point(x, y);

                if (gameMessage.playAreas().get(gameMessage.teamId()).grid().isEmpty(newPoint)) {
                    command.addAction((new CommandActionBuild(TowerType.SPEAR_SHOOTER, newPoint)));

                    System.out.println("New tower at " + x + "-" + y);
                    placed = true;
                    break;

                }
            }

            fric -= 200;
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