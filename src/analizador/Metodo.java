package analizador;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metodo {

	private ArrayList<String> raw;
	private ArrayList<String> llamaA;
	private ArrayList<String> llamadoPor;
	
	private String nombre;
	
	private int cantidadLineas;
	private int cantidadComentarios;
	private float porcentajeComentarios;
	private int complejidadCiclomatica;
	private int longitud;
	private int volumen;
	private int esfuerzo;
	
	public Metodo(ArrayList<String> raw) {
		this.raw = raw;
		this.llamadoPor = new ArrayList<String>();
		this.cantidadLineas = 0;
		this.cantidadComentarios = 0;
		this.porcentajeComentarios = 0;
		this.longitud = 0;
		this.volumen = 0;
		this.esfuerzo = 0;
		Pattern p1 = Pattern.compile("((\\p{Alnum}+|[-_]+)+[(])|((\\p{Alnum}+|[-_]+)+\\s[(])"); // para obtener el nombre al lado del parentesis
		Pattern p2 = Pattern.compile("((\\p{Alnum}+|[-_]+)+)"); // para obtener el nombre y sacar el parentesis
		Matcher m1 = p1.matcher(this.raw.get(0));
		if (m1.find()) {
			Matcher m2 = p2.matcher(m1.group());
			if (m2.find()) {
				this.nombre = m2.group();
			}
		}
		this.procesar();
	}
	
	
	public int getCantidadLineas() {
		return cantidadLineas;
	}


	public int getCantidadComentarios() {
		return cantidadComentarios;
	}


	public float getPorcentajeComentarios() {
		return porcentajeComentarios;
	}


	public int getComplejidadCiclomatica() {
		return complejidadCiclomatica;
	}


	public int getFanIn() {
		return this.llamadoPor.size();
	}


	public int getFanOut() {
		return this.llamaA.size();
	}


	public int getLongitud() {
		return longitud;
	}


	public int getVolumen() {
		return volumen;
	}


	public int getEsfuerzo() {
		return esfuerzo;
	}
	
	public ArrayList<String> getRaw() {
		return raw;
	}


	private void procesar() {
		Comentarios c = new Comentarios();
		ComplejidadCiclomatica cc = new ComplejidadCiclomatica();
		Halstead h = new Halstead();
		FanIn fo = new FanIn();
		
		for (String linea : this.raw) {
			c.analizarLinea(linea);
			cc.analizarLinea(linea);
			h.analizarLinea(linea);
			fo.analizarLinea(linea);
		}
		
		this.cantidadLineas = c.getCantidadLineas();
		this.cantidadComentarios = c.getCantidadComentarios();
		this.porcentajeComentarios = c.getPorcentajeComentarios();
		this.complejidadCiclomatica = cc.getComplejidadTotal();
		this.longitud = h.getLongitud();
		this.volumen = h.getVolumen();
		this.esfuerzo = h.getEsfuerzo();
		this.llamaA = fo.getLista();		
	}
	
	public String toString() {
		return "Nombre: "+this.nombre+"\nLineas: "+this.cantidadLineas+"\nComentarios: "+this.cantidadComentarios+"\nPorcentaje de Comentarios: "+this.porcentajeComentarios+"\nComplejidad ciclomatica: "+this.complejidadCiclomatica+"\nLongitud: "+this.longitud+"\nVolumen: "+this.volumen+"\nEsfuerzo: "+this.esfuerzo+"\nFan in: "+this.getFanIn()+"\nFan out: "+this.getFanOut()+"\nLlamados: "+this.llamaA+"\nLlamado por: "+this.llamadoPor;
	}
	
	public void agregarFanIn(String n) {
		this.llamadoPor.add(n);
	}
	
	public String getNombre() {
		return this.nombre;
	}

	public ArrayList<String> getListaFanOut() {
		return this.llamaA;
	}
	
}
