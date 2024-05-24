import java.util.List;
import java.util.Random;

/**
 * @author Jingning Qian, Baorui Chen
 */
public class Agent extends Turtle {
    protected double risk_aversion;
    protected double hardship;
    protected boolean active;
    protected boolean is_dead;
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
        if(this.patch != null) {
            Patch newPatch = map.findEmpty(this.patch);
            if (jail_term == 0 && newPatch != null) {
                patch.moveToNewPatch(this, newPatch);
            }
        }
    }

    public boolean getActive() {
        return active;
    }

    /**
     * set the behavior of each agent
     */
    public void determine_behaviour() {
        if (this.patch != null) {
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
    }

    //caught this agent
    public void caught() {
        Random random = new Random();
        int severity_level = random.nextInt(100);
        Params.SEVERITY Severity;
        if (severity_level >= 0 && severity_level <= 49){
            Severity = Params.SEVERITY.LOW;
        }else if (severity_level > 49 && severity_level <=89){
            Severity = Params.SEVERITY.MEDIUM;
        }else{
            Severity = Params.SEVERITY.HIGH;
        }
        switch (Severity){
            case LOW: this.jail_term = 30; break;
            case MEDIUM: this.jail_term = 60; break;
            case HIGH: {
                /**
                 * simulate the agent was killed by the cop if their severity considered to high
                 */
                this.patch.getContent().remove(this);
                this.is_dead = true;
                break;
            }
            default: this.jail_term = 0; break;
        }
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
