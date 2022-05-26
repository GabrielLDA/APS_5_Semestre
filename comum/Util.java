package comum;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Util {

	public static boolean enviaMsg(Socket connection, String msg) {
		try {
			ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			output.writeObject(msg);
		}catch(IOException e) {
			System.err.println("Erro no envio da mensagem: " + e.getMessage());
		}
		return false;
	}
	
	
	public static String recebeMsg(Socket connection) {
		String resposta = null;
		try {
			ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
			resposta = (String) input.readObject();
		
		}catch(IOException e) {
			System.err.println("Erro ao receber a mensagem: " + e.getMessage());
		}catch(ClassNotFoundException e) {
			System.err.println("Erro ao receber a mensagem: " + e.getMessage());
		}
		return resposta;
		
	}
	
	
}
