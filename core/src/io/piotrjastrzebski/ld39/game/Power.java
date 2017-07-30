package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import io.piotrjastrzebski.ld39.game.building.*;

public class Power {
    private float totalPower;
    private float requiredPower;
    private Buildings buildings;

    public Power (Buildings buildings) {
        this.buildings = buildings;
    }

    private Array<PowerProducer> allProducers = new Array<>();
    private Array<PowerConsumer> allConsumers = new Array<>();
    private Array<Grid> grids = new Array<>();
    public void update(float delta) {
        totalPower = 0;
        allConsumers.clear();
        allProducers.clear();
        for (Building building : buildings.getAll()) {
            if (building instanceof PowerProducer) {
                allProducers.add((PowerProducer)building);
            }
            if (building instanceof PowerConsumer) {
                allConsumers.add((PowerConsumer)building);
            }
        }

        grids.clear();
        for (PowerProducer producer : allProducers) {
            if (!(producer instanceof PowerConnector)) {
                throw new AssertionError("Welp");
            }
            PowerConnector connector = (PowerConnector)producer;
            Grid selected = null;
            for (Grid grid : grids) {
                if (grid.producers.contains(producer)) {
                    selected = grid;
                    break;
                }
            }
            if (selected == null) {
                selected = new Grid();
                grids.add(selected);
            }

            addConnectors(selected, connector);
        }

        for (Grid grid : grids) {
            float gridPower = 0;
            float gridRequired = 0;
            for (PowerProducer producer : grid.producers) {
                totalPower += producer.storage();
                gridPower += producer.storage();
            }

            requiredPower = 0;
            for (PowerConsumer consumer : grid.consumers) {
                requiredPower += consumer.required();
                gridRequired += consumer.required();
            }

            if (gridPower >= gridRequired) {
                for (PowerProducer producer : grid.producers) {
                    gridRequired = producer.consume(gridRequired);
                }
                for (PowerConsumer consumer : grid.consumers) {
                    consumer.provide();
                }
            }
        }
    }

    private void addConnectors (Grid grid, PowerConnector source) {
        if (!grid.connectors.add(source)) return;
        if (source instanceof PowerProducer) {
            grid.producers.add((PowerProducer)source);
        }
        if (source instanceof PowerConsumer) {
            grid.consumers.add((PowerConsumer)source);
        }
        for (PowerConnector connector : source.connected()) {
            addConnectors(grid, connector);
        }
    }

    public void debugDraw(ShapeRenderer shapes) {
        if (false) return;
        shapes.setColor(Color.CYAN);

        for (Grid grid : grids) {
            for (PowerConnector connector : grid.connectors) {
                Building cc = (Building)connector;
                for (PowerConnector other : connector.connected()) {
                    Building oc = (Building)other;
                    shapes.line(cc.cx(), cc.cy(), oc.cx(), oc.cy());
                }
            }
        }

    }

    static class Grid {
        ObjectSet<PowerProducer> producers = new ObjectSet<>();
        ObjectSet<PowerConsumer> consumers = new ObjectSet<>();
        ObjectSet<PowerConnector> connectors = new ObjectSet<>();
    }

    public float getTotalPower () {
        return totalPower;
    }
}
