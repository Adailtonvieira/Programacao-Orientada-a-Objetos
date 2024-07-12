import java.util.ArrayList;

public class Cruzamento {
    private ArrayList<Semaforo> semaforos;

    public Cruzamento() {
        semaforos = new ArrayList<>();
        inicializarSemaforos();
    }

    private void inicializarSemaforos() {
        semaforos.add(new Semaforo(5000, 2000, 15000));
        semaforos.add(new Semaforo(5000, 2000, 15000));
        semaforos.add(new Semaforo(5000, 2000, 15000));
        semaforos.add(new Semaforo(5000, 2000, 15000));

        new Thread(new Runnable() {
            @Override
            public void run() {
                sincronizar();
            }
        }).start();
    }

    private void sincronizar() {
        int index = 0;
        try {
            // Inicia o primeiro semáforo em amarelo e os demais em vermelho
            semaforos.get(0).estadoAtual = Semaforo.Estado.VERDE;
            Thread.sleep(semaforos.get(0).getTempoAmarelo());

            while (true) {
                for (int i = 0; i < semaforos.size(); i++) {
                    semaforos.get(i).parar();
                }

                Semaforo semaforoAtual = semaforos.get(index);
                semaforoAtual.estadoAtual = Semaforo.Estado.VERDE;
                semaforoAtual.resetTimer();
                for (int i = 0; i < semaforoAtual.getTempoVerde() / 1000; i++) {
                    Thread.sleep(1000);
                    semaforoAtual.incrementTimer();
                }

                semaforoAtual.estadoAtual = Semaforo.Estado.AMARELO;
                semaforoAtual.resetTimer();
                for (int i = 0; i < semaforoAtual.getTempoAmarelo() / 1000; i++) {
                    Thread.sleep(1000);
                    semaforoAtual.incrementTimer();
                }

                semaforoAtual.estadoAtual = Semaforo.Estado.VERMELHO;

                // Passar para o próximo semáforo
                index = (index + 1) % semaforos.size();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Semaforo> getSemaforos() {
        return semaforos;
    }
}
