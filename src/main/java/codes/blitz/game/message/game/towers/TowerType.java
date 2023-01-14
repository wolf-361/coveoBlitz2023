package codes.blitz.game.message.game.towers;

public enum TowerType
{
    SPEAR_SHOOTER("SPEAR_SHOOTER"),
    SPIKE_SHOOTER("SPIKE_SHOOTER"),
    BOMB_SHOOTER("BOMB_SHOOTER");

    private final String text;

    TowerType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
