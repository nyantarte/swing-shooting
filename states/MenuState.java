package states;
import java.awt.*;
import ui.*;
import ui.UISurface.POS;
import game.*;
public class MenuState implements IState{
    public void onPaint(Graphics g){

    }
    public void onTimer(UISurface s){

        if(0==GameEngine.getPlayerCharaList().size()){
            GameEngine.setState(new RootboxState());
            return;
        }
        s.begin(POS.NORTH);
        if(s.button("Dock", true)){
            GameEngine.setState(new DockState());
        }else if(s.button("Party",true)){
            GameEngine.setState(new PartyState());
        }else if(s.button("Mission",true)){
            GameEngine.setState(new ShootingState(GameEngine.getShootingTask("test")));
        }

        s.end();
        //System.out.println("A");

    }


}