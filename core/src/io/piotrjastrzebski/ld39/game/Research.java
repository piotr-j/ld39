package io.piotrjastrzebski.ld39.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.ld39.game.building.Building;
import io.piotrjastrzebski.ld39.game.building.Buildings;
import io.piotrjastrzebski.ld39.game.building.ResearchLab;

public class Research {
    private Array<ResearchLab> labs = new Array<>();
    private Buildings buildings;
    private Array<ResearchStep> steps = new Array<>();
    private int currentStep = 0;
    private float researchPerLab = 50;
    public static float efficiency = 0.5f;

    public Research (Buildings buildings) {
        this.buildings = buildings;
        steps.add(new ResearchStep(.5f ));
        steps.add(new ResearchStep(1f));
        steps.add(new ResearchStep(3f));
        steps.add(new ResearchStep(10f ));
        steps.add(new ResearchStep(15f ));
        steps.add(new ResearchStep(25f ));
        steps.add(new ResearchStep(27f ));
        steps.add(new ResearchStep(29f ));
        steps.add(new ResearchStep(35f ));
        steps.add(new ResearchStep(40f ));

//        for (ResearchStep step : steps) {
//            Gdx.app.log("Step" + step.id, step.efficiency + " -> " + step.researchReq);
//        }

    }

    public void update(float delta) {
        labs.clear();
        for (Building building : buildings.getAll()) {
            if (building instanceof ResearchLab) {
                labs.add((ResearchLab)building);
            }
        }
        ResearchStep step = steps.get(currentStep);
        for (ResearchLab lab : labs) {
            if (lab.isPowered()) {
                step.researchRemaining -= researchPerLab * delta;
            }
            if (step.researchRemaining <= 0) {
                if (currentStep < steps.size -1) {
                    step = steps.get(++currentStep);
                }
            }
        }
        efficiency = steps.get(currentStep).efficiency;
    }

    public String info() {
        ResearchStep step = steps.get(currentStep);
        if (step.researchRemaining <= 0) {
            return "All research done!";
        } else {
            String info = "Researching " + step.efficiency + "% efficient panels\n";
            info += "Progress " + ((1-step.researchRemaining/step.researchReq) * 100) + "%";
            return info;
        }
    }

    public float getPanelEfficiency() {
        return steps.get(currentStep).efficiency;
    }

    public boolean lastResearched() {
        return currentStep == steps.size-1;
    }

    static class ResearchStep {
        static int ids = 1;
        int id = ids++;
        float efficiency;
        float researchReq;
        float researchRemaining;

        public ResearchStep (float efficiency) {
            this.efficiency = efficiency;
            int cat = (int)(efficiency/10);
            researchRemaining = researchReq = 1000 * id + 2000 * cat + efficiency * (id * id * 10);
        }
    }
}
