import java.util.Scanner;
import java.util.Stack;


public class Conventer {
    private static String operators = "(+-*/^";
    public static double evaluate (String expression){
        String postfixExp = infixToPostfix(expression);
        Stack<Double> arr = new Stack<Double>();
        String[] operands = postfixExp.split(" ");
        double result = 0;
        for (int i = 0; i < operands.length; ++i){
            String curr =  operands[i];
            double digit;
            if(curr.equals("0")){
                ++i;
                arr.push(Double.parseDouble(operands[i]) * -1);
            } else if (curr.equals("_")){
                Stack<Double> tempArr = new Stack<Double>();
                do {
                    double temp = arr.pop();
                    if(temp != result) {
                        temp *= -1;
                        tempArr.push(temp);}
                }while (arr.size() > 0);
                arr = tempArr;
            }
            else {
                try {
                    arr.push(Double.parseDouble(curr));
                }
                catch(NumberFormatException ex) {
                    double firstNum = arr.pop();
                    double secondNum = arr.pop();
                    switch(curr)
                    {
                        case "+":
                            result = secondNum + firstNum;
                            break;
                        case "-":
                            result = secondNum - firstNum;
                            break;
                        case "*":
                            result = secondNum * firstNum;
                            break;
                        case "/":
                            if (firstNum == 0)throw new ArithmeticException();
                            result = secondNum / firstNum;
                            break;
                        case "^":
                            result = Math.pow(secondNum, firstNum);
                            break;
                        default:
                            throw new ArithmeticException();
                    }
                    arr.push(result);
                }
            }
        }
        return  result;
    }
    public static String infixToPostfix(String exp) {
        exp = exp.replaceAll("\\s", "");
        StringBuilder result = new StringBuilder();
        char temp;
        Stack<Character> operator = new Stack<>();
        boolean isMinusBeforeBkt = false;
        char[] chars = new char[exp.length()];
        exp.getChars(0, exp.length(), chars, 0);

        for (int i = 0; i < chars.length; ++i){
            char symbol = chars[i];
            if(Character.isDigit(symbol)){
                String stemp = checkNumberDigits(chars, i);
                i += stemp.length()-1;
                result.append(stemp + " ");
            } else if (symbol == '('){
                operator.push(symbol);
            } else if (symbol == ')'){
                if (isMinusBeforeBkt){
                    result.append("_ ");
                }
                temp = operator.pop();
                while (temp != '('){
                    result.append(temp + " ");
                    temp = operator.pop();
                }
                if (!operator.contains('(')){
                    isMinusBeforeBkt = false;
                }}
            else{
                if (symbol == '-' && (i == 0 || (operators.indexOf(exp.charAt(i-1)) != -1 && (Character.isDigit(chars[i+1]) || chars[i+1] == '(')))){
                    if(chars[i+1]=='('){
                        isMinusBeforeBkt = true;
                    } else {
                        result.append("0 ");
                    }

                } else if (operator.size() != 0 && precedence(operator.peek(), symbol)) {
                    if (isMinusBeforeBkt)
                        result.append("_ ");
                    temp = operator.pop();
                    while (precedence(temp, symbol)) {
                        result.append(temp + " ");
                        if (operator.size() == 0) {
                            break;
                        }
                        temp = operator.pop();
                    }
                    operator.push(symbol);
                }
                else{
                    operator.push(symbol);
                }
            }
        }
        if(isMinusBeforeBkt)
            result.append("_ ");
        while (operator.size() > 0) {
            temp = operator.pop();
            result.append(temp + " ");
        }

        return result.toString();
    }

    private static boolean precedence (char firstOper, char secondOper){
        int [] precedence = {1, 2, 2, 3, 3, 3};
        return precedence[operators.indexOf(firstOper)] >= precedence[operators.indexOf(secondOper)];
    }
    private static String checkNumberDigits (char[] exp, int position){
        String result = "";
        for (; position < exp.length; ++position){
            char c = exp[position];
            if (c == '.' || Character.isDigit(c)){
                result += c;
            } else {
                --position;
                break;
            }
        }
        return result;
    }
    private static int checkNumberSize(String expression, int position) { // перевірка чи число не є дробовим
        char[] exp = new char[expression.length()];
        expression.getChars(0, expression.length(), exp, 0);
        for (; position < exp.length; ) {
            char c = exp[position];
            if (c == '.' || Character.isDigit(c)) {
                ++position;
            } else {
                break;
            }
        }
        return position;
    }

    public static double EvalEqn(String exp){
     double res = 0;
     int x = exp.indexOf('x');
     String coeff = "";
     String left = "";
     String right = "";
     double c = 0, l = 0, r = 0;
     for(int i = 0; i < exp.length(); i++){
         int ind = checkNumberSize(exp, i);
         String ex = exp.substring(i, ind);
         if(!operators.contains(ex)){
         if(c==0){
         if(x - 1 == exp.indexOf("*x") || x-1== exp.indexOf("/x")){
             coeff = exp.substring(0, x-1);
             if(exp.charAt(0)== '-'){
             if(ex.length()+1 == coeff.length() && (exp.charAt(i+ex.length()+1)!='*'||exp.charAt(i+ex.length())!='/')){
                 c = Double.parseDouble(ex);}}
             else if(ex.length() == coeff.length() && (exp.charAt(i+ex.length()+1)!='*'||exp.charAt(i+ex.length())!='/')){
                     c = Double.parseDouble(ex);}
             else{
             c = evaluate(coeff);
             }
             i = x+1;}
         }else if(r==0 && i > exp.indexOf('=')){
                 if(exp.charAt(exp.indexOf('=')+1)=='-'){
                     right = exp.substring(i, exp.length());
                     if(ind-i == ex.length()){
                         String rightMinus = exp.substring(i - 1, ind);
                         r = Double.parseDouble(rightMinus);}
                     else{
                         r = evaluate(right);}
                 }else{
                     right = exp.substring(i, exp.length());
                     if(ind-i == ex.length() && right.length() == ex.length()){
                         r = Double.parseDouble(ex);}
                     else{
                         r = evaluate(right);
                     }
                 }
                 i = exp.length();
             }
         else if (exp.charAt(i+ex.length())== '='){
             if(exp.charAt(x+1)=='+'){
                 left = exp.substring(x+2, i+ex.length());
                 if(exp.indexOf('=')-(exp.indexOf('+')+1)==ex.length()){
                     l = Double.parseDouble(ex);
                 }else{
                 l = evaluate(left);
                 }
             }else if(exp.charAt(x+1)=='-'){
                 String leftMi = exp.substring(i-1, ind);
                 left = exp.substring(x+1, i+ex.length());
                 if(exp.indexOf('=')-exp.indexOf('-') == leftMi.length()){
                     l = Double.parseDouble(leftMi);
                 }else{
                 l = evaluate(left);
                 }
             }
             i = exp.indexOf('=');
         }
     }}
     if(x - 1 == exp.indexOf("*x")){
     res = (r-l)/c;}
     else if (x-1== exp.indexOf("/x")) {
     res = c/(r-l);
     }
        return res;
    }

public static void main(String[] args) {
        int i = 0;
        int id = 1;
        String idexp = "";
        String exp = "";
        String res = "";


        Scanner scan = new Scanner(System.in);

        System.out.println("Input expression");
        exp = scan.nextLine(); // сканування виразу
        res = String.valueOf(EvalEqn(exp));
        System.out.println(res);
    }
}
