package game;
import java.util.*;
import java.util.logging.Logger;


import java.awt.event.*;
import java.io.*;
import assets.*;
import assets.RANK;
import assets.Charactor.TYPE;
import scripting.lisp.*;
import states.*;
import config.*;
import utils.*;

public class GameEngine{
    private static ArrayList<Charactor> s_charaList=new ArrayList<>();
    private static ArrayList<Charactor> s_playerCharaList=new ArrayList<>();
    private static ArrayList<Item> s_itemList=new ArrayList<>();
    private static Charactor s_playerUse;
    private static LispVM.NativeMethodType s_charaFunc=new LispVM.NativeMethodType(){
        @Override
        public int call(LispVM vm,int argNum, Stack<Object> o){
            Charactor c=new Charactor();
            for(int i=0;i < argNum;++i){
                Pair<String,Object> param=(Pair<String,Object>)o.pop();
                if("atk".equals(param.first)){
                    c.setAtkRank(RANK.valueOf((String)param.second));
                }else if("life".equals(param.first)){
                    c.setLifeRank(RANK.valueOf((String)param.second));
                }else if("airAtk".equals(param.first)){
                    c.setAirAtkRank(RANK.valueOf((String)param.second));
                }else if("airDef".equals(param.first)){
                    c.setAirDefRank(RANK.valueOf((String)param.second));
                }else if("name".equals(param.first)){
                    c.setName((String)param.second);
                }else if("type".equals(param.first)){
                    if(Charactor.s_specifyTbl.containsKey(param.second)){
                        c=(Charactor)Charactor.s_specifyTbl.get(param.second).clone();

                    }else{
                        c.setType(TYPE.valueOf((String)param.second));
                    }
                }else if("item".equals(param.first.substring(0,param.first.length()-1))){
                    int idx=Integer.valueOf(param.first.substring(param.first.length()-1));
                    String name=(String)param.second;
                    for(Item item:s_itemList){
                        if(name.equals(item.getName())){
                            c.setItem(idx, item);
                            break;
                        }
                    }
                }
            }
            s_charaList.add(c);
            o.push(c);
            return 1;
        }

    };


    private static LispVM.NativeMethodType s_pair=new LispVM.NativeMethodType(){
    
        @Override
        public int call(LispVM vm, int argNum, Stack<Object> o) {
            Object second=o.pop();
            String first=(String)o.pop();
            o.push(new Pair<String,Object>(first,second));
            return 1;
        }
    };

    private static LispVM.NativeMethodType s_task=new LispVM.NativeMethodType(){
    
        @Override
        public int call(LispVM vm, int argNum, Stack<Object> o) {
            Logger.getGlobal().info(String.format("Begin %s argNum=%d",this.toString(),argNum));
            ShootingState.Task task=new ShootingState.Task();
            for(int i=0;i < argNum;++i){
                Pair<String,Object> a=(Pair<String,Object>)o.pop();
                if("chara".equals(a.first)){
                    Logger.getGlobal().info(String.format("Parameter chara found %s",a.toString()));
                    for(Charactor c:s_charaList){                
                        if(c.getName().equals(a.second)){
                            task.obj=c;
                            break;
                        }
                    }
                }else if("num".equals(a.first)){
                    task.num=(int)(((Float)a.second).floatValue());
                }else if("levelUp".equals(a.first)){
                    task.numNeedToNextLevel=(int)(((Float)a.second).floatValue());
                }
            }
            o.push(task);
            Logger.getGlobal().info("End "+this.toString());
            return 1;
        }
    };
    private static LispVM.NativeMethodType s_sTaskList=new LispVM.NativeMethodType(){
    
        @Override
        public int call(LispVM vm, int argNum, Stack<Object> o) {
            ArrayList<ShootingState.Task> taskList=new ArrayList<ShootingState.Task>();
            for(int i=0;i < argNum-1;++i){
                ShootingState.Task t=(ShootingState.Task)o.pop();
                taskList.add(t);
            }
            String name=(String)o.pop();
            s_shootingTaskList.put(name,taskList.toArray());
            return 0;
        }
    };

    private static LispVM.NativeMethodType s_itemFunc=new LispVM.NativeMethodType(){
    
        @Override
        public int call(LispVM vm, int argNum, Stack<Object> o) {
            Item item=new Item();
            for(int i=0;i < argNum;++i){
                Pair<String,Object> a=(Pair<String,Object>)o.pop();
                if("name".equals(a.first)){
                    item.setName((String)a.second);
                }else if("atk".equals(a.first)){
                    item.setAtkRank(RANK.valueOf((String)a.second));
                }else if("cool".equals(a.first)){
                    item.setCoolRank(RANK.valueOf((String)a.second));
                }else if("type".equals(a.first)){
                    item.setType(Item.TYPE.valueOf((String)a.second));
                }
            }
            s_itemList.add(item);
            return 0;
        }
    };
    private static MainWindow s_mainWin;
    private static Stack<IState> s_stateStack=new Stack<>();
    private static boolean[] s_keyStates;
    private static HashMap<Integer,Integer> s_keyCodeTbl=new HashMap<>();

    private static HashMap<String,Object[]> s_shootingTaskList=new HashMap<>();
    static{
        s_keyCodeTbl.put(KeyEvent.VK_DOWN, 0);
        s_keyCodeTbl.put(KeyEvent.VK_UP, 1);
        s_keyCodeTbl.put(KeyEvent.VK_LEFT,2);
        s_keyCodeTbl.put(KeyEvent.VK_RIGHT,3);
        s_keyCodeTbl.put(KeyEvent.VK_Z,4);
        s_keyStates=new boolean[s_keyCodeTbl.size()];
    }
    public static IState getCurState(){
        if(s_stateStack.empty()){
            s_stateStack.push(new MenuState());
        }
        return s_stateStack.peek();
    }
    /**
     * ゲームの状態を遷移させる
     * @param s 遷移先の状態オブジェクトへのインスタンス
     */
    public static void setState(IState s){
        s_stateStack.push(s);
    }
    
    /**
     * ゲームの状態を一つ前に戻す
     */
    public static void popState(){
        s_stateStack.pop();
    }
    public static MainWindow getMainWin(){
        if(null==s_mainWin){
            s_mainWin=new MainWindow(Config.MAIN_WIN_W, Config.MAIN_WIN_H);
        }
        return s_mainWin;
    }

    /**
     * テキストファイルを読み込む
     * @param file 読み込むテキストファイル
     * @return 読み込みに成功した場合は読み込んだテキスト、
     *          失敗した場合はnull
     */
    public static String loadTextFile(File file){
        try{
            BufferedReader reader=new BufferedReader(new FileReader(file));
            StringBuffer sb=new StringBuffer();
            String line;
            while(null!=(line=reader.readLine())){
                sb.append(line+"\n");
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;

    }
    /**
     * Lispスクリプトを実行する
     * @param file 実行するスクリプトファイル
     */
    public static void runScriptFile(File file){
        
        String src=loadTextFile(file);
        if(null!=src){
            runVMState(src);
        }
    }
    /**
     * Lisp仮想機械オブジェクトを生成する
     * @return 生成したLisp仮想機械オブジェクト
     */
    private static LispVM createVMState(){
            LispVM vm=new LispVM();
            vm.reg("chara", s_charaFunc);
            vm.reg("pair",s_pair);
            vm.reg("task", s_task);
            vm.reg("sTaskL",s_sTaskList);

            vm.reg("item",s_itemFunc);
            return vm;
    }
    /**
     * Lispスクリプトを実行する
     * @param src   実行するLispスクリプト
     */
    private static void runVMState(String src){
        LispVM vm=createVMState();
        try{
            new LispSyntaxParser(vm, new LispLexerParser(src));
            System.out.println();
            vm.dump();
            vm.run();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void loadItemAssets(){
        runAllScriptsInDir(Config.ITEM_ASSET_DIR_PATH);
        s_itemList.sort(new Comparator<Item>(){
            
            public int compare(Item c1,Item c2){
                return c1.getName().compareTo(c2.getName());
            } 
        });
        dumpItems();
    }
    public static void loadCharaAssets(){
        runAllScriptsInDir(Config.CHARA_ASSET_DIR_PATH);
        s_charaList.sort(new Comparator<Charactor>(){
            
            public int compare(Charactor c1,Charactor c2){
                return c1.getName().compareTo(c2.getName());
            } 
        });
    }


    public static void loadShootingAssets(){
        runAllScriptsInDir(Config.SHOOTING_TASK_ASSET_DIR_PATH);
        dumpShootingTask();
    }

    private static void dumpItems(){
        for(Item i:s_itemList){
            Logger.getGlobal().info(String.format("Name=%s Atk=%s Cool=%s",i.getName(),i.getAtkRank().toString(),i.getCoolRank().toString()));
        }
    }
    private static void dumpShootingTask(){
        for(String k:s_shootingTaskList.keySet()){
            Object[] tl=s_shootingTaskList.get(k);
            Logger.getGlobal().info(String.format("Shooting task %s %d items",k,tl.length));
            for(Object o:tl){
                Logger.getGlobal().info(o.toString());
            }
        }
    }
    public static Object[] getShootingTask(String name){

        return s_shootingTaskList.get(name);
    }
    private static void runAllScriptsInDir(String dirPath){
        String[] fileList=(new File(dirPath)).list();
        for(String fileName:fileList){
            int extIdx=fileName.lastIndexOf(".");
            String ext=fileName.substring(extIdx);
            if(".lsp".equals(ext)){
                runScriptFile(new File(dirPath,fileName));
            }
        }
    }
    public static ArrayList<Charactor> getPlayerCharaList(){
        return s_playerCharaList;
    }
    public static ArrayList<Charactor> getCharaList(){
        return s_charaList;
    }
    public static void setPlayerUseChara(Charactor c){
        s_playerUse=c;
    }
    public static Charactor getPlayerUseChara(){
        return s_playerUse;
    }
    public static void addKeyPressedState(int k){
        int kCode=s_keyCodeTbl.get(k);
        s_keyStates[kCode]=true;

    }
    public static void addKeyReleasedState(int k){
        int kCode=s_keyCodeTbl.get(k);
        s_keyStates[kCode]=false;


    }

    public static boolean isKeyPressed(int k){
        int kCode=s_keyCodeTbl.get(k);
        return s_keyStates[kCode];
    }
}