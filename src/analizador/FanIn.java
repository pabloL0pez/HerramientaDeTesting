package analizador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FanIn {
	
	private Pattern patronFuncion = Pattern.compile("((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
	private ArrayList<String> llamados;
	private Pattern patronFuncionParentesis = Pattern.compile("((\\p{Alnum}+|[-_]+)+[(])|((\\p{Alnum}+|[-_]+)+\\s[(])"); // para obtener el nombre al lado del parentesis
	private Pattern patronPalabra = Pattern.compile("((\\p{Alnum}+|[-_]+)+)"); // para obtener el nombre y sacar el parentesis
	private boolean enComentario;
	private boolean enFuncion;
	private boolean sinLlave;
	private int llaves;
	private String palabras_reservadas[] = {
			"if", "for", "while", "switch", "return"
	};
	private List<String> palabrasReservadas = Arrays.asList(palabras_reservadas);
	
	public FanIn() {
		this.enComentario = false;
		this.enFuncion = false;
		this.sinLlave = false;
		this.llaves = 0;
		this.llamados = new ArrayList<String>();
	}
	
	public void analizarLinea(String linea) {
		if (!enComentario) {	
			if (enFuncion) {
				if (!sinLlave) {
					Matcher m = patronFuncionParentesis.matcher(linea);
					while (m.find()) {
						if (!m.group().contains("//") && !m.group().contains("/*") && !m.group().contains("*/")) { // saco los for y whiles en comentarios
							Matcher m2 = patronPalabra.matcher(m.group());
							if (m2.find()) {
								if (!this.palabrasReservadas.contains(m2.group())) this.llamados.add(m2.group());
							}
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
					llaves+=linea.length() - linea.replace("{", "").length();
					if (llaves == 0) this.sinLlave = true;
				}
			}
		} else {
			if (linea.contains("*/")) enComentario = false;
		}
	}
	
	public ArrayList<String> getLista(){
		return this.llamados;
	}
}
