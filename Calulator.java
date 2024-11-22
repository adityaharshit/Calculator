import java.util.*;
class Calculator {

    String postfix(String expression) {
        StringBuilder pre = new StringBuilder();
        Stack<String> stack = new Stack<>();
        int length = expression.length();
        for (int i = 0; i < length; i++) {
            char ch = expression.charAt(i);
            if (ch == ' ')
                continue;
            if (ch == '(') {
                stack.push("(");
            } else if (ch == ')') {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    pre.append(stack.pop());
                }
                stack.pop();
            } else if ((ch >= '0' && ch <= '9')) {
                int j = i + 1;
                while (j < length && ((expression.charAt(j) >= '0' && expression.charAt(j) <= '9') || expression.charAt(j)=='.')) {
                    j++;
                }
                pre.append(expression.substring(i, j));
                pre.append("$");
                i = j - 1;
            } else {
                while (!stack.isEmpty() && !stack.peek().equals("(")
                        && precedence(stack.peek().charAt(0)) >= precedence(ch)) {
                    pre.append(stack.pop());
                }
                stack.push(Character.toString(ch));
            }
        }
        while (!stack.isEmpty()) {
            pre.append(stack.pop());
        }
        return pre.toString();
    }

    int precedence(Character symbol) {
        switch (symbol) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 3;
            case '(':
                return 100;
        }
        return 0;
    }

    public void evaluate(String pre){
        Stack<Float> stack = new Stack<>();
        int len= pre.length();
        boolean isInvalid=false;
        for(int i=0;i<len;i++){
            if(pre.charAt(i)>='0' && pre.charAt(i)<='9'){
                int j=i+1;
                while(j<len && pre.charAt(j)!='$'){
                    j++;
                }
                float num = Float.parseFloat(pre.substring(i, j));
                stack.push((float)num);
                i=j;
            }else{
                if(stack.size()<2){
                    isInvalid=true;
                    break;
                } 
                char operator=pre.charAt(i);
                float a = stack.pop();
                float b = stack.pop();
                switch (operator) {
                    case '+': 
                        stack.push(a+b);
                        break;
                    case '-': 
                        stack.push(b-a);
                        break;
                    case '*': 
                        stack.push(a*b);
                        break;
                    case '/':
                        if(a==0) isInvalid=true;
                        else stack.push(b/a);
                        break;
                }
            }
        }
        if(stack.size()>1 || isInvalid) System.out.println("Invalid Expression");
        else System.out.println(stack.pop());
    }

    public boolean validateExpression(String expression){
        // Check for brackets
        if(expression.trim().length()==0) return false;
        boolean containsDigits=false;
        Stack<Character> stack = new Stack<>();
        for(int i=0;i<expression.length();i++){
            if(Character.isDigit(expression.charAt(i))) containsDigits=true;
            if(expression.charAt(i)=='(') stack.push('(');
            else if(expression.charAt(i)==')'){
                if(stack.isEmpty()) return false;
                else stack.pop();
            }
        }
        if(!stack.isEmpty() || !containsDigits){
            return false;
        } 
        // Check for incorrect values
        try{
            HashSet<Character> allowedSymbols = new HashSet<>(Arrays.asList('+', '-', '/', '*', '(', ')', ' '));
            for(int i=0;i<expression.length();i++){
                int j=i;
                char ch = expression.charAt(i);
                if (!Character.isDigit(ch) && !allowedSymbols.contains(ch) && ch!='.'){
                    System.out.println("Invalid characters encountered");
                    return false;
                } 
                if(allowedSymbols.contains(ch)) continue;
                while(j<expression.length() && !allowedSymbols.contains(expression.charAt(j))) j++;
                Float.parseFloat(expression.substring(i, j));
            }
        }catch(NumberFormatException nfe){
            System.out.println(nfe.getMessage());
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the expression to calculate : ");
        String expression = sc.nextLine();
        sc.close();
        Calculator c = new Calculator();
        if(!c.validateExpression(expression))System.out.println("Invalid Expression!");
        else{
            String pre = c.postfix(expression);
            c.evaluate(pre);
        }
    }
}