package codes.blitz.game.bot;

import codes.blitz.game.message.game.GameMap;
import codes.blitz.game.message.game.PlayArea;
import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.*;
import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;
import codes.blitz.game.message.game.Path;

import java.util.*;

public class Bot
{
    private GameMap gameMap;
    // map Distance 1 et 2
    private int[][] mapD1, mapD2;

    // Listes des truc a placer
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

        // get the map
        if (gameMessage.round().intValue() == 0)
        {
            // get the original map
            gameMap = gameMessage.map();
            // Calculer D1 et D2
            getMap();

            // Calculer tous les nombre de chemin de 3



        }

        // Placer les unités sur les troisChemin

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

        while(fric >= 280) {
            // make a tupple of the number of tiles and the point
            maxPath = (0, new Point(0,0));

            for (int i = 0; i < gameMap.height(); i++)
            {
                for (int j = 0; j < gameMap.width(); j++)
                {
                    if (mapD1[i][j] >= 2)
                    {
                        if (mapD1[i][j] > maxPath.getKey())
                        {
                            maxPath = (mapD1[i][j], new Point(i, j));
                        }
                    }
                }
            }
            Point newPoint = maxPath.getValue();
            command.addAction((new CommandActionBuild(TowerType.SPIKE_SHOOTER, newPoint)));
        }

        while (fric >= 20000000) {

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

    private void getMap()
    {
        // calculate the path
        Path[] path = gameMap.paths();

        // width = y et height = x
        int[][] map = new int[gameMap.height()][gameMap.width()];
        mapD1 = new int[gameMap.height()][gameMap.width()];
        mapD2 = new int[gameMap.height()][gameMap.width()];

        // initialize la map a zero
        for (int i = 0; i < gameMap.height(); i++)
        {
            for (int j = 0; j < gameMap.width(); j++)
            {
                map[i][j] = 0;
                mapD1[i][j] = 0;
                mapD2[i][j] = 0;
            }
        }

        // Ajouter les chemin comme -1
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path[i].tiles().length; j++) {
                // le point du chemin
                Point point = path[i].tiles()[j];
                map[point.y()][point.x()] = -1;
                mapD1[point.y()][point.x()] = -1;
                mapD2[point.y()][point.x()] = -1;
            }
        }

        // On itterre sur la map pour calculer le nombre de tuiles du chemin accessible depuis chaque tuile
        for (int i = 0; i < gameMap.height(); i++)
        {
            for (int j = 0; j < gameMap.width(); j++)
            {
                // Si la tuile n'est pas un chemin
                if (map[i][j] != -1)
                {
                    // On itterre sur les tuiles adjacentes avec D1
                    for (int k = i - 1; k <= i + 1; k++)
                    {
                        for (int l = j - 1; l <= j + 1; l++)
                        {
                            // Si la tuile est dans la map
                            if (k >= 0 && k < gameMap.height() && l >= 0 && l < gameMap.width())
                            {
                                // Si la tuile est un chemin
                                if (map[k][l] == -1)
                                {
                                    // On incremente le nombre de chemin accessible
                                    mapD1[i][j]++;
                                }
                            }
                        }
                    }

                    // On itterre sur les tuiles adjacentes avec D2
                    for (int k = i - 2; k <= i + 2; k++)
                    {
                        for (int l = j - 2; l <= j + 2; l++)
                        {
                            // Si la tuile est dans la map
                            if (k >= 0 && k < gameMap.height() && l >= 0 && l < gameMap.width())
                            {
                                // Si la tuile est un chemin
                                if (map[k][l] == -1)
                                {
                                    // On incremente le nombre de chemin accessible
                                    mapD2[i][j]++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}