import java.net.InetAddress;

public class Jogador {
    private String nome;
    private int porta;
    private InetAddress ip;
    private int pontuacao;

    public Jogador(String nome, int porta, InetAddress ip, int pontuacao) {
        this.nome = nome;
        this.porta = porta;
        this.ip = ip;
        this.pontuacao = pontuacao;
    }

    // Getters e setters (opcional)

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}
    