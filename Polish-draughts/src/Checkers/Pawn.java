package Checkers;


public class Pawn {
    private Color color;
    private Coordinates position;
    private boolean isCrowned = false;
    private boolean isSelected = false;

    public Pawn(int player, int row, int col){
        color = new Color(player);
        position = new Coordinates(row, col);
    }

    public Pawn(int player, int row, int col, boolean isCrowned){
        color = new Color(player, isCrowned);
        position = new Coordinates(row, col);
        this.isCrowned = isCrowned;
    }


    public Pawn(Pawn pawn){
        color = pawn.color;
        position = pawn.position;
        isCrowned = pawn.isCrowned;
    }

    public int getPlayerColor() {
        return color.getPlayerColor();
    }

    public String getSymbol(){
        return color.getSymbol();
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public void setPosition(int row, int col){
        this.position = new Coordinates(row, col);
    }

    public void setCrowned(boolean crowned) {
        isCrowned = crowned;
        color = new Color(color.getPlayerColor(), crowned);
    }

    public boolean getCrown(){
        return isCrowned;
    }

    public boolean isCorrectDirection(Coordinates newPos){
        if(color.getPlayerColor() == 1 && newPos.isUp()){
            return true;
        }
        return color.getPlayerColor() == 2 && !newPos.isUp();
    }

    public boolean isEnemy(Pawn pawn){
        return pawn != null && getPlayerColor() != pawn.getPlayerColor();
    }

    public void select(){
        color.changeColor("yellow");
    }

    public void unselect(){
        color.resetColor();
    }
}

