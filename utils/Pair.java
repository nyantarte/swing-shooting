package utils;
public class Pair<F,S> {
    public F first;
    public S second;


    public Pair(F f,S s){
        first=f;
        second=s;
    }
    public Pair(){}

    @Override
    public String toString(){
        return String.format("Pair first=%s second=%s",first.toString(),second.toString());
    }
}