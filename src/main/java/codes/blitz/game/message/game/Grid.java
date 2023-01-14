package codes.blitz.game.message.game;

import java.util.HashMap;
import java.util.Optional;

public class Grid extends HashMap<Integer, HashMap<Integer, Tile>>
{
    public Optional<Tile> getTileAt(Point position)
    {
        return Optional.ofNullable(this.get(position.x())).map(map -> map.get(position.y()));
    }

    public Boolean isEmpty(Point position)
    {
        Optional<Tile> tile = Optional.ofNullable(this.get(position.x())).map(map -> map.get(position.y()));
        if (tile.isEmpty()) {
            return true;
        }
        return tile.get().towers().length == 0 && tile.get().enemies().length == 0 && tile.get().paths().length == 0 && !tile.get().hasObstacle();
    }
}
