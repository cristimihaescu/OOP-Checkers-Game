package Checkers;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Objects;


public class Game {

    private Board board;
    private int boardSize;

    private final Scanner playerInput = new Scanner(System.in);

    public static void main(String[] args) {
        int round = 1;
        int enemyPlayer =2;
        Game game = new Game();
        game.start();
        while(game.checkForWinner(enemyPlayer)) {
            int playerNumber = round % 2 == 1 ? 1 : 2;
            enemyPlayer = playerNumber == 1 ? 2 : 1;
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
    }

    private void playRound(int player) {
        board.printBoard();
        System.out.printf("Player %s's turn !\n", player);
        System.out.println("Type a pawn's position !");
        String input = playerInput.nextLine();
        while (!checkSelectInput(player, input)) {
            input = playerInput.nextLine();
        }
        int[] selectedPawnPosition = convertInputToCoordinate(input);
        System.out.println(Arrays.toString(selectedPawnPosition));
        Pawn pawn = board.getFields()[selectedPawnPosition[0]][selectedPawnPosition[1]];
        TryToMakeMove(pawn);
    }

    private boolean checkForWinner(int enemyPlayer){
        for (int row = 0; row < boardSize; row++) {
            for(int col = 0; col < boardSize; col++){
                if(board.getColorFromCoordinate(row, col) == enemyPlayer) {
                    return true;
                }
            }
        }
        return false;
    }

    private void TryToMakeMove(Pawn selectedPawn){
        System.out.println("Where do you want to move this pawn ?");
        String input = playerInput.nextLine();
        while(!checkIfQuit(input) || !checkNewPosition(input, selectedPawn)){
            System.out.println("Invalid position !");
            input = playerInput.nextLine();
        }
        int[] movePosition = convertInputToCoordinate(input);
        Coordinates coordinates = new Coordinates(movePosition[0],movePosition[1]);
        board.movePawn(selectedPawn, coordinates);
    }

    private boolean checkSelectInput(int player, String input){
        if (checkIfQuit(input)) System.exit(0);
        if (checkMoveInput(input)) return false;
        int[] Coord = convertInputToCoordinate(input);
        if(checkIfPlayerPawn(player, Coord)){
            Pawn pawn = board.getFields()[Coord[0]][Coord[1]];
            if(board.canMove(pawn)){
                return true;
            }else{
                System.out.println("You can't move that pawn!");
                return false;
            }
        }else{
            System.out.println("Please select a pawn from yours!");
            return false;
        }
    }
    private boolean checkMoveInput(String playerInput){
        char letter = Character.toLowerCase(playerInput.charAt(0));
        int number;
        try {
            number = Integer.parseInt(playerInput.substring(1));
        } catch (NumberFormatException e) {
            return true;
        }
        if (Character.isDigit(letter) || (letter -'a') > boardSize) {
            return true;
        }
        if (0>number || number> boardSize) {
            System.out.println("Out of bounds !\n");
            return true;
        }
        return false;
    }
    private boolean checkNewPosition(String playerInput, Pawn pawn){
        if (checkIfQuit(playerInput)) System.exit(0);
        if (checkMoveInput(playerInput)) return false;
        int[] movePosition = convertInputToCoordinate(playerInput);
        return board.checkMove(pawn, movePosition[0], movePosition[1]);
    }
    private boolean checkIfPlayerPawn(int player, int[] playerCoord){
        return board.getColorFromCoordinate(playerCoord[0], playerCoord[1]) == player;
    }


        private int[] convertInputToCoordinate (String playerInput){
            int letter = Character.toLowerCase(playerInput.charAt(0)) - 'a';
            int number = Integer.parseInt(playerInput.substring(1)) - 1;
            return new int[]{letter, number};
        }

    private boolean checkIfQuit(String input){
        if(Objects.equals(input, "quit") || Objects.equals(input, "exit")){
            return true;
        }
        return false;
    }
    }
