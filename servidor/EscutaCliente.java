package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import comum.Util;

//implementa thread 
public class EscutaCliente implements Runnable{
		
	private String conn_info;
	private Socket conn;
	private Servidor server;
	private boolean runs;
	
	public EscutaCliente(String conn_info, Socket conn, Servidor server) {
		this.conn_info  = conn_info;
		this.conn 		= conn;
		this.server 	= server;
		this.runs   = false;
	}
	
	public boolean runs() {
		return runs;
	}
	
	public void setRuns() {
		this.runs = runs;
	}
	
	public void run() {
		runs = true;
		String msg;
		while(runs) {
			msg = Util.recebeMsg(conn);
			if(msg.equals("sair")) {
				server.getCliente().remove(conn_info);
				try {
					conn.close();
				}catch(IOException e){
					System.out.println("Erro escuta cliente" +e.getMessage());
				}
				runs = false;
			}else if(msg.equals("GET_CONNECTED_USERS")) {
				System.out.println("Atualize a lista de contatos");
				String resposta = "";
				//concatenando lista de contatos
				for(Map.Entry<String, EscutaCliente> pair: server.getCliente().entrySet()) {
					resposta += (pair.getKey() + ";");
				}
				Util.enviaMsg(conn, resposta);
			}else {
			System.out.println("Mensagem"+ msg +"recebida.");
			}
		}
	}
	
	
}
