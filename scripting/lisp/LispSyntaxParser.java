package scripting.lisp;

/**
 * @brief Class to 
 */
public class LispSyntaxParser {

    
    private LispVM m_vm;

    public LispSyntaxParser(LispVM vm,LispLexerParser p)
    throws LispParsingError
    {
        m_vm=vm;
        parseChunck(p);
    }
    private void parseChunck(LispLexerParser p)
    throws LispParsingError
    {
        while(!p.isEnd()){
            parseValue(p);

        }
    }

    private void parseValue(LispLexerParser p) throws LispParsingError{
        String t=p.peekToken();
        if("(".equals(t)){
            parseList(p);
            
        }else if(Character.isDigit(t.charAt(0))){
            parseNum(p);
        }else if(Character.isLetter(t.charAt(0))){
            parseIdent(p);
        }else if('\"'==t.charAt(0)){
            parseLitStr(p);
        }
    }
    private void parseList(LispLexerParser p)
    throws LispParsingError
    {
        assert "("==p.peekToken();
        String t=p.getNextToken();

        if(!")".equals(t)){
            String fname=t;
            if("let".equals(fname)){
                parseLet(p);
            }else if("label".equals(fname)){
                parseLabel(p);
            }else if("ret".equals(fname)){
                parseRet(p);
            }
            else{
                p.getNextToken();
                int argNum=parseArgs(p);
                m_vm.addCallCmd(fname,argNum);

            }
            
        }
    }

    private int parseArgs(LispLexerParser p)
    throws LispParsingError
    {
        String t=p.peekToken();
        int argNum=0;
        while(!p.isEnd() && !")".equals(t)){
            ++argNum;
            t=p.peekToken();
            parseValue(p);
            if("(".equals(t)){
                m_vm.addPushRCCmd();
            }
            t=p.peekToken();
        }
        p.getNextToken();
        return argNum;
    }
    private void parseLet(LispLexerParser p)
    throws LispParsingError
    {
        assert "let".equals(p.peekToken());

        String vname=p.getNextToken();
        String t=p.getNextToken();
        if(!")".equals(t)){
            parseValue(p);
        }
        m_vm.addBindCmd(vname);
        p.getNextToken();
    }
    private void parseNum(LispLexerParser p){
        m_vm.addPushCmd(Float.parseFloat(p.peekToken()));
        p.getNextToken();
    }

    private void parseIdent(LispLexerParser p){
        m_vm.addPushDefinedObj(p.peekToken());
        p.getNextToken();
    }
    private void parseLitStr(LispLexerParser p)throws LispParsingError{
        String t=p.peekToken();
        if('\"'!=t.charAt(t.length()-1)){
            throw new LispParsingError("Literal string needs \" ");
        }

        m_vm.addPushCmd(t.substring(1, t.length()-1));
        p.getNextToken();
    }
    private void parseLabel(LispLexerParser p)
    throws LispParsingError
 
    {
        String ln=p.getNextToken();
        m_vm.reg(ln,m_vm.getCmdSize());
        String t=p.getNextToken();
        if(")".equals(t)){
            throw new LispParsingError("Label statement needs )");
        }
    }
    private void parseRet(LispLexerParser p)
    throws LispParsingError
    {
        m_vm.addRetCmd();
        String t=p.getNextToken();
        if(")".equals(t)){
            throw new LispParsingError("Ret statement needs )");
        }
    }
}