package assets;
import java.awt.*;

import config.Config;
import states.*;
public class Bullet implements IMove{

    private Vector m_pos;
    private Vector m_dir;

    private int m_atk;
    private boolean m_doHitCheck=true;
    public Bullet(Vector p,Vector d){
        m_pos=p;
        m_dir=d;
    }
    public Bullet(Vector p,Vector d,int atk){
        m_pos=p;
        m_dir=d;
        m_atk=atk;
    }
    public Bullet(){
        m_pos=(Vector)Vector.zero.clone();
        m_dir=(Vector)Vector.zero.clone();
    }
    public Vector getPos(){
        return m_pos;
    }
    public void setPos(Vector v){
        m_pos=v;
    }
    public void paint(Graphics g){
        g.setColor(Color.WHITE);

        float l=m_pos.getX()-Config.BULLET_SIZE/2;
        float t=m_pos.getY()-Config.BULLET_SIZE/2;
        g.fillRect((int)l,(int)t,Config.BULLET_SIZE,Config.BULLET_SIZE);
    }

    public void setDir(Vector v){
        m_dir=v;
    }
    public void move(IState s){
        m_pos=Vector.add(m_pos,m_dir);

    }

    public int getLife(){
        return 1;
    }
    public int getAtk(){
        return m_atk;
    }
    public void setAtk(int i){
        m_atk=i;
    }

    public boolean doHitCheck(){
        return m_doHitCheck;
    }
    public void setDoHitCheck(boolean f){
        m_doHitCheck=f;
    }
    public void onHit(int atk){}

    public Object clone(){
        return new Bullet();
    }

    @Override
    public String toString(){
        return "bullet";
    }
}