import java.net.*;
import java.io.*;
import java.util.Scanner;

public class UDPClientTest {
    public static void main(String[] args){ 
        DatagramSocket aSocket = null;

        try{
            
            aSocket = new DatagramSocket();
            //byte[] msg = args[0].getBytes();
            InetAddress aHost = InetAddress.getByName(args[0]);
            int serverPort = Integer.parseInt(args[1]);
        	//DatagramPacket request = new DatagramPacket(msg, msg.length, aHost, serverPort);
        	//aSocket.send(request);	

            Scanner scan = new Scanner(System.in);
            System.out.println("Digite seu nick:");
            String nick = scan.nextLine();

            /*byte[] msg1 = nick.getBytes();
            DatagramPacket request2 = new DatagramPacket(msg1, msg1.length, aHost, serverPort);
            aSocket.send(request2);	*/

            System.out.println("Bem vindo, " + nick);

            boolean jogoAtivo = true;
            while(jogoAtivo){
                System.out.println("""
            ╔════════════════════════════════════════════════════════╗
            ║                MENU - Jogo da Sobrevivência            ║
            ╠════════════════════════════════════════════════════════╣
            ║  1  - Ver as regras do jogo                            ║
            ║  2  - Iniciar o jogo                                   ║
            ║  3  - Sair do jogo                                     ║
            ╚════════════════════════════════════════════════════════╝
            """);

            System.out.print("O que deseja: ");
            String escolha = scan.nextLine();
            scan.nextLine(); 

            switch (escolha) {
                case "1":
                    System.out.println("Regras do jogo: ...");

                     System.out.println("""
                    ----------------------------------------------------------------------------
                    No início, três jogadores jogam, escolhendo um número entre 0 e 100.
                    O servidor do jogo (Juiz) receberá os três números escolhidos e calculará a média dos valores recebidos.
                    O resultado das médias é então multiplicado por 0,8.
                    Este novo valor resultante será o valor alvo.
                    O valor alvo é comparado com os valores que cada jogador escolheu.
                    O jogador que mais se distanciou do valor alvo perde dois pontos.
                    O jogador que mais se aproximou do valor alvo não perde pontos.
                    O outro jogador perde apenas um ponto.
                    O jogador que chegar a menos seis pontos primeiro será eliminado definitivamente do jogo.
                    Quando restarem apenas dois jogadores, as regras do jogo mudam.
                    O jogador que mais se distanciar do valor alvo perde um ponto.
                    O outro jogador não perde pontos.
                    O jogador que primeiro chegar a menos seis pontos será eliminado do jogo.
                    O último jogador é declarado vencedor do Jogo da Sobrevivência Numérica.                   ----------------------------------------------------------------------------
                    """);
                    System.out.print("Pressione ENTER para voltar ao menu...");
                    scan.nextLine(); 

                    break;
                case "2":

                    System.out.println("Iniciando o jogo...");
                    byte[] msg1 = nick.getBytes();
                    DatagramPacket request2 = new DatagramPacket(msg1, msg1.length, aHost, serverPort);
                    aSocket.send(request2);
                    System.out.println("Enviando seu cadastro para o servidor...");
                    System.out.println("Aguardando oponentes...");
                    

                    byte[] blocoRetorno = new byte[1000];
                    DatagramPacket pacoteRetorno = new DatagramPacket(blocoRetorno, blocoRetorno.length);
                    aSocket.receive(pacoteRetorno);

                    String retorno = new String(pacoteRetorno.getData());
                    System.out.println(retorno.trim());

                    boolean ativo= true;

                    while(ativo){
                        System.out.println("Escolha um número entre 0 e 100:");
                        System.out.print("Digite um numero:");
                        int numero = scan.nextInt();

                        byte[] msg2 = String.valueOf(numero).getBytes();
                        DatagramPacket request3 = new DatagramPacket(msg2, msg2.length, aHost, serverPort);
                        aSocket.send(request3);

                        byte[] novoBlocoRetorno = new byte[1000];
                        DatagramPacket novoPacoteRetorno = new DatagramPacket(novoBlocoRetorno, novoBlocoRetorno.length);
                        aSocket.receive(novoPacoteRetorno);

                        String mensagemRecebida = new String(novoPacoteRetorno.getData()).trim();
                        int pontos = Integer.parseInt(mensagemRecebida);

                        System.out.println("Seu Placar é: " + pontos);

                        if(pontos <= -6){
                            System.out.println("Você foi eliminado do jogo!");
                            ativo = false;
                        }
                        else if(pontos == 10){
                            System.out.println("Você ganhou o jogo!");
                            ativo = false;
                        } 
                    }	

                    /*byte[] blocoRetorno2 = new byte[1000];
                    DatagramPacket pacoteRetorno2 = new DatagramPacket(blocoRetorno2, blocoRetorno2.length);
                    aSocket.receive(pacoteRetorno2);

                    String retorno2 = new String(pacoteRetorno2.getData());
                    System.out.println(retorno2.trim());*/


                    break;
                case "3":
                    System.out.println("Saindo do jogo...");
                    jogoAtivo = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

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