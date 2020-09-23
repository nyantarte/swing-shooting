package states;
import java.awt.*;

import game.GameEngine;
import ui.*;
import ui.UISurface.POS;
import config.*;

public class GameOverState implements IState{
    private boolean m_result;

    public GameOverState(boolean r){
        m_result=r;
    }
    public void onPaint(Graphics g){
    }
    public void onTimer(UISurface s){
        
        s.begin(POS.CENTER);
        s.label(m_result?"Victory":"Game over", true);
        boolean moveMenu=s.button("OK", true);
        s.end();
        if(moveMenu){
            GameEngine.popState();
        }
    }

}