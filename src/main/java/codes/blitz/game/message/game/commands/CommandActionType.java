package codes.blitz.game.message.game.commands;

public enum CommandActionType
{
    BUILD("BUILD"),
    SELL("SELL"),
    SEND_REINFORCEMENTS("SEND_REINFORCEMENTS");

    private final String text;

    CommandActionType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
