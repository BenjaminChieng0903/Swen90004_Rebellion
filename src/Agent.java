import java.util.List;
import java.util.Random;

/**
 * @author Jingning Qian, Baorui Chen
 */
public class Agent extends Turtle {
    protected double risk_aversion;
    protected double hardship;
    protected boolean active;
    protected int jail_term;

    public Agent(SystemMap map) {
        super(map);
        setup();
    }

    public void setPatch(Patch patch) {
        this.patch = patch;
    }

    @Override
    public void move() {
        Patch newPatch = map.findEmpty(this.patch);
        if (jail_term == 0 && newPatch != null) {
            patch.moveToNewPatch(this, newPatch);
        }
    }

    public boolean getActive() {
        return active;
    }

    /**
     * set the behavior of each agent
     */
    public void determine_behaviour() {
        List<Turtle> vision = map.getNeighbors(patch);
        int cop_num = 0;
        int agent_num = 1;// + himself
        for (Turtle t : vision) {
            if (t.getClass().equals(Agent.class)) {
                if (((Agent) t).getActive()) {
                    agent_num++;
                }
            } else if (t.getClass().equals(Cop.class)) {
                cop_num++;
            }
        }
        double net_risk = risk_aversion * (1 - Math.exp(-Params.K * Math.floor(cop_num / agent_num)));
        double grievance = hardship * (1 - Params.GOVERNMENT_LEGITIMACY);
        if (grievance - net_risk > Params.THRESHOLD) {
            active = true;
        } else {
            active = false;
        }
    }

    //caught this agent
    public void caught() {
        Random random = new Random();
        jail_term = random.nextInt(Params.MAX_JAIL_TERM) + 1;
        active = false;
    }

    //check the status of the agent
    public boolean injail() {
        if (jail_term > 0) {
            return true;
        } else {
            return false;
        }
    }

    //reduce the jail term
    public void reduce_jail_term() {
        if (jail_term > 0) {
            jail_term--;
        }
    }

    private void setup() {
        Random random = new Random();
        this.risk_aversion = random.nextDouble();
        this.active = false;
        this.hardship = random.nextDouble();
        this.jail_term = 0;
    }
}
