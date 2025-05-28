import java.net.*;
import java.io.*;
import java ControleJogadores;

public class UDPClientSimple {
    public static void main(String[] args){ 

        DatagramSocket aSocket = null;
        try{
        	aSocket = new DatagramSocket();
            byte[] msg = args[0].getBytes();
            InetAddress aHost = InetAddress.getByName(args[1]);
            int serverPort = Integer.parseInt(args[2]);
        	DatagramPacket request = new DatagramPacket(msg, msg.length, aHost, serverPort);
            System.out.println("Enviando mensagem '" + args[0] + "' para o Servidor UDP Simples.");
        	aSocket.send(request);	

            byte[] buffer = new byte[1000];  
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);  
            
            String received = new String(reply.getData(), 0, reply.getLength());
            System.out.println("Resposta do Servidor UDP: " + received);
        	
        }catch(SocketException e){
        	System.out.println("Socket " + e.getMessage());
        } catch(IOException e)
        	{System.out.println("IO " + e.getMessage());
        } finally{
        	if(aSocket != null) 
        		aSocket.close();
        }
    }
}               