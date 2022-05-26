package cliente;

import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;

import comum.Util;
import servidor.EscutaCliente;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ServerCloneException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends JFrame{

	private ArrayList<String> chats_abertos;
	private Map<String, EscutaClientec> ec;
	private ArrayList<String> usuarios_conectados;
    private String conn_info;
    private Socket conn;
    private ServerSocket servidor;
    private boolean runs;
    
    private JLabel jl_title;
    private JButton jb_get_connected , jb_start_talk;
    private JList jlist;
    private JScrollPane scroll;

    public Home(Socket conn, String conn_info){
        super("Chat - Home");
        this.conn = conn;
        this.conn_info = conn_info;
        initComponents();
        insertActions();
        insertComponents();
        configComponents();
        start();
    }

    private void configComponents()
    {
        this.setLayout(null);
        this.setMinimumSize(new DimensionUIResource(400, 600));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        
        jl_title.setBounds(10,10,360,40);
        jl_title.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        jb_get_connected.setBounds(400,10,180,40);
        jb_get_connected.setFocusable(false);


        jb_start_talk.setBounds(10,400,575,40);
        jb_start_talk.setFocusable(false);

        jlist.setBorder(BorderFactory.createTitledBorder("Usu√°rios Online"));   
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scroll.setBounds(10,60,575,335);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);




        
    }

    

   private void initComponents()
   {
	   runs = false;
	   servidor = null;
	   ec = new HashMap<String, EscutaClientec>();
	   chats_abertos = new ArrayList<String>();
	   usuarios_conectados = new ArrayList<String>();
       jl_title = new JLabel("<Usu·rio : " + conn_info.split(":" )[0] + (">") , SwingConstants.CENTER);
       jb_get_connected = new JButton("Atualizar Contatos");
       jb_start_talk = new JButton("Abrir Conversa");
       jlist = new JList<>();
       scroll = new JScrollPane(jlist);
    }

    private void insertComponents()
    {
       this.add(jl_title);
       this.add(jb_get_connected);
       this.add(scroll);
       this.add(jb_start_talk);
       this.add(jl_title);

    }

    private void insertActions()
    {
    	this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
							
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				runs = false;
				Util.enviaMsg(conn, "sair");	
				System.out.println("Conex„o encerrada com suesso");

			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				
			}
		});
		
    	jb_get_connected.addActionListener(event -> usuariosConectados());
    	jb_start_talk.addActionListener(event -> chatAberto());
    }

    private void start()
    {
        this.pack();
        this.setVisible(true);
        iniciaServidor(this, Integer.parseInt(conn_info.split(":")[2]));
    }

    private void usuariosConectados() {
    	Util.enviaMsg(conn, "GET_CONNECTED_USERS");
    	String resposta = Util.recebeMsg(conn);
    	jlist.removeAll();
    	usuarios_conectados.clear();
    	for(String info: resposta.split(";")){
    		if(!info.equals(conn_info)) {
    			usuarios_conectados.add(info);
    		}
    	}
    	jlist.setListData(usuarios_conectados.toArray());
    }

	public ArrayList<String> getChats_abertos() {
		return chats_abertos;
	}

	

	public Map<String, EscutaClientec> getEc() {
		return ec;
	}

	public String getConn_info() {
		return conn_info;
	}

	private void chatAberto() {
		
		int index = jlist.getSelectedIndex();
		if(index != -1)
		{
			String conn_info = jlist.getSelectedValue().toString();
			String[] splited = conn_info.split(":");
			if(!chats_abertos.contains(conn_info)) 
			{
				try {
					Socket conn = new Socket(splited[1], Integer.parseInt(splited[2]));
					Util.enviaMsg(conn, "chat-aberto;"+ this.conn_info);
					EscutaClientec ecs = new EscutaClientec(conn, this);
					ecs.setChat(new Chat(this, conn, conn_info, this.conn_info.split(":")[0]));
					ecs.setChataberto(true);
					ec.put(conn_info, ecs);
					chats_abertos.add(conn_info);
					new Thread(ec).start();
				}catch(IOException e) {
					System.err.println("Erro ao abrir chat!" + e.getMessage());
				}
			}
		}
	}
	
    private void iniciaServidor(Home home, int port) {
    	
    	new Thread() {
    		
    		public void run() {
    			runs = true;
    			try {
    				servidor = new ServerSocket(port);
    				System.out.println("ClientServer/ Servidor do lado cliente iniciado na porta:"
    						+ port + "...");
    				while(runs) {
    					Socket conn = servidor.accept();
    					EscutaClientec ec = new EscutaClientec(conn, home);
    					new Thread(ec).start();
    				}
    			}catch(IOException e) {
    				System.out.println("Erro ao startar server"+ e.getMessage());
    			}
    		}
    		
    		
    		
    	}.start();
    	
    }

	   

}