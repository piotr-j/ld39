package io.piotrjastrzebski.ld39.game.building;

public interface PowerConsumer {
    float required();
    void provide();
}
