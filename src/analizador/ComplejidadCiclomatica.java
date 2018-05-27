package analizador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplejidadCiclomatica {
	private int metodos;
	private int complejidadTotal;
	private int complejidadMaxima;
	private int auxComplejidadMaxima;
	private int llaves;
	private boolean enFuncion;
	private boolean enComentario;
	private boolean sinLlave;
	
	public ComplejidadCiclomatica() {
		this.metodos = 0;
		this.complejidadTotal = 0;
		this.complejidadMaxima = 0;
		this.auxComplejidadMaxima = 0;
		this.llaves = 0;
		this.enFuncion = false;
		this.enComentario = false;
		this.sinLlave = false;
	}
	
	private Pattern patronFuncion = Pattern.compile("((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
	private Pattern patronComplejidad = Pattern.compile("([/][*].*[*][/]|[/][*].*|.*[*][/]|[/][/].+)|while|if|switch|case|for|[|]+|&&");
	
	public void analizarLinea(String linea) {
		if (!enComentario) {	
			if (enFuncion) {
				if (!sinLlave) {
					Matcher m = patronComplejidad.matcher(linea);
					while (m.find()) {
						if (!m.group().contains("//") && !m.group().contains("/*") && !m.group().contains("*/")) { // saco los for y whiles en comentarios
							complejidadTotal++;
							auxComplejidadMaxima++;
						}
						else {
							if (m.group().contains("/*")) {
								enComentario = true;
							}
						}
					}
					llaves+=linea.length() - linea.replace("{", "").length();
					llaves-=linea.length() - linea.replace("}", "").length();
					if(llaves<=0) {
						enFuncion = false;
						if (auxComplejidadMaxima > complejidadMaxima) complejidadMaxima = auxComplejidadMaxima;
						auxComplejidadMaxima = 0;
					}
				} else {
					if (linea.contains("{")) {
						this.sinLlave = false;
						llaves+=linea.length() - linea.replace("{", "").length();
					}
				}
			} else {
				if (patronFuncion.matcher(linea).find()) {
					this.enFuncion = true;
					complejidadTotal++;
					auxComplejidadMaxima++;
					metodos++;
					llaves+=linea.length() - linea.replace("{", "").length();
					if (llaves == 0) this.sinLlave = true;
				}
			}
		} else {
			if (linea.contains("*/")) enComentario = false;
		}
	}
	
	public float getComplejidadPromedio() {
		return (float)complejidadTotal/metodos;
	}
	
	public int getComplejidadTotal() {
		return this.complejidadTotal;
	}
	
	public String toString() {
		return "Metodos: "+this.metodos+"\nComplejidad total: "+this.complejidadTotal+"\nComplejidad promedio: "+this.getComplejidadPromedio()+"\nComplejidad máxima hallada: "+this.complejidadMaxima;
	}

}
