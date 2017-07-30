package io.piotrjastrzebski.ld39.game.building;

import com.badlogic.gdx.utils.ObjectSet;

public interface PowerConnector {
    boolean connect(PowerConnector other);
    Building owner ();

    void disconnect (PowerConnector connector);

    void disconnectAll ();

    ObjectSet<PowerConnector> connected();
}
