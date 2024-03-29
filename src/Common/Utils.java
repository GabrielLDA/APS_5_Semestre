package Common;


import java.io.*;
import java.net.*;

public class Utils {
    
    public static boolean sendMessage(Socket connection, String message){
        try{
            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            output.writeObject(message);
            return true;
        }catch(IOException ex){
            System.err.println("[ERROR:sendMessage] -> " + ex.getMessage());
        }
        return false;
    
    }
    
    public static String receiveMessage(Socket connection){
        String response = null;
        try{
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            response = (String) input.readObject();
        }catch(IOException ex){
            System.err.println("[ERROR:receiveMessage] -> " + ex.getMessage());
        }catch(ClassNotFoundException ex){
            System.err.println("[ERROR:receiveMessage] -> " + ex.getMessage());

        }
        return response;
    }
    
}
