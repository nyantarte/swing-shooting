package game;

import javax.swing.*;

import ui.UISurface;
import config.*;

import java.awt.*;
import java.awt.event.*;

/**
 * メインウィンドウクラス
 */
public class MainWindow extends JFrame implements ActionListener{


    private Timer m_timer=new Timer(Config.TIME_PER_FRAME,this);/*!メインループ用のタイマー*/
    private Font m_font;
    private UISurface m_uiSurface;
    public MainWindow(int w,int h){
        setSize(w,h);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        m_font=new Font(getFont().getFontName(),getFont().getStyle(),32);
        m_uiSurface=new UISurface(w, h, m_font);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                m_uiSurface.addMouseState((int)e.getX(), (int)e.getY());
            }
            
        });
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                GameEngine.addKeyPressedState(e.getKeyCode());
            }
            @Override
            public void keyReleased(KeyEvent e){
                GameEngine.addKeyReleasedState(e.getKeyCode());
            }
        });
        m_timer.setRepeats(true);
        m_timer.start();
    }

    @Override
    public void paint(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        GameEngine.getCurState().onPaint(g);
        if(null!=m_uiSurface){
            m_uiSurface.setGraphics(g);
            GameEngine.getCurState().onTimer(m_uiSurface);
        }
    }
    public void actionPerformed(ActionEvent e){
                
        repaint();
    }
}