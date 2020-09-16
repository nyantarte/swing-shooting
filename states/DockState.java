package states;
import java.awt.*;

import game.GameEngine;
import ui.*;
import ui.UISurface.POS;
import assets.*;
import config.*;
public class DockState implements IState{
    public void onPaint(Graphics g){

    }
    public void onTimer(UISurface s){
        s.begin(POS.WEST);
        if(s.button(Config.RETURN_BTN_CAPTION, true)){
            GameEngine.popState();
        }
        s.end();


        s.begin(POS.CENTER);
        int i=s.list(GameEngine.getPlayerCharaList().toArray());  
        
        s.end();
        if(-1 < i){
            Charactor target=GameEngine.getPlayerCharaList().get(i);
            GameEngine.setState(new CharaDetailsState(target));
        }
    }

}