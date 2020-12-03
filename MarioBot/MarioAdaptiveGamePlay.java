import Agent.PlayAgent;
import MarioSDK.component.GamePlay;
import MarioSDK.component.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MarioAdaptiveGamePlay {

    public static double time = 0;
    public static double win = 0;
    public static boolean flag = true;
    public static String display = "";
    public static void printResults(Result result, int gameNumber) {
        System.out.println("****************************************************************");
        System.out.println("Game - "+gameNumber);
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println(" Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps());
        time+=result.getMaxJumpAirTime();
        if(result.getGameStatus().toString().equals("WIN")){
            win+=1;
        }


        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
       try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } 
        catch (IOException e) {
        }
        return content;
    }

    public static void main(String[] args) {
        GamePlay game = new GamePlay();
        int gameNumber = Integer.parseInt(args[0]);
        int timer = Integer.parseInt(args[1]);
        String classifyLevels = "classifyLevel/Level";
        flag = true;
        System.out.println("****************************************************************");
        List<Result> result = new ArrayList<>();
        Random rand = new Random();
        int i = rand.nextInt(gameNumber/2);
        Map<Integer,Boolean> history = new HashMap();
        boolean flag = true;
        while(i>=1 && i<=gameNumber) {
            if(!history.containsKey(i)) {
                history.put(i,true);
                printProgress(i - 1, gameNumber);
                Result r = game.runGame(new PlayAgent(), getLevel(classifyLevels + i + ".txt"), timer, 0, true);
                result.add(r);
                if(r.getGameStatus().toString().equals("WIN")) {
                    flag = true;
                    i++;
                } else {
                    flag = false;
                    i--;
                }
            }else{
                if(flag) {
                    i++;
                }
                else {
                    i--;
                }
            }
        }
        printProgress(gameNumber,gameNumber);

        for(i=0;i<result.size();i++) {

            printResults(result.get(i),i+1);
        }

        System.out.println("\n****************************************************************");

        System.out.println("****************************************************************");
        System.out.println(" Average Max Air Time: " + time/gameNumber);
        System.out.println(" Number of Games Won: " + win);
        System.out.println(" Number of Games Loss: " + (gameNumber - win));
        System.out.println(" Average game Won: " + ((win/gameNumber)*100));
        System.out.println("****************************************************************");

    }

    public static void printProgress( int currentGame,int gameLength) {

        int num = (currentGame*10/gameLength);

        String temp = "";
        for(int j=0;j<display.length();j++)
            System.out.print("\b");
        temp = "[ ";
        for(int j=0;j<num;j++)
            temp +="=";
        for(int j=0;j<10-num;j++)
            temp +=" ";

        temp += (" ] "+ ((currentGame*100/gameLength)) +"% games Completed");
        display = temp;
        System.out.print(temp);

    }
}
