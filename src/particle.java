public class particle {
    public int xpos;
    public int ypos;
    public int charFrus;
    public int charIndex;

    public particle (int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
    }

    public int getxPos() {
        return xpos;
    }

    public void setxPos(int input) {
        this.xpos = input;
    }

    public int getyPos() {
        return ypos;
    }

    public void setyPos(int input) {
        this.ypos = input;
    }

    public int getCharFrus() {
        return charFrus;
    }

    public String toString(particle i) {
        return "x: " + i.getxPos() + " y: " + i.getyPos();
    }

    public void setCharFrus(int input) {
        this.charFrus = input;
    }

}
