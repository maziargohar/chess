import java.util.*;

import static java.lang.Math.abs;

class scoreboard {
    String name;
    int score = 0;
    int wins = 0;
    int losses = 0;
    int draws = 0;

    scoreboard(String name) {
        this.name = name;
    }

}

class scoreboardComparator implements Comparator<scoreboard> {
    @Override
    public int compare(scoreboard a, scoreboard b) {
        if (b.score - a.score != 0) { // if scores are different
            return b.score - a.score;
        } else { // if scores are same
            if (b.wins - a.wins != 0) { // if wins are different
                return b.wins - a.wins;
            } else { // if wins are same
                if (b.draws - a.draws != 0) { // if draws are different
                    return b.draws - a.draws;
                } else { // if draws are same
                    if (b.losses - a.losses != 0) { // if losses are different
                        return a.losses - b.losses;
                    } else { // if losses are same
                        return a.name.compareTo(b.name);
                    }
                }
            }
        }
    }
}
public class chess {
    public static TreeMap<String,String> passwords = new TreeMap<>();
    public static ArrayList<scoreboard> scores = new ArrayList<>();
    public static String WhitePlayer , BlackPlayer;

    public static int limits = 0;
    public static boolean hasLimit = true;

    public static int WhiteUndoCount = 2 , BlackUndoCount = 2;

    public static String[][] board = {
            {"Rb","Nb","Bb","Qb","Kb","Bb","Nb","Rb"},
            {"Pb","Pb","Pb","Pb","Pb","Pb","Pb","Pb"},
            {"  ","  ","  ","  ","  ","  ","  ","  "},
            {"  ","  ","  ","  ","  ","  ","  ","  "},
            {"  ","  ","  ","  ","  ","  ","  ","  "},
            {"  ","  ","  ","  ","  ","  ","  ","  "},
            {"Pw","Pw","Pw","Pw","Pw","Pw","Pw","Pw"},
            {"Rw","Nw","Bw","Qw","Kw","Bw","Nw","Rw"}
    };
    public static String[][] backupboard = new String[8][8];
    public static String backupSelection , backupInputSelection;
    public static boolean isSelected;
    public static boolean isWhiteTurn = true;
    public static String selectedPosition , inputSelectedPosition;
    public static LinkedList<String> allMoves = new LinkedList<>();
    public static LinkedList<String> whiteMoves = new LinkedList<>();
    public static LinkedList<String> blackMoves = new LinkedList<>();
    public static LinkedList<String> allKilled = new LinkedList<>();
    public static LinkedList<String> whiteKilled = new LinkedList<>();
    public static LinkedList<String> blackKilled = new LinkedList<>();
    public static boolean isMoved = false;

    public static void reset(){
        board = new String[][]{
                {"Rb", "Nb", "Bb", "Qb", "Kb", "Bb", "Nb", "Rb"},
                {"Pb", "Pb", "Pb", "Pb", "Pb", "Pb", "Pb", "Pb"},
                {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
                {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
                {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
                {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
                {"Pw", "Pw", "Pw", "Pw", "Pw", "Pw", "Pw", "Pw"},
                {"Rw", "Nw", "Bw", "Qw", "Kw", "Bw", "Nw", "Rw"}
        };
         backupboard = new String[8][8];
         selectedPosition = "";
         allMoves = new LinkedList<>();
         whiteMoves = new LinkedList<>();
         blackMoves = new LinkedList<>();
         allKilled = new LinkedList<>();
         whiteKilled = new LinkedList<>();
         blackKilled = new LinkedList<>();
         BlackPlayer = "" ;
        isWhiteTurn = true;
        hasLimit = true;
        WhiteUndoCount = 2 ; BlackUndoCount = 2;
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FirstMenu.start(input);
    }

    public static boolean isNotValidPosition(String position) {
        String[] positions = position.split(",");
        return Integer.parseInt(positions[0]) > 8 || Integer.parseInt(positions[0]) < 1 || Integer.parseInt(positions[1]) > 8 || Integer.parseInt(positions[1]) < 1;
    }

    public static boolean isWhitePiece(String position) {
        String[] positions = position.split(",");
        if (board[Integer.parseInt(positions[0])-1][Integer.parseInt(positions[1])-1].equals("  "))
            return false;
        return board[Integer.parseInt(positions[0])-1][Integer.parseInt(positions[1])-1].charAt(1) == 'w';
    }

    public static boolean isBlackPiece(String position) {
        String[] positions = position.split(",");
        if (board[Integer.parseInt(positions[0])-1][Integer.parseInt(positions[1])-1].equals("  "))
            return false;
        return board[Integer.parseInt(positions[0])-1][Integer.parseInt(positions[1])-1].charAt(1) == 'b';
    }

    public static boolean isValidMove(String selectedPosition, String position) {
        String selectedPiece = board[Integer.parseInt(selectedPosition.split(",")[0])-1][Integer.parseInt(selectedPosition.split(",")[1])-1];
        switch (selectedPiece.charAt(0)) {
            case 'P':
                return isValidPawnMove(selectedPosition, position);
            case 'R':
                return isValidRookMove(selectedPosition, position);
            case 'N':
                return isValidKnightMove(selectedPosition, position);
            case 'B':
                return isValidBishopMove(selectedPosition, position);
            case 'Q':
                return isValidQueenMove(selectedPosition, position);
            case 'K':
                return isValidKingMove(selectedPosition, position);
            default:
                return false;
        }
    }

    private static boolean isValidKingMove(String selectedPosition, String position) {
        String[] positions = position.split(",");
        int x = Integer.parseInt(positions[0]);
        int y = Integer.parseInt(positions[1]);
        int selectedX = Integer.parseInt(selectedPosition.split(",")[0]);
        int selectedY = Integer.parseInt(selectedPosition.split(",")[1]);
        return abs(x - selectedX) <= 1 && abs(y - selectedY) <= 1;
    }

    private static boolean isValidQueenMove(String selectedPosition, String position) {
        return isValidBishopMove(selectedPosition, position) || isValidRookMove(selectedPosition, position);
    }

    private static boolean isValidBishopMove(String selectedPosition, String position) {
        String[] positions = position.split(",");
        int x = Integer.parseInt(positions[0]) - 1 ;
        int y = Integer.parseInt(positions[1]) - 1 ;
        int x1 = Integer.parseInt(selectedPosition.split(",")[0]) - 1 ;
        int y1 = Integer.parseInt(selectedPosition.split(",")[1]) - 1 ;

        if(abs(x1 - x) == abs(y1 - y)) {
            if(x1 > x && y1 > y) {
                for(int i = 1; i < abs(x1 - x); i++) {
                    if(!board[x1-i][y1-i].equals("  ")) {
                        return false;
                    }
                }
            } else if(x1 > x && y1 < y) {
                for(int i = 1; i < abs(x1 - x); i++) {
                    if(!board[x1-i][y1+i].equals("  ")) {
                        return false;
                    }
                }
            } else if(x1 < x && y1 > y) {
                for(int i = 1; i < abs(x1 - x); i++) {
                    if(!board[x1+i][y1-i].equals("  ")) {
                        return false;
                    }
                }
            } else if(x1 < x && y1 < y) {
                for(int i = 1; i < abs(x1 - x); i++) {
                    if(!board[x1+i][y1+i].equals("  ")) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isValidKnightMove(String selectedPosition, String position) {
        String[] positions = position.split(",");
        int x = Integer.parseInt(positions[0]);
        int y = Integer.parseInt(positions[1]);
        int selectedX = Integer.parseInt(selectedPosition.split(",")[0]);
        int selectedY = Integer.parseInt(selectedPosition.split(",")[1]);
        return (abs(x-selectedX) == 2 && abs(y-selectedY) == 1) || (abs(x-selectedX) == 1 && abs(y-selectedY) == 2);
    }

    private static boolean isValidRookMove(String selectedPosition, String position) {
        String[] positions = position.split(",");
        int x = Integer.parseInt(positions[0]);
        int y = Integer.parseInt(positions[1]);
        int selectedX = Integer.parseInt(selectedPosition.split(",")[0]);
        int selectedY = Integer.parseInt(selectedPosition.split(",")[1]);
        if (x == selectedX) {
            if (y > selectedY) {
                for (int i = selectedY+1; i < y; i++) {
                    if (board[selectedX-1][i-1].charAt(0) != ' ') {
                        return false;
                    }
                }
            } else {
                for (int i = selectedY-1; i > y; i--) {
                    if (board[selectedX-1][i-1].charAt(0) != ' ') {
                        return false;
                    }
                }
            }
        } else if (y == selectedY) {
            if (x > selectedX) {
                for (int i = selectedX+1; i < x; i++) {
                    if (board[i-1][selectedY-1].charAt(0) != ' ') {
                        return false;
                    }
                }
            } else {
                for (int i = selectedX-1; i > x; i--) {
                    if (board[i-1][selectedY-1].charAt(0) != ' ') {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private static boolean isValidPawnMove(String selectedPosition, String position) {
        String[] positions = position.split(",");
        if (isWhitePiece(selectedPosition)) {
            if ((Integer.parseInt(positions[0]) == (Integer.parseInt(selectedPosition.split(",")[0]) - 1)) && //move forward
                    (Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1])) &&
                    !isWhitePiece(position) && !isBlackPiece(position)) {
                return true;
            } else if ((Integer.parseInt(positions[0]) == (Integer.parseInt(selectedPosition.split(",")[0]) - 1)) && //attack forward
                    ((Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1]) + 1) ||
                            (Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1]) - 1))
                    && isBlackPiece(position)) {
                return true;
            } else return ((Integer.parseInt(positions[0]) == 5 )&&( (Integer.parseInt(selectedPosition.split(",")[0]) == 7)) && //first double move forward
                    (Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1])) &&
                    !isWhitePiece(position) && !isBlackPiece(position)) ;
        }
        if (isBlackPiece(selectedPosition)) {
            if ((Integer.parseInt(positions[0]) == (Integer.parseInt(selectedPosition.split(",")[0]) + 1)) && //move forward
                    (Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1])) &&
                    !isWhitePiece(position) && !isBlackPiece(position)) {
                return true;
            } else if ((Integer.parseInt(positions[0]) == (Integer.parseInt(selectedPosition.split(",")[0]) + 1)) && //attack forward
                    ((Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1]) + 1) ||
                            (Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1]) - 1))
                    && isWhitePiece(position) )
                            return true;
            else return ((Integer.parseInt(positions[0]) == 4 )&&( (Integer.parseInt(selectedPosition.split(",")[0]) == 2)) && //first double move forward
                    (Integer.parseInt(positions[1]) == Integer.parseInt(selectedPosition.split(",")[1])) &&
                    !isWhitePiece(position) && !isBlackPiece(position)) ;
        }
        return false;
    }

    public static boolean isKingDead(String color) {
        switch (color){
            case "white" :
                for (int i = 0; i < 8 ; i++)
                    for (int j = 0; j < 8; j++)
                        if (board[i][j].equals("Kw")) return false;
                break;
            case "black" :
                for (int i = 0; i < 8 ; i++)
                    for (int j = 0; j < 8; j++)
                        if (board[i][j].equals("Kb")) return false;
                break;
        }
        return true;
    }

    public static void show_board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println();
        }
    }

    public static void move(String selectedPosition, String position , String inputPosition) {
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, backupboard[i], 0, 8);
        }
        chess.backupInputSelection = inputSelectedPosition;
        chess.backupSelection = selectedPosition;
        boolean kill = false;
        String[] positions = position.split(",");
        int x = Integer.parseInt(positions[0]);
        int y = Integer.parseInt(positions[1]);
        int selectedX = Integer.parseInt(selectedPosition.split(",")[0]);
        int selectedY = Integer.parseInt(selectedPosition.split(",")[1]);
        if (isWhitePiece(position)){
            whiteKilled.add(board[x-1][y-1]+" killed in spot "+inputPosition);
            kill = true;
        }
        else if (isBlackPiece(position)){
            blackKilled.add(board[x-1][y-1]+" killed in spot "+inputPosition);
            kill = true;
        }
        if (kill){
            allMoves.add(board[selectedX-1][selectedY-1]+" "+inputSelectedPosition+" to "+inputPosition+" destroyed "+board[x-1][y-1]);
            if (isWhiteTurn)
                whiteMoves.add(board[selectedX-1][selectedY-1]+" "+inputSelectedPosition+" to "+inputPosition+" destroyed "+board[x-1][y-1]);
            else
                blackMoves.add(board[selectedX-1][selectedY-1]+" "+inputSelectedPosition+" to "+inputPosition+" destroyed "+board[x-1][y-1]);

        }else {
            allMoves.add(board[selectedX-1][selectedY-1]+" "+inputSelectedPosition+" to "+inputPosition);
            if (isWhiteTurn)
                whiteMoves.add(board[selectedX-1][selectedY-1]+" "+inputSelectedPosition+" to "+inputPosition);
            else
                blackMoves.add(board[selectedX-1][selectedY-1]+" "+inputSelectedPosition+" to "+inputPosition);
        }
        if (kill) {
            System.out.println("rival piece destroyed");
            allKilled.add(board[x-1][y-1]+" killed in spot "+inputPosition);
        }else
            System.out.println("moved");
        board[x-1][y-1] = board[selectedX-1][selectedY-1];
        board[selectedX-1][selectedY-1] = "  ";
    }

    public static void undo() {
        for (int i = 0; i < 8; i++) {
            System.arraycopy(backupboard[i], 0, board[i], 0, 8);
        }
        selectedPosition = backupSelection;
        inputSelectedPosition = backupInputSelection;
    }

    public static void show_moves(String a) {
        switch (a) {
            case "A" :
                for (String s:allMoves)
                    System.out.println(s);
                break;
            case "W" :
                for (String s:whiteMoves)
                    System.out.println(s);
                break;
            case "B" :
                for (String s:blackMoves)
                    System.out.println(s);
                break;
        }
    }
}

class FirstMenu {

    static void start(Scanner input){
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            String inputString = input.nextLine();
            String[] inputArray = inputString.split(" ");
            if ( inputArray.length > 3 ||
                    ( ( inputString.contains("register") ||
                            inputString.contains("login") ||
                            inputString.contains("remove")) &&
                            inputArray.length != 3 )){
                System.out.println("invalid command");
                continue;
            }
            switch (inputArray[0]) {
                case "register":
                    register(inputArray[1], inputArray[2]);
                    break;
                case "login":
                    isLoggedIn = login(inputArray[1], inputArray[2]);
                    break;
                case "remove":
                    remove(inputArray[1], inputArray[2]);
                    break;
                case "list_users":
                    list_users();
                    break;
                case "exit":
                    System.out.println("program ended");
                    System.exit(0);
                    break;
                case "help":
                    help();
                    break;
                default :
                    System.out.println("invalid command");
            }
        }
        SecondMenu.start(input);
    }
    static void register(String username, String password){
        if(isNotValid(username, password))
            return ;
        if(chess.passwords.containsKey(username)){
            System.out.println("a user exists with this username");
        }
        else{
            chess.passwords.put(username,password);
            chess.scores.add(new scoreboard(username));
            System.out.println("register successful");
        }
    }
    static boolean login(String username, String password){
        if(isNotValid(username, password))
            return false;
        if(!chess.passwords.containsKey(username)){
            System.out.println("no user exists with this username");
            return false;
        }
        else{
            if(chess.passwords.get(username).equals(password)){
                System.out.println("login successful");
                chess.WhitePlayer = username;
                return true;
            }
            else{
                System.out.println("incorrect password");
                return false;
            }
        }
    }
    static void remove(String username, String password){
        if(isNotValid(username, password))
            return;
        if(!chess.passwords.containsKey(username)){
            System.out.println("no user exists with this username");
        }
        else{
            if(chess.passwords.get(username).equals(password)){
                chess.passwords.remove(username);
                chess.scores.removeIf(s -> s.name.equals(username));
                System.out.println("removed "+ username + " successfully");
            }
            else{
                System.out.println("incorrect password");
            }
        }

    }
    static void list_users(){
        for (String key : chess.passwords.keySet()) {
            System.out.println(key);
        }
    }
    static void help(){
        System.out.println("register [username] [password]");
        System.out.println("login [username] [password]");
        System.out.println("remove [username] [password]");
        System.out.println("list_users");
        System.out.println("help");
        System.out.println("exit");
    }

    static boolean isNotValid(String username, String password){
        if (username.matches("\\w+") && password.matches("\\w+"))
            return false;
        else if (!username.matches("\\w+")){
            System.out.println("username format is invalid");
            return true;
        }
        else{
            System.out.println("password format is invalid");
            return true;
        }
    }


}

class SecondMenu {
    static void start(Scanner input) {
        boolean isLoggedOutOrNewGame = false;
        int code = 2;
        while (!isLoggedOutOrNewGame) {
            String inputString = input.nextLine();
            String[] inputArray = inputString.split(" ");
            switch (inputArray[0]) {
                case "new_game":
                    if (new_game(inputArray[1], inputArray[2])) {
                        code = 3;
                        isLoggedOutOrNewGame = true;
                        System.out.println("new game started successfully between "+chess.WhitePlayer+" and "+chess.BlackPlayer+" with limit "+chess.limits);
                    }
                    break;
                case "scoreboard":
                    scoreboard();
                    break;
                case "list_users":
                    list_users();
                    break;
                case "logout":
                    System.out.println("logout successful");
                    code = 1;
                    isLoggedOutOrNewGame = true;
                    break;
                case "help":
                    help();
                    break;
                default:
                    System.out.println("invalid command");
            }
        }
        switch (code) {
            case 1:
                FirstMenu.start(input);
                break;
            case 3:
                ThirdMenu.start(input);
                break;
        }
    }

    static boolean isNotValid(String username, String limits) {
        try {
            Integer.parseInt(limits);
        }
        catch (NumberFormatException e) {
            System.out.println("invalid command");
            return true;
        }
        if (username.matches("\\w+") && !username.equals(chess.WhitePlayer) && Integer.parseInt(limits) >= 0)
            return false;
        else if (!username.matches("\\w+")) {
            System.out.println("username format is invalid");
            return true;
        }  else if (Integer.parseInt(limits) < 0) {
            System.out.println("number should be positive to have a limit or 0 for no limit");
            return true;
        } else if (username.equals(chess.WhitePlayer)) {
            System.out.println("you must choose another player to start a game");
            return true;
        } else if (!chess.passwords.containsKey(username)){
            System.out.println("no user exists with this username");
            return true;
        } else {
            return false;
        }
    }

    static boolean new_game(String username, String limit) {
        if (isNotValid(username, limit))
            return false;
        if (!chess.passwords.containsKey(username)) {
            System.out.println("no user exists with this username");
            return false;
        } else{
            chess.BlackPlayer = username;
            chess.limits = Integer.parseInt(limit);
            if (chess.limits == 0)
                chess.hasLimit = false;
            return true;
        }

    }

    static void scoreboard(){
        chess.scores.sort(new scoreboardComparator());
        for (scoreboard s : chess.scores) {
            System.out.println(s.name + " " + s.score+" "+s.wins+" "+s.draws+" "+s.losses);
        }
    }

    static void list_users(){
        FirstMenu.list_users();
    }

    static void help(){
        System.out.println("new_game [username] [limit]");
        System.out.println("scoreboard");
        System.out.println("list_users");
        System.out.println("help");
        System.out.println("logout");
    }
}

class ThirdMenu {
    public static boolean isUndo = false;
    public static String changeCoordinate(String coordinate){
        String[] coordinateArray = coordinate.split(",");
        return (9-Integer.parseInt(coordinateArray[0])+","+ coordinateArray[1]);
    }
    static void start(Scanner input)
     {
         boolean isNotEnd = true;
         while (isNotEnd) {
             String inputString = input.nextLine();
             String[] inputArray = inputString.split(" ");
             switch (inputArray[0]) {
                 case "select" :
                     if(isCoordinateValid(inputArray[1]))
                        select(changeCoordinate(inputArray[1]),inputArray[1]);
                     break;
                 case "deselect" :
                        deselect();
                        break;
                 case "move" :
                     if(isCoordinateValid(inputArray[1]))
                        move(changeCoordinate(inputArray[1]),inputArray[1]);
                     break;
                 case "next_turn" :
                        next_turn();
                        if(IsKingDead() || IsLimitEnd())
                            isNotEnd = false;
                        break;
                 case "show_turn" :
                        show_turn();
                        break;
                 case "undo" :
                     if (!inputString.equals("undo")){
                         System.out.println("invalid command");
                         break;
                     }
                    undo();
                    break;
                 case "undo_number" :
                        undo_number();
                        break;
                 case "show_moves" :
                     show_moves(inputString.contains("-all"));
                     break;
                 case "show_killed" :
                        show_killed(inputString.contains("-all"));
                        break;
                 case "show_board" :
                        show_board();
                        break;
                 case "help" :
                        help();
                        break;
                 case "forfeit" :
                        forfeit();
                        isNotEnd = false;
                        break;
                 default :
                     System.out.println("invalid command");
             }
         }
         chess.reset();
         SecondMenu.start(input);
     }
     private static boolean isCoordinateValid(String coordinate){
         String[] coordinateArray = coordinate.split(",");
         if(!coordinateArray[0].matches("\\d") || !coordinateArray[1].matches("\\d")){
             System.out.println("wrong coordination");
             return false;
         }
         return true;
     }
    private static boolean IsLimitEnd() {
        if (chess.limits == 0 && chess.hasLimit)
            result("draw",false);
        return chess.limits == 0 && chess.hasLimit;
    }

    static void select(String position,String inputPosition){
            if(chess.isNotValidPosition(position)){
                System.out.println("wrong coordination");
                return;
            }
            if(chess.isWhiteTurn){
                if(chess.isWhitePiece(position)){
                    chess.selectedPosition = position;
                    chess.inputSelectedPosition = inputPosition;
                    chess.isSelected = true;
                    System.out.println("selected");
                } else if (chess.isBlackPiece(position)){
                    System.out.println("you can only select one of your pieces");
                } else {
                    System.out.println("no piece on this spot");
                }
            }
            else{
                if(chess.isBlackPiece(position)){
                    chess.selectedPosition = position;
                    chess.inputSelectedPosition = inputPosition;
                    chess.isSelected = true;
                    System.out.println("selected");
                }
                else{
                    System.out.println("you can only select one of your pieces");
                }
            }
        }
        static void deselect(){
            if(!chess.isSelected){
                System.out.println("no piece is selected");
                return;
            }
            chess.selectedPosition = "";
            chess.inputSelectedPosition = "";
            chess.isSelected = false;
            System.out.println("deselected");
        }
        static void move(String position,String inputPosition){
            if(!chess.isSelected){
                System.out.println("do not have any selected piece");
                return;
            }
            if(chess.isNotValidPosition(position)){
                System.out.println("wrong coordination");
                return;
            }
            if(chess.isWhiteTurn){
                if(chess.isWhitePiece(position)){
                    System.out.println("cannot move to the spot");
                    return;
                }
            }
            else{
                if(chess.isBlackPiece(position)){
                    System.out.println("cannot move to the spot");
                    return;
                }
            }
            if(chess.isValidMove(chess.selectedPosition, position)){
                chess.move(chess.selectedPosition, position , inputPosition);
                chess.isSelected = false;
                chess.isMoved = true;
            }
            else{
                System.out.println("cannot move to the spot");
            }
        }
        static void next_turn(){
            if (chess.isMoved){
                chess.isWhiteTurn = !chess.isWhiteTurn;
                System.out.println("turn completed");
                isUndo = false;
                chess.limits--;
                chess.isMoved = false;
            }
            else System.out.println("you must move then proceed to next turn");
        }
        static void show_turn(){
            if(chess.isWhiteTurn){
                System.out.println("it is player "+chess.WhitePlayer+" turn with color white");
            }
            else{
                System.out.println("it is player "+chess.BlackPlayer+" turn with color black");
            }
        }
        static void undo(){
            if(!chess.isMoved){
                System.out.println("you must move before undo");
                return;
            }
            if (isUndo){
                System.out.println("you have used your undo for this turn");
                return;
            }
            if(chess.isWhiteTurn){
                if (chess.WhiteUndoCount > 0){
                    chess.WhiteUndoCount--;
                    chess.whiteMoves.remove(chess.whiteMoves.size()-1);
                    chess.undo();
                }else {
                    System.out.println("you cannot undo anymore");
                    return;
                }
            }
            else{
                if (chess.BlackUndoCount > 0){
                    chess.undo();
                    chess.BlackUndoCount--;
                    chess.blackMoves.remove(chess.blackMoves.size()-1);
                }else {
                    System.out.println("you cannot undo anymore");
                    return;
                }
            }
            chess.isMoved = false;
            chess.isSelected = true;
            chess.allMoves.remove(chess.allMoves.size()-1);
            System.out.println("undo completed");
            isUndo = true;
        }
        static void undo_number(){
            if(chess.isWhiteTurn)
                System.out.println("you have "+chess.WhiteUndoCount+" undo moves");
            else
                System.out.println("you have "+chess.BlackUndoCount+" undo moves");

        }
        static void show_moves(boolean isAll){
            if (isAll)
                chess.show_moves("A");
            else if (chess.isWhiteTurn)
                chess.show_moves("W");
            else
                chess.show_moves("B");
        }
        static void show_killed(boolean isAll){
            if(isAll)
                for (String s: chess.allKilled)
                    System.out.println(s);
            else if (chess.isWhiteTurn)
                for (String s: chess.whiteKilled)
                    System.out.println(s);
            else
                for (String s: chess.blackKilled)
                    System.out.println(s);

        }
        static void show_board(){
            chess.show_board();
        }
        static void help(){
            System.out.println("select [x],[y]");
            System.out.println("deselect");
            System.out.println("move [x],[y]");
            System.out.println("next_turn");
            System.out.println("show_turn");
            System.out.println("undo");
            System.out.println("undo_number");
            System.out.println("show_moves [-all]");
            System.out.println("show_killed [-all]");
            System.out.println("show_board");
            System.out.println("help");
            System.out.println("forfeit");
        }
        static void forfeit(){
            System.out.println("you have forfeited");
            if(chess.isWhiteTurn)
                result("black",true);
            else
                result("white",true);
        }
        public static boolean IsKingDead(){

            if(chess.isWhiteTurn){
                if(chess.isKingDead("white")){
                    result("black",false);
                    return true;
                }
            }
            else{
                if(chess.isKingDead("black")){
                    result("white",false);
                    return true;
                }
            }
            return false;
        }
        public static void result(String result,boolean isForfeit){
        String winner = "" , loser = "";
        boolean hasWinner = false;
            switch (result){
                case "white":
                    System.out.println("player "+chess.WhitePlayer+" with color white won");
                    winner = chess.WhitePlayer;
                    loser = chess.BlackPlayer;
                    hasWinner = true;
                    break;
                case "black":
                    System.out.println("player "+chess.BlackPlayer+" with color black won");
                    winner = chess.BlackPlayer;
                    loser = chess.WhitePlayer;
                    hasWinner = true;
                    break;
                case "draw":
                    System.out.println("draw");
                    for (scoreboard s: chess.scores)
                        if (s.name.equals(chess.WhitePlayer)){
                            s.score++; s.draws++;}
                        else if (s.name.equals(chess.BlackPlayer)){
                            s.score++; s.draws++;}
                    break;

            }
            if (hasWinner)
                for (scoreboard s : chess.scores)
                    if (s.name.equals(winner)) {
                        s.score += 3;
                        s.wins++;
                        if (isForfeit)
                            s.score--;
                    } else if (s.name.equals(loser)) {
                        s.losses++;
                        if (isForfeit)
                            s.score--;
                    }
        }
}