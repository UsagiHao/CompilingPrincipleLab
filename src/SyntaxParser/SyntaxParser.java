package SyntaxParser;

import LexicalAnalyzer.LexicalAnalyzer;
import Tools.Token;

import java.util.ArrayList;
import java.util.Arrays;

public class SyntaxParser {
    private static ArrayList<Token> stack = new ArrayList<>();//文法符号栈
    private static ArrayList<Token> input = new ArrayList<>();//输入缓冲区
    private static ArrayList<String> output = new ArrayList<>();//结果输出流
    //预测分析表
    private static int[][] M = {
            {0, -1, -1, 1, -1, -1, -1, -1, -1, 2, -1, -1, -1, -1, -1, -1},
            {3, -1, -1, -1, 3, -1, -1, -1, -1, -1, -1, -1, 3, -1, -1, -1},
            {-1, -1, 5, -1, -1, 5, -1, -1, -1, -1, 4, -1, -1, -1, -1, 5},
            {6, -1, -1, -1, 6, -1, -1, -1, -1, -1, -1, -1, 6, -1, -1, -1},
            {-1, -1, 8, -1, -1, 8, -1, -1, -1, -1, 8, 7, -1, -1, -1, 8},
            {11, -1, -1, -1, 9, -1, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1},
            {12, -1, -1, -1, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
            {-1, -1, -1, -1, -1, 14, -1, -1, -1, -1, -1, -1, -1, 13, -1, 14},
            {16, -1, -1, -1, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
    };

    private static String[] generations = {
            "S->id=E;",
            "S->if(C){S}else{S}",
            "S->while(C){S}",
            "E->TE'",
            "E'->+TE'",
            "E'->ε",
            "T->FT'",
            "T'->*FT'",
            "T'->ε",
            "F->(E)",
            "F->num",
            "F->id",
            "C->DC'",
            "C'->||DC'",
            "C'->ε",
            "D->(C)",
            "D->id==num"
    };

    private static String[] VnList = {"S", "E", "E'", "T", "T'", "F", "C", "C'", "D"};

    private static void parse() {
        while (!input.isEmpty()){
            Token inputElement = input.get(0);
            Token stackElement = stack.get(stack.size() - 1);
            //非终结符
            if (Arrays.asList(VnList).contains(stackElement.getProperty())) {
                boolean result = reduce(stackElement, inputElement);
                if (!result) {
                    return;
                }
            }
            //终结符
            else {
                //匹配,两者皆弹出
                if (inputElement.getType() == "id" && stackElement.getType() == "Vt") {
                    input.remove(0);
                    stack.remove(stack.size() - 1);
                } else if (inputElement.getType() == "int" && stackElement.getType() == "Vt") {
                    input.remove(0);
                    stack.remove(stack.size() - 1);
                } else if (inputElement.getProperty().equals(stackElement.getProperty())) {
                    input.remove(0);
                    stack.remove(stack.size() - 1);
                }
                //不匹配,错误
                else {
                    System.out.println("Error");
                    return;
                }

            }
        }
        for (String anOutput : output) {
            System.out.println(anOutput);
        }
    }

    private static boolean reduce(Token stackToken, Token inputToken) {
        int rowIndex = getRowIndex(stackToken);
        int columnIndex = getColumnIndex(inputToken);
        if ((rowIndex != -1) && (columnIndex != -1)) {
            int item = M[rowIndex][columnIndex];
            if (item == -1) {
                System.out.println("No generation Error");
                return false;
            } else {
                output.add(generations[item]);
                stack.remove(stack.size() - 1);
                switch (item) {
                    case 0:
                        stack.add(new Token("punctuation", ";"));
                        stack.add(new Token("Vn", "E"));
                        stack.add(new Token("operation", "="));
                        stack.add(new Token("Vt", "id"));
                        break;
                    case 1:
                        stack.add(new Token("punctuation", "}"));
                        stack.add(new Token("Vn", "S"));
                        stack.add(new Token("punctuation", "{"));
                        stack.add(new Token("reservedWord", "else"));
                        stack.add(new Token("punctuation", "}"));
                        stack.add(new Token("Vn", "S"));
                        stack.add(new Token("punctuation", "{"));
                        stack.add(new Token("operation", ")"));
                        stack.add(new Token("Vn", "C"));
                        stack.add(new Token("operation", "("));
                        stack.add(new Token("reservedWord", "if"));
                        break;
                    case 2:
                        stack.add(new Token("punctuation", "}"));
                        stack.add(new Token("Vn", "S"));
                        stack.add(new Token("punctuation", "{"));
                        stack.add(new Token("operation", ")"));
                        stack.add(new Token("Vn", "C"));
                        stack.add(new Token("operation", "("));
                        stack.add(new Token("reservedWord", "while"));
                        break;
                    case 3:
                        stack.add(new Token("Vn", "E'"));
                        stack.add(new Token("Vn", "T"));
                        break;
                    case 4:
                        stack.add(new Token("Vn", "E'"));
                        stack.add(new Token("Vn", "T"));
                        stack.add(new Token("reservedWord", "+"));
                        break;
                    case 5:
                        break;
                    case 6:
                        stack.add(new Token("Vn", "T'"));
                        stack.add(new Token("Vn", "F"));
                        break;
                    case 7:
                        stack.add(new Token("Vn", "T'"));
                        stack.add(new Token("Vn", "F"));
                        stack.add(new Token("operation", "*"));
                        break;
                    case 8:
                        break;
                    case 9:
                        stack.add(new Token("operation", ")"));
                        stack.add(new Token("Vn", "E"));
                        stack.add(new Token("operation", "("));
                        break;
                    case 10:
                        stack.add(new Token("Vt", "num"));
                        break;
                    case 11:
                        stack.add(new Token("Vt", "id"));
                        break;
                    case 12:
                        stack.add(new Token("Vn", "C'"));
                        stack.add(new Token("Vn", "D"));
                        break;
                    case 13:
                        stack.add(new Token("Vn", "C'"));
                        stack.add(new Token("Vn", "D"));
                        stack.add(new Token("operation", "||"));
                        break;
                    case 14:
                        break;
                    case 15:
                        stack.add(new Token("operation", ")"));
                        stack.add(new Token("Vn", "C"));
                        stack.add(new Token("operation", "("));
                        break;
                    case 16:
                        stack.add(new Token("Vt", "num"));
                        stack.add(new Token("operation", "=="));
                        stack.add(new Token("Vt", "id"));
                        break;
                }
            }
            return true;

        } else {
            System.out.println("Reduce Error");
            return false;
        }
    }


    private static int getRowIndex(Token token) {
        for (int i = 0; i < VnList.length; i++) {
            if (VnList[i].equals(token.getProperty())) {
                return i;
            }
        }
        return -1;
    }

    private static int getColumnIndex(Token token) {
        String vt = token.getProperty();
        if (token.getType() == "id") {
            vt = "id";
        }
        if (token.getType() == "int") {
            vt = "num";
        }
        switch (vt) {
            case "id":
                return 0;
            case "=":
                return 1;
            case ";":
                return 2;
            case "if":
                return 3;
            case "(":
                return 4;
            case ")":
                return 5;
            case "{":
                return 6;
            case "}":
                return 7;
            case "else":
                return 8;
            case "while":
                return 9;
            case "+":
                return 10;
            case "*":
                return 11;
            case "num":
                return 12;
            case "||":
                return 13;
            case "==":
                return 14;
            case "$":
                return 15;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        ArrayList<Token> tokens = lexicalAnalyzer.getTokens();
        input.addAll(tokens);
        stack.add(new Token("Vt", "S"));
        parse();

    }

}