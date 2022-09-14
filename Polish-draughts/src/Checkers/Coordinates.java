package Checkers;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Coordinates getDifference(Coordinates coordinates){
        int x = this.x - coordinates.getX();
        int y = this.y - coordinates.getY();
        Coordinates diff =  new Coordinates(x, y);
        return  diff;
    }

    public boolean isSymmetric(){
        return Math.abs(x) == Math.abs(y);
    }

    public int howManyCell(){
        return Math.abs(Math.max(x, y));
    }

    public Coordinates getMiddle(Coordinates coordinates){
        int x = this.x + (coordinates.getX() - this.x)/2;
        int y = this.y + (coordinates.getY() - this.y)/2;
        return new Coordinates(x, y);
    }
}
