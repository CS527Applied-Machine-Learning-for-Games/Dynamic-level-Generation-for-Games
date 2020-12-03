package MarioSDK.component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LevelModel {

    public static final char EMPTY = '-';

    private char[][] map;

    public LevelModel(int levelWidth, int levelHeight) {
        this.map = new char[levelWidth][levelHeight];
    }

    public LevelModel clone() {
        LevelModel model = new LevelModel(this.getWidth(), this.getHeight());
        for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
                model.map[x][y] = this.map[x][y];
            }
        }
        return model;
    }

    public int getWidth() {
        return this.map.length;
    }

    public int getHeight() {
        return this.map[0].length;
    }

    public void setBlock(int x, int y, char value) {
        if (x < 0 || y < 0 || x > this.map.length - 1 || y > this.map[0].length - 1) return;
        this.map[x][y] = value;
    }

    public void setRectangle(int startX, int startY, int width, int height, char value) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.setBlock(startX + x, startY + y, value);
            }
        }
    }

    public void copyFromString(int targetX, int targetY, int sourceX, int sourceY, int width, int height, String level) {
        String[] lines = level.split("\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int maxWidth = lines[0].length();
                int maxHeight = lines.length;
                this.setBlock(x + targetX, y + targetY, lines[Math.min(y + sourceY, maxHeight - 1)].charAt(Math.min(x + sourceX, maxWidth - 1)));
            }
        }
    }


    public void clearMap() {
        this.setRectangle(0, 0, this.getWidth(), this.getHeight(), EMPTY);
    }

    public String getMap() {
        String result = "";
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                result += map[x][y];
            }
            result += "\n";
        }

        try{
            String content = result;
            String path="GeneratedLevels/newLevel.txt";
            File file = new File(path);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);


            bw.write(content);


            bw.close();
            }
            catch(Exception e){
                System.out.println(e);    
            }

        return result;


    }

}
