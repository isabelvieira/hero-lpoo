import com.googlecode.lanterna.graphics.TextGraphics;

public abstract class Element {

    protected Position position;

    Element(int x, int y){
        position = new Position(x,y);
    }

    public void setX(int x) {
        position.setX(x);
    }

    public void setY(int y) {
        position.setY(y);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setPosition(Position position){
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    public Position getPosition() {
        return position;
    }

    public abstract void draw(TextGraphics graphics);

}
