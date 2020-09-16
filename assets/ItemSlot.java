package assets;

public class ItemSlot implements Cloneable{

    public long prevUsed;
    public Item item;
    

    public ItemSlot(Item i){
        item=i;
        prevUsed=0;
    }

    public Object clone(){
        return new ItemSlot(item);
    }
}