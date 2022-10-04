import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;


public class RpnStacker2 {
	
    private static Scanner sc;

	public static enum TokenType  {
    	
    	NUM, VAR, ATOM, 

    	
    	MINUS, PLUS, SLASH, STAR,
    	
    	EOF
    }
    
    public static class Token {
    	
        public final TokenType  type;
        public final String lexeme; 
        
        public Token(TokenType  type, String value) {
            this.type = type;
            this.lexeme = value;
        }
        
        @Override
        public String toString() {
        	return "Token [type=" + this.type + ", lexeme=" + this.lexeme + "]";
        }
    }

    public static String getLexeme(String string, int index) {
    	
        int j = index;
        
        for( ; j < string.length(); ) {
            if(Character.isLetter(string.charAt(j))) {
                j++;
            } else {
                return string.substring(index, j);
            }
        }
        return string.substring(index, j);
    }
    

    public static List<Token> lex(String input, Boolean badEntry, Vector<String> variablesNames) {
    	
        List<Token> result = new ArrayList<Token>();
        
        for(int i = 0; i < input.length(); ) {
        	
            switch(input.charAt(i)) {
            
            case '-':
                result.add(new Token(TokenType.MINUS, "-"));
                i++;
                break;
            case '+':
                result.add(new Token(TokenType.PLUS, "+"));
                i++;
                break;
            case '/':
                result.add(new Token(TokenType.SLASH, "/"));
                i++;
                break;
            case '*':
                result.add(new Token(TokenType.STAR, "*"));
                i++;
                break;
            case '\u0000':
                result.add(new Token(TokenType.EOF, "\u0000"));
                i++;
                break;
                
            default:
            	
                if(Character.isWhitespace(input.charAt(i))) {
                    i++;
                } else if((int)input.charAt(i) > 47 && (int)input.charAt(i) < 58) { 
                	
                	String number = String.valueOf(input.charAt(i));
                	
                	while((int)input.charAt(i+1) > 47 && (int)input.charAt(i+1) < 58) { 
                		number = number.concat(String.valueOf(input.charAt(i+1)));
                		i++;
                	}
                	
                    result.add(new Token(TokenType.NUM, number));
                    i++;
                    break;
                } else if(variablesNames.contains(Character.toString(input.charAt(i))) || 
                		!Character.isWhitespace(input.charAt(i+1))) { 
                	
                	String variable = Character.toString(input.charAt(i));
                	
                	while(!Character.isWhitespace(input.charAt(i+1))) { 
                		variable = variable.concat(String.valueOf(input.charAt(i+1)));
                		i++;
                	}
                	
                	result.add(new Token(TokenType.VAR, variable)); 
                    i++;
                    break;
                } else {
                    String atom = getLexeme(input, i);
                    i += atom.length();
                    result.add(new Token(TokenType.ATOM, atom));
                }
                
                break;
                
            }
            
        }
        
        return result;
        
    }
    
    public static void main(String[] args) {

        String test = "11 6 2 3 + -11 * / * 17 + 5 +";
        String[] testCharString = test.split(" ");
        
        sc = new Scanner(System.in);
        
        String entry = "";
        
        Vector<String> variablesNames = new Vector<String>();
    	Vector<Integer> variablesValues = new Vector<Integer>();
        
        while(!(entry.equals("stop"))) {							// para sair do programa escreva "stop" como input
        	
        	entry = sc.nextLine();
        	String[] entryCharString = entry.split(" ");
        	        	
        	Boolean badEntry = false;
        	Vector<Token> badEntries = new Vector<Token>();
        	
        	List<Token> tokens = null;
        	
        	if (entry.contains("=")) {								
        		String[] splitTemp = entry.split("=");
        		variablesNames.add(splitTemp[0].replaceAll(" ", ""));
        		variablesValues.add(Integer.parseInt(splitTemp[1].replaceAll(" ", "")));
        		
        	} else if (entry.equals("test")) {						
        															
        		tokens = lex(test, badEntry, variablesNames);
        		
            	RPN_Stack str = new RPN_Stack();
            	int result = str.RPN_Stacker(testCharString, variablesNames, variablesValues);
            	System.out.println("No bad entries, here is your result:");
            	System.out.println(result);
        		
        	} else {
        		
        		tokens = lex(entry, badEntry, variablesNames);
        		
        		for(Token t : tokens) {								// procura por entradas falhas
                    System.out.println(t);
                    if ((t.type == RpnStacker2.TokenType.ATOM) || (t.type == RpnStacker2.TokenType.VAR && !variablesNames.contains(t.lexeme))) {
                    	badEntries.add(t);
                    	badEntry = true;
                    }
                }
        		
            	if(badEntry == false){ 								// se não tiver entrada falha
                	RPN_Stack str = new RPN_Stack();
                    int result = str.RPN_Stacker(entryCharString, variablesNames, variablesValues);
                    System.out.println();
                    System.out.println("No bad entries, here is your result:");
                    System.out.println(result); 					// resultado
                } else {
                	System.out.println();
                	System.out.println("You typed this/these bad entry/entries:");
                	for (Token b: badEntries) {
                		System.out.println(b.lexeme); 				// se não, printar entrada falha
                	}
                	
                }
        		
        	}
        	
        }
        
    }

}