package assets;

import java.util.*;
import java.awt.*;
public class Vector implements Cloneable{
	public float[] xyz=new float[3];
	static final Vector zero=new Vector(0,0,0);	
	public Vector(){}
	public Vector(float x,float y,float z){
		xyz[0]=x;
		xyz[1]=y;
		xyz[2]=z;
	}

	public Vector(Rectangle r){
		setX(r.x+r.width/2);
		setY(r.y+r.height/2);
		setZ(0.0f);
	}
	public float getX(){
		return xyz[0];
	}
	public void setX(float f){
		xyz[0]=f;
	}
	public float getY(){
		return xyz[1];
	}
	public void setY(float f){
		xyz[1]=f;
	}
	public float getZ(){
		return xyz[2];
	}
	public void setZ(float f){
		xyz[2]=f;
	}

	public float length(){
		return (float)Math.sqrt(getX()*getX()+getY()*getY()+getZ()*getZ());
	
	
	}
	public Rectangle convRect(int w,int h){
		return new Rectangle((int)(getX()-w/2),(int)(getY()-h/2),w,h);		
	}
	public boolean equals(Object o){
		Vector v=(Vector)o;
		return getX()==v.getX() && getY()==v.getY() && getZ()==v.getZ();
	}
	@Override
	public String toString(){
		return String.format("x=%f,y=%f",getX(),getY());
	}
	public Rectangle getRect(int w,int h){
		return new Rectangle((int)(getX()-w/2),(int)(getY()-h/2),w,h);
	}
	public Object clone(){
		return new Vector(getX(),getY(),getZ());
	}
	public static Vector normalize(Vector v){
		float len=v.length();
		Vector r=new Vector();
		for(int i=0;i < v.xyz.length;++i){
			r.xyz[i]=v.xyz[i]/len;
		}
		return r;
	}
	public static Vector add(Vector v1,Vector v2){
	
		Vector r=new Vector();
		for(int i=0;i < v1.xyz.length;++i){
			r.xyz[i]=v1.xyz[i]+v2.xyz[i];
		}
		return r;
	}
	public static Vector sub(Vector v1,Vector v2){
	
		Vector r=new Vector();
		for(int i=0;i < v1.xyz.length;++i){
			r.xyz[i]=v1.xyz[i]-v2.xyz[i];
		}
		return r;
	}
	public static Vector mul(Vector v,float f){
		Vector r=new Vector();
		for(int i=0;i< v.xyz.length;++i){
			r.xyz[i]=v.xyz[i]*f;
		}
		return r;
	}


	public static Vector div(Vector v,float f){
		Vector r=new Vector();
		for(int i=0;i< v.xyz.length;++i){
			r.xyz[i]=v.xyz[i]/f;
		}
		return r;
	}

	public static float getDistance(Vector v1,Vector v2){
		Vector dir=Vector.sub(v1, v2);
		return dir.length();
	}

	public static boolean isCollide(Vector v1,float r1,Vector v2,float r2){
		Vector dir=Vector.sub(v1, v2);
		float dist=dir.length();
		return dist <(r1+r2);
	}
}
