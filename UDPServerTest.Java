import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UDPServerTest {
    public static void main(String[] args){
        int porta = Integer.parseInt(args[0]);
        DatagramSocket bagulho = null;

        try{

            List<Jogador> jogadores = new ArrayList<>();

            System.out.println("Esperando jogadores...");
            bagulho = new DatagramSocket(porta);

            while(jogadores.size() <3){
                byte[] bloco = new byte[1000];
                DatagramPacket requisao = new DatagramPacket(bloco, bloco.length);
                System.out.println("Bora manda as informações ai");
                bagulho.receive(requisao);
                
                String nick = new String(requisao.getData());
                InetAddress enderecoCliente = requisao.getAddress();
                int portaCliente = requisao.getPort();


                boolean existe = false;
                for (Jogador jogador : jogadores){
                    if(jogador.getIp().equals(enderecoCliente) && jogador.getPorta() == portaCliente){
                        existe = true;
                        break;
                    }
                }
                

                if(!existe){
                    Jogador jogador = new Jogador(nick, portaCliente, enderecoCliente, 0);
                    jogadores.add(jogador);
 
                    System.out.println("Nome do jogador: " + jogador.getNome());
                    System.out.println("Cliente IP: " + jogador.getIp());
                    System.out.println("Cliente Porta: " + jogador.getPorta());
                
                }else{
                    System.out.println("Jogador já existe na lista.");
                }

                
            }

            for (Jogador jogador : jogadores) {
                
                String mensagem = "Jogadores encontrados...";

                byte[] blocomsg = mensagem.getBytes();
                DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, jogador.getIp(), jogador.getPorta());
                bagulho.send(pacotemsg);	
                
            }
                    
        }
        
        catch(SocketException erro){
            System.out.println("Socket deu erro aqui pai" + erro.getMessage());
        }

        catch(IOException error){
            System.out.println("IO deu erro aqui pai" + error.getMessage());

        }

        finally{
            if(bagulho != null) {
                bagulho.close();
            }
        }
    }
}