package CheckersGame.game;

import java.util.ArrayList;

public class Board {
    private final int boardSize;
    private final Pawn[][] pawns;
    private final String bgColor = TerminalColors.WHITE_BACKGROUND_BRIGHT + "   " + TerminalColors.RESET;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        pawns = new Pawn[boardSize][boardSize];
        constructBoard(boardSize);
    }

    private void constructBoard(int boardSize) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (row % 2 != col % 2 && row < 4) {
                    pawns[row][col] = new Pawn(2, row, col);
                } else if (row % 2 != col % 2 && row > boardSize - 5) {
                    pawns[row][col] = new Pawn(1, row, col);
                }
            }
        }
    }


    public Board(Board board) {
        boardSize = board.boardSize;
        pawns = new Pawn[boardSize][boardSize];
        for (int i = 0; i < board.boardSize; i++) {
            for (int j = 0; j < board.boardSize; j++) {
                if (board.pawns[i][j] != null) {
                    pawns[i][j] = new Pawn(board.pawns[i][j]);
                }
            }
        }
    }

    public Pawn[][] getPawns() {
        return pawns;
    }

    public Pawn getPawnByCoords(Coordinates field) {
        return pawns[field.getX()][field.getY()];
    }

    public void printBoard() {
        String header = " ";
        for (int i = 1; i <= pawns.length; i++) {
            if (i < 11) {
                header = header.concat("  " + i);
            } else {
                header = header.concat(" " + i);
            }
        }
        System.out.println(header);

        int rowCounter = 0;
        int colCounter = 0;
        for (Pawn[] row : pawns) {
            System.out.print((char) ('A' + rowCounter));
            if (rowCounter % 2 == 1) {
                System.out.print(" ");
            }
            rowCounter++;
            for (Pawn pawn : row) {
                System.out.print(" ");
                if (pawn == null && (colCounter % 2 != rowCounter % 2)) {
                    System.out.print(bgColor);
                } else if (pawn == null && (colCounter % 2 == rowCounter % 2)) {
                    System.out.print(" ");
                } else {
                    System.out.print(pawn.getSymbol());
                }
                colCounter++;
            }
            System.out.print("\n");
        }
    }

    public boolean checkMove(Pawn pawn, int row, int col) {
        Coordinates newField = new Coordinates(row, col);
        Coordinates distance = pawn.getPosition().getDifference(newField);

        if (!isInBoard(newField) || !isFieldEmpty(newField)) {
            return false;
        } else if (distance.isSymmetric()) {
            if (pawn.getCrown()) {
                return checkKingMove(pawn, newField);
            } else {
                return checkPawnMove(pawn, newField, distance);
            }
        }
        return false;
    }

    public boolean isFieldEmpty(Coordinates field) {
        return pawns[field.getX()][field.getY()] == null;
    }

    public void removePawn(Coordinates field) {
        pawns[field.getX()][field.getY()] = null;
    }

    public void movePawn(Pawn pawn, Coordinates field) {
        Coordinates original = pawn.getPosition();
        pawns[pawn.getPosition().getX()][pawn.getPosition().getY()] = null;

        if (pawn.getCrown()) {
            Coordinates[] betweenFields = pawn.getPosition().getInBetweens(field);
            for (Coordinates middleField : betweenFields) {
                if (middleField != null) {
                    removePawn(middleField);
                }
            }
        } else {
            if (original.getDifference(field).howManyCell() == 2) {
                removePawn(original.getMiddle(field));
            }
            crownPawn(pawn, field);
        }

        pawn.setPosition(field.getX(), field.getY());
        pawns[field.getX()][field.getY()] = pawn;
    }

    public boolean isInBoard(Coordinates field) {
        return field.getX() < boardSize && field.getY() < boardSize && 0 <= field.getX() && 0 <= field.getY();
    }

    public boolean canTakeEnemy(Pawn pawn) {
        Coordinates[] neighbours = pawn.getPosition().getDiagNeighbours(2);
        for (Coordinates field : neighbours) {
            if (isInBoard(field) && isFieldEmpty(field)) {
                Coordinates middle = pawn.getPosition().getMiddle(field);
                if (pawn.isEnemy(pawns[middle.getX()][middle.getY()])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMove(Pawn pawn) {
        Coordinates[] neighbours = pawn.getPosition().getDiagNeighbours(1);
        for (Coordinates field : neighbours) {
            if (isInBoard(field) && isFieldEmpty(field)) {
                if (pawn.getCrown() || pawn.isCorrectDirection(pawn.getPosition().getDifference(field))) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getColorFromCoordinate(int x, int y) {
        boolean isPawn = pawns[x][y] != null;
        if (isPawn) {
            return pawns[x][y].getPlayerColor();
        }
        return 0;
    }

    public void crownPawn(Pawn pawn, Coordinates field) {
        if ((field.getX() == 0 && pawn.getPlayerColor() == 1) || (field.getX() == boardSize - 1 && pawn.getPlayerColor() == 2)) {
            if (!pawn.getCrown()) {
                pawn.setCrowned(true);
            }
        }
    }

    public boolean checkPawnMove(Pawn pawn, Coordinates newField, Coordinates distance) {
        if (pawn.isCorrectDirection(distance) || isMoveTake(pawn, newField)) {
            switch (distance.howManyCell()) {
                case 1:
                    return isFieldEmpty(newField);
                case 2:
                    Coordinates middleField = pawn.getPosition().getMiddle(newField);
                    if (pawns[middleField.getX()][middleField.getY()] == null) {
                        return false;
                    } else {
                        int middleColor = pawns[middleField.getX()][middleField.getY()].getPlayerColor();
                        return middleColor != pawn.getPlayerColor();
                    }
                default:
                    return false;
            }
        }
        return false;
    }

    public boolean checkKingMove(Pawn pawn, Coordinates newField) {
        Coordinates[] betweenFields = pawn.getPosition().getInBetweens(newField);
        int pawns = 0;
        if (betweenFields != null) {
            for (Coordinates field : betweenFields) {
                if (field != null) {
                    if (!isFieldEmpty(field)) {
                        pawns++;
                    }
                }
            }
        }
        return pawns <= 1;
    }

    public int countKings(int player) {
        ArrayList<Pawn> kings = selectPawns();
        kings.removeIf(pawn -> !pawn.getCrown() || pawn.getPlayerColor() != player);
        return kings.size();
    }

    public ArrayList<Pawn> selectPawns() {
        ArrayList<Pawn> pawns = new ArrayList<Pawn>();
        for (Pawn[] row : this.pawns) {
            for (Pawn pawn : row) {
                if (pawn != null) {
                    pawns.add(pawn);
                }
            }
        }
        return pawns;
    }

    public int countPawns() {
        return selectPawns().size();
    }

    public ArrayList<Pawn> selectEnemyPawns(int player) {
        ArrayList<Pawn> pawns = selectPawns();
        pawns.removeIf(pawn -> pawn.getCrown() || pawn.getPlayerColor() != player);
        return pawns;
    }

    public boolean isMoveTake(Pawn pawn, Coordinates newField) {
        Board tempBoard = new Board(this);
        int oldPawnNumber = tempBoard.countPawns();

        tempBoard.movePawn(tempBoard.getPawnByCoords(pawn.getPosition()), newField);
        int newPawnNumber = tempBoard.countPawns();
        return newPawnNumber < oldPawnNumber;
    }
}
