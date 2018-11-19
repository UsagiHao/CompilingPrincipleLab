package LexicalAnalyzer;

import Tools.Token;
import Tools.Tool;

import java.io.*;
import java.util.ArrayList;

public class LexicalAnalyzer {
    private static ArrayList<Character> input = new ArrayList<>();
    private static int index = 0;

    //处理输入
    public static void getInput(){
        String inputFile = "src/LexicalAnalyzer/input.txt";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
            String line="";
            while((line = br.readLine()) != null){
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ' ' || line.charAt(i) == '\n') {
                        continue;
                    }
                    input.add(line.charAt(i));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Token scanner(){
        ArrayList<Character> words = new ArrayList<>();
        char ch = input.get(index++);

        if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_') || (ch == '$')){
            while ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch == '_') || (ch == '$')){
                words.add(ch);
                ch = input.get(index++);
                //判断是否是关键字
                for(int i = 0; i < Tool.reservedWords.length; i++){
                    if (Tool.charsToString(words).equals(Tool.reservedWords[i])){
                        index--;
                        return new Token("reservedWord", Tool.reservedWords[i]);
                    }
                }
            }
            //不是关键字则是标识符
            index--;
            return new Token("id", Tool.charsToString(words));
        }
        //判断是否是数字
        else if (ch >= '0' && ch <= '9') {
            int num = 0;
            while (ch >= '0' && ch <= '9') {
                num = num * 10 + ch - '0';
                ch = input.get(index++);
            }
            index--;
            //大小越界
            if (num < 0) {
                return new Token("error", "overflow");
            }
            else{
                return new Token("int", String.valueOf(num));
            }
        }
        //其他情况
        else {
            words.add(ch);
            switch (ch){
                case '"':
                    do{
                        ch = input.get(index++);
                        words.add(ch);
                    }while(ch != '"');
                    return new Token("string", Tool.charsToString(words));
                case '+':
                    ch = input.get(index++);
                    if(ch == '+'){
                        return new Token("operation", "++");
                    }
                    else if(ch == '='){
                        return new Token("operation", "+=");
                    }

                    else {
                        index--;
                        return new Token("operation", "+");
                    }

                case '-':
                    ch = input.get(index++);
                    if(ch == '-'){
                        return new Token("operation", "--");
                    } else if(ch == '='){
                        return new Token("operation", "-=");
                    } else if (ch >= '0' && ch <= '9') { // 可能是负数
                        int num = 0;
                        while (ch >= '0' && ch <= '9') {
                            num = num * 10 + ch - '0';
                            ch = input.get(index++);
                        }
                        if (num < 0) {
                            return new Token("error", "overflow");
                        }
                        return new Token("int", String.valueOf(num*(-1)));
                    }
                    else{
                        return new Token("operation", "-");
                    }
                case '=':
                    ch = input.get(index++);
                    if(ch == '='){
                        return new Token("operation", "==");
                    }
                    else {
                        index--;
                        return new Token("operation", "=");
                    }
                case '!':
                    ch = input.get(index++);
                    if(ch == '='){
                        return new Token("operation", "!=");
                    }
                    else {
                        index--;
                        return new Token("operation", "!");
                    }
                case '<':
                    ch = input.get(index++);
                    if(ch == '='){
                        return new Token("operation", "<=");
                    }
                    else if(ch == '<'){
                        return new Token("operation", "<<");
                    }
                    else if(ch == '>'){
                        return new Token("operation", "<>");
                    }
                    else {
                        index--;
                        return new Token("operation", "<");
                    }
                case '>':
                    ch = input.get(index++);
                    if(ch == '='){
                        return new Token("operation", ">=");
                    }
                    else if(ch == '>'){
                        return new Token("operation", ">>");
                    }
                    else {
                        index--;
                        return new Token("operation", ">");
                    }
                case '&':
                    ch = input.get(index++);
                    if(ch == '&'){
                        return new Token("operation", "&&");
                    }
                    else {
                        index--;
                        return new Token("operation", "&");
                    }

                case '|':
                    ch = input.get(index++);
                    if(ch == '|'){
                        return new Token("operation", "||");
                    }
                    else {
                        index--;
                        return new Token("operation", "|");
                    }
                case '[':
                case ']':
                case '(':
                case ')':
                case '*':
                case '/':
                case '%':
                    return new Token("operation", String.valueOf(ch));
                case '{':
                case '}':
                case ',':
                case ';':
                case '.':
                    return new Token("punctuation", String.valueOf(ch));
                default:
                    return new Token("error", "undefined token");

            }
        }

    }


    public static void createOutput(ArrayList<Token> output){
        File outputFile = new File("src/LexicalAnalyzer/output.txt");
        try {
            outputFile.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, false));
            for (Token t : output) {
                bw.write(t.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Token> getTokens(){
        ArrayList<Token> tokens = new ArrayList<>();
        getInput();
        while(index < input.size()){
            Token token = scanner();
            tokens.add(token);
        }
        return tokens;
    }

    public static void main(String[] args){
        getInput();
        ArrayList<Token> result = new ArrayList<>();
        while(index < input.size()){
            Token token = scanner();
            result.add(token);
        }
        createOutput(result);
    }

}