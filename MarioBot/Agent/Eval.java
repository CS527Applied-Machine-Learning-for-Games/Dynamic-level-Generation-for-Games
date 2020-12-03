package Agent;

import MarioSDK.component.Result;

import java.util.List;

public class Eval {

     public static List<MapResult> sortMap(List<MapResult> result) {
        for(int i=0;i<result.size();i++){
            for(int j=i+1;j<result.size();j++){
                if(check(result.get(i).result,result.get(j).result)){
                    MapResult temp = result.get(i);
                    result.set(i,result.get(j));
                    result.set(j,temp);
                }
            }
        }

        return result;
    }

    private static boolean check(Result result1, Result result2) {
            if(result1.getGameStatus().toString().equals("WIN") && !result2.getGameStatus().toString().equals("WIN") )
                return false;
            else if(!result1.getGameStatus().toString().equals("WIN") && result2.getGameStatus().toString().equals("WIN") )
                return true;

            if(result1.getCompletionPercentage()>result2.getCompletionPercentage())
                return false;
            else if(result1.getCompletionPercentage()<result2.getCompletionPercentage())
            return true;

        if((int) Math.ceil(result1.getRemainingTime() / 1000f)>(int) Math.ceil(result2.getRemainingTime() / 1000f))
            return false;
        else if((int) Math.ceil(result1.getRemainingTime() / 1000f)<(int) Math.ceil(result2.getRemainingTime() / 1000f))
            return true;

        if(result1.getCurrentCoins()>result2.getCurrentCoins())
            return false;
        else if(result1.getCurrentCoins()<result2.getCurrentCoins())
            return true;

        if(result1.getKillsTotal()<result2.getKillsTotal())
            return false;
        else if(result1.getKillsTotal()>result2.getKillsTotal())
            return true;

        if(result1.getNumJumps()<=result2.getNumJumps())
            return false;
        else
            return true;
    }
}
