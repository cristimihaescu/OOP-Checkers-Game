package Checkers;

import java.util.Arrays;
import java.util.Scanner;


public class Game {

    private Board board;
    private int boardSize;

    private final Scanner playerInput = new Scanner(System.in);

    public static void main(String[] args) {
        int round = 1;
        Game game = new Game();
        game.start();
        while (!game.checkForWinner()) {
            int playerNumber = round % 2 == 1 ? 1 : 2;
            game.playRound(playerNumber);
            round++;
        }
    }

    private void start() {
        System.out.println("Welcome to Checkers !");
        System.out.println("Choose a board size between 10-20 !");
        int boardSize = playerInput.nextInt();
        playerInput.nextLine();
        while (boardSize < 10 || boardSize > 20) {
            System.out.println("Insert a size between 10-20 !");
            boardSize = playerInput.nextInt();
        }
        this.boardSize = boardSize;
        board = new Board(boardSize);
        board.printBoard();
    }

    private void playRound(int player) {
        System.out.printf("Player %s's turn !\n", player);
        System.out.println("Type a pawn's position !");
        String input = playerInput.nextLine();
        while (!checkSelectInput(player, input)) {
            input = playerInput.nextLine();
        }
        int[] selectedPawnPosition = convertInputToCoordinate(input);
        Coordinates coordinates = new Coordinates(selectedPawnPosition[0], selectedPawnPosition[1]);
    }

    private boolean checkForWinner() {
        return false;
    }

    private boolean checkSelectInput(int player, String playerInput) {
        char letter = Character.toLowerCase(playerInput.charAt(0));
        int number;
        try {
            number = Integer.parseInt(playerInput.substring(1));
        } catch (NumberFormatException e) {
            return false;
        }
        if (Character.isDigit(letter) || (letter -'a') > boardSize) {
            return false;
        }
        if (0>number || number> boardSize) {
            System.out.println("Out of bounds !\n");
            return false;
        }
        int[] Coord = convertInputToCoordinate(playerInput);
        if (checkIfPlayerPawn(player, Coord)) {
            return true;
        } else {
            System.out.println("Select a pawn from yours !");
            return false;
        }
        private boolean checkIfPlayerPawn ( int player, int[] playerCoord){
            try {
                board.getFields()[playerCoord[0]][playerCoord[1]].getColor();
            } catch (NullPointerException e) {
                return false;
            }
            if(board.getFields()[playerCoord[0]][playerCoord[1]].getColor() == player){
                return true;
            }
            return false;
        }

        private int[] convertInputToCoordinate (String playerInput){
            int letter = Character.toLowerCase(playerInput.charAt(0)) - 'a';
            int number = Integer.parseInt(playerInput.substring(1)) - 1;
            return new int[]{letter, number};
        }
    }
