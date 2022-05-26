package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import comum.Util;

//import com.sun.javafx.collections;

import comum.Util;

public class Servidor {

	public static final String HOST = "127.0.0.1";
	public static final int PORT = 4444;
	
	private ServerSocket server;
	private Map<String, EscutaCliente> clientes;
	
	public Servidor() {
	
		try {
			String conn_info;
			clientes = new HashMap<String, EscutaCliente>();
			server = new ServerSocket(PORT);
			System.out.println("Servidor iniciado com sucesso! No host: "+ HOST +"e na porta: " + PORT);
			while(true)
			{
				Socket connection = server.accept();
				conn_info = Util.recebeMsg(connection);
				if(checaLogin(conn_info)) {
					EscutaCliente ec = new EscutaCliente(conn_info, connection, this);
					clientes.put(conn_info, ec);
					Util.enviaMsg(connection, "Sucesso");
					new Thread(ec).start();
				}else {
					Util.enviaMsg(connection, "Erro");
				}
			}
		
		} catch (IOException e) {
			System.err.println("Erro ao conectar ao servidor! " + e.getMessage());
		}
	}
	
	public Map<String, EscutaCliente> getCliente(){
		return clientes;
	}
	
	private boolean checaLogin(String conn_info){
		String[] splited = conn_info.split(":");
		//para cada par no clientes
		           //<Key,    Value>
		for(Map.Entry<String, EscutaCliente> pair: clientes.entrySet()) {
			String[] partes = pair.getKey().split(";");
			if(partes[0].toLowerCase().equals(splited[0].toLowerCase())) 
			{
				return false;
			//checando partes[1] = endereço ip/ partes[2] = porta	
			}else if((partes[1] + partes[2]).equals(splited[1]+splited[2])){
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Servidor server = new Servidor();
	}
	
	
}
