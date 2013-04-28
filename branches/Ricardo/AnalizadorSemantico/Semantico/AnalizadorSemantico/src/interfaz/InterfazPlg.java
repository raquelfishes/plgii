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
	private JScrollPane sc_TA_ID; // Scroll pane for text area identificadores
	private JScrollPane sc_TA_T; // Scroll pane for text area tokens
	private static JTextArea JTextAreaIdentificadores;
	private static JTextArea JTextAreaTokens;
	private static JTextArea JTextAreaAvisos;
	private String fich;
	private JTextArea JTextAreaArchivo;
	
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
	
	public void escrbirTokens(String arg0){
		String parcial = JTextAreaTokens.getText();
		JTextAreaTokens.setText(parcial+"\n"+arg0);
	}
	
	
	private InterfazPlg() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void escribirIdentificadores(String id){
		String parcial = JTextAreaIdentificadores.getText();
		JTextAreaIdentificadores.setText(parcial+"\n"+id);
	}
	public static void escribirTokens(String t){
		String parcial = JTextAreaTokens.getText();
		JTextAreaTokens.setText(parcial+"\n"+t);
	}
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
		frmGrupo.setBounds(100, 100, 903, 740);
		frmGrupo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGrupo.getContentPane().setLayout(new MigLayout("", "[178.00,grow][349.00,grow][306.00]", "[64.00][514.00,grow][grow 600]"));
		
		
		JPanel JPanelResultados = new JPanel();
		
		JTextAreaIdentificadores = new JTextArea();
		JTextAreaIdentificadores.setEditable(false);
		
		JTextAreaTokens = new JTextArea();
		JTextAreaTokens.setEditable(false);
		
		sc_TA_ID = new JScrollPane(JTextAreaIdentificadores);
		sc_TA_ID.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		sc_TA_T = new JScrollPane(JTextAreaTokens);
		sc_TA_T.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JLabel JLabel_Identificadores = new JLabel("Identificadores");
		JLabel_Identificadores.setFont(new Font("Tahoma", Font.BOLD, 12));
		JLabel_Identificadores.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel JLabel_Tokens = new JLabel("Tokens");
		JLabel_Tokens.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel_Tokens.setFont(new Font("Tahoma", Font.BOLD, 12));
		frmGrupo.getContentPane().add(JPanelResultados, "cell 2 0 1 2,alignx center,aligny center");
		JPanelResultados.setLayout(new MigLayout("", "[238px]", "[15px][195px][15px][195px]"));
		JPanelResultados.add(sc_TA_ID, "cell 0 1,grow");
		JPanelResultados.add(sc_TA_T, "cell 0 3,grow");
		JPanelResultados.add(JLabel_Identificadores, "cell 0 0,alignx center,aligny top");
		JPanelResultados.add(JLabel_Tokens, "cell 0 2,alignx center,aligny top");
		
		JPanel JPanelBotones = new JPanel();
		frmGrupo.getContentPane().add(JPanelBotones, "cell 0 1,alignx center,aligny top");
		JPanelBotones.setLayout(new MigLayout("", "[]", "[][25px][][]"));
		
		JButton JButton_Abrir = new JButton("Abrir");
		JButton_Abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		JPanelBotones.add(JButton_Abrir, "cell 0 1,growx,aligny top");
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
		
		JButton JButton_Salir = new JButton("Salir");
		JPanelBotones.add(JButton_Salir, "cell 0 2,growx,aligny center");
		JButton_Salir.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				frmGrupo.dispose();
				System.exit(0);
			}
		});
		JButton_Salir.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton JButton_Ejecutar = new JButton("Ejecutar");
		JPanelBotones.add(JButton_Ejecutar, "cell 0 3,alignx right,aligny top");
		JButton_Ejecutar.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JPanel JPanelTexto = new JPanel();
		frmGrupo.getContentPane().add(JPanelTexto, "cell 1 0 1 2,grow");
		JPanelTexto.setLayout(new MigLayout("", "[grow]", "[][grow]"));
		
		JLabel JLabelNombre = new JLabel("Archivo leido:");
		JLabelNombre.setHorizontalAlignment(SwingConstants.CENTER);
		JLabelNombre.setFont(new Font("Tahoma", Font.BOLD, 12));
		JPanelTexto.add(JLabelNombre, "cell 0 0");
		
		JScrollPane JScrollPaneArchivo = new JScrollPane();
		JPanelTexto.add(JScrollPaneArchivo, "cell 0 1,grow");
		
		JTextAreaArchivo = new JTextArea();
		JTextAreaArchivo.setEditable(false);
		JScrollPaneArchivo.setViewportView(JTextAreaArchivo);
		JPanel JPanelAvisos = new JPanel();
	 	
		frmGrupo.getContentPane().add(JPanelAvisos, "cell 0 2 3 1,grow");
		 	
		JPanelAvisos.setLayout(new MigLayout("", "[grow]", "[grow]"));
		JScrollPane JScrollPaneAvisos = new JScrollPane();
		JPanelAvisos.add(JScrollPaneAvisos, "cell 0 0,grow");		
		JTextAreaAvisos = new JTextArea();
		JTextAreaAvisos.setEditable(false);
		JScrollPaneAvisos.setViewportView(JTextAreaAvisos);
		JButton_Ejecutar.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent arg0) {
			   
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
	}
}