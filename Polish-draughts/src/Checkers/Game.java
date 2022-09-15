package Checkers;

import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    private Board board;
    private int boardSize;
    private final String player1Color = Color.getPawnSymbol(1);
    private final String player2Color = Color.getPawnSymbol(2);;


    private final Scanner playerInput = new Scanner(System.in);


    public static void main(String[] args) {
        int round = 1;
        int playerNumber = 1;
        int enemyPlayerNumber = 2;

        Game game = new Game();
        game.start();
        while(!game.checkForWinner(enemyPlayerNumber,playerNumber)){
            playerNumber = round % 2 == 1 ? 1 : 2;
            enemyPlayerNumber = playerNumber == 1 ? 2 : 1;
            game.playRound(playerNumber);
            round++;
        }
        game.printGameResult(enemyPlayerNumber,playerNumber);

    }


    private void start(){
        clearConsole();
        System.out.println("Welcome to Checkers !");
        System.out.println("Please choose a board size between 10-20");
        int boardSize = 0;
        boolean isValidInput = false;
        while(!isValidInput){
            try {
                boardSize = playerInput.nextInt();
                playerInput.nextLine();
                isValidInput = true;
            }catch (InputMismatchException e){
                playerInput.nextLine();
                System.out.println("Invalid input !");
            }
        }

        while (boardSize<10 || boardSize>20){
            if(boardSize == 999 || boardSize == 998){
                break;
            }
            System.out.println("Wrong input, please insert a number between 10 and 20!");
            boardSize = playerInput.nextInt();
        }
        if(boardSize<=20){
            this.boardSize = boardSize;
            board = new Board(boardSize);
        }else{
            playerInput.nextLine();
        }

    }


    private void playRound(int player){
        clearConsole();
        board.printBoard();
        System.out.printf("Player %s's round! (%s )\n",player, player==1?player1Color:player2Color);
        System.out.println("Please select a pawn's position :");
        String input = playerInput.nextLine();
        while(!checkSelectInput(player, input)){
            input = playerInput.nextLine();
        }
        int[] selectedPawnPosition = convertInputToCoordinate(input);
        Pawn pawn = board.getFields()[selectedPawnPosition[0]][selectedPawnPosition[1]];
        TryToMakeMove(pawn);
        pawn.unselect();
    }


    private boolean checkForWinner(int enemyPlayer,int player){
        if(board.countPawns()==2){
            return checkForDraw(enemyPlayer, player);
        }else {
            return checkForVictory(enemyPlayer);
        }
    }
    private boolean checkForVictory(int enemyPlayer){
        for (int row = 0; row < boardSize; row++) {
            for(int col = 0; col < boardSize; col++){
                if(board.getColorFromCoordinate(row, col) == enemyPlayer) {
                    Pawn enemyPawn = board.getFields()[row][col];
                    boolean enemyCanMove = false;
                    if(board.canMove(enemyPawn) || board.canTakeEnemy(enemyPawn)){
                        enemyCanMove = true;
                    }
                    if(enemyCanMove){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean checkForDraw(int enemyPlayer, int player){
        return board.countKings(enemyPlayer) == 1 && board.countKings(player) == 1;
    }

    private void printGameResult(int enemyPlayer, int player){
        clearConsole();
        board.printBoard();
        if(board.countPawns()==2){
            if(board.countKings(enemyPlayer) == 1 && board.countKings(player) == 1){
                System.out.println("It's a Draw!");
            }else{
                System.out.printf("Player %s has won! (%s )\n",player, player==1?player1Color:player2Color);
            }
        }else{
            System.out.printf("Player %s has won! (%s )\n",player, player==1?player1Color:player2Color);
        }
    }

    private void TryToMakeMove(Pawn selectedPawn){
        selectedPawn.select();
        clearConsole();
        board.printBoard();
        System.out.println("Where do you want to move this pawn ?");
        String input = playerInput.nextLine();
        while(!checkNewPosition(input, selectedPawn, false)){
            System.out.println("Invalid position !");
            input = playerInput.nextLine();
        }
        int[] movePosition = convertInputToCoordinate(input);
        Coordinates coordinates = new Coordinates(movePosition[0],movePosition[1]);
        if (board.isMoveTake(selectedPawn, coordinates)) {
            board.movePawn(selectedPawn, coordinates);
            if(board.canTakeEnemy(board.getPawnByCoords(coordinates))){
                board.printBoard();
                TryToTakePiece(board.getPawnByCoords(coordinates));
            }
        }
        else {
            board.movePawn(selectedPawn, coordinates);
        }
    }

    private void TryToTakePiece(Pawn selectedPawn){
        clearConsole();
        board.printBoard();
        System.out.println("You can still take pieces!");
        Coordinates coordinates;
        String input = playerInput.nextLine();
        while (!checkNewPosition(input, selectedPawn, true)) {
            System.out.println("Invalid position !");
            input = playerInput.nextLine();
        }
        int[] movePosition = convertInputToCoordinate(input);
        coordinates = new Coordinates(movePosition[0], movePosition[1]);
        board.movePawn(selectedPawn, coordinates);
        if(board.canTakeEnemy(board.getPawnByCoords(coordinates))){
            TryToTakePiece(board.getPawnByCoords(coordinates));
        }
    }


    private boolean checkSelectInput(int player, String input){
        if (checkIfQuit(input)) System.exit(0);
        if (checkMoveInput(input)) return false;
        int[] Coord = convertInputToCoordinate(input);
        if(checkIfPlayerPawn(player, Coord)){
            Pawn pawn = board.getFields()[Coord[0]][Coord[1]];
            if(board.canMove(pawn) || board.canTakeEnemy(pawn)){
                return true;
            }else{
                System.out.println("You can't move with that pawn!");
                return false;
            }
        }else{
            System.out.println("Please select a pawn from yours!");
            return false;
        }
    }


    private boolean checkMoveInput(String playerInput) {
        if(playerInput.length() == 0){
            return true;
        }
        char letter = Character.toLowerCase(playerInput.charAt(0));
        int number;
        try {
            number = Integer.parseInt(playerInput.substring(1));
        }catch (NumberFormatException e){
            return true;
        }
        if(Character.isDigit(letter) || letter-'a'>boardSize){
            return true;
        }
        if(0>number || number>boardSize){
            System.out.println("Out of bounds!\n");
            return true;
        }
        return false;
    }


    private boolean checkNewPosition(String playerInput, Pawn pawn, boolean onlyTake){
        if (checkIfQuit(playerInput)) System.exit(0);
        if (checkMoveInput(playerInput)) return false;
        int[] movePosition = convertInputToCoordinate(playerInput);
        if(onlyTake){
            return board.checkMove(pawn, movePosition[0], movePosition[1]) && board.isMoveTake(pawn, new Coordinates(movePosition[0], movePosition[1]));
        }
        else return board.checkMove(pawn, movePosition[0], movePosition[1]);
    }


    private boolean checkIfPlayerPawn(int player, int[] playerCoord){
        return board.getColorFromCoordinate(playerCoord[0], playerCoord[1]) == player;
    }


    private int[] convertInputToCoordinate(String playerInput){
        int letter = Character.toLowerCase(playerInput.charAt(0))-'a';
        int number = Integer.parseInt(playerInput.substring(1))-1;
        return new int[]{letter, number};
    }



    private boolean checkIfQuit(String input){
        if(Objects.equals(input, "quit") || Objects.equals(input, "exit")){
            return true;
        }
        return false;
    }

    public static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
