package netrequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 * @author Andres F. Cardenas
 */
public class NetRequest extends JFrame implements ActionListener, Runnable {
    
    /*Se crean las variables globales*/
    JLabel labelWebPage = new JLabel("Web Page: ", JLabel.RIGHT);
    JLabel labelAutor = new JLabel("By Andres F. Cardenas", JLabel.RIGHT);
    JLabel labelMilliseconds = new JLabel("Milliseconds: ", JLabel.RIGHT);
    
    JTextField textWebPage = new JTextField("http://google.com.co", 15);
    JTextField textMiliseconds = new JTextField("1000", 15);
    
    JTextArea textLogs = new JTextArea(20, 30);
    JScrollPane scrollTextLogs = new JScrollPane(textLogs);
    
    ImageIcon iconStart = new ImageIcon("./images/alta.png");
    ImageIcon iconStop = new ImageIcon("./images/baja.png");
    
    JButton buttonStart = new JButton("Start", iconStart);
    JButton buttonStop = new JButton("Stop", iconStop);   
        
    JMenuBar menuBar = new JMenuBar();
    JMenu menuFile = new JMenu("File");
    JMenu menuHelp = new JMenu("Help");
    JMenuItem menuItemQuit = new JMenuItem("Quit");
    JMenuItem menuItemAboutNetRequest = new JMenuItem("About NetRequest");
    
    JPanel panel = new JPanel(new GridLayout(3, 2));   
    
    Thread threadRequest;
    
    long milli;
    String web;
    
    public NetRequest () {
        
        super("NetRequest");
        setDefaultCloseOperation(EXIT_ON_CLOSE);         
        
        BorderLayout layoutGeneral = new BorderLayout();
        setLayout(layoutGeneral);
        
        buttonStop.setEnabled(false);
        textLogs.setEditable(false);
        
        /*se crea y se configura la barra de menu*/
        menuFile.add(menuItemQuit);
        menuHelp.add(menuItemAboutNetRequest);        
        menuBar.add(menuFile);
        menuBar.add(menuHelp); 
        setJMenuBar(menuBar);
        
        /*se crea la estructura principal de la interfaz grafica*/
        panel.add(labelWebPage);
        panel.add(textWebPage);
        panel.add(labelMilliseconds);
        panel.add(textMiliseconds);
        panel.add(buttonStart);
        panel.add(buttonStop);
        add(panel, BorderLayout.NORTH);
        add(scrollTextLogs, BorderLayout.CENTER);
        add(labelAutor, BorderLayout.SOUTH);
        
        buttonStart.addActionListener(this);
        buttonStop.addActionListener(this);
        menuItemQuit.addActionListener(this);
        menuItemAboutNetRequest.addActionListener(this);
        
        setVisible(true);        
    }
    
    @Override
    public void actionPerformed (ActionEvent event) {
        
        if (event.getSource() == menuItemQuit){
            
            System.exit(0);
        } 
        
        if (event.getSource() == menuItemAboutNetRequest){
            
            JOptionPane.showMessageDialog(null,
                    "***NetRequest***\n"
                    + "Miniaplicacion que satura la red\n"
                    + "para acelerar las descargas\n"
                    + "\nBy Andres F. Cardenas",
                    "About NetRequest",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
        if (event.getSource() == buttonStart){
            
            milli = Long.parseLong(textMiliseconds.getText());
            web = textWebPage.getText();
            textLogs.append("==============================\n");
            textLogs.append("Creando trafico con " + web + " a " + milli + " Ms\n");
            textLogs.append("==============================\n");
            
            textWebPage.setEditable(false);
            textMiliseconds.setEditable(false);
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);           
            
            threadRequest = new Thread(this);
            threadRequest.start();
        }
        
        if (event.getSource() == buttonStop){  
            
            textWebPage.setEditable(true);
            textMiliseconds.setEditable(true);
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true);
            threadRequest = null;            
        }
    }
    
    @Override
    public void run () { 
        
        try {
            
            int cont = 0;
            
            while (threadRequest != null) {
                
                URL webUrl = new URL(web);
                HttpURLConnection content = (HttpURLConnection) webUrl.openConnection();                                
                content.connect();
                content.getContent();
                content.disconnect();
                
                textLogs.append( cont++ + ". Creando trafico " + web + "\n");
                threadRequest.sleep(milli);               
            }
            
        } catch (InterruptedException ie){
            
            threadRequest = null;
            textWebPage.setEditable(true);
            textMiliseconds.setEditable(true);
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true); 
            
        } catch (MalformedURLException mue){
            
            JOptionPane.showMessageDialog(null,
                    "La URL dada esta mal, "
                    + "verifique que la url esta "
                    + "escrita correctamente.",
                    "Error en URL",
                    JOptionPane.ERROR_MESSAGE);            
            threadRequest = null;
            textWebPage.setEditable(true);
            textMiliseconds.setEditable(true);
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true); 
            
        } catch (IOException ioe){
            
            JOptionPane.showMessageDialog(null,
                    "La URL dada esta mal, "
                    + "verifique que la url esta "
                    + "escrita correctamente.",
                    "Error en URL",
                    JOptionPane.ERROR_MESSAGE); 
            threadRequest = null;
            textWebPage.setEditable(true);
            textMiliseconds.setEditable(true);
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true); 
            
        } catch (IllegalArgumentException iae) {
            
            JOptionPane.showMessageDialog(null,
                    "El protocolo http:// esta bien, "
                    + "pero falta el host, por favor "
                    + "escriba un host correcto.",
                    "Error en el Host",
                    JOptionPane.ERROR_MESSAGE); 
            threadRequest = null;
            textWebPage.setEditable(true);
            textMiliseconds.setEditable(true);
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true);            
        }
        
        textLogs.append("\n************************************\n\n");
    }

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        
        NetRequest mainWindow = new NetRequest();
        mainWindow.pack();
    }
}
