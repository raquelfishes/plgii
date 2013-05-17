package interfaz;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import javacc.Compilador;
import javacc.ParseException;
import javacc.SimpleNode;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import tabla.simbolos.v2.Atributos;
import traductor.Traductor;


public class InterfazPlg {

	private JFrame frmGrupo;
	private JFrame frmGrupoID;
	private static JTextArea JTextAreaAvisos;
	private static JTextArea JTextAreaCF;
	private static JTextArea JTextAreaCI;
	private String fich;
	private JTextArea JTextAreaArchivo;
	private static JTextArea JTextArea_ID;
	private static InterfazPlg INSTANCE;
	private List<Atributos> lista;
	private Compilador compilador;
	private static boolean CIVacio;
	
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

	public static void generarFicheroENS () throws IOException{
		try{
	          File f1 = new File("ejemplos/ProgramaFinal.txt");
	          File f2 = new File("ENS2001/CodigoFinal.ens");
	          InputStream in = new FileInputStream(f1);

	          //For Append the file.
	          //OutputStream out = new FileOutputStream(f2,true);

	          //For Overwrite the file.
	          OutputStream out = new FileOutputStream(f2);

	          byte[] buf = new byte[1024];
	          int len;
	          while ((len = in.read(buf)) > 0){
	            out.write(buf, 0, len);
	          }
	          in.close();
	          out.close();
	          System.out.println("Generado el código final en ensamblador en el directorio ENS2001");
	        }
        catch(FileNotFoundException ex){
         
        }
	        
		
	}
	
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JTextAreaAvisos.append(text);
			}
		});
	}
			 
	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
	    
		    public void write(int b) throws IOException {
		      updateTextArea(String.valueOf((char) b));
		    }
		 
		    public void write(byte[] b, int off, int len) throws IOException {
		      updateTextArea(new String(b, off, len));
		    }
		 
		    public void write(byte[] b) throws IOException {
		      write(b, 0, b.length);
		    }
	 	};
	 
	 	System.setOut(new PrintStream(out, true));
	 	System.setErr(new PrintStream(out, true));
	}	
	
	
	public static void escribirFicheroCI() throws IOException{
	    		
		
		JTextAreaCI.setText("");
    	String pathCI = "ejemplos/ProgramaIntermedio.txt"; 
    	FileReader lector = new FileReader(pathCI);
    	BufferedReader buffer = new BufferedReader(lector);
        String linea = "";
        linea = buffer.readLine();
        int numLinea = 1;
        
        while((linea = buffer.readLine()) != null){
        	JTextAreaCI.append(numLinea + ": " + linea + "\n");
        	numLinea ++;
        }
        
        //Aviso para que no muestre por pantalla CF si no se ha generado CI.
        if (numLinea == 0) {
        	CIVacio = true;
        	JTextAreaCI.setText("Error al generar el Código Intermedio.");
        }
        
        buffer.close();
        lector.close();

	}
	
	public static void escribirFicheroCF() throws IOException{
		
//	    if (!CIVacio){
			JTextAreaCF.setText("");
	    	String pathCF = "ejemplos/ProgramaFinal.txt"; 
	    	FileReader lector = new FileReader(pathCF);
	        BufferedReader buffer = new BufferedReader(lector);
	        String linea = "";
	        linea = buffer.readLine();
	        int numLinea = 1;
	        
	        while((linea = buffer.readLine()) != null){
	        	JTextAreaCF.append(numLinea + ": " + linea + "\n");
	        	numLinea ++;
	        }
	        buffer.close();
	        lector.close();
//	    } else {
//	    	JTextAreaCF.setText("Código Intermedio no generado, no se ha podido generar el Código Final.");
//	    }
	    	

}
	
	private void abrirFichero (){
	        
	        try {  
	        	JTextAreaArchivo.setText("");
	        	FileReader lector = new FileReader(fich);
	            BufferedReader buffer = new BufferedReader(lector);
	            String linea = "";
	            int numLinea = 1;
	            
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
		frmGrupo.setLocationRelativeTo(null);
		frmGrupo.setTitle("Grupo 3 - Compilador Java ");
		frmGrupo.setBounds(100, 100, 1299, 692);
		frmGrupo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGrupo.getContentPane().setLayout(new MigLayout("", "[180.00,grow][360.00,grow][324.00,grow,fill][327.00,grow][]", "[64.00,grow][416.00,grow][grow 600]"));
		
		JTextArea_ID = new JTextArea();
		
		JPanel JPanelCI = new JPanel();
		frmGrupo.getContentPane().add(JPanelCI, "cell 2 0 1 2,grow");
		JPanelCI.setLayout(new MigLayout("", "[grow]", "[16.00][grow]"));
		
		JLabel JLabelCI = new JLabel("C\u00F3digo Intermedio Generado:");
		JLabelCI.setHorizontalAlignment(SwingConstants.CENTER);
		JLabelCI.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelCI.add(JLabelCI, "cell 0 0");
		
		JScrollPane JScrollPaneCI = new JScrollPane();
		JPanelCI.add(JScrollPaneCI, "cell 0 1,grow");
		
		JTextAreaCI = new JTextArea();
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
		JPanelCF.add(JScrollPaneCF, "cell 0 1, grow");
		
		JTextAreaCF = new JTextArea();
		JTextAreaCF.setEditable(false);
		JScrollPaneCF.setViewportView(JTextAreaCF);
		
		JPanel JPanelBotones = new JPanel();
		frmGrupo.getContentPane().add(JPanelBotones, "cell 0 1,alignx center,aligny top");
		JPanelBotones.setLayout(new MigLayout("", "[168.00]", "[31.00px][31.00][70.00][31.00][30.00][31.00][102.00][][31.00]"));
		
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
			public void mousePressed(MouseEvent arg0) {
				if(fich!=null){
					if (fich.endsWith(".pdf")) System.out.print("*.PDF no es un formato correcto.");
						 
				         try {
				        	 if (compilador == null) 
				        		 compilador = new Compilador(new java.io.FileInputStream(fich));
				        	 else
				        		 Compilador.ReInit(new java.io.FileInputStream(fich));
				        	 
							 Compilador.initGestorTS();
							 Atributos.resetAliasCounter();

						     System.out.println ("Compilador: Leyendo de fichero ");
				        	        	
					     }
				         catch(java.io.FileNotFoundException e) {
				        	 System.out.println ("Compilador: El fichero no ha sido encontrado.");
				        	 return;
				         }
				         try {
				        	 Compilador.fichero = new java.io.DataOutputStream( new java.io.FileOutputStream("ejemplos/ProgramaIntermedio.txt"));
				         }
				         catch(java.io.FileNotFoundException e){
					        System.out.println ("Error, el fichero ProgramaIntermedio no existe");
					        return;
				         }
				         try {
				        	 JTextAreaCI.setText("");
							 JTextAreaCF.setText("");
							 JTextAreaAvisos.setText("");
							 JTextArea_ID.setText("");		
				        	 
				        	 
				        	 SimpleNode root = Compilador.compilar();
				        	 Atributos.resetAliasCounter();
				        	 root.dump("");
				        	 System.out.println ("Compilador: La entrada ha sido leida con \u00e9xito.");
				        	 compilador.rootNode().interpret();
				        	 escribirFicheroCI();
			        	 
			        	     //Codigo final
				        	 Compilador.traductor = new Traductor(Compilador.gestorTS);
				        	 Compilador.traductor.traduce("ejemplos/ProgramaIntermedio.txt", "ejemplos/ProgramaFinal.txt");
				        	 System.out.println("Traducido.");
				        	 
				        	 if (!CIVacio) {
					        	 generarFicheroENS();
					        	 escribirFicheroCF();
				        	 } else {
				        		 JTextAreaCF.setText("Código Intermedio no generado, no se ha podido generar el Código Final.");
				        	 }
				        
				        	 lista = Compilador.gestorTS.dameListaAtributosDeClase();

					      }
					      catch(ParseException e){
					        System.out.println ("Compilador: Ha ocurrido un error durante el an\u00e1lisis.");
					        System.out.println (e.getMessage());
					      }
					      catch(TokenMgrError e){
					        System.out.println ("Compilador: Ha ocurrido un error.");
					        System.out.println (e.getMessage());
					      } catch (IOException e) {
					    	JOptionPane.showMessageDialog (frmGrupo, "Archivo Vacío."); 
						}
				}		
			
			}
		});
		
		JButton_Ejecutar.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_Ejecutar, "cell 0 1,growx,aligny center");
		
		//Descomentar esto si se quiere ver una lista con Lexemas.
		
		JButton JButton_ConsultaId = new JButton("Ver ElementosTS"); // Cambiar por Lexemas en su caso.
		JButton_ConsultaId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frmGrupoID = new JFrame();
				frmGrupoID.setTitle("Grupo3 - Compilador Java - GestionTS");
				frmGrupoID.setBounds(100, 100, 460, 421);
				frmGrupoID.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				frmGrupoID.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow]"));
				
				JLabel lblListaDeTS = new JLabel("Lista de elementos procesados:");
				lblListaDeTS.setHorizontalAlignment(SwingConstants.CENTER);
				lblListaDeTS.setFont(new Font("Tahoma", Font.BOLD, 12));
				frmGrupoID.getContentPane().add(lblListaDeTS, "cell 0 0");
				
				JScrollPane JScrollPane_ID = new JScrollPane();
				frmGrupoID.getContentPane().add(JScrollPane_ID, "cell 0 1,grow");
				
				
				JTextArea_ID.setText("----Lexemas----");
				for(int i=0; i< lista.size(); i++) {
	        		String parcial = JTextArea_ID.getText();
	        		JTextArea_ID.setText(parcial+"\n"+lista.get(i).getLexema());
	            }
				
				String parcialA = JTextArea_ID.getText();
				JTextArea_ID.setText(parcialA+"\n"+"----Alias----");
				for(int i=0; i< lista.size(); i++) {
	        		String parcial = JTextArea_ID.getText();
	        		JTextArea_ID.setText(parcial+"\n"+lista.get(i).getAlias());
	            }
				
				String parcialT = JTextArea_ID.getText();
				JTextArea_ID.setText(parcialT+"\n"+"----Tipos----");
				for(int i=0; i< lista.size(); i++) {
	        		String parcial = JTextArea_ID.getText();
	        		JTextArea_ID.setText(parcial+"\n"+lista.get(i).getTipo());
	            }
				
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
				Runtime obj = Runtime.getRuntime(); 
				try {
					
					obj.exec("ENS2001/winens.exe");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}  
				
			}
		});
		JButton_ENS.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_ENS, "cell 0 5,growx,aligny center");
		
		JButton JButton_Reset = new JButton("Reset");
		JButton_Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fich = null;
				JTextAreaArchivo.setText("");
				JTextAreaCI.setText("");
				JTextAreaCF.setText("");
				JTextAreaAvisos.setText("");
				JTextArea_ID.setText("");
				
			}
		});
		JButton_Reset.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelBotones.add(JButton_Reset, "cell 0 7,growx,aligny center");
		
		JButton JButton_Salir = new JButton("Salir");
		JButton_Salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmGrupo.dispose();
				System.exit(0);
			}
		});
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
		JScrollPaneAvisos.setFocusable(false);
		JPanelAvisos.add(JScrollPaneAvisos, "cell 0 0,grow");		
		JTextAreaAvisos = new JTextArea();
		redirectSystemStreams();
		JScrollPaneAvisos.setViewportView(JTextAreaAvisos);
	}
}