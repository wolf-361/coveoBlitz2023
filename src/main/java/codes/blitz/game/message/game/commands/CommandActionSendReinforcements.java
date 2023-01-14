package codes.blitz.game.message.game.commands;

import java.util.Objects;

import codes.blitz.game.message.game.enemies.EnemyType;

public class CommandActionSendReinforcements extends CommandAction
{
    private final EnemyType enemyType;
    private final String team;

    public CommandActionSendReinforcements(EnemyType enemyType, String team)
    {
        this.enemyType = enemyType;
        this.team = team;
        setAction(CommandActionType.SEND_REINFORCEMENTS);
    }

    public EnemyType getEnemyType()
    {
        return enemyType;
    }

    public String getTeam()
    {
        return team;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommandActionSendReinforcements that = (CommandActionSendReinforcements) o;
        return enemyType == that.enemyType && Objects.equals(team, that.team);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), enemyType, team);
    }
}
