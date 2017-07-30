package io.piotrjastrzebski.ld39.game.building;

public interface PowerProducer {
    float storage();
    float consume (float power);
}
