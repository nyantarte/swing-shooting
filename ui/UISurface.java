package ui;
import java.awt.*;
import java.awt.event.*;

import config.Config;

/**
 * UIを描画するイメージサーフェース
 */
public class UISurface {

    private Size m_size;    /*!描画先のサイズ*/
    private Point m_nextPos;/*!描画先の座標*/
    private Font m_font;
    public enum POS{
        NORTH,
        CENTER,
        EAST,
        WEST,
        SOUTH
    }
    private POS m_pos;/*!描画位置*/
    private Graphics m_g;/*!描画先のグラフィックスオブジェクト*/
    private Point m_clickPos;
    public UISurface(int w,int h,Font f){
        m_size=new Size(w,h);
        m_font=f;
    }
    public void setGraphics(Graphics g){
        m_g=g;
        m_g.setFont(m_font);
    }
    /**
     * UIの描画を開始する際に必ず呼び出す
     * @param pos   描画先を指定するPOS値
     */
    public void begin(POS pos){
        m_pos=pos;
        m_nextPos=new Point(getDefaultLeftPos(),getDefaultTopPos());
    }
    private int getDefaultLeftPos(){
        if(POS.NORTH==m_pos){
            return 0;
        }else if(POS.CENTER==m_pos){
            return m_size.width/3;
        }else if(POS.EAST==m_pos){
            return m_size.width/3*2;
        }else if(POS.WEST==m_pos){
            return 0;
        }else {

           return 0;
        }

    } 
    private int getDefaultTopPos(){
        if(POS.NORTH==m_pos){
            return 16+m_g.getFont().getSize();
        }else if(POS.CENTER==m_pos){
            return 16+m_g.getFont().getSize();
        }else if(POS.EAST==m_pos){
            return 16+m_g.getFont().getSize();
        }else if(POS.WEST==m_pos){
            return 16+m_g.getFont().getSize();
        }else{
            return (m_size.height-16)/3+m_g.getFont().getSize();
        }

    }

    public void end(){


    }
    /**
     * ボタンを描画する
     * @param caption   描画する文字列
     * @param doBreak   開業するならtrue
     * @return ボタンがクリックされたならtrue
     */
    public boolean button(String caption,boolean doBreak){
        return button(caption,-1,doBreak);
        
    }
    /**
     * ボタンを描画する
     * @param caption   描画する文字列
     * @param forceWidth    描画されるボタンの横幅を指定しないなら-1,指定するならその横幅
     * @param doBreak   開業するならtrue
     * @return ボタンがクリックされたならtrue
     */
    public boolean button(String caption,int forceWidth,boolean doBreak){
        int w=m_g.getFontMetrics().stringWidth(caption);
        if(-1!=forceWidth){
            w=forceWidth;
        }
        m_g.setColor(Color.WHITE);
        m_g.fillRect(m_nextPos.x, m_nextPos.y-m_g.getFontMetrics().getHeight(), w, m_g.getFontMetrics().getHeight());
        m_g.setColor(Color.BLACK);
        m_g.drawString(caption,m_nextPos.x, m_nextPos.y);

        boolean clickState=false;
        if(isClicked()){
            clickState=isMouseInRect(m_nextPos.x,m_nextPos.y-m_g.getFontMetrics().getHeight(),w,m_g.getFontMetrics().getHeight());
            if(clickState){
                m_clickPos=null;
            }
        }

        if(doBreak){
            m_nextPos.y+=m_g.getFontMetrics().getHeight();
            m_nextPos.x=getDefaultLeftPos();
        }else{
            m_nextPos.x+=w;
        }
        return clickState;
    
    }

    /**
     * テキストを描画する
     * @param caption   描画する文字列
     * @param doBreak   開業するならtrue
     */
    public void label(String caption,boolean doBreak){
        int w=m_g.getFontMetrics().stringWidth(caption);
        m_g.setColor(Color.WHITE);
        m_g.drawString(caption,m_nextPos.x, m_nextPos.y);
        if(doBreak){
            m_nextPos.y+=m_g.getFontMetrics().getHeight();
            m_nextPos.x=getDefaultLeftPos();
        }else{
            m_nextPos.x+=w;
        }

    }
    /**
     * リストを描画する
     * @param l         描画するリスト
     * @param doBreak   開業するならtrue
     */
    public int list(Object[] l){
        int yPos=m_nextPos.y-m_g.getFontMetrics().getHeight();
        int lH=Config.MAIN_WIN_H-m_nextPos.y;
        int hItemNum=lH/m_g.getFontMetrics().getHeight();
        int tW=0;
        for(int i=0;i < Math.min(hItemNum,l.length);++i){
            Object o=l[i];
            String s=o.toString();
            tW=Math.max(tW,m_g.getFontMetrics().stringWidth(s));

        }

        int clickIdx=-1;
        for(int i=0;i < Math.min(hItemNum,l.length);++i){
            Object o=l[i];
            String s=o.toString();
            if(button(s, tW,true)){
                clickIdx=i;
            }

        }
        int scrollH=(int)(lH*((hItemNum < l.length)?((float)hItemNum)/l.length:1.0f));
        m_g.setColor(Color.WHITE);
        m_g.fillRect(m_nextPos.x+ tW, yPos, 32, scrollH);
        return clickIdx;
    }
    public void addMouseState(int x,int y){
        m_clickPos=new Point(x,y);
    }
    private boolean isClicked(){
        return null!=m_clickPos;
    }

    private boolean isMouseInRect(int x,int y,int w,int h){
        Point mousePos=m_clickPos;
        if(x < mousePos.x && 
           y< mousePos.y && 
           mousePos.x < (x+w) &&
           mousePos.y < (y+h)){
               return true;
           }

        return false;
    }
    

}