
package config;
import java.awt.*;

/**
 * ゲーム内の定数を記述する為のスタティッククラス
 */
public class Config {
    //フォルダパス関連
    public static final String ASSET_DIR_PATH="/Users/shimuya/Desktop/game/assets";
    public static final String ITEM_ASSET_DIR_PATH=ASSET_DIR_PATH+"/item";
    public static final String CHARA_ASSET_DIR_PATH=ASSET_DIR_PATH+"/chara";
    public static final String SHOOTING_TASK_ASSET_DIR_PATH=ASSET_DIR_PATH+"/shooting_task";
 
    //メインウィンドウ関連
    public static final int MAIN_WIN_W=640;
    public static final int MAIN_WIN_H=480;

    public static final int FPS_NUM=15;
    public static final int TIME_PER_FRAME=1000/FPS_NUM;


    public static final Color DEF_TEXT_COLOR=Color.WHITE;

    

    //シューティング関連
    public static final int FIELD_GEN_COUNT=FPS_NUM*5;
    public static final int FIELD_MOVE_COUNT=FIELD_GEN_COUNT;
    public static final float FIELD_MOVE_SPEED=MAIN_WIN_W/FIELD_MOVE_COUNT;
    public static final int DEF_CHARA_SIZE=32;
    public static final int CHARA_HIT_CHECK_SIZE=DEF_CHARA_SIZE;
    public static final int PLAYER_MOVE_SPEED=16;
    public static final int BULLET_SIZE=16;
    public static final int BULLET_HIT_CHECK_SIZE=BULLET_SIZE;
    public static final int BULLET_MOVE_SPEED=PLAYER_MOVE_SPEED;

    public static final int ITEM_SLOT_NUM=5;

    public static final String RETURN_BTN_CAPTION="Return";

    public static final long SRANK_COOL_INTERVAL=TIME_PER_FRAME;
    public static final long ARANK_COOL_INTERVAL=TIME_PER_FRAME*FPS_NUM/2;
    public static final long BRANK_COOL_INTERVAL=TIME_PER_FRAME*FPS_NUM;
    public static final long CRANK_COOL_INTERVAL=(long)(TIME_PER_FRAME*FPS_NUM*1.5f);
    public static final long DRANK_COOL_INTERVAL=TIME_PER_FRAME*FPS_NUM*2;
    public static final long ERANK_COOL_INTERVAL=System.currentTimeMillis();


    public static final int ERANK_CHARA_ATK_VALUE=0;
    public static final int DRANK_CHARA_ATK_VALUE=ERANK_CHARA_ATK_VALUE+1;
    public static final int CRANK_CHARA_ATK_VALUE=DRANK_CHARA_ATK_VALUE+1;
    public static final int BRANK_CHARA_ATK_VALUE=CRANK_CHARA_ATK_VALUE+1;
    public static final int ARANK_CHARA_ATK_VALUE=BRANK_CHARA_ATK_VALUE+1;
    public static final int SRANK_CHARA_ATK_VALUE=ARANK_CHARA_ATK_VALUE+1;

    public static final int ERANK_CHARA_LIFE_VALUE=0;
    public static final int DRANK_CHARA_LIFE_VALUE=ERANK_CHARA_LIFE_VALUE+1;
    public static final int CRANK_CHARA_LIFE_VALUE=DRANK_CHARA_LIFE_VALUE+1;
    public static final int BRANK_CHARA_LIFE_VALUE=CRANK_CHARA_LIFE_VALUE+1;
    public static final int ARANK_CHARA_LIFE_VALUE=BRANK_CHARA_LIFE_VALUE+1;
    public static final int SRANK_CHARA_LIFE_VALUE=ARANK_CHARA_LIFE_VALUE+1;

}