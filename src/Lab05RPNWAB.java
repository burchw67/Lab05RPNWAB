import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Stack;

public class Lab05RPNWAB
{
    public static void main(String[] args)
    {
        //Intro and input Strings
        String introString = "Welcome to the RPN calculator!\n" +
                             "Author: William A. Burch\n" +
                             "> Strings should be comma delimited with no spaces! Ex: '1,2,+' will yield '1+2'.\n" +
                             "> Square Root, Reciporical, and Sign Flipping operations can be done withh 'sqrt', 'recip, and 'plusminus' respectively\n" +
                             "> Type 'quit' to exit\n";
        String input;
        String quit = "N";

        //Scanner for input, and input reading
        Scanner scanMan = new Scanner(System.in);

        //ArrayList to store tokenizer copy
        ArrayList<String> tokiCopy = new ArrayList<>();

        System.out.println(introString);

        while (quit.charAt(0) != 'Y' && quit.charAt(0) != 'y')
        {
            System.out.print("Enter your comma delimited RPN string: "); // Print introduction string

            //Input reading
            input = scanMan.nextLine();

            if(input.equals("quit"))
            {
                System.out.print("Are you sure you want to quit? [Y/N] ");
                input = scanMan.nextLine();

                if(Character.toUpperCase(input.charAt(0)) == 'N' || Character.toLowerCase(input.charAt(0)) == 'n')
                {
                    break;
                }
                else if(Character.toUpperCase(input.charAt(0)) == 'Y' || Character.toLowerCase(input.charAt(0)) == 'y')
                {
                    quit = "yes";
                    break;
                }
                else
                {
                    break;
                }
            }
            else
            {
                //Tokenizer creation and tokenization
                StringTokenizer toki = new StringTokenizer(input, ",");

                while (toki.hasMoreTokens()) {
                    tokiCopy.add(toki.nextToken());
                }

                if(!tokenCheck(tokiCopy))
                {
                    System.out.println("\nInvalid input string! Try again.\n");
                }
                else
                {
                    tokenMath(tokiCopy);
                }
            }
            tokiCopy.clear();
        }
    }




//--------------------------------------------------------------Helper Methods-------------------------------------------------------------//
    public static boolean tokenCheck(ArrayList<String> tokenMan)
    {
        int opCount = 0;
        int digitCount = 0;
        int unaryCount = 0;
        String token;

        //Read through the tokens to count how many operators, unary operators, and digits there are
        for(int i = 0; i < tokenMan.size(); i++)
        {
            token = tokenMan.get(i);
            if(token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^"))
            {
                opCount++;
            }
            else if(token.equals("sqrt") || token.equals("recip") || token.equals("plusminus"))
            {
                unaryCount++;
            }
            else
            {
                digitCount++;
            }
        }

        //If there is 1 or more unary operators there must be at least one digit.
        if(unaryCount == 0)
        {
            //If there are no unary operators there must be exactly 1 more digits than operators
            return digitCount == opCount + 1;
        }
        else
        {
            //If there is a unary operator, there must be at least 1 digit, and the previous condition must remain true.
            if(digitCount == 0)
                return false;
            else
                return digitCount == opCount + 1;
        }
    }

    public static void tokenMath(ArrayList<String> tokenMan)
    {
        //Stack for tokens
        Stack<Double> tokens = new Stack<>();

        //String for input
        String token = "default";

        //Double for holding left and right hand values and results
        Double result = 0.0;
        Double lhs, rhs, alpDoub;
        boolean invOp = false;

        //Integer for Indexing
        int index = 0;

        //Run through the copied token list and check for operators and digits
        for(int i = 0; i < tokenMan.size(); i++)
        {
            token = tokenMan.get(i);

            //If it is an operator, there must be at least 2 items on the stack
            if(token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^"))
            {
                if(tokens.size() < 2)
                {
                    System.out.println("\nInvalid operator placement! Try again.\n");
                    break;
                }
                else
                {
                    switch(token)
                    {
                        case "+":
                            rhs = tokens.pop();
                            lhs = tokens.pop();
                            result = lhs + rhs;
                            tokens.push(result);
                            break;

                        case "-":
                            rhs = tokens.pop();
                            lhs = tokens.pop();
                            result = lhs - rhs;
                            tokens.push(result);
                            break;

                        case "*":
                            rhs = tokens.pop();
                            lhs = tokens.pop();
                            result = lhs * rhs;
                            tokens.push(result);
                            break;

                        case "/":
                            rhs = tokens.pop();
                            lhs = tokens.pop();

                            if (rhs == 0) {
                                invOp = true;
                                break;
                            } else {
                                result = lhs / rhs;
                                tokens.push(result);
                                break;
                            }

                        case "^":
                            rhs = tokens.pop();
                            lhs = tokens.pop();
                            result = Math.pow(lhs, rhs);
                            tokens.push(result);

                        break;
                    }
                }
            }
            //If it's a unary operator, there must be at least one item on the stack
            else if(token.equals("sqrt") || token.equals("recip") || token.equals("plusminus"))
            {
                if(tokens.isEmpty())
                {
                    System.out.println("\nInvalid operator placement! Try again.\n");
                }
                else
                {
                    switch(token)
                    {
                        case "sqrt":
                            alpDoub = tokens.pop();
                            if (alpDoub < 0.0)
                            {
                                invOp = true;
                                break;
                            }
                            result = Math.sqrt(alpDoub);
                            tokens.push(result);
                            break;

                        case "recip":
                            alpDoub = tokens.pop();
                            result = 1 / alpDoub;
                            tokens.push(result);
                            break;

                        case "plusminus":
                            alpDoub = tokens.pop();
                            result = alpDoub * -1;
                            tokens.push(result);
                            break;
                    }
                }
            }
            else
            {
                tokens.push(Double.parseDouble(token));
            }
        }

        //Kill the process if an invalid operation is detected like /0 or invalid operator placement
        if(invOp == true)
        {
            System.out.println("\nInvalid operator placement! Try Again!\n");
            invOp = false;
            tokens.clear();
        }
        else
        {
            System.out.println("\nThe answer is: " + result + "\n");
            tokens.clear();
        }
    }
}
