import javax.swing.*;
import java.awt.*;

public class Semaforo implements Runnable {
    public enum Estado {
        VERDE, AMARELO, VERMELHO
    }

    public Estado estadoAtual;
    private int tempoVerde;
    private int tempoAmarelo;
    private int tempoVermelho;
    private boolean running;
    private int timer;

    public Semaforo(int tempoVerde, int tempoAmarelo, int tempoVermelho) {
        this.tempoVerde = tempoVerde;
        this.tempoAmarelo = tempoAmarelo;
        this.tempoVermelho = tempoVermelho;
        this.estadoAtual = Estado.VERMELHO;
        this.running = true;
        this.timer = 0;
    }

    public void parar() {
        running = false;
    }

    public Estado getEstadoAtual() {
        return estadoAtual;
    }

    public int getTempoVerde() {
        return tempoVerde;
    }

    public int getTempoAmarelo() {
        return tempoAmarelo;
    }

    public int getTempoVermelho() {
        return tempoVermelho;
    }

    public int getTimer() {
        return timer;
    }

    public void resetTimer() {
        this.timer = 0;
    }

    public void incrementTimer() {
        this.timer++;
    }

    @Override
    public void run() {
        while (running) {
            try {
                synchronized (this) {
                    estadoAtual = Estado.VERMELHO;
                    resetTimer();
                    for (int i = 0; i < tempoVermelho / 1000; i++) {
                        Thread.sleep(1000);
                        incrementTimer();
                    }

                    estadoAtual = Estado.VERDE;
                    resetTimer();
                    for (int i = 0; i < tempoVerde / 1000; i++) {
                        Thread.sleep(1000);
                        incrementTimer();
                    }

                    estadoAtual = Estado.AMARELO;
                    resetTimer();
                    for (int i = 0; i < tempoAmarelo / 1000; i++) {
                        Thread.sleep(1000);
                        incrementTimer();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
