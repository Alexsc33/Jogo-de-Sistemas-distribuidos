import java.net.*;
import java.io.*;

public class UDPServerSimple {
    public static void main(String[] args){
        int portNumber = Integer.parseInt(args[0]);
        int loops = 2;
        DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(portNumber);
            while(loops != 0){
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                System.out.println("Esperando mensagem chegar no receive.");
                aSocket.receive(request);               
                String data = new String(request.getData());
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                byte[] responseData = data.getBytes();
                DatagramPacket response = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                System.out.println("Enviando mensagem de volta para o Cliente UDP.");
                aSocket.send(response);	

                System.out.println("Mensagem recebida do Cliente UDP Simples = " + data.trim());
                System.out.println("Cliente IP: " + clientAddress.getHostAddress());
                System.out.println("Cliente Porta: " + clientPort);
                loops--;
            }
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