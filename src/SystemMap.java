import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SystemMap {
    private List<Patch> grid;
    private List<Agent> agents;
    private List<Cop> cops;
    public int mapsize = Params.MAP_SIZE;
    int patches_num = mapsize * mapsize;
    private int COP_AMOUNT = (int) (Params.INITIAL_COP_DENSITY * patches_num);
    private int AGENT_AMOUNT = (int) (Params.INITIAL_AGENT_DENSITY * patches_num);

    /**
     * create map with patches
     */
    public SystemMap() {
        grid = new ArrayList<Patch>(patches_num);
        for (int i = 0; i < patches_num; i++) {
            Patch patch = new Patch();
            patch.setXY(i/mapsize, i%mapsize);
//            System.out.println(i+","+i/mapsize+","+ i%mapsize);
            grid.add(patch);
        }
        if (Params.INITIAL_AGENT_DENSITY + Params.INITIAL_COP_DENSITY <= 1) {
            agents = new ArrayList<Agent>(AGENT_AMOUNT);
            cops = new ArrayList<Cop>(COP_AMOUNT);
            for (int i = 0; i < AGENT_AMOUNT; i++) {
                Agent agent = new Agent(this);
                agents.add(agent);
            }
            for (int i = 0; i < COP_AMOUNT; i++) {
                Cop cop = new Cop(this);
                cops.add(cop);
            }
        } else {
            throw new StackOverflowError();
        }
    }

    /**
     * put agents and cops into patches randomly
     */
    public void setup() {
        Random random = new Random();
        for (Cop cop : cops){
            var randomNum = random.nextInt(patches_num - 1);
            /**
             * if patch is empty, means the case can be taken
             */
            while(!grid.get(randomNum).getTurtle().isEmpty()) {
                randomNum = random.nextInt(patches_num - 1);
            }
            /**
             * adding cop in empty patch, stored in turtle list.
             */
            grid.get(randomNum).add(cop);
            /**
             * Patch is also attached to turtle
             */
            cop.setPatch(grid.get(randomNum));

        }

        for (Agent agent : agents){
            var randomNum = random.nextInt(patches_num - 1);
            while(!grid.get(randomNum).getTurtle().isEmpty()) {
                randomNum = random.nextInt(patches_num - 1);
            }
            grid.get(randomNum).add(agent);
            agent.setPatch(grid.get(randomNum));
        }
        System.out.println("Setup has been finished!");
    }

    public static int[] getRandom(int Width, int Height){
        Random random = new Random();
        int randomX = random.nextInt(Width);
        int randomY = random.nextInt(Height);
        int[] randomPair = {randomX, randomY};
        return randomPair;
    }

    /**
     * the turtles' changes for each tick
     */
    public void step() {
        Collections.shuffle(agents);
        for (Agent agent : agents) {
            agent.move();
            agent.reduce_jail_term();
            if(!agent.injail()){
                agent.determine_behaviour();
            }
        }
        Collections.shuffle(cops);
        for (Cop cop : cops){
            cop.move();
            cop.enforce();
        }
    }

    /**
     * Get all patches in vision radius
     * @param patch
     * @return list of patch
     */
    public ArrayList<Patch> patchesInVision(Patch patch) {
        int x = patch.getX();
        int y = patch.getY();
        ArrayList<Patch> patches = new ArrayList<Patch>();
        double radius = Params.VISION;
        for (int i = 0; i < radius; i++) {
            for (int j = 0; j*j < radius*radius - i*i; j++) {
                if (i == 0 && j == 0) {continue;}
                patches.add(getPatch(x + i, y + j));
                patches.add(getPatch(x + i, y - j));
                patches.add(getPatch(x - i, y + j));
                patches.add(getPatch(x - i, y - j));
//				System.out.println(x+","+y+","+i+","+j);
//				System.out.println("--------------");
            }
        }
        return patches;
    }

    /**
     * Return a random empty patch in vision
     * @param patch
     * @return
     */
    public Patch findEmpty(Patch patch) {
        ArrayList<Patch> patches = patchesInVision(patch);
        Random random = new Random();
        while (!patches.isEmpty()){
            int i = random.nextInt(patches.size() - 1);
            /**
             * if a random patch in vision radius is empty, return
             */
            if (patches.get(i).empty()) {
                return patches.get(i);
            } else {
                patches.remove(i);
            }
        }
        return null;
    }

    /**
     * Return all neighbors
     * @param patch
     * @return
     */
    public List<Turtle> getNeighbors(Patch patch) {
        ArrayList<Patch> patches = patchesInVision(patch);
        List<Turtle> neighbors = new ArrayList<>();
        for (Patch p : patches) {
            neighbors.addAll(p.getTurtle());
        }
        return neighbors;
    }

    public Patch getPatch(int x, int y) {
        int x1 = (x + mapsize) % mapsize;
        int y1 = (y + mapsize) % mapsize;
        return grid.get(x1 * mapsize + y1);
    }

    public String counter(){
        int jailed = 0;
        int quiet = 0;
        int active = 0;
        for(Agent a:agents){
            if(a.getActive()){
                active++;
            }else if(a.injail()){
                jailed++;
            }else{
                quiet++;
            }
        }
        return quiet+","+active+","+jailed;
    }
}
