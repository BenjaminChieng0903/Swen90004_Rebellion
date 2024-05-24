import java.util.ArrayList;
import java.util.List;

public class Patch {
    private ArrayList<Turtle> content;
    int x, y;

    //constructor
    public Patch() {
        content = new ArrayList<Turtle>();
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ArrayList<Turtle> getContent() {
        return content;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean empty() {
        for (Turtle t : content) {
            if (t.getClass().equals(Agent.class)) {
                if (!((Agent) t).injail()) {
                    return false;
                }
            }
            if (t.getClass().equals(Cop.class)) {
                return false;
            }
        }
        return true;
    }

    public void moveToNewPatch(Turtle t, Patch newPatch) {
        content.remove(t);
        newPatch.add(t);
        t.setPatch(newPatch);
    }

    public void add(Turtle t) {
        content.add(t);
    }

    //get all turtles in the patch
    public List<Turtle> getTurtle() {
        return content;
    }
}
