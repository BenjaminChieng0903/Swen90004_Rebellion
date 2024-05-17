public abstract class Turtle {
    SystemMap map;
    Patch patch;

    public void setPatch(Patch patch) {
        this.patch = patch;
    }

    public Turtle(SystemMap map){
        this.map = map;
    }

    public abstract void move();
}
