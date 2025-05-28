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
                
                String vazio = new String(requisao.getData());
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
                    byte[] blocoNick = new byte[1000];
                    DatagramPacket nick = new DatagramPacket(blocoNick, blocoNick.length);
                    bagulho.receive(nick);

                    String dado = new String(nick.getData());

                    Jogador jogador = new Jogador(dado, portaCliente, enderecoCliente, 0);
                    jogadores.add(jogador);
                    System.out.println("Jogador adicionado: " + jogador.getPorta());
                }
                else{
                    System.out.println("Jogador já existe na lista.");
                }

            }

            List<String> jogadoresConfirmados = new ArrayList<>();
            int cadastrados =0;

            while(cadastrados < 3){
                byte[] blocoEscolha = new byte[1000];
                DatagramPacket pacoteEscolha = new DatagramPacket(blocoEscolha, blocoEscolha.length);
                bagulho.receive(pacoteEscolha);

                String choice = new String(pacoteEscolha.getData());
                InetAddress enderecoCadastro = pacoteEscolha.getAddress();
                int portaCadastro = pacoteEscolha.getPort();

                String idJogador = enderecoCadastro.toString() + ":" + portaCadastro;
            

            switch(choice.trim()){
                case "1":
                    //System.out.println("Iniciando o jogo com " + jogadores.size() + " jogadores.");
                    // Aqui você pode adicionar a lógica para iniciar o jogo

                    if(!jogadoresConfirmados.contains(idJogador)){
                        jogadoresConfirmados.add(idJogador);
                        cadastrados++;

                        for (Jogador jogador : jogadores) {
                            if (jogador.getPorta() == portaCadastro && jogador.getIp().equals(enderecoCadastro)) {
                               
                                System.out.println("Nome do jogador: " + jogador.getNome());
                                System.out.println("Cliente IP: " + jogador.getIp());
                                System.out.println("Cliente Porta: " + jogador.getPorta());

                                String mensagem = (cadastrados < 3)
                                ?"Aguardando oponentes..."
                                : "Jogadores encontrados...";

                                byte[] blocomsg = mensagem.getBytes();
                                DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, enderecoCadastro, portaCadastro);
                                bagulho.send(pacotemsg);	
                            }
                        }

                        if(cadastrados == 3) {
                            for (Jogador jogador : jogadores) {

                                String mensagem = "Jogadores encontrados...";

                                byte[] blocomsg = mensagem.getBytes();
                                DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, jogador.getIp(), jogador.getPorta());
                                bagulho.send(pacotemsg);
                            }

                        }
                    }

                    break;

                case "2":
                    System.out.println("Saindo do jogo.");
                    break;
                default:
                    System.out.println("Escolha inválida.");
            }

            

        }

        }
        
        catch(SocketException erro){
            System.out.println("Socket de erro aqui pai" + erro.getMessage());
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