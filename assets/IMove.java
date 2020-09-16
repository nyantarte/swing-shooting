package assets;
import java.awt.*;
import states.*;
public interface IMove {

    public Vector getPos();
    public void setPos(Vector v);
    public void setDir(Vector v);
    public void paint(Graphics g);
    public void move(IState s);
    public Object clone();
    public boolean doHitCheck();
    public void setDoHitCheck(boolean f);

    public int getLife();
    public int getAtk();
    public void onHit(int atk);
}