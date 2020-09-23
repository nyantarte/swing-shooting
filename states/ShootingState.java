package states;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import config.*;
import game.GameEngine;
import assets.*;
import ui.*;
public class ShootingState implements IState{
    public static class Task{
        public IMove obj;
        public int num;
        public int numNeedToNextLevel;
        public Task(){}
        public Task(IMove m,int n){
            obj=m;
            num=n;
        }
        @Override
        public String toString(){
            return String.format("Name %s  Num %d",obj.toString(),num);
        }
    }
    private Charactor m_playerChara;
    private ArrayList<IMove> m_objList=new ArrayList<>();
    private int m_fieldGenCount=Config.FIELD_GEN_COUNT;
    private Bullet m_playerBullet;
    private Object[] m_taskList;
    private int m_gameLevel=0;
    private int m_objInserted=0;
    private long m_curTime;
    private int m_playerBeated=0;
    public ShootingState(Object[] tl){
        m_playerChara=GameEngine.getPlayerUseChara();
        m_playerChara.setPos(new Vector(Config.DEF_CHARA_SIZE,Config.MAIN_WIN_H/2,0));
        m_taskList=tl;
    }
    public void onPaint(Graphics g){
        for(IMove m :m_objList){
            if(null!=m){
                m.paint(g);
            }
        }
        m_playerChara.paint(g);

        if(null!=m_playerBullet){
            m_playerBullet.paint(g);
        }
    }
    public void onTimer(UISurface s){
        m_curTime=System.currentTimeMillis();
        if(0>=m_playerChara.getLife()){
            GameEngine.popState();;
            GameEngine.setState(new GameOverState(false));
            return;
        }
        /**
         * オブジェクトの移動及び解放処理
         * 
         */
        for(int i=0;i < m_objList.size();++i){
            IMove m=m_objList.get(i);
            if(null!=m){
                Vector pos=m.getPos();
                //画面外か？
                if(0>=pos.getX() || 0>=m.getLife()){
                    m_objList.set(i, null);
                    if(m instanceof Charactor && 0>=m.getLife()){
                        ++m_playerBeated;
                    }
                }else{
                    float objHitSize=0.0f;
                    //移動
                    if(m instanceof Charactor){
                        objHitSize=Config.DEF_CHARA_SIZE;//Config.CHARA_HIT_CHECK_SIZE;
                    }else if(m instanceof Bullet){
                        objHitSize=Config.BULLET_HIT_CHECK_SIZE;
                    }
                    if(m.doHitCheck()){
                        if(Vector.isCollide(pos, objHitSize, m_playerChara.getPos(),Config.CHARA_HIT_CHECK_SIZE)){
                            Logger.getGlobal().info("Begin hit task");
                            System.out.println(String.format("Attacker %s atk=%d",m.toString(),m.getAtk()));
                            m.setDoHitCheck(false);
                            m_playerChara.onHit(m.getAtk());
                            Logger.getGlobal().info(String.format("Defender %s life=%d",m_playerChara.toString(),m_playerChara.getLife()));
                            Logger.getGlobal().info("End hit task");
                        }
                    }
                    if(m.doHitCheck() && null!=m_playerBullet){
                        if(Vector.isCollide(pos, objHitSize, m_playerBullet.getPos(),Config.BULLET_SIZE)){
                            m.onHit(1);//m_playerBullet.getAtk());
                            m_playerBullet=null;
                        }
                    }
                    m.move(this);
                }

            }

        }

        //オブジェクト生成
        if(0>=m_fieldGenCount){
            
            Task t=(Task)m_taskList[m_gameLevel];
            if(t.numNeedToNextLevel==m_playerBeated){
                ++m_gameLevel;
            }
            if(m_taskList.length<=m_gameLevel){
                GameEngine.popState();;
                GameEngine.setState(new GameOverState(true));
                return;
    
            }else{
                t=(Task)m_taskList[m_gameLevel];
            }
            //オブジェクトは複数個生成を指定できる
            //ただし、１フレーム内に生成できる数は１個のみ
            if(m_objInserted< t.num){   //全て生成済？
                if(0==m_objInserted){   //コンパクション処理
                    for(int i=0;i < m_objList.size();){
                        IMove o=m_objList.get(i);
                        if(null==o){
                            m_objList.remove(i);
                            continue;
                        }
                        ++i;
                    }
                }

                
                IMove m=(IMove)t.obj.clone();
                Charactor c=(Charactor)m;
            //    c.setLife(1);
                m_objList.add(m);
                c.move(this);
                ++m_objInserted;
            }else{
                //全個数生成完了
                m_objInserted=0;
                m_fieldGenCount=Config.FIELD_GEN_COUNT;
            }
        }else{
            --m_fieldGenCount;
        }

        //プレイヤーが撃てるのは１個のみ？
        if(null!=m_playerBullet){
            Vector bulletPos=m_playerBullet.getPos();

            //画面外なら撃つことができる
            if(0>= bulletPos.getX() || Config.MAIN_WIN_W <= bulletPos.getX() ||
               0>= bulletPos.getY() || Config.MAIN_WIN_H <= bulletPos.getY()){
                   m_playerBullet=null;
            }else{
                m_playerBullet.move(this);
            }
        }
        Vector v=m_playerChara.getPos();

        if(GameEngine.isKeyPressed(KeyEvent.VK_DOWN)){
            v.setY(v.getY()+Config.PLAYER_MOVE_SPEED);
        }else if(GameEngine.isKeyPressed(KeyEvent.VK_UP)){
            v.setY(v.getY()-Config.PLAYER_MOVE_SPEED);

        }

        if(GameEngine.isKeyPressed(KeyEvent.VK_LEFT)){
            v.setX(v.getX()-Config.PLAYER_MOVE_SPEED);

        }else if(GameEngine.isKeyPressed(KeyEvent.VK_RIGHT)){
            v.setX(v.getX()+Config.PLAYER_MOVE_SPEED);

        }

        if(GameEngine.isKeyPressed(KeyEvent.VK_Z) && null==m_playerBullet){
            m_playerBullet=new Bullet((Vector)m_playerChara.getPos().clone(),new Vector(Config.BULLET_MOVE_SPEED,0,0));
        }

    }


    public long getCurTime(){
        return m_curTime;
    }

    public void addBullet(Vector pos,Vector dir,int atk){
        m_objList.add(new Bullet(pos,dir,atk));
    }
    public Vector getPlayerPos(){
        return m_playerChara.getPos();
    }
}