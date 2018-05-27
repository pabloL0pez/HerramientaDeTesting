package analizador;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.InputEvent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Interfaz extends JFrame {

	private JPanel contentPane;
	private JTextField campoLineasDeCodigo;
	private JTextField campoLineasDeComentarios;
	private JTextField campoPorcentajeDeComentarios;
	private JTextField campoComplejidadCiclomatica;
	private JTextField campoLongitud;
	private JTextField campoVolumen;
	private JTextField campoFanIn;
	private JTextField campoFanOut;
	
	private JTextPane panelTextoCodigo;

	@SuppressWarnings("rawtypes")
	private JList listaArchivos;
	@SuppressWarnings("rawtypes")
	private DefaultListModel modeloListaArchivos;
	@SuppressWarnings("rawtypes")
	private JList listaClases;
	@SuppressWarnings("rawtypes")
	private DefaultListModel modeloListaClases;
	@SuppressWarnings("rawtypes")
	private JList listaMetodos;
	@SuppressWarnings("rawtypes")
	private DefaultListModel modeloListaMetodos;
	
	Clase clase = null;
	ArrayList<String> archivos = new ArrayList<String>();
	ArrayList<Metodo> metodos = new ArrayList<Metodo>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interfaz frame = new Interfaz();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Interfaz() {
		setTitle("Herramienta de testing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 882, 621);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 866, 21);
		contentPane.add(menuBar);

		JMenu mnNewMenu = new JMenu("Archivo");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Abrir");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.showOpenDialog(fileChooser);
				File directorio = fileChooser.getSelectedFile();
				File[] archivosDirectorio = new File(directorio.getPath()).listFiles();
				archivos.clear();
				modeloListaArchivos.clear();
				modeloListaClases.clear();
				modeloListaMetodos.clear();
				limpiarDatosAnalisis();
				for (File archivo : archivosDirectorio) {
					if (getFileExtension(archivo).equals("java")) {
						archivos.add(archivo.getAbsolutePath());
						modeloListaArchivos.addElement(archivo.getName());
					}
				}
			}
		});

		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		mnNewMenu.add(mntmNewMenuItem);

		JLabel lblSeleccioneUnArchivo = new JLabel("Seleccione un archivo:");
		lblSeleccioneUnArchivo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnArchivo.setBounds(10, 32, 218, 14);
		contentPane.add(lblSeleccioneUnArchivo);

		JLabel lblSeleccioneUnaClase = new JLabel("Seleccione una clase:");
		lblSeleccioneUnaClase.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnaClase.setBounds(10, 181, 218, 14);
		contentPane.add(lblSeleccioneUnaClase);

		JLabel lblSeleccioneUnMtodo = new JLabel("Seleccione un m\u00E9todo:");
		lblSeleccioneUnMtodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnMtodo.setBounds(293, 181, 218, 14);
		contentPane.add(lblSeleccioneUnMtodo);

		JLabel lblCdigo = new JLabel("C\u00F3digo del m\u00E9todo:");
		lblCdigo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCdigo.setBounds(10, 332, 218, 14);
		contentPane.add(lblCdigo);

		JScrollPane panelArchivos = new JScrollPane();
		panelArchivos.setBounds(10, 48, 545, 122);
		contentPane.add(panelArchivos);

		JTextPane panelTextoArchivos = new JTextPane();
		panelTextoArchivos.setEditable(false);
		panelArchivos.setViewportView(panelTextoArchivos);

		JScrollPane panelClases = new JScrollPane();
		panelClases.setBounds(10, 199, 262, 122);
		contentPane.add(panelClases);

		JTextPane panelTextoClases = new JTextPane();
		panelTextoClases.setEditable(false);
		panelClases.setViewportView(panelTextoClases);

		JScrollPane panelMetodos = new JScrollPane();
		panelMetodos.setBounds(293, 199, 262, 122);
		contentPane.add(panelMetodos);

		JTextPane panelTextoMetodos = new JTextPane();
		panelTextoMetodos.setEditable(false);
		panelMetodos.setViewportView(panelTextoMetodos);

		JScrollPane panelCodigo = new JScrollPane();
		panelCodigo.setBounds(10, 351, 846, 220);
		contentPane.add(panelCodigo);

		panelTextoCodigo = new JTextPane();
		panelTextoCodigo.setEditable(false);
		panelCodigo.setViewportView(panelTextoCodigo);

		listaArchivos = new JList();
		modeloListaArchivos = new DefaultListModel();
		listaArchivos.setModel(modeloListaArchivos);
		panelArchivos.setViewportView(listaArchivos);

		listaClases = new JList();
		modeloListaClases = new DefaultListModel();
		listaClases.setModel(modeloListaClases);
		panelClases.setViewportView(listaClases);

		listaMetodos = new JList();
		modeloListaMetodos = new DefaultListModel();
		listaMetodos.setModel(modeloListaMetodos);
		panelMetodos.setViewportView(listaMetodos);

		listaArchivos.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (listaArchivos.getSelectedValue() != null) {
						try {
							clase = new Clase(archivos.get(listaArchivos.getSelectedIndex()));
							metodos = clase.getMetodos();
							modeloListaClases.clear();
							modeloListaMetodos.clear();
							limpiarDatosAnalisis();
							modeloListaClases.addElement(listaArchivos.getSelectedValue().toString().substring(0, listaArchivos.getSelectedValue().toString().indexOf(".")));
						} catch (IOException ex) {
							System.err.println("ARCHIVO NO ENCONTRADO");
						}
					}
				}
			}
		});

		listaClases.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (listaClases.getSelectedValue() != null) {
						modeloListaMetodos.clear();
						limpiarDatosAnalisis();
						for (Metodo metodo : metodos) {
							modeloListaMetodos.addElement(metodo.getNombre());
						}
					}
				}
			}
		});

		listaMetodos.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (listaMetodos.getSelectedValue() != null) {
						Metodo metodo = metodos.get(listaMetodos.getSelectedIndex());
						String codigo = "";
						
						for(String linea : metodo.getRaw()) {
							codigo += linea + "\n";
						}

						panelTextoCodigo.setText(codigo);
						campoLineasDeCodigo.setText(Integer.toString(metodo.getCantidadLineas()));
						campoLineasDeComentarios.setText(Integer.toString(metodo.getCantidadComentarios()));
						campoPorcentajeDeComentarios.setText(Float.toString(metodo.getPorcentajeComentarios()));
						campoComplejidadCiclomatica.setText(Integer.toString(metodo.getComplejidadCiclomatica()));
						campoLongitud.setText(Integer.toString(metodo.getLongitud()));
						campoVolumen.setText(Integer.toString(metodo.getVolumen()));
						campoFanIn.setText(Integer.toString(metodo.getFanIn()));
						campoFanOut.setText(Integer.toString(metodo.getFanOut()));
					}
				}
			}
		});

		JLabel lblAnlisisDelMtodo = new JLabel("An\u00E1lisis del m\u00E9todo:");
		lblAnlisisDelMtodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAnlisisDelMtodo.setBounds(573, 32, 218, 14);
		contentPane.add(lblAnlisisDelMtodo);

		JLabel lblLneasDeCdigo = new JLabel("L\u00EDneas de c\u00F3digo");
		lblLneasDeCdigo.setBounds(573, 57, 218, 14);
		contentPane.add(lblLneasDeCdigo);

		JLabel lblLneasDeComentarios = new JLabel("L\u00EDneas de comentarios");
		lblLneasDeComentarios.setBounds(573, 82, 218, 14);
		contentPane.add(lblLneasDeComentarios);

		JLabel lblPorcentajeDeComentarios = new JLabel("Porcentaje de comentarios");
		lblPorcentajeDeComentarios.setBounds(573, 107, 218, 14);
		contentPane.add(lblPorcentajeDeComentarios);

		JLabel lblComplejidadCiclomatica = new JLabel("Complejidad ciclom\u00E1tica");
		lblComplejidadCiclomatica.setBounds(573, 132, 218, 14);
		contentPane.add(lblComplejidadCiclomatica);

		JLabel lblLongitud = new JLabel("Longitud");
		lblLongitud.setBounds(573, 156, 218, 14);
		contentPane.add(lblLongitud);

		JLabel lblVolumen = new JLabel("Volumen");
		lblVolumen.setBounds(573, 181, 218, 14);
		contentPane.add(lblVolumen);

		JLabel lblFanin = new JLabel("Fan-In");
		lblFanin.setBounds(573, 206, 218, 14);
		contentPane.add(lblFanin);

		JLabel lblFanout = new JLabel("Fan-Out");
		lblFanout.setBounds(573, 231, 218, 14);
		contentPane.add(lblFanout);

		campoLineasDeCodigo = new JTextField();
		campoLineasDeCodigo.setEditable(false);
		campoLineasDeCodigo.setBounds(770, 54, 86, 20);
		contentPane.add(campoLineasDeCodigo);
		campoLineasDeCodigo.setColumns(10);

		campoLineasDeComentarios = new JTextField();
		campoLineasDeComentarios.setEditable(false);
		campoLineasDeComentarios.setColumns(10);
		campoLineasDeComentarios.setBounds(770, 79, 86, 20);
		contentPane.add(campoLineasDeComentarios);

		campoPorcentajeDeComentarios = new JTextField();
		campoPorcentajeDeComentarios.setEditable(false);
		campoPorcentajeDeComentarios.setColumns(10);
		campoPorcentajeDeComentarios.setBounds(770, 104, 86, 20);
		contentPane.add(campoPorcentajeDeComentarios);

		campoComplejidadCiclomatica = new JTextField();
		campoComplejidadCiclomatica.setEditable(false);
		campoComplejidadCiclomatica.setColumns(10);
		campoComplejidadCiclomatica.setBounds(770, 129, 86, 20);
		contentPane.add(campoComplejidadCiclomatica);

		campoLongitud = new JTextField();
		campoLongitud.setEditable(false);
		campoLongitud.setColumns(10);
		campoLongitud.setBounds(770, 153, 86, 20);
		contentPane.add(campoLongitud);

		campoVolumen = new JTextField();
		campoVolumen.setEditable(false);
		campoVolumen.setColumns(10);
		campoVolumen.setBounds(770, 178, 86, 20);
		contentPane.add(campoVolumen);

		campoFanIn = new JTextField();
		campoFanIn.setEditable(false);
		campoFanIn.setColumns(10);
		campoFanIn.setBounds(770, 203, 86, 20);
		contentPane.add(campoFanIn);

		campoFanOut = new JTextField();
		campoFanOut.setEditable(false);
		campoFanOut.setColumns(10);
		campoFanOut.setBounds(770, 228, 86, 20);
		contentPane.add(campoFanOut);
	}

	private String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}
	
	private void limpiarDatosAnalisis() {
		panelTextoCodigo.setText("");
		campoLineasDeCodigo.setText("");
		campoLineasDeComentarios.setText("");
		campoPorcentajeDeComentarios.setText("");
		campoComplejidadCiclomatica.setText("");
		campoLongitud.setText("");
		campoVolumen.setText("");
		campoFanIn.setText("");
		campoFanOut.setText("");
	}

}
