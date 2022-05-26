package cliente;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;

import comum.Util;

import javafx.event.Event;
import javafx.scene.layout.Border;

public class Chat extends JFrame{

    private JLabel jl_title;
    private JEditorPane menssagens;
    private JTextField jt_menssagens;
    private JButton jb_menssagem;
    private JPanel panel;
    private JScrollPane scroll;
    
    private Home home;
    private Socket conn;
    private ArrayList<String> menssagem_list;
    private String connection_info;



    public Chat(Home home, Socket conn, String connection_info, String title){
        super("Chat" + title);
        this.home = home;
        this.conn = conn;
        this.connection_info = connection_info;
        initComponents();
        insertActions();
        insertComponents();
        configComponents();
        start();

    }

    private void configComponents()
    {
        this.setMinimumSize(new Dimension(480,720) );
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menssagens.setContentType("text/html");
        menssagens.setEditable(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jb_menssagem.setSize(100,40);

        
    }

    

   private void initComponents()
    {

        menssagem_list = new ArrayList<String>();
        jl_title = new JLabel(connection_info.split(":")[0], SwingConstants.CENTER);
        menssagens = new JEditorPane();
        scroll = new JScrollPane(menssagens);
        jt_menssagens = new JTextField();
        jb_menssagem = new JButton("Enviar");
        panel = new JPanel(new BorderLayout());

        
    }

    private void insertComponents()
    {
       this.add(jl_title, BorderLayout.NORTH);
       this.add(scroll, BorderLayout.CENTER);
       this.add(panel, BorderLayout.SOUTH);
       panel.add(jt_menssagens, BorderLayout.CENTER);
       panel.add(jb_menssagem, BorderLayout.EAST);


    }

    private void insertActions()
    {
        jb_menssagem.addActionListener(event -> send());
        jt_menssagens.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e){
             }
           
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyChar() == KeyEvent.VK_ENTER)
                {
                    send();
                }
            }

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				Util.enviaMsg(conn, "chat_close");
				home.getChats_abertos().remove(connection_info);
				home.getEc().get(connection_info).setChataberto(false);
				home.getEc().get(connection_info).setRuns(false);
				home.getEc().remove(connection_info);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
        	
        });
    }

    public void append_menssagem(String received)
    {
        menssagem_list.add(received);
        String menssagem = "";
        for (String str : menssagem_list){
            menssagem += str;

        }
        menssagens.setText(menssagem);
 
    }

    private void send()
    {
        if(jt_menssagens.getText().length()>0){
        DateFormat df = new SimpleDateFormat("hh/mm/ss");
        String msg = "<b>["+ df.format(new Date()) + "] Eu: </b><i>"+ jt_menssagens.getText() + "</i><br>";
        Util.enviaMsg(conn, "Mensagem;" + "<b>[" + df.format(new Date()) + "] "+ this.jl_title + ": </b><i>" + jt_menssagens.getText() +"</i><br>" );
        append_menssagem("<b>[" + df.format(new Date()) + " ]Eu: </b><i>" + jt_menssagens.getText() +"</i><br>" );
        jt_menssagens.setText("");
    }
    }

    private void start()
    {
        this.pack();
        this.setVisible(true);
    }

    
    
    
    
}






