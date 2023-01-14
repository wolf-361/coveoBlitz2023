package codes.blitz.game.message.game.commands;

import java.util.Objects;

public class CommandAction
{
    private CommandActionType action;

    public void setAction(CommandActionType action)
    {
        this.action = action;
    }

    public CommandActionType getAction()
    {
        return action;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandAction that = (CommandAction) o;
        return action == that.action;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(action);
    }
}
