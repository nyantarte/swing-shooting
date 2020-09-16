package states;
import java.awt.*;
import ui.*;
import ui.UISurface.POS;
import assets.*;
import config.Config;
import game.GameEngine;
public class CharaDetailsState implements IState{

    private Charactor m_target;

    public CharaDetailsState(Charactor c){
        m_target=c;
    }
    public void onPaint(Graphics g){


    }
    public void onTimer(UISurface s){

        s.begin(POS.WEST);
        if(s.button(Config.RETURN_BTN_CAPTION, true)){
            GameEngine.popState();
        }
        s.end();

        s.begin(POS.CENTER);
        s.label(String.format("Name %s",m_target.getName()),true);
        s.label(String.format("Level %d",m_target.getLevel()),true);
        s.label(String.format("Life %s",m_target.getLifeRank().toString()),true);
        s.label(String.format("Atk %s",m_target.getAtkRank().toString()), true);
        s.label(String.format("Air atk %s",m_target.getAirAtkRank().toString()),true);
        s.label(String.format("Air def %s",m_target.getAirDefRank().toString()),true);
        for(int i=0;i < Config.ITEM_SLOT_NUM;++i){
            Item item=m_target.getItem(i);
            if(null!=item){
                s.label(String.format("Slot%d %s",i+1,item.getName()), true);
            }
        }
        s.end();
        
    }


}