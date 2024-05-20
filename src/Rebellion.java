import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Rebellion {
    int tick;
    boolean running;
    SystemMap map;
    protected static PrintWriter f;

    public Rebellion() {
        map = new SystemMap();
        map.setup();
        tick = 0;
        //f.println(map.counter());
        try {
            f = new PrintWriter("result.csv");
            f.println("quiet,active,jailed");
            f.println(map.counter());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void runRebellion(int num) {
        if (!running) {
            running = true;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (running) {
                        map.step();
                        tick++;
                        f.println(map.counter());
                        if (tick == num) {
                            System.out.println("Finished "+ num + " ticks!");
                            f.close();
                            running = false;
                        }
                    }
                }
            });
            t.start();
        }else{
            running = false;
        }
    }
}
