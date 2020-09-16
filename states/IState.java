package states;
import java.awt.*;
import ui.*;
public interface IState {

    public void onPaint(Graphics g);
    public void onTimer(UISurface s);
}