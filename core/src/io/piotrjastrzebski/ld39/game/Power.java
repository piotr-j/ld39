package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.ld39.game.building.Building;
import io.piotrjastrzebski.ld39.game.building.Buildings;
import io.piotrjastrzebski.ld39.game.building.PowerConsumer;
import io.piotrjastrzebski.ld39.game.building.PowerProducer;

public class Power {
    private float totalPower;
    private float requiredPower;
    private Buildings buildings;

    public Power (Buildings buildings) {
        this.buildings = buildings;
    }

    private Array<PowerProducer> producers = new Array<>();
    private Array<PowerConsumer> consumers = new Array<>();
    public void update(float delta) {
        totalPower = 0;
        consumers.clear();
        producers.clear();
        for (Building building : buildings.getAll()) {
            if (building instanceof PowerProducer) {
                producers.add((PowerProducer)building);
            }
            if (building instanceof PowerConsumer) {
                consumers.add((PowerConsumer)building);
            }
        }

        // TODO only connected!
        for (PowerProducer producer : producers) {
            totalPower += producer.storage();
        }

        requiredPower = 0;
        for (PowerConsumer consumer : consumers) {
            requiredPower += consumer.required();
        }

        if (totalPower >= requiredPower) {
            for (PowerProducer producer : producers) {
                requiredPower = producer.consume(requiredPower);
            }
            for (PowerConsumer consumer : consumers) {
                consumer.provide();
            }
        }

    }

    public float getTotalPower () {
        return totalPower;
    }
}
