package assets;

import config.Config;

public class Item {

    private String m_name;
    private RANK m_atkRank;
    private RANK m_coolRank;
    public enum TYPE{
        CANNON,
        ANTI_AIR_CANNON,
        TORPEDO
    }
    private TYPE m_type;

    private int m_atk;
    private long m_coolTime;

    public String getName(){
        return m_name;

    }
    public void setName(String n){
        m_name=n;
    }

    public RANK getAtkRank(){
        return m_atkRank;
    }
    public void setAtkRank(RANK r){
        m_atkRank=r;
    }
    public RANK getCoolRank(){
        return m_coolRank;
    }
    public void setCoolRank(RANK r){
        m_coolRank=r;
        if(RANK.S==r){
            m_coolTime=Config.SRANK_COOL_INTERVAL;
        }else if(RANK.A==r){
            m_coolTime=Config.ARANK_COOL_INTERVAL;
        }else if(RANK.B==r){
            m_coolTime=Config.BRANK_COOL_INTERVAL;
        }else if(RANK.C==r){
            m_coolTime=Config.CRANK_COOL_INTERVAL;
        }else if(RANK.D==r){
            m_coolTime=Config.DRANK_COOL_INTERVAL;
        }else{
            m_coolTime=Config.ERANK_COOL_INTERVAL;
        }
    }

    public TYPE getType(){
        return m_type;
    }
    public void setType(TYPE t){
        m_type=t;
    }
    public int getAtk(){
        return m_atk;
    }
    public long getCoolTime(){
        return m_coolTime;
    }
    @Override
    public String toString(){
        return m_name;
    }
}