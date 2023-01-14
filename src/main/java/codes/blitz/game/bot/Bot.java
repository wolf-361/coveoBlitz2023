package codes.blitz.game.bot;

import codes.blitz.game.message.game.GameMap;
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
    // Map Max value
    private int maxD1 = 0, maxD2 = 0;
    // Listes des truc a placer
    private List<Point> pufferToPlace = new ArrayList<>();
    private List<Point> lanceToPlace = new ArrayList<>();
    private List<Point> cannonToPlace = new ArrayList<>();
    private List<Point> dernierRecours = new ArrayList<>();


    private int compteur = 0;

    private boolean initWasDone = false;

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

        int sentNumber = 0;

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
            getCanonList();
            getDernierRecours();

        }

        // premier tour de jeux
        if (!initWasDone) {
            // 3 lance et attaque 3 enemis
            for (int i = 0; i < 3; i++) {
                if (lanceToPlace.size() > 0) {
                    command.addAction(new CommandActionBuild(TowerType.SPEAR_SHOOTER, lanceToPlace.get(0)));
                    lanceToPlace.remove(0);
                    command.addAction(new CommandActionSendReinforcements(EnemyType.LVL2, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                }

                initWasDone = true;
            }
        }

        int monaie = gameMessage.teamInfos().get(gameMessage.teamId()).money().intValue();

        // Action pour les autres tour de jeux

        if (gameMessage.round().intValue() < 6) {
            if (compteur % 2 == 0 && pufferToPlace.size() > 0 && monaie >= 280) {
                command.addAction(new CommandActionBuild(TowerType.SPIKE_SHOOTER, pufferToPlace.get(0)));
                pufferToPlace.remove(0);
                // retire l'argent
                monaie -= 280;
                // change le compteur
                compteur++;

            } else if (compteur % 2 == 1 && monaie >= 200 && lanceToPlace.size() > 0) {
                command.addAction(new CommandActionBuild(TowerType.SPEAR_SHOOTER, lanceToPlace.get(0)));
                command.addAction(new CommandActionSendReinforcements(EnemyType.LVL2, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                lanceToPlace.remove(0);
                // retire l'argent
                monaie -= 200;
                // change le compteur
                compteur++;
            }

        } else if (cannonToPlace.size() > 0 && monaie >= 600) {
            if (compteur % 2 == 1) {
                command.addAction(new CommandActionBuild(TowerType.BOMB_SHOOTER, cannonToPlace.get(0)));
                cannonToPlace.remove(0);
                // retire l'argent
                monaie -= 200;
                // change le compteur
                compteur++;
            }
            if (compteur % 2 == 0) {
                if (gameMessage.round().intValue() > 11) {
                    command.addAction(new CommandActionSendReinforcements(EnemyType.LVL8, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                } else {
                    command.addAction(new CommandActionSendReinforcements(EnemyType.LVL2, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                }
                compteur++;
            }


        }

        if (dernierRecours.size() > 0 && monaie >= 1000) {
            System.out.println("Dernier recours");
            command.addAction(new CommandActionBuild(TowerType.BOMB_SHOOTER, dernierRecours.get(0)));
            dernierRecours.remove(0);

            // retire l'argent
            monaie -= 200;
            // change le compteur
            compteur++;

            if (compteur % 2 == 0) {
                if (gameMessage.round().intValue() > 11) {
                    command.addAction(new CommandActionSendReinforcements(EnemyType.LVL8, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                } else {
                    command.addAction(new CommandActionSendReinforcements(EnemyType.LVL2, List.of(gameMessage.teams()).stream().filter(teamId -> !teamId.equals(gameMessage.teamId())).findFirst().orElseThrow()));
                }
            }
        }


        return command;
    }



    private void getCanonList() {
        // les meilleurs points pour les canons
        for (int i = maxD2; i > 7; i--)
        {
            for (int j = 0; j < mapD2.length; j++)
            {
                for (int k = 0; k < mapD2[j].length; k++)
                {
                    if (mapD2[j][k] == i)
                    {
                        cannonToPlace.add(new Point(k, j));
                    }
                }
            }
        }
        Collections.shuffle(cannonToPlace);
    }

    private void getLanceList() {
        // les pire place pour les lance
        for (int i = 8; i > 6; i--)
        {
            for (int j = 0; j < mapD2.length; j++)
            {
                for (int k = 0; k < mapD2[j].length; k++)
                {
                    if (mapD2[j][k] == i)
                    {
                        lanceToPlace.add(new Point(k, j));
                    }
                }
            }
        }
        Collections.shuffle(lanceToPlace);
    }

    private void getPufferList() {
        // width = y et height = x et height = i et width = j
        // itérer sur les points de la map D1
        for (int i = maxD2; i > 0; i--) {
            for (int j = 0; j < mapD1.length; j++) {
                for (int k = 0; k < mapD1[j].length; k++) {
                    if (mapD1[j][k] == i) {
                        pufferToPlace.add(new Point(k, j));
                    }
                }
            }
        }
        //Collections.shuffle(pufferToPlace);
    }

    private void getDernierRecours() {
        // width = y et height = x et height = i et width = j
        // itérer sur les points de la map D1
        for (int i = maxD2; i >= 4; i--) {
            for (int j = 0; j < mapD1.length; j++) {
                for (int k = 0; k < mapD1[j].length; k++) {
                    if (mapD1[j][k] == i) {
                        dernierRecours.add(new Point(k, j));
                    }
                }
            }
        }
        Collections.shuffle(dernierRecours);

    }

    private void getMap()
    {
        // calculate the path
        Path[] path = gameMap.paths();

        // width = y et height = x et height = i et width = j
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
                // Si la tuile n'est pas un chemin ou un obstacle
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