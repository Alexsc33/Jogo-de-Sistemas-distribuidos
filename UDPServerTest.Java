import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UDPServerTest {
    public static void main(String[] args){
        int porta = Integer.parseInt(args[0]);
        DatagramSocket bSocket = null;

        try{

            List<Jogador> jogadores = new ArrayList<>();
            List<Jogador> eliminados = new ArrayList<>();

            System.out.println("""
            ================================================================================
            Regras do Jogo da Sobrevivência Numérica:
            ================================================================================
            No início três jogadores jogam, escolhendo um número entre 0 e 100.
            O Servidor do jogo receberá os três números escolhidos e calculará a média dos valores recebidos.
            O resultado das médias é então multiplicado por 0,8.
            Este novo valor resultante será o valor alvo.
            O valor alvo é comparado com os valores que cada jogador escolheu.
            O jogador que mais se distanciou do valor alvo, perde dois pontos.
            O jogador que mais se aproximou do valor alvo, não perde pontos.
            O outro jogador perde apenas um ponto.
            O jogador que chegar a menos seis pontos, primeiro, será eliminado definitivamente do jogo.
            Quando restarem apenas dois jogadores, as regras do jogo mudam.
            O jogador que mais se distanciar do valor alvo, perde um ponto.
            O outro jogador, não perde pontos.
            O jogador que primeiro chegar a menos seis pontos, será eliminado do jogo.
            O último jogador é declarado vencedor do Jogo da Sobrevivência Numérica.
            ================================================================================

                """);

            System.out.println("Esperando jogadores...");
            bSocket = new DatagramSocket(porta);

            while(jogadores.size() <3){
                byte[] bloco = new byte[1000];
                DatagramPacket requisao = new DatagramPacket(bloco, bloco.length);
                bSocket.receive(requisao);
                
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
 
                    System.out.println("\nNome do jogador: " + jogador.getNome());
                    System.out.println("Cliente IP: " + jogador.getIp());
                    System.out.println("Cliente Porta: " + jogador.getPorta());
                
                }else{
                    System.out.println("\nJogador já existe na lista.");
                }

                
            }

            for (Jogador jogador : jogadores) {
                
                String mensagem = "Jogadores encontrados, Enviando atutorização para: " + jogador.getNome();

                byte[] blocomsg = mensagem.getBytes();
                DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, jogador.getIp(), jogador.getPorta());
                bSocket.send(pacotemsg);	
                
            }

            System.out.println("\nQue comecem os jogos...\n");

            int[] numeros = new int[3];
            InetAddress[] enderecosClientes = new InetAddress[3];
            int[] portasClientes = new int[3];

            while(jogadores.size() >= 3){
                System.out.println("\nAguardando os numeros de cada jogador...\n");
                
                for(int i=0; i<3; i++){
                    byte[] bloco = new byte[1000];
                    DatagramPacket requisao = new DatagramPacket(bloco, bloco.length);
                    bSocket.receive(requisao);
                    
                    numeros[i] = Integer.parseInt(new String(requisao.getData()).trim());
                    enderecosClientes[i] = requisao.getAddress();
                    portasClientes[i] = requisao.getPort();

                    for(Jogador jogador : jogadores) {
                        if(jogador.getIp().equals(enderecosClientes[i]) && jogador.getPorta() == portasClientes[i]) {
                            System.out.println("Jogador " + jogador.getNome() + " escolheu o número: " + numeros[i]);
                        }
                    }
                }

                System.out.println("\nCalculando Média...");
                double media = (numeros[0] + numeros[1] + numeros[2]) / 3.0;
                System.out.println("Média encontrada: " + media);

                System.out.println("\nRealizando cálculo do valor alvo...");
                double valorAlvo = media * 0.8;
                System.out.println("Valor alvo encontrado: " + valorAlvo);

                System.out.println("\nRealizando calculo do placar do jogo...\n");

                double[] distancia= new double[3];
                
                for (int i = 0; i < 3; i++) {
                    distancia[i] = Math.abs(numeros[i] - valorAlvo);
                }

                for (int i = 0; i < 3; i++) {
                    for (Jogador jogador : jogadores) {
                        if (jogador.getIp().equals(enderecosClientes[i]) && jogador.getPorta() == portasClientes[i]) {
                            System.out.println("Jogador " + jogador.getNome() + " está a " + distancia[i] + " de distância do valor alvo.");
                        }
                    }
                }

                int maisProximo = 0;
                int maisDistante = 0;

                for (int i = 1; i < 3; i++) {
                    if (distancia[i] < distancia[maisProximo]) {
                        maisProximo = i;
                    }
                    if (distancia[i] > distancia[maisDistante]) {
                        maisDistante = i;
                    }
                }

                System.out.println("\n");

                for (int i = 0; i < 3; i++) {
                    for (Jogador jogador : jogadores) {
                        if (jogador.getIp().equals(enderecosClientes[i]) && jogador.getPorta() == portasClientes[i]) {
                            if (i == maisProximo) {
                                jogador.setPontuacao(jogador.getPontuacao() + 0);  
                                System.out.println("Jogador " + jogador.getNome() + " vai receber 0 pontos.");
                            } else if (i == maisDistante) {
                                jogador.setPontuacao(jogador.getPontuacao() - 2);  
                                System.out.println("Jogador " + jogador.getNome() + " vai receber -2 pontos.");
                            } else {
                                jogador.setPontuacao(jogador.getPontuacao() - 1);  
                                System.out.println("Jogador " + jogador.getNome() + " vai receber -1 pontos.");
                            }

                        }   
                    }
                }

                System.out.println("\nAtualizando quantidade de jogadores...\n");


                for (Jogador jogador : jogadores) {
                    System.out.println("Enviando placar para " + jogador.getNome() + " com " + jogador.getPontuacao() + " pontos.");
                    String mensagem =  String.valueOf(jogador.getPontuacao());

                    byte[] blocomsg = mensagem.getBytes();
                    DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, jogador.getIp(), jogador.getPorta());
                 bSocket.send(pacotemsg);

                }

                for(Jogador jogador : jogadores){
                    if(jogador.getPontuacao() <= -6){
                        eliminados.add(jogador);
                    }
                }

                jogadores.removeIf(jogador -> jogador.getPontuacao() <= -6);
            }

            while(jogadores.size()== 2){
                System.out.println("\nAguardando os numeros de cada jogador...\n");
                
                for(int i=0; i<2; i++){
                    byte[] bloco = new byte[1000];
                    DatagramPacket requisao = new DatagramPacket(bloco, bloco.length);
                 bSocket.receive(requisao);
                    
                    numeros[i] = Integer.parseInt(new String(requisao.getData()).trim());
                    enderecosClientes[i] = requisao.getAddress();
                    portasClientes[i] = requisao.getPort();

                    for(Jogador jogador : jogadores) {
                        if(jogador.getIp().equals(enderecosClientes[i]) && jogador.getPorta() == portasClientes[i]) {
                            System.out.println("Jogador " + jogador.getNome() + " escolheu o número: " + numeros[i]);
                        }
                    }
                }

                System.out.println("\nCalculando Média...");
                double media = (numeros[0] + numeros[1]) / 2.0;
                System.out.println("Média encontrada: " + media);

                System.out.println("\nRealizando cálculo do valor alvo...");
                double valorAlvo = media * 0.8;
                System.out.println("Valor alvo encontrado: " + valorAlvo);

                System.out.println("\nRealizando calculo do placar do jogo...\n");

                double[] distancia= new double[2];
                
                for (int i = 0; i < 2; i++) {
                    distancia[i] = Math.abs(numeros[i] - valorAlvo);
                }

                for (int i = 0; i < 2; i++) {
                    for (Jogador jogador : jogadores) {
                        if (jogador.getIp().equals(enderecosClientes[i]) && jogador.getPorta() == portasClientes[i]) {
                            System.out.println("Jogador " + jogador.getNome() + " está a " + distancia[i] + " de distância do valor alvo.");
                        }
                    }
                }

                int maisProximo = 0;
                int maisDistante = 0;

                /*for (int i = 1; i < 2; i++) {
                    if (distancia[i] < distancia[maisProximo]) {
                        maisProximo = i;
                    }
                    if (distancia[i] > distancia[maisDistante]) {
                        maisDistante = i;
                    }
                }*/

               if(distancia[0] < distancia[1]) {
                    maisProximo = 0;
                    maisDistante = 1;
                }else if(distancia[0] > distancia[1]) {
                    maisProximo = 1;
                    maisDistante = 0;
                
                }else {
                    maisProximo = 1;
                    maisDistante = 0;
                }

                System.out.println("\n");

                for (int i = 0; i < 2; i++) {
                    for (Jogador jogador : jogadores) {
                        if (jogador.getIp().equals(enderecosClientes[i]) && jogador.getPorta() == portasClientes[i]) {
                            if (i == maisProximo) {
                                jogador.setPontuacao(jogador.getPontuacao() + 0); 
                                System.out.println("Jogador " + jogador.getNome() + " vai receber 0 pontos.");
                            } else if (i == maisDistante) {
                                jogador.setPontuacao(jogador.getPontuacao() - 1);  
                                System.out.println("Jogador " + jogador.getNome() + " vai receber -1 pontos.");
                            }
                            
                        }   
                    }
                }

                System.out.println("\nAtualizando quantidade de jogadores...\n");

                for (Jogador jogador : jogadores) {
                    System.out.println("Enviando placar para " + jogador.getNome() + " com " + jogador.getPontuacao() + " pontos.");
                    String mensagem =  String.valueOf(jogador.getPontuacao());

                    byte[] blocomsg = mensagem.getBytes();
                    DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, jogador.getIp(), jogador.getPorta());
                    bSocket.send(pacotemsg);

                }

                for(Jogador jogador : jogadores){
                    if(jogador.getPontuacao() <= -6){
                        eliminados.add(jogador);
                    }
                }

                jogadores.removeIf(jogador -> jogador.getPontuacao() <= -6);

            }

            while(jogadores.size()== 1){
                for(Jogador jogador : jogadores){
                    jogador.setPontuacao(10);
                    System.out.println("\nJogador " + jogador.getNome() + " ganhou o jogo com " + jogador.getPontuacao() + " pontos.");

                    String mensagem =  String.valueOf(jogador.getPontuacao());

                    byte[] blocomsg = mensagem.getBytes();
                    DatagramPacket pacotemsg = new DatagramPacket(blocomsg, blocomsg.length, jogador.getIp(), jogador.getPorta());
                    bSocket.send(pacotemsg);
                }

                System.out.println("\nPlacar final:");

                for (Jogador jogador : jogadores) {
                    System.out.println("Jogador " + jogador.getNome() + " ganhou o jogo com " + jogador.getPontuacao() + " pontos.");
                }

                for (Jogador jogador : eliminados) {
                    System.out.println("Jogador " + jogador.getNome() + " foi eliminado com " + jogador.getPontuacao() + " pontos.");
                }
                jogadores.removeIf(jogador -> jogador.getPontuacao() == 10);
                break;
            }
                    
        }
        
        catch(SocketException erro){
            System.out.println("Socket apresenta os seguintes erros:" + erro.getMessage());
        }

        catch(IOException error){
            System.out.println("IO apresenta os seguintes erros:" + error.getMessage());

        }

        finally{
            if (bSocket != null) {
             bSocket.close();
            }
        }
    }
}