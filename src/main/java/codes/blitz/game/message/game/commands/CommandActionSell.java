package codes.blitz.game.message.game.commands;

import java.util.Objects;

import codes.blitz.game.message.game.Point;

public class CommandActionSell extends CommandAction
{
    private final Point position;

    public CommandActionSell(Point position)
    {
        this.position = position;
        this.setAction(CommandActionType.SELL);
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
        CommandActionSell that = (CommandActionSell) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), position);
    }
}
