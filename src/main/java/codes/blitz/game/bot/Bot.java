package codes.blitz.game.bot;

import codes.blitz.game.message.game.GameMap;
import codes.blitz.game.message.game.PlayArea;
import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.GameMessage;
import codes.blitz.game.message.game.commands.*;
import codes.blitz.game.message.game.enemies.EnemyType;
import codes.blitz.game.message.game.towers.TowerType;
import codes.blitz.game.message.game.Path;
import org.glassfish.grizzly.utils.Pair;

import java.util.*;

public class Bot
{
    private GameMap gameMap;
    // map Distance 1 et 2
    private int[][] mapD1, mapD2;
    // Map Max value
    private int maxD1 = 0, maxD2 = 0;
    // Listes des truc a placer
    private List<Point> pufferToPlace = new ArrayList<>();
    private List<Point> lanceToPlace = new ArrayList<>();
    public Bot()
    {
        System.out.println("Papa ? (Daddy)");
        // initialize some variables you will need throughout the game here
    }

    /*
     * Here is where the magic happens. I bet you can do better ;)
     */
    public Command getCommand(GameMessage gameMessage)
    {
        Command command =  new Command();

        // avant le jeux
        if (gameMessage.round().intValue() == 0)
        {
            // get the original map
            gameMap = gameMessage.map();
            // Calculer D1 et D2
            getMap();

            // Calculer les points a placer
            getPufferList();
            getLanceList();

            // print lance
            System.out.println("Lance : ");
            for (Point p : lanceToPlace)
            {
                System.out.println(p.x() + " " + p.y());
            }
        }

        // premier tour de jeux
        if (gameMessage.round().intValue() == 1) {
            // 3 lance
            for (int i = 0; i < 3; i++) {
                if (lanceToPlace.size() > 0) {
                    command.addAction(new CommandActionBuild(TowerType.SPEAR_SHOOTER, lanceToPlace.get(0)));
                    lanceToPlace.remove(0);
                }
            }
        }

        // Placer les unités

        int sentNumber = 0;

        int monaie = gameMessage.teamInfos().get(gameMessage.teamId()).money().intValue();




        return command;
    }

    private void getLanceList() {
        int LanceMax = 4;
        while (LanceMax > 4) {
            for (int i = 0; i < mapD2.length; i++) {
                for (int j = 0; j < mapD2[i].length; j++) {
                    if (mapD2[i][j] == LanceMax) {
                        lanceToPlace.add(new Point(i, j));
                    }
                }
            }
            LanceMax--;
        }
        Collections.shuffle(lanceToPlace);
    }

    private void getPufferList() {
        // itérer sur les points de la map
        while (maxD1 >= 3) {
            for (int i = 0; i < mapD1.length; i++) {
                for (int j = 0; j < mapD1[i].length; j++) {
                    if (mapD1[i][j] == maxD1) {
                        pufferToPlace.add(new Point(i, j));
                    }
                }
            }
            maxD1--;
        }
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

                    // On compare le nombre de chemin accessible avec le max
                    maxD1 = Math.max(maxD1, mapD1[i][j]);
                    maxD2 = Math.max(maxD2, mapD2[i][j]);
                }
            }
        }
    }
}