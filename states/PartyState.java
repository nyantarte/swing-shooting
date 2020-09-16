package states;
import java.awt.*;
import java.util.*;
import game.GameEngine;
import ui.*;
import ui.UISurface.POS;
import assets.*;
import config.Config;
public class PartyState implements IState{

    private int m_slotSelecting=-1;
    public void onPaint(Graphics g){}
    public void onTimer(UISurface s){
        boolean isNeedReturn=false;
        s.begin(POS.WEST);
            isNeedReturn= s.button(Config.RETURN_BTN_CAPTION, true);
            Charactor c=GameEngine.getPlayerUseChara();
            if(s.button(null==c?"Slot1":c.getName(), true)){
                m_slotSelecting=0;
            }
        s.end();
        if(-1<m_slotSelecting){
            s.begin(POS.CENTER);
            int idx=s.list(GameEngine.getPlayerCharaList().toArray());
            s.end();
            if(-1<idx){
                ArrayList<Charactor> cList=GameEngine.getPlayerCharaList();
                GameEngine.setPlayerUseChara(cList.get(idx));
                m_slotSelecting=-1;
            }            
        }

        if(isNeedReturn){
            GameEngine.popState();
        }
    }

}