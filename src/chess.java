import java.util.HashMap;
import java.util.Scanner;

public class chess {
    public static HashMap<String,String> passwords = new HashMap<>();
    public static HashMap<String,Integer> score = new HashMap<>();
    public static HashMap<String,Integer> wins = new HashMap<>();
    public static HashMap<String,Integer> draws = new HashMap<>();
    public static HashMap<String,Integer> loses = new HashMap<>();
    public static String WhitePlayer , BlackPlayer;
    public static int limits;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        FirstMenu.start(input);
    }

}

class FirstMenu {

    static void start(Scanner input){
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            String inputString = input.nextLine();
            String[] inputArray = inputString.split(" ");
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
            }
        }
        SecondMenu.start(input);
    }
    static void register(String username, String password){
        if(isNotValid(username, password))
            return ;
        if(chess.passwords.containsKey(username)){
            System.out.println("username already exists");
        }
        else{
            chess.passwords.put(username,password);
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
        if (username.matches("\\w+") && !username.equals(chess.WhitePlayer) && Integer.parseInt(limits) > 0)
            return false;
        else if (!username.matches("\\w+")) {
            System.out.println("username format is invalid");
            return true;
        } else if (username.equals(chess.WhitePlayer)) {
            System.out.println("you must choose another player to start a game");
            return true;
        } else {
            System.out.println("number should be positive to have a limit or 0 for no limit");
            return true;
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
            return true;
        }

    }

    static void scoreboard(){
        for (String key : chess.score.keySet()) {
            System.out.println(key + " " + chess.score.get(key)+" "+chess.wins.get(key)+" "+chess.draws.get(key)+" "+chess.loses.get(key));
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
    static void start(Scanner input){}
    /* {
        boolean isLoggedOutOrNewGame = false;
        int code = 2;
        while (!isLoggedOutOrNewGame) {
            String inputString = input.nextLine();
            String[] inputArray = inputString.split(" ");
            switch (inputArray[0]) {
                case "move":
                    if (move(inputArray[1], inputArray[2], inputArray[3])) {
                        code = 3;
                        isLoggedOutOrNewGame = true;
                    }
                    break;
                case "undo":
                    undo();
                    break;
                case "save":
                    save();
                    break;
                case "load":
                    load();
                    break;
                case "exit":
                    System.out.println("program ended");
                    System.exit(0);
                    break;
                case "help":
                    help();
                    break;
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

    static boolean move(String username, String x1, String y1) {
        if (isNotValid(username, x1, y1))
            return false;
        if (!chess.passwords.containsKey(username)) {
            System.out.println("no user exists with this username");
            return false;
        } else {
            if (chess.WhitePlayer.equals(username)) {
                if (chess.board[Integer.parseInt(x1)][Integer.parseInt(y1)] == ' ') {
                    chess.board[Integer.parseInt(x1)][Integer.parseInt(y1)] = 'W';
                    chess.board[Integer.parseInt(x1)][Integer.parseInt(y1)] = ' ';
                    return true;
                } else {
                    System.out.println("this cell is occupied");
                    return false;
                }
            } else {
            }
        }
    }*/
}