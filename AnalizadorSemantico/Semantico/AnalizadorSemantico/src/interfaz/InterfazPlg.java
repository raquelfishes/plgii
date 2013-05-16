package interfaz;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javacc.Compilador;
import javacc.ParseException;
import javacc.TokenMgrError;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;


public class InterfazPlg {

	private JFrame frmGrupo;
	private JFrame frmGrupoID;
	private static JTextArea JTextAreaAvisos;
	private String fich;
	private JTextArea JTextAreaArchivo;
	private static JTextArea JTextArea_ID;
	private static InterfazPlg INSTANCE;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					//UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel());
					//InterfazPlg window = new InterfazPlg();
					InterfazPlg window = getInstance();
					window.frmGrupo.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new InterfazPlg();
        }
    }
	
	public static InterfazPlg getInstance() {
        createInstance();
        return INSTANCE;
    }
	
	/*public void escrbirTokens(String arg0){
		String parcial = JTextAreaTokens.getText();
		JTextAreaTokens.setText(parcial+"\n"+arg0);
	}*/
	
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private InterfazPlg() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void escribirIdentificadores(String id){
		String parcial = JTextArea_ID.getText();
		JTextArea_ID.setText(parcial+"\n"+id);
	}
	/*
	public static void escribirTokens(String t){
		String parcial = JTextAreaTokens.getText();
		JTextAreaTokens.setText(parcial+"\n"+t);
	}*/
	public static void escribirAvisos(){
		String t ="Procesando regla: "+ Thread.currentThread().getStackTrace()[2].getMethodName();
		String parcial = JTextAreaAvisos.getText();
		JTextAreaAvisos.setText(parcial+"\n"+t);
	}

private void abrirFichero (){
        
        try {  
        	JTextAreaArchivo.setText("");
        	FileReader lector = new FileReader(fich);
            BufferedReader buffer = new BufferedReader(lector);
            String linea = "";
            linea = buffer.readLine();
            int numLinea = 0;
            
            while((linea = buffer.readLine()) != null){
            	JTextAreaArchivo.append(numLinea + ": " + linea + "\n");
            	numLinea ++;
            }
            buffer.close();
            lector.close();
                
                
        }catch(Exception e){
                
        e.printStackTrace();
        
        }
}
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() throws IOException {		
		try {
			UIManager.setLookAndFeel(new de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel());
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		} catch (java.text.ParseException e2) {
			e2.printStackTrace();
		}
		
		
		frmGrupo = new JFrame();
		frmGrupo.setTitle("Grupo 3 - Compilador Java ");
		frmGrupo.setBounds(100, 100, 1299, 692);
		frmGrupo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGrupo.getContentPane().setLayout(new MigLayout("", "[180.00,grow][360.00,grow][324.00,grow,fill][327.00,grow][]", "[64.00,grow][480.00,grow][grow 600]"));
		
		JPanel JPanelCI = new JPanel();
		frmGrupo.getContentPane().add(JPanelCI, "cell 2 0 1 2,grow");
		JPanelCI.setLayout(new MigLayout("", "[grow]", "[16.00][grow]"));
		
		JLabel JLabelCI = new JLabel("C\u00F3digo Intermedio Generado:");
		JLabelCI.setHorizontalAlignment(SwingConstants.CENTER);
		JLabelCI.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelCI.add(JLabelCI, "cell 0 0");
		
		JScrollPane JScrollPaneCI = new JScrollPane();
		JPanelCI.add(JScrollPaneCI, "cell 0 1,grow");
		
		JTextArea JTextAreaCI = new JTextArea();
		JTextAreaCI.setEditable(false);
		JScrollPaneCI.setViewportView(JTextAreaCI);
		
		JPanel JPanelCF = new JPanel();
		frmGrupo.getContentPane().add(JPanelCF, "cell 3 0 1 2,grow");
		JPanelCF.setLayout(new MigLayout("", "[grow]", "[15.00][grow]"));
		
		JLabel JLabelCF = new JLabel("C\u00F3digo Final Generado:");
		JLabelCF.setHorizontalAlignment(SwingConstants.CENTER);
		JLabelCF.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelCF.add(JLabelCF, "cell 0 0");
		
		JScrollPane JScrollPaneCF = new JScrollPane();
		JPanelCF.add(JScrollPaneCF, "cell 0 1,grow");
		
		JTextArea JTtextAreaCF = new JTextArea();
		JTtextAreaCF.setEditable(false);
		JScrollPaneCF.setViewportView(JTtextAreaCF);
		
		JPanel JPanelBotones = new JPanel();
		frmGrupo.getContentPane().add(JPanelBotones, "cell 0 1,alignx center,aligny top");
		JPanelBotones.setLayout(new MigLayout("", "[168.00]", "[31.00px][31.00][81.00][31.00][30.00][31.00][102.00][][31.00]"));
		
		JButton JButton_Abrir = new JButton("Abrir");
		JButton_Abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		JPanelBotones.add(JButton_Abrir, "cell 0 0,growx,aligny center");
		JButton_Abrir.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent arg0) {

					boolean cerradoDialog = false;
					try {
					  	JFileChooser selecFich = new JFileChooser();
					  	selecFich.setDialogTitle("Elegir un fichero fuente");
					  	try {
					  		FileNameExtensionFilter filtro=new FileNameExtensionFilter("java","java");
						  	selecFich.setFileFilter(filtro);
						  	//System.out.print(filtro.toString());
						  	
						  	cerradoDialog = selecFich.showOpenDialog(frmGrupo)== JFileChooser.CANCEL_OPTION;
										    	
						  	if (!cerradoDialog){
						  		fich = selecFich.getSelectedFile().getPath();
						    //System.out.print(fich.toString());
						  	}
						    	
					  	} catch (IllegalArgumentException e2){
					  		 JOptionPane.showMessageDialog (frmGrupo, "La extensión del archivo es incorrecta."); 
					  	}	  	
					    
					} catch (Exception e1) {
				    	 JOptionPane.showMessageDialog (frmGrupo, "No has seleccionado ningun fichero"); 
				    }
					
					if (!cerradoDialog){
						abrirFichero();
					}
				}	
		});
		
		JButton_Abrir.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton JButton_Ejecutar = new JButton("Ejecutar");
		JButton_Ejecutar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(fich!=null){
					if (fich.endsWith(".pdf")) System.out.print("*.PDF no es un formato correcto.");
					try {
						new Compilador(new java.io.FileInputStream(fich));
						
						try {
							Compilador.usaInterfaz = true;
							Compilador.initGestorTS();
							Compilador.compilar();
					        
					        System.out.println ("ExampleParser: La entrada ha sido leida con \u00e9xito.");
					      }
					      catch(ParseException e){
					        System.out.println ("ExampleParser: Ha ocurrido un error durante el an\u00e1lisis.");
					        System.out.println (e.getMessage());
					        JOptionPane.showMessageDialog (frmGrupo, e.getMessage(), "ExampleParser: Ha ocurrido un error durante el analisis.", JOptionPane.ERROR_MESSAGE); 
					      }
					      catch(TokenMgrError e){
					        System.out.println ("ExampleParser: Ha ocurrido un error.");
					        System.out.println (e.getMessage());
					        System.out.println (e.getMessage());
					        JOptionPane.showMessageDialog (frmGrupo, e.getMessage(), "ExampleParser: Ha ocurrido un error.", JOptionPane.ERROR_MESSAGE); 
					      }
						
					} catch (FileNotFoundException fn1) {
						JOptionPane.showMessageDialog (frmGrupo, "Selecciona un archivo antes de ejecutar."); 
					}
				}		
			
			}
		});
		
		JButton_Ejecutar.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_Ejecutar, "cell 0 1,growx,aligny center");
		
		JButton JButton_ConsultaId = new JButton("Ver Identificadores");
		JButton_ConsultaId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frmGrupoID = new JFrame();
				frmGrupoID.setTitle("Grupo3 - Compilador Java - Identificadores");
				frmGrupoID.setBounds(100, 100, 460, 421);
				frmGrupoID.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmGrupoID.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow]"));
				
				JLabel lblListaDeIdentificadores = new JLabel("Lista de Identificadores procesados:");
				lblListaDeIdentificadores.setHorizontalAlignment(SwingConstants.CENTER);
				lblListaDeIdentificadores.setFont(new Font("Tahoma", Font.BOLD, 12));
				frmGrupoID.getContentPane().add(lblListaDeIdentificadores, "cell 0 0");
				
				JScrollPane JScrollPane_ID = new JScrollPane();
				frmGrupoID.getContentPane().add(JScrollPane_ID, "cell 0 1,grow");
				
				JTextArea_ID = new JTextArea();
				JTextArea_ID.setEditable(false);
				JScrollPane_ID.setViewportView(JTextArea_ID);
				
				frmGrupoID.setVisible(true);
				
			}
		});
		JButton_ConsultaId.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_ConsultaId, "cell 0 4,growx,aligny center");
		
		JButton JButton_ENS = new JButton("Ejecutar ENS2001");
		JButton_ENS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Prueba de botón ENS2001 ");
				// Hasta que el código final no haya terminado, este botón debe ser not enable.
				//     TODO: Usar misma variable que en el Botón: Ver CF.
				
				//TODO: Generar un fichero de tipo CFinal.ens que contiene ProgramaFinal.txt y sirve de entrada al ENS2001.
				//TODO: Generar una llamada a WENS2001 e intentar que abra directamente este fichero.
			}
		});
		JButton_ENS.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_ENS, "cell 0 5,growx,aligny center");
		
		JButton JButton_Reset = new JButton("Reset");
		JButton_Reset.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_Reset, "cell 0 7,growx,aligny center");
		
		JButton JButton_Salir = new JButton("Salir");
		JButton_Salir.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_Salir, "cell 0 8,growx");
		
		JPanel JPanelTexto = new JPanel();
		frmGrupo.getContentPane().add(JPanelTexto, "cell 1 0 1 2,grow");
		JPanelTexto.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel JLabelArchivo = new JLabel("Archivo leido:");
		JLabelArchivo.setHorizontalAlignment(SwingConstants.CENTER);
		JLabelArchivo.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelTexto.add(JLabelArchivo, "cell 0 0");
		
		JScrollPane JScrollPaneArchivo = new JScrollPane();
		JPanelTexto.add(JScrollPaneArchivo, "cell 0 1,grow");
		
		JTextAreaArchivo = new JTextArea();
		JTextAreaArchivo.setEditable(false);
		JScrollPaneArchivo.setViewportView(JTextAreaArchivo);
		JPanel JPanelAvisos = new JPanel();
	 	
		frmGrupo.getContentPane().add(JPanelAvisos, "cell 0 2 4 1,grow");
		 	
		JPanelAvisos.setLayout(new MigLayout("", "[grow]", "[grow]"));
		JScrollPane JScrollPaneAvisos = new JScrollPane();
		JPanelAvisos.add(JScrollPaneAvisos, "cell 0 0,grow");		
		JTextAreaAvisos = new JTextArea();
		JTextAreaAvisos.setEditable(false);
		JScrollPaneAvisos.setViewportView(JTextAreaAvisos);
	}
}