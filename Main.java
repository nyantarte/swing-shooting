import game.*;
public class Main {

    public static void main(String[] args){

        GameEngine.loadItemAssets();
        GameEngine.loadCharaAssets();
        GameEngine.loadShootingAssets();
        GameEngine.getMainWin();
    }
}