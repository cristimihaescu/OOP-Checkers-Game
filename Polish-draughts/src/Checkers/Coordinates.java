package Checkers;

public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates getDifference(Coordinates coordinates){
        int x = coordinates.x - this.x;
        int y = coordinates.y - this.y;
        return new Coordinates(x, y);
    }

    public boolean isSymmetric(){
        return Math.abs(x) == Math.abs(y);
    }

    public int howManyCell(){
        return Math.abs(Math.max(x, y));
    }

    public Coordinates getMiddle(Coordinates coordinates){
        int x = this.x + (coordinates.x - this.x)/2;
        int y = this.y + (coordinates.y - this.y)/2;
        return new Coordinates(x, y);
    }

    public Coordinates getNormal(){
        return new Coordinates(x/Math.abs(x), y/Math.abs(y));
    }

    public boolean isUp(){
        return x < 0;
    }

    public Coordinates[] getDiagNeighbours(int distance){
        Coordinates[] neighbours = new Coordinates[4];
        neighbours[0] = new Coordinates(x-distance, y-distance);
        neighbours[1] = new Coordinates(x-distance, y+distance);
        neighbours[2] = new Coordinates(x+distance, y+distance);
        neighbours[3] = new Coordinates(x+distance, y-distance);

        return neighbours;
    }

    public Coordinates[] getInBetweens(Coordinates coordinates){
        int length = getDifference(coordinates).howManyCell();
        if(length != 0) {
            Coordinates direction = getDifference(coordinates).getNormal();
            Coordinates[] between = new Coordinates[length];
            for (int i = 1; i < length; i++) {
                between[i - 1] = new Coordinates(x + i * direction.x, y + i * direction.y);
            }
            return between;
        }
        return new Coordinates[0];
    }
}
