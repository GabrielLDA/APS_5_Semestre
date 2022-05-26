package cliente;

import java.io.IOException;
import java.net.Socket;

import comum.Util;

public class EscutaClientec implements Runnable{

	private boolean runs;
	private boolean chataberto;
	private Socket conn;
	private Home home;
	private String conn_info;
	private Chat chat;
	
	
	
	public EscutaClientec(Socket conn, Home home) 
	{
		this.runs = false;
		this.chataberto = false;
		this.conn = conn;
		this.home = home;
		this.conn_info = null;
		this.chat = null;
	}

		
	public boolean isRuns() {
		return runs;
	}




	public void setRuns(boolean runs) {
		this.runs = runs;
	}




	public boolean isChataberto() {
		return chataberto;
	}




	public void setChataberto(boolean chataberto) {
		this.chataberto = chataberto;
	}




	public Chat getChat() {
		return chat;
	}




	public void setChat(Chat chat) {
		this.chat = chat;
	}



	@Override
	public void run() {
		runs = true;
		String msg;
		while(runs) {
			msg = Util.recebeMsg(conn);
			if(msg == null || msg.equals("chat_close")) {
				if(chataberto) {
					home.getChats_abertos().remove(conn_info);
					home.getEc().remove(conn_info);
					chataberto = false;
					try {
						conn.close();
					}catch(IOException e) {
						System.out.println("Erro Ec"+e.getMessage());
					}
					chat.dispose();
				}
				runs = false;
			}else {
				String [] campos = msg.split(";");
				if(campos.length > 1) 
				{
					if(campos[0].equals("chat-aberto")) {
						String [] splited = campos[1].split(":");
						String conn_info = campos[1];
						if(!chataberto)
						{
							home.getChats_abertos().add(conn_info);
							home.getEc().put(conn_info, this);
							chataberto = true;
							chat = new Chat(home, conn, conn_info, home.getConn_info());
							
						}
					}else if(campos[0].equals("Mensagem")) 
					{
						String message = "";
						for(int i = 1; i<campos.length; i++) 
						{
							message += campos[i];
							if(i > 1) 
							{	
								message += ";";
							}
						}
						chat.append_menssagem(message);
					}
				}
			  	
			}
			System.out.println("Mensagem recebida"  + msg);
		}
	}
	
}
