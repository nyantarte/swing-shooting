package assets;
import config.*;
public enum RANK {
    S(Config.SRANK_CHARA_LIFE_VALUE,Config.SRANK_CHARA_ATK_VALUE),
    A(Config.ARANK_CHARA_LIFE_VALUE,Config.ARANK_CHARA_ATK_VALUE),
    B(Config.BRANK_CHARA_LIFE_VALUE,Config.BRANK_CHARA_ATK_VALUE),
    C(Config.CRANK_CHARA_LIFE_VALUE,Config.CRANK_CHARA_ATK_VALUE),
    D(Config.DRANK_CHARA_LIFE_VALUE,Config.DRANK_CHARA_ATK_VALUE),
    E(Config.ERANK_CHARA_LIFE_VALUE,Config.ERANK_CHARA_ATK_VALUE);


    public int charaLifeValue;
    public int charaAtkValue;

    private RANK(int cl,int ca){
        charaLifeValue=cl;
        charaAtkValue=ca;
    }
}