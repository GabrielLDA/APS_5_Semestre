package cliente;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;

import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import servidor.Servidor;
import comum.Util;

public class Login  extends JFrame {
    private JButton jb_login;
    private JLabel jl_user, jl_port, jl_title;
    private JTextField jt_user, jt_port;


     public Login()
     {
         super("Login");
         initComponents();
         configComponents();
         insertComponents();
         insertActions();
         start();

     }

     private void configComponents()
     {
         this.setLayout(null);
         this.setMinimumSize(new Dimension(400,300));
         this.setResizable(false);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.getContentPane().setBackground(new Color.WHITE);

         jl_title.setBounds(10,10,375,100);
         ImageIcon icon = ImageIcon("logo.png");
         jl_title.setIcon(new ImageIcon(icon.getImage().getScaledInstance(375, 100, Image.;)));

         jb_login.setBounds(10,220,375,40);

         jl_user.setBounds(10,120,100,40);
         jl_user.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));

         jl_port.setBounds(10,140,100,40);
         jl_port.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));

         jt_user.setBounds(120,120,265,40);
         jt_port.setBounds(120,170,265,40);





     }

     private ImageIcon ImageIcon(String string) {
        return null;
    }

    private void initComponents()
     {
        jb_login = new JButton("Entrar");
        jl_user = new JLabel("Apelido", SwingConstants.CENTER);
        jl_port = new JLabel("Porta",SwingConstants.CENTER);
        jl_title = new JLabel();
        jt_user = new JTextField();
        jt_port = new JTextField();
     }

     private void insertComponents()
     {
        this.add( jb_login);
        this.add( jl_user);
        this.add( jl_port);
        this.add( jl_title);
        this.add( jt_user);
        this.add( jt_port);

     }

     private void insertActions()
     {
    	     	 
    	 jb_login.addActionListener(event -> {
    		 try {
    			 String nickname = jt_user.getText();
        		 jt_user.setText("");
        		 int port = Integer.parseInt(jt_port.getText());
        		 jt_port.setText("");
        		 Socket connection = new Socket(Servidor.HOST, Servidor.PORT);
        		 String conn_info = (nickname +":"+ connection.getLocalAddress().getHostAddress()+":"+ port);
        		 Util.enviaMsg(connection, conn_info);
        		 if(Util.recebeMsg(connection).toLowerCase().equals("Sucesso")) {
        			 new Home(connection, conn_info);
        			 this.dispose();
        		 }else {
        			 JOptionPane.showMessageDialog(null, "Ja tem algum usuário usando esse nickname, ou com esse host/porta"
        			 		+ "tente outra porta.");
        		 }
    		 } catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro, verifique se o servidor está executando.");
			 }
    	 	});
  
     }

     private void start()
     {
         this.pack();
         this.setVisible(true);
     }

     public static void main(String[] args) {

        Login login = new Login();
         
     }


    
}
