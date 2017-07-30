package io.piotrjastrzebski.ld39.game.building;

public interface PowerConnector {
    void connect(PowerConnector other);
    Building owner ();

    void disconnect (PowerConnector connector);
}
