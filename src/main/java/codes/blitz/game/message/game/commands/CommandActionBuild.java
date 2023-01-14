package codes.blitz.game.message.game.commands;

import java.util.Objects;

import codes.blitz.game.message.game.Point;
import codes.blitz.game.message.game.towers.TowerType;

public class CommandActionBuild extends CommandAction
{
    private final TowerType towerType;
    private final Point position;

    public CommandActionBuild(TowerType towerType, Point position)
    {
        this.position = position;
        this.towerType = towerType;
        setAction(CommandActionType.BUILD);
    }

    public TowerType getTowerType()
    {
        return towerType;
    }

    public Point getPosition()
    {
        return position;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommandActionBuild that = (CommandActionBuild) o;
        return towerType == that.towerType && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), towerType, position);
    }
}
