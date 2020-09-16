package scripting.lisp;

import java.util.*;
/**
 * 
 * lisp仮想機械の為のクラス
 */
public class LispVM {

    /**
     * @brief Basic command class to execute for virtual machine
     */
    private static abstract class Cmd{

        /**
         * @brief Method to execute the command
         * @param vm Virtual machine instance to execute for.
         */
        abstract void exec(LispVM vm);

    }
    
    private static class PushCmd extends Cmd{

        public Object op1;

        public PushCmd(Object o){
            op1=o;
        }
        public void exec(LispVM vm){
            vm.m_stack.push(op1);
        }
        @Override
        public String toString(){
            return String.format("push %s",op1.toString());
        }
    }
    private static class PushObjCmd extends Cmd{

        public String name;
        public PushObjCmd(String n){
            name=n;
        }
        public void exec(LispVM vm){
            vm.m_stack.push(vm.m_nameTbl.get(name));
        }

        @Override
        public String toString(){
            return String.format("push obj %s",name);
        }
    }

    private static class PushRCCmd extends Cmd{
        public void exec(LispVM vm){
            vm.m_stack.push(vm.m_rc);
        }

        @Override
        public String toString(){
            return "push rc";
        }

    }
    private static class CallCmd extends Cmd{

        public String name;
        public int argNum;
        public CallCmd(String n,int an){
            name=n;
            argNum=an;
        }
        public void exec(LispVM vm){
            NativeMethodType m=(NativeMethodType) vm.m_nameTbl.get(name);
            int rc=m.call(vm,argNum,vm.m_stack);
            if(1 < rc){
                Object[] a=new Object[rc];
                for(int i=0;i < a.length;++i){
                    a[a.length-i-1]=vm.m_stack.pop();
                }
                vm.m_rc=a;
            }else if(1==rc){
                vm.m_rc=vm.m_stack.pop();
            }else{
                vm.m_rc=null;
            }

        }

        @Override
        public String toString(){
            return String.format("call %s", name);
        }

    }
    private static class BindCmd extends Cmd{
        public String name;
        public BindCmd(String n){
            name=n;
        }
        public void exec(LispVM vm){
            
            vm.m_nameTbl.put(name,vm.m_rc=vm.m_stack.pop());
        }
        @Override
        public String toString(){
            return String.format("bind %s",name);
        }
    }

    private static class RetCmd extends Cmd{
        public void exec(LispVM vm){
            vm.m_cmdPos=(Integer)vm.m_stack.pop();

        }

        @Override
        public String toString(){
            return "ret";
        }
    }

    public static abstract class NativeMethodType{
        public abstract int call(LispVM vm,int argNum, Stack<Object> o);
    }
    private HashMap<String,Object> m_nameTbl=new HashMap<>();
    private Stack<Object> m_stack=new Stack<>();
    private List<Cmd> m_cmdBuf=new ArrayList<>();
    private int m_cmdPos;
    private Object m_rc;
    public void addPushCmd(Object o){
        m_cmdBuf.add(new PushCmd(o));
    }

    public void addPushDefinedObj(String n){
        m_cmdBuf.add(new PushObjCmd(n));
    }
    public void addPushRCCmd(){
        m_cmdBuf.add(new PushRCCmd());
    }
    public void addCallCmd(String f,int argNum){
        m_cmdBuf.add(new CallCmd(f,argNum));
    }
    public void addBindCmd(String n){
        m_cmdBuf.add(new BindCmd(n));
    }
    public void addRetCmd(){
        m_cmdBuf.add(new RetCmd());
    }
    public void dump(){
        for(Cmd c:m_cmdBuf){
            System.out.println(c.toString());
        }
    }

    public void run(){
        int i=0;
        run(i);
    }
    private void run(int i){
//        m_retPosStack.push(m_cmdPos);
        m_stack.push(m_cmdPos);
        m_cmdPos=i;
        while(m_cmdPos < m_cmdBuf.size()){
            Cmd cmd=m_cmdBuf.get(m_cmdPos++);
            cmd.exec(this);
        
        }
        m_cmdPos=(Integer)m_stack.pop();


    }

    public void run(String l){
        int i=(Integer)m_nameTbl.get(l);
        run(i);
    }
    public void reg(String name,Object f){
        m_nameTbl.put(name,f);
    }

    public int getCmdSize(){
        return m_cmdBuf.size();
    }
}