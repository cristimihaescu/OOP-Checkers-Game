package Checkers;

import java.util.Arrays;
import static java.util.Objects.isNull;

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


    public Pawn[][] getFields() {
        return fields;
    }
    public void printBoard() {
        String header = " ";
        for (int i = 1; i <=fields.length ; i++) {
            if(i<10){
                header = header.concat("  "+i);
            }else{
                header = header.concat(" "+i);
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
                    if (pawn.getColor() == 1 && !pawn.getCrown()){
                        System.out.print("W");
                    }if(pawn.getColor() == 2 && !pawn.getCrown()){
                        System.out.print("B");
                    }else if(pawn.getColor() == 1 && pawn.getCrown()){
                        System.out.println("⛃");
                    }else if(pawn.getColor() == 2 && pawn.getCrown()){
                        System.out.println("⛁");
                    }
                }
            }
            System.out.print("\n");
        }
    }

    public boolean checkMove(Pawn pawn, int row, int col){
        Coordinates newField = new Coordinates(row, col);
        Coordinates distance = pawn.getPosition().getDifference(newField);


        if (!isInBoard(newField)) {
            return false;
        }
        else if (!isFieldEmpty(newField)) {
            return false;
        }
        else if (distance.isSymmetric()) {
            if (pawn.getCrown()) {
                return checkKingMove(pawn, newField);
            }
            else return checkPawnMove(pawn, newField, distance);
        }
        return false;
    }

    public boolean isFieldEmpty(Coordinates field) {
        return fields[field.getX()][field.getY()] == null;
    }

    public void removePawn(Coordinates field) {
        fields[field.getX()][field.getY()] = null;
    }

    public void movePawn(Pawn pawn, Coordinates field) {
        Coordinates original = pawn.getPosition();
        fields[pawn.getPosition().getX()][pawn.getPosition().getY()] = null;
        if (pawn.getCrown()) {
            Coordinates[] betweenFields = pawn.getPosition().getInBetweens(field);
            for (Coordinates middleField : betweenFields) {
                fields[middleField.getX()][middleField.getY()] = null;
            }
        } else {
            if (original.getDifference(field).howManyCell() == 2) {
                removePawn(original.getMiddle(field));
            }
            crownPawn(pawn, field);
        }
        pawn.setPosition(field.getX(), field.getY());
        fields[field.getX()][field.getY()] = pawn;
        }

        public boolean isInBoard (Coordinates field){
            return field.getX() < boardSize && field.getY() < boardSize && 0 <= field.getX() && 0 <= field.getY();
        }
        public boolean canTakeEnemy (Pawn pawn){
            Coordinates[] neighbours = pawn.getPosition().getDiagNeighbours(2);
            for (Coordinates field : neighbours) {
                if (isInBoard(field)) {
                    if (isFieldEmpty(field)) {
                        Coordinates middle = pawn.getPosition().getMiddle(field);
                        if (pawn.isEnemy(fields[middle.getX()][middle.getY()])) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public boolean canMove (Pawn pawn){
            Coordinates[] neighbours = pawn.getPosition().getDiagNeighbours(1);
            for (Coordinates field : neighbours) {
                if (isInBoard(field)) {
                    if (isFieldEmpty(field)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean canMove ( int[] coords){
            Pawn pawn = fields[coords[0]][coords[1]];
            Coordinates[] neighbours = pawn.getPosition().getDiagNeighbours(1);
            for (Coordinates field : neighbours) {
                if (isInBoard(field)) {
                    if (isFieldEmpty(field)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int getColorFromCoordinate ( int x, int y){
            boolean isPawn = fields[x][y] != null;
            if (isPawn) {
                return fields[x][y].getColor();
            } else {
                return 0;
            }
        }

        public void crownPawn (Pawn pawn, Coordinates field){
            if (field.getX() == 0 || field.getX() == boardSize - 1) {
                if (!pawn.getCrown()) {
                    pawn.setCrowned(true);
                }
            }
        }

    public boolean checkPawnMove(Pawn pawn, Coordinates newField, Coordinates distance) {
        if (pawn.isCorrectDirection(distance)) {
            switch (distance.howManyCell()) {
                case 1:
                    return isFieldEmpty(newField);
                case 2:
                    Coordinates middleField = pawn.getPosition().getMiddle(newField);
                    if (fields[middleField.getX()][middleField.getY()] == null) {
                        return false;
                    } else {
                        int middleColor = fields[middleField.getX()][middleField.getY()].getColor();
                        return middleColor != pawn.getColor();
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    public boolean checkKingMove(Pawn pawn, Coordinates newField){
        Coordinates[] betweenFields = pawn.getPosition().getInBetweens(newField);
        int pawns = 0;
        for (Coordinates field : betweenFields){
            if(!isFieldEmpty(field)){
                pawns++;
            }
        }
        return pawns <= 1;
    }
}


