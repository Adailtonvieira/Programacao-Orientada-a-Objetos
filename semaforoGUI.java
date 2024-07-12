import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SemaforoGUI extends JFrame {
    private Cruzamento cruzamento;
    private JPanel panel;
    private JLabel timerLabel;
    private int timer;

    public SemaforoGUI() {
        cruzamento = new Cruzamento();
        timer = 0;

        setTitle("Simulação de Cruzamento com Semáforos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                desenharCruzamento(g);
                desenharSemaforos(g);
            }
        };

        timerLabel = new JLabel("Timer: 0");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.SOUTH);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        timer++;
                        timerLabel.setText("Timer: " + timer);
                        repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void desenharCruzamento(Graphics g) {
        // Desenha as ruas do cruzamento
        g.setColor(Color.LIGHT_GRAY);

        // Rua horizontal superior
        g.fillRect(50, 100, 700, 40);
        g.setColor(Color.WHITE);
        g.fillRect(50, 120, 700, 10);

        // Rua horizontal inferior
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(50, 450, 700, 40);
        g.setColor(Color.WHITE);
        g.fillRect(50, 470, 700, 10);

        // Rua vertical esquerda
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(100, 50, 40, 500);
        g.setColor(Color.WHITE);
        g.fillRect(120, 50, 10, 500);

        // Rua vertical direita
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(450, 50, 40, 500);
        g.setColor(Color.WHITE);
        g.fillRect(470, 50, 10, 500);

        // Meio-fio
        g.setColor(Color.DARK_GRAY);
        g.fillRect(40, 90, 720, 20);
        g.fillRect(40, 440, 720, 20);
        g.fillRect(90, 40, 20, 520);
        g.fillRect(440, 40, 20, 520);

        // Faixas de pedestres
        g.setColor(Color.WHITE);
        g.fillRect(370, 90, 20, 10);   // Faixa superior
        g.fillRect(370, 440, 20, 10);  // Faixa inferior
        g.fillRect(90, 370, 10, 20);   // Faixa esquerda
        g.fillRect(440, 370, 10, 20);  // Faixa direita

        // Setas para indicar que neste semaforo pode viraar à esquerda
        g.setColor(Color.BLACK);
        desenharSeta(g, 270, 150, 300, 180);  // Seta superior direita
        desenharSeta(g, 450, 270, 420, 300);  // Seta direita inferior
        desenharSeta(g, 330, 450, 300, 420);  // Seta inferior esquerda
        desenharSeta(g, 150, 330, 180, 300);  // Seta esquerda superior
    }

    private void desenharSeta(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(x1, y1, x2, y2);

        // Calcula o ângulo da seta
        double angle = Math.atan2(y2 - y1, x2 - x1);

        // Desenha a seta
        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(x2, y2);
        arrowHead.addPoint((int) (x2 - 10 * Math.cos(angle - Math.PI / 6)), (int) (y2 - 10 * Math.sin(angle - Math.PI / 6)));
        arrowHead.addPoint((int) (x2 - 10 * Math.cos(angle + Math.PI / 6)), (int) (y2 - 10 * Math.sin(angle + Math.PI / 6)));
        g2d.fill(arrowHead);

        g2d.dispose();
    }

    private void desenharSemaforos(Graphics g) {
        ArrayList<Semaforo> semaforos = cruzamento.getSemaforos();
        int[][] positions = {
            {350, 30},   // Topo da rua horizontal superior
            {350, 470},  // Topo da rua horizontal inferior
            {30, 350},   // Lateral da rua vertical esquerda
            {470, 350}   // Lateral da rua vertical direita
        };

        for (int i = 0; i < semaforos.size(); i++) {
            Semaforo semaforo = semaforos.get(i);
            int x = positions[i][0];
            int y = positions[i][1];

            g.setColor(Color.BLACK);
            g.fillRect(x, y, 20, 60);

            // Luz Vermelha
            g.setColor(semaforo.getEstadoAtual() == Semaforo.Estado.VERMELHO ? Color.RED : Color.GRAY);
            g.fillOval(x + 2, y + 2, 16, 16);

            // Luz Amarela
            g.setColor(semaforo.getEstadoAtual() == Semaforo.Estado.AMARELO ? Color.YELLOW : Color.GRAY);
            g.fillOval(x + 2, y + 22, 16, 16);

            // Luz Verde
            g.setColor(semaforo.getEstadoAtual() == Semaforo.Estado.VERDE ? Color.GREEN : Color.GRAY);
            g.fillOval(x + 2, y + 42, 16, 16);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SemaforoGUI frame = new SemaforoGUI();
                frame.setVisible(true);
            }
        });
    }
}
