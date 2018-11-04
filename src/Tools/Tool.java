package Tools;

import java.util.ArrayList;

public class Tool {

    //保留字
    public static String[] reservedWords={"class", "public", "protected", "private", "static", "void", "main", "int", "double", "float", "char",
            "String", "if", "else", "else if", "switch", "case", "for", "while", "do", "break", "try","catch"};

    //操作符
    static String[] operation={"[", "]", "(", ")", "++", "--", "!",  "*", "/", "%", "+", "-", "<<", ">>", "<>", "<", "=", ">", "==", "!=", "|",
            "&", "&&", "||", "+=", "-=", "<=", ">="};

    //界符（标点符号）
    static String[] punctuation={",", "{", "}", ";", "."};

    public static String charsToString(ArrayList<Character> list){
        char[] chars = new char[list.size()];
        for(int i = 0; i < list.size(); i++){
            chars[i] = list.get(i);
        }
        return String.valueOf(chars);
    }
}
