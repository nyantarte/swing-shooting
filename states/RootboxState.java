package states;
import java.awt.*;

import game.GameEngine;
import ui.*;
import ui.UISurface.POS;
import assets.*;
public class RootboxState implements IState{

    private Charactor m_newChara;
    public RootboxState(){
        Charactor c=GameEngine.getCharaList().get(0);
        GameEngine.getPlayerCharaList().add(m_newChara=(Charactor)c.clone());
        if(null==GameEngine.getPlayerUseChara()){
            GameEngine.setPlayerUseChara(m_newChara);
        }
    }
    public void onPaint(Graphics g){}
    public void onTimer(UISurface s){

        s.begin(POS.CENTER);
        s.label(m_newChara.getName(), true);
        if(s.button("OK",true)){
            GameEngine.popState();
        }
        s.end();

    }

}