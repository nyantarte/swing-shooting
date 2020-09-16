
package assets;
import java.awt.*;
import config.Config;
import states.*;
/**
 * キャラクタークラス
 */
public class Charactor implements IMove,Cloneable{

    private String m_name;
    public enum TYPE{
        BATTLESHIP,
        AIR_CARRIER,
        CRUISER,
        DESTROYER,
    }
    private TYPE m_type;/*! 艦首*/
    private RANK m_atkRank;     /*!火力*/
    private RANK m_lifeRank;    /*!耐久*/
    private RANK m_airAtkRank;  /*!制空*/
    private RANK m_airDefRank;  /*!対空*/
    private Vector m_pos;
    private Vector m_dir;
    private int m_life;
    private int m_atk;
    private int m_level;
    private ItemSlot[] m_itemSlots=new ItemSlot[Config.ITEM_SLOT_NUM];
    private int m_count=0;
    private boolean m_doHitCheck=true;
    public Charactor(){}
    private Charactor(Charactor c){
        this.m_name=c.m_name;
        this.m_type=c.m_type;
        this.m_atkRank=c.m_atkRank;
        this.m_lifeRank=c.m_lifeRank;
        this.m_airAtkRank=c.m_airAtkRank;
        this.m_airDefRank=c.m_airDefRank;

        this.m_pos=new Vector();
        this.m_dir=new Vector();

        for(int i=0;i < this.m_itemSlots.length;++i){
            if(null!=c.m_itemSlots[i]){
                this.m_itemSlots[i]=(ItemSlot)c.m_itemSlots[i].clone();
            }
        }
        initParams();
    }
    public String getName(){
        return m_name;
    }
    public void setName(String n){
        m_name=n;
    }

    public TYPE getType(){
        return m_type;
    }
    public void setType(TYPE t){
        m_type=t;
    }
    public RANK getAtkRank(){
        return m_atkRank;
    }
    public void setAtkRank(RANK r){
        m_atkRank=r;
    }
    public RANK getLifeRank(){
        return m_lifeRank;
    }
    public void setLifeRank(RANK r){
        m_lifeRank=r;
    }
    public RANK getAirAtkRank(){
        return m_airAtkRank;
    }
    public void setAirAtkRank(RANK r){
        m_airAtkRank=r;
    }
    public RANK getAirDefRank(){
        return m_airDefRank;
    }
    public void setAirDefRank(RANK r){
        m_airDefRank=r;
    }

    public Vector getPos(){
        return m_pos;
    }
    public void setPos(Vector v){
        m_pos=v;
    }

    public int getLife(){
        return m_life;
    }
    public void setLife(int i){
        m_life=i;
    }

    public int getAtk(){
        return m_atk;
    }
    public void setAtk(int i){
        m_atk=i;
    }
    public int getLevel(){
        return m_level;
    }
    public void setLevel(int i){
        m_level=i;
    }

    
    public void paint(Graphics g){
        g.setColor(Color.WHITE);
        g.drawRect((int)m_pos.getX()-Config.DEF_CHARA_SIZE/2, (int)m_pos.getY()-Config.DEF_CHARA_SIZE/2, Config.DEF_CHARA_SIZE,Config.DEF_CHARA_SIZE);
    }
    @Override
    public String toString(){
        return m_name;
    }


    public Object clone(){
        return new Charactor(this);

    }

    public void move(IState s){
        if(0==m_count){
            //オブジェクトは常に画面右端から登場する
            float y=Config.DEF_CHARA_SIZE/2+ (float)(Math.random()*(Config.MAIN_WIN_H-Config.DEF_CHARA_SIZE));
            Vector pos=new Vector(Config.MAIN_WIN_W-1,y,0);
            setPos(pos);
            setDir(new Vector(-Config.FIELD_MOVE_SPEED,0,0));
        }else{
            ShootingState ss=(ShootingState)s;
            for (ItemSlot itemSlot : m_itemSlots) {
                if(null!=itemSlot && null!=itemSlot.item){
                    long subTime=ss.getCurTime()- itemSlot.prevUsed;
                    if(itemSlot.item.getCoolTime() < subTime){
                        Item item=itemSlot.item;
                        if(Item.TYPE.CANNON== item.getType()){
                            Vector v=Vector.sub(ss.getPlayerPos(), getPos());
                            v=Vector.mul(Vector.normalize(v),Config.BULLET_MOVE_SPEED);
                            ss.addBullet(getPos(), v, itemSlot.item.getAtk());
                        }

                        itemSlot.prevUsed=ss.getCurTime();
                        break;
                    }
                }
            }
        }

        ++m_count;
        m_pos=Vector.add(m_pos,m_dir);
    }
    public void setDir(Vector v){
        m_dir=v;
    }

    public void setItem(int i,Item item){
        m_itemSlots[i]=new ItemSlot(item);
    }
    public Item getItem(int i){
        return (null!=m_itemSlots[i])?m_itemSlots[i].item:null;

        
    }
    public boolean doHitCheck(){
        return m_doHitCheck;
    }
    public void setDoHitCheck(boolean f){
        m_doHitCheck=f;
    }

    public void onHit(int atk){
        m_life-=atk;
    }

    private void initParams(){
        m_life=m_lifeRank.charaLifeValue;
        m_atk=m_atkRank.charaAtkValue;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof Charactor){
            Charactor c=(Charactor)o;

            return m_name.equals(c.m_name);
        }else{
            String n=(String)o;
            return m_name.equals(n);
        }
    }
}
