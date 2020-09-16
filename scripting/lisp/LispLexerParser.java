package scripting.lisp;

import java.util.*;
/**
 * Class for parsing lisp token lexers
 * lisp字句解析の為のクラス
 */
public class LispLexerParser{

    private List<String> m_tokenList=new ArrayList<>();/*! 切り出した結果を納めるコンテナ*/
    private int m_pos;  /*!切り出し結果の現在の参照位置*/


    /**
     * 
     * @param s String to parse 
     *          解析を行いたいソース文字列
     * @throws  Throws a LispParsingError class when there was a error parsing
     *          解析に不正な字句が見つかったらLispParsingErrorを投げる
     */
    public LispLexerParser(String s)
    throws LispParsingError
    {
        parse(s);
    }
    /**
     * @return 現在参照しているトークン
     */
    public String peekToken(){
        return m_tokenList.get(m_pos);
    }
    /**
     * 参照位置を一つ後ろに進める
     * @return 新しい参照位置にあるトークン
     *         isEndがtrueとなる場合は空文字列
     */
    public String getNextToken(){
        ++m_pos;
        if((m_pos < m_tokenList.size())){
            return m_tokenList.get(m_pos);
    
        }
        return "";
    }
    /**
     * 
     * @return 参照位置が末尾ならtrue
     */
    public boolean isEnd(){
        return m_pos>=m_tokenList.size();
    }

    /**
     * 字句解析を行う
     * @param s 解析対象となる文字列
     */
    private void parse(String s)throws LispParsingError{
        int i=0;
        while(i < s.length()){
            if(Character.isWhitespace(s.charAt(i))){
                ++i;
                continue;
            }

            char c=s.charAt(i);

            if('('==c || ')'==c){
                m_tokenList.add(Character.toString(c));
                ++i;
            }else if(Character.isDigit(c)){
                StringBuilder sb=new StringBuilder();

                do{
                    sb.append(c);
                    ++i;
                }while(i < s.length() && Character.isDigit((c=s.charAt(i))));
                m_tokenList.add(sb.toString());
            }else if(Character.isLetter(c)){
                StringBuilder sb=new StringBuilder();

                do{
                    sb.append(c);
                    ++i;
                }while(i < s.length() && Character.isLetterOrDigit((c=s.charAt(i))));
                m_tokenList.add(sb.toString());
            }else if('\"'==c){
                StringBuilder sb=new StringBuilder();
                do{
                    sb.append(c);
                    ++i;
                }while(i < s.length() && '\"'!=(c=s.charAt(i)));
                sb.append(s.charAt(i++));
                m_tokenList.add(sb.toString());

            }else{
                throw new LispParsingError(String.format("Invalid token %s", c));
            }

        }
    }
}