package io.piotrjastrzebski.ld39.game.building;

import io.piotrjastrzebski.ld39.game.Coal;

public interface CoalConsumer {
    boolean accept(Coal coal);
}
