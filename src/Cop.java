import java.util.List;
import java.util.Random;

public class Cop extends Turtle {

    public Cop(SystemMap map) {
        super(map);
    }

    public void setPatch(Patch patch) {
        this.patch = patch;
    }

    @Override
    public void move() {
        Patch newPatch = map.findEmpty(this.patch);
        if (newPatch != null) {
            this.patch.moveToNewPatch(this, newPatch);
        }
    }

    public void enforce() {
        List<Turtle> vision = map.getNeighbors(patch);
        Random random = new Random();
        while (!vision.isEmpty()) {
            int i = random.nextInt(vision.size());
            if (vision.get(i).getClass().equals(Agent.class)) {
                Agent agent = (Agent) vision.get(i);
                if (agent.getActive()) {
                    patch.moveToNewPatch(this, agent.patch);
                    agent.caught();
                    return;
                } else {
                    vision.remove(i);
                }
            } else {
                vision.remove(i);
            }
        }
    }
}

