package analizador;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Halstead {

	private boolean enComentario;
	private HashMap<String, Integer> tokens = new HashMap<String, Integer>();
	private boolean cabeceraMetodo;
	private String lista_operadores[] = {
			"if", "for", "while", "else", "{", "}", "new", "switch","(",")","do","class","extends",
			"return", "finally", "throw", "throws", "=", "==", "+=", "-=", "*=", "/=", ">", "<", ">>",
			"<<", ">>>", ">=", "<=", "+", "/", "*", "-", "++", "--", "return",
			"&&","||"
	};
	private String lista_incontables[] = {
			"public", "static", "private", "[", "]"
	};

	private List<String> incontables = Arrays.asList(lista_incontables);
	private List<String> operadores = Arrays.asList(lista_operadores);
	
	private Pattern patron = Pattern.compile("[/][*].*[*][/]|[/][*].*|.*[*][/]|[/][/].+|[\"].*[\"]|[+*-/%&|^<>=]+|[{()}\\[]|[a-zA-Z0-9-_]+");
	
	public Halstead() {
		this.enComentario = false;
		this.cabeceraMetodo = true;
	}
	
	public void analizarLinea(String linea) {
		if (!this.cabeceraMetodo) {
			if (linea.contains("/*")) {
				enComentario = true;
				Matcher m = patron.matcher(linea);
				while(m.find()) {
					agregarToken(m.group());
				}
			} else {
				if (linea.contains("*/")) {
					enComentario = false;
				}
			}
			
			if (!enComentario) {
				Matcher m = patron.matcher(linea);
				while(m.find()) {
					agregarToken(m.group());
				}
			}
		} else {
			this.cabeceraMetodo = false;
			agregarToken("{");
		}
		
	}
		
	
	private void agregarToken(String tok) {
		if (!this.incontables.contains(tok)) {
			if (this.tokens.containsKey(tok)) {
				this.tokens.replace(tok, this.tokens.get(tok) + 1);
			} else {
				this.tokens.put(tok, 1);
			}
		}	
	}
	
	public int getCantidadOperadoresUnicos() {
		int cont = 0;
		for (String item : this.tokens.keySet()) {
			if (this.operadores.contains(item)) {
				cont++;
			}
		}
		return cont;
	}
	
	public int getCantidadOperadoresTotales() {
		int suma = 0;
		for (String item : this.tokens.keySet()) {
			if (this.operadores.contains(item)) {
				suma+=this.tokens.get(item);
			}
		}
		return suma;
	}

	public int getCantidadOperandosUnicos() {
		int cont = 0;
		for (String item : this.tokens.keySet()) {
			if (!this.operadores.contains(item) && !item.contains("//") && !item.contains("/*") && !item.contains("*/")) {
				cont++;
			}
		}
		return cont;
	}
	
	public int getCantidadOperandosTotales() {
		int suma = 0;
		for (String item : this.tokens.keySet()) {
			if (!this.operadores.contains(item) && !item.contains("//") && !item.contains("/*") && !item.contains("*/")) {
				suma+=this.tokens.get(item);
			}
		}
		return suma;
	}
	
	public String toString() {
		return "\nOperadores unicos: "+this.getCantidadOperadoresUnicos()+
				"\nOperadores totales: "+this.getCantidadOperadoresTotales()+
				"\nOperandos unicos: "+this.getCantidadOperandosUnicos()+
				"\nOperandos totales: "+this.getCantidadOperandosTotales()+
				"\n"+this.tokens.toString()+
				"\nLongitud: "+this.getLongitud()+
				"\nVolumen: "+this.getVolumen()+
				"\nEsfuerzo: "+this.getEsfuerzo();
	}
	
	public int getLongitud() {
		return this.getCantidadOperadoresTotales()+this.getCantidadOperandosTotales();
	}
	
	public int getVolumen() {
		return this.getLongitud()*binlog(this.getCantidadOperadoresUnicos()+this.getCantidadOperandosUnicos());
	}
	
	public int getEsfuerzo() {
		return this.getVolumen()/this.getLongitud();
	}
	
	private static int binlog( int bits )
	{
	    int log = 0;
	    if( ( bits & 0xffff0000 ) != 0 ) { bits >>>= 16; log = 16; }
	    if( bits >= 256 ) { bits >>>= 8; log += 8; }
	    if( bits >= 16  ) { bits >>>= 4; log += 4; }
	    if( bits >= 4   ) { bits >>>= 2; log += 2; }
	    return log + ( bits >>> 1 );
	}
}