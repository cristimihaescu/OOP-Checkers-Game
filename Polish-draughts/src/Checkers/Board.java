package Checkers;

import java.util.Arrays;

public class Board {

    private int boardSize;
    private Pawn[][] fields;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        fields = new Pawn[boardSize][boardSize];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (row % 2 != col % 2) {
                    fields[row][col] = new Pawn(2, row, col);
                }
            }
        }
        for (int row = boardSize - 4; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (row % 2 != col % 2) {
                    fields[row][col] = new Pawn(1, row, col);
                }
            }
        }
    }

    public Pawn[][]getFields(){
        return fields;
    }

    public void printBoard() {
        String header = " ";
        for (int i = 1; i <= fields.length; i++) {
            if (i < 10) {
                header = header.concat("  " + i);
            } else {
                header = header.concat(" " + i);
            }
        }
        System.out.println(header);
        char rowIndex = 'A';

        for (Pawn[] row : fields) {
            System.out.print(rowIndex);
            rowIndex++;
            for (Pawn pawn : row){
                System.out.print("  ");
                if(pawn == null){
                    System.out.print(".");
                }
                else {
                    if (pawn.getColor() == 1){
                        System.out.print("W");
                    }else{
                        System.out.print("B");
                    }

                }
            }
            System.out.print("\n");
        }
    }

    public boolean checkMove(Pawn pawn, int row, int col){
        Coordinates newField = new Coordinates(row, col);
        Coordinates distance = pawn.getPosition().getDifference(newField);

        if(row >= boardSize || col >=boardSize || 0 > row || 0 > col){
            return false;
        }
        else if(!isFieldEmpty(newField)){
            return false;
        }
        else if(distance.isSymmetric()){
            switch(distance.howManyCell()){
                case 1:
                    return isFieldEmpty(newField);
                case 2:
                    Coordinates middleField = pawn.getPosition().getMiddle(newField);
                    if(fields[middleField.getX()][middleField.getY()]== null){
                        return false;
                    }
                    else{
                        int middleColor = fields[middleField.getX()][middleField.getY()].getColor();
                        return middleColor != pawn.getColor();
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    public boolean isFieldEmpty(Coordinates field){
        return fields[field.getX()][field.getY()] == null;
    }

    public void removePawn(Coordinates field){
        Coordinates original = pawn.getPosition();
        fields[pawn.getPosition().getX()][pawn.getPosition().getY()] = null;
        pawn.setPosition(field.getX(), field.getY());
        fields[field.getX()][field.getY()] = pawn;

        if(original.getDifference(field).howManyCell() == 2){
            removePawn(original.getMiddle(field));
        }
    }
}
