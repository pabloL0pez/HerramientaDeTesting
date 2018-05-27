package analizador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Clase {

	private ArrayList<String> raw;
	private ArrayList<Metodo> metodos;
	private String nombre;
	//private Pattern patronClase = Pattern.compile("\\s*(public|private)\\s+class\\s+(\\w+)\\s+((extends\\s+\\w+)|(implements\\s+\\w+( ,\\w+)*))?\\s*\\{");
	private Pattern patronClase = Pattern.compile("class");
	private Pattern patronFuncion = Pattern.compile("((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");


	public Clase(String archivo) throws IOException {
		this.raw = new ArrayList<String>();
		this.metodos = new ArrayList<Metodo>();
		this.leerArchivo(archivo);
		this.extraerMetodos();
		FanIn.analizar(this);
	}
	
	public void agregarLinea(String linea) {
		raw.add(linea);
	}
	
	public ArrayList<String> getRaw(){
		return this.raw;
	}
	
	public String toString() {
		String linea = "";
		for (String s : this.raw) {
			linea += s + "\n";
		}
		return linea;
	}
	
	public String toStringUnaLinea() {
		String linea = "";
		for (String s : this.raw) {
			linea += s;
		}
		return linea;
	}
	
	private void extraerMetodos() {
		boolean sinLlave = false;
		boolean enFuncion = false;
		int llaves = 0;
		ArrayList<String> m = null;
		
		for (String linea : this.raw) {
			if (enFuncion) {
				if (!sinLlave) {
					m.add(linea);
					llaves+=linea.length() - linea.replace("{", "").length();
					llaves-=linea.length() - linea.replace("}", "").length();
					if(llaves<=0) {
						enFuncion = false;
						this.metodos.add(new Metodo(m));
					}
				} else {
					if (linea.contains("{")) sinLlave = false;
					llaves+=linea.length() - linea.replace("{", "").length();
				}
			} else {
				if (patronFuncion.matcher(linea).find()) {
					enFuncion = true;
					llaves+=linea.length() - linea.replace("{", "").length();
					if (llaves == 0) sinLlave = true;
					m = new ArrayList<String>();
					m.add(linea);
				}
			}
		}
		
		
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	private void leerArchivo(String archivo) throws IOException { // lee el archivo perteneciente a la clase
		boolean enClase = false;
		int llaves = 0;
		String linea;
		File f = new File(archivo);
		BufferedReader bf = new BufferedReader(new FileReader(f));
		while ((linea = bf.readLine()) != null) {
			if (enClase) { // si se encuentra leyendo la clase guarda las lineas en el arraylist
				agregarLinea(linea);
				llaves+=linea.length() - linea.replace("{", "").length();
				llaves-=linea.length() - linea.replace("}", "").length();
				if(llaves<=0) {
					enClase = false;
					agregarLinea(linea);
				}
			} else { // si no encontro todavia una clase busca el patron y si lo encuentra lo guarda
				Matcher m = patronClase.matcher(linea); 
				if (m.find()) {
					enClase = true;
					agregarLinea(linea);
					llaves++;
					Matcher n = Pattern.compile("class \\p{Alnum}+").matcher(linea); // busco el nombre que se encuentra al lado de la palabra reservada class
					if (n.find()) {
						this.nombre = n.group().substring(6, n.group().length());
					}
				}
				
			}
		}
		bf.close();
	}
	
	public ArrayList<Metodo> getMetodos(){
		return this.metodos;
	}
}
