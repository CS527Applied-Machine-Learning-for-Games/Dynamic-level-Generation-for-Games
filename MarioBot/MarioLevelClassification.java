import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Agent.Eval;
import Agent.MapResult;
import Agent.PlayAgent;
import MarioSDK.component.GamePlay;
import MarioSDK.component.Result;

public class MarioLevelClassification {

    public static String display = "";

    public static String getLevel(String filepath) {
        String content = "";
       // System.out.println("****************************************************************");
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } 
        catch (IOException e) {
        }
        return content;
    }

    public static void main(String[] args) {
        GamePlay game = new GamePlay();
        Eval ev = new Eval();
        int gameNumber = Integer.parseInt(args[0]);
        int timer = Integer.parseInt(args[1]);
        String gamePath = "RunLevels/Level";
        String classifyLevels = "classifyLevel/Level";
        System.out.println("****************************************************************");
        List<MapResult> mapResult = new ArrayList<>();
        for(int i=1;i<=gameNumber;i++) {
            printProgress(i-1,gameNumber);
            Result r = game.runGame(new PlayAgent(), getLevel(gamePath + i + ".txt"), timer, 0, true);
            mapResult.add(new MapResult(r,gamePath + i + ".txt"));
        }
        printProgress(gameNumber,gameNumber);

        mapResult = ev.sortMap(mapResult);

        System.out.println("\n****************************************************************");


        for(int i=0;i<mapResult.size();i++) {
            System.out.println(classifyLevels+(i+1)+".txt :: "+ mapResult.get(i).fileName);
            writeToFile(classifyLevels+(i+1)+".txt",getLevel(mapResult.get(i).fileName));
        }
    }

    public static void writeToFile(String fileName,String content) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
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
