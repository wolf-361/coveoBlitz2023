package codes.blitz.game.bot;

import codes.blitz.game.message.game.GameMap;
import codes.blitz.game.message.game.Path;
import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.Command;
import codes.blitz.game.message.game.commands.CommandActionBuild;
import codes.blitz.game.message.game.commands.CommandActionSell;
import codes.blitz.game.message.game.commands.CommandActionSendReinforcements;
import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;

import java.util.List;
import java.util.Map;

public class Bot
{
    private GameMap gameMap;
    // map Distance 1 et 2
    private int[][] mapD1, mapD2;

    // Si les unités nécessaires sont déja placer
    private boolean lancier, poissonHerisson, cannonier;

    public Bot()
    {
        System.out.println("Initializing your super duper mega bot.");
        // initialize some variables you will need throughout the game here
        lancier = false;
        poissonHerisson = false;
        cannonier = false;
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

            // On affiche la map
            System.out.println("Map : ");
            for (int i = 0; i < gameMap.height(); i++)
            {
                for (int j = 0; j < gameMap.width(); j++)
                {
                    System.out.print(map[i][j] + " ");
                }
                System.out.println();
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

            // Afficher la map D1
            System.out.println("Map D1");
            for (int i = 0; i < gameMap.height(); i++)
            {
                for (int j = 0; j < gameMap.width(); j++)
                {
                    System.out.print(mapD1[i][j] + " ");
                }
                System.out.println();
            }

            // Afficher la map D2
            System.out.println("Map D2");
            for (int i = 0; i < gameMap.height(); i++)
            {
                for (int j = 0; j < gameMap.width(); j++)
                {
                    System.out.print(mapD2[i][j] + " ");
                }
                System.out.println();
            }
        }


        command.addAction(new CommandActionSendReinforcements(EnemyType.LVL1,
                List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));

        return command;
    }
}