package org.training;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Moore Makinesini simüle eden Java Swing programı
 *
 * Kullanıcıdan aşağıdaki bilgileri alır:
 *  - Durum kümesi (Q: {q0,q1,q2,...})
 *  - Girdi alfabesi (S: {a,b})
 *  - Çıktı alfabesi (Γ: {0,1})
 *  - Geçiş tablosu (Her satır TAB ile ayrılmış)
 *  - Çıktı tablosu (Her satır TAB ile ayrılmış)
 *  - Giriş stringi
 *
 * Alınan bilgilerle, her geçiş hesaplanır ve durum geçişleri ile üretilen çıktı
 * hem metinsel hem de görsel olarak kullanıcıya sunulur.
 */
public class MooreSimulatorGUI extends JFrame {

    // Arayüz bileşenleri
    private final JTextField txtStates;
    private final JTextField txtInputAlphabet;
    private final JTextField txtOutputAlphabet;
    private final JTextArea areaTransitionTable;
    private final JTextArea areaOutputTable;
    private final JTextField txtInputString;
    private final JTextArea areaResult;
    private final SimulationPanel simulationPanel;

    public MooreSimulatorGUI() {
        setTitle("Moore Makinesi Simülatörü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Üst panel: Kullanıcı giriş alanları
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Satır 1: Durum Kümesi
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Durumlar (virgülle ayrılmış, ilk durum başlangıç):"), gbc);
        txtStates = new JTextField("q0,q1,q2,q3");
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(txtStates, gbc);

        // Satır 2: Girdi Alfabesi
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Girdi Alfabesi (virgülle ayrılmış):"), gbc);
        txtInputAlphabet = new JTextField("a,b");
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(txtInputAlphabet, gbc);

        // Satır 3: Çıktı Alfabesi
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        inputPanel.add(new JLabel("Çıktı Alfabesi (virgülle ayrılmış):"), gbc);
        txtOutputAlphabet = new JTextField("0,1");
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        inputPanel.add(txtOutputAlphabet, gbc);

        // Satır 4: Geçiş Tablosu
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        areaTransitionTable = new JTextArea(5, 40);
        areaTransitionTable.setBorder(BorderFactory.createTitledBorder("Geçiş Tablosu (Her satır: <durum> TAB <sembol1 için sonraki durum> TAB <sembol2 için sonraki durum> ... )"));
        // Örnek veri
        areaTransitionTable.setText("q0\tq1\tq0\nq1\tq2\tq0\nq2\tq2\tq3\nq3\tq1\tq0");
        inputPanel.add(new JScrollPane(areaTransitionTable), gbc);

        // Satır 5: Çıktı Tablosu
        gbc.gridx = 0;
        gbc.gridy = 4;
        areaOutputTable = new JTextArea(3, 40);
        areaOutputTable.setBorder(BorderFactory.createTitledBorder("Çıktı Tablosu (Her satır: <durum> TAB <çıktı>)"));
        // Örnek veri
        areaOutputTable.setText("q0\t1\nq1\t0\nq2\t0\nq3\t1");
        inputPanel.add(new JScrollPane(areaOutputTable), gbc);

        // Satır 6: Girdi Stringi
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Girdi Stringi:"), gbc);
        txtInputString = new JTextField("abab");
        gbc.gridx = 1;
        inputPanel.add(txtInputString, gbc);

        // Satır 7: Simülasyonu Başlatan Buton
        JButton btnSimulate = new JButton("Simüle Et");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(btnSimulate, gbc);

        add(inputPanel, BorderLayout.NORTH);

        // Alt panel: Sonuç metni ve görsel gösterim alanı
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("Simülasyon Sonucu"));

        // Metinsel Sonuç Alanı
        areaResult = new JTextArea(5, 40);
        areaResult.setEditable(false);
        outputPanel.add(new JScrollPane(areaResult), BorderLayout.NORTH);

        // Grafiksel Gösterim için Özel Panel
        simulationPanel = new SimulationPanel();
        simulationPanel.setPreferredSize(new Dimension(600, 300));
        simulationPanel.setBackground(Color.WHITE);
        outputPanel.add(simulationPanel, BorderLayout.CENTER);

        add(outputPanel, BorderLayout.CENTER);

        // Buton tıklama olayı: Lambda kullanarak simülasyonu çalıştır
        btnSimulate.addActionListener(e -> runSimulation());

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Verilen kullanıcı girdilerine dayanarak simülasyonu çalıştırır.
     */
    private void runSimulation() {
        try {
            // 1. Kullanıcıdan Durum, Alfabe ve Girdi alınması
            String[] states = txtStates.getText().trim().split("\\s*,\\s*");
            if (states.length == 0) {
                throw new Exception("Durum bilgisi boş!");
            }
            String[] inputAlphabet = txtInputAlphabet.getText().trim().split("\\s*,\\s*");
            String[] outputAlphabet = txtOutputAlphabet.getText().trim().split("\\s*,\\s*");

            // 2. Geçiş Tablosunun Okunması ve Haritaya Aktarılması (Map<Durum, Map<Girdi, SonrakiDurum>>)
            Map<String, Map<String, String>> transitionTable = new HashMap<>();
            String[] transitionLines = areaTransitionTable.getText().trim().split("\\n");
            for (String line : transitionLines) {
                String[] cols = line.split("\\t");
                if (cols.length < inputAlphabet.length + 1) {
                    throw new Exception("Geçiş tablosunda yeterli sütun bulunamadı: " + line);
                }
                String currentState = cols[0].trim();
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < inputAlphabet.length; i++) {
                    String symbol = inputAlphabet[i].trim();
                    String nextState = cols[i + 1].trim();
                    row.put(symbol, nextState);
                }
                transitionTable.put(currentState, row);
            }

            // 3. Çıktı Tablosunun Okunması (Map<Durum, Çıktı>)
            Map<String, String> outputTable = new HashMap<>();
            String[] outputLines = areaOutputTable.getText().trim().split("\\n");
            for (String line : outputLines) {
                String[] cols = line.split("\\t");
                if (cols.length < 2) {
                    throw new Exception("Çıktı tablosu satırı hatalı: " + line);
                }
                String state = cols[0].trim();
                String outSymbol = cols[1].trim();
                outputTable.put(state, outSymbol);
            }

            // 4. Girdi Stringinin Alınması
            String inputString = txtInputString.getText().trim();
            if (inputString.isEmpty()) {
                throw new Exception("Girdi stringi boş!");
            }

            // 5. Simülasyonun Gerçekleştirilmesi
            List<String> simulationPath = new ArrayList<>();
            List<String> producedOutput = new ArrayList<>();
            List<String> inputForTransition = new ArrayList<>();

            // Başlangıç durumunun eklenmesi
            String currentState = states[0];
            simulationPath.add(currentState);
            producedOutput.add(outputTable.getOrDefault(currentState, "?"));

            // Girdi karakterlerine göre geçişlerin yapılması
            for (int i = 0; i < inputString.length(); i++) {
                String symbol = String.valueOf(inputString.charAt(i));
                if (!transitionTable.containsKey(currentState)) {
                    throw new Exception("Geçiş tablosunda '" + currentState + "' durumu bulunamadı!");
                }
                Map<String, String> transitions = transitionTable.get(currentState);
                if (!transitions.containsKey(symbol)) {
                    throw new Exception("(" + currentState + ", " + symbol + ") için geçiş tanımlı değil!");
                }
                String nextState = transitions.get(symbol);
                simulationPath.add(nextState);
                producedOutput.add(outputTable.getOrDefault(nextState, "?"));
                inputForTransition.add(symbol);
                currentState = nextState;
            }

            // 6. Metinsel Sonuçların Gösterilmesi
            StringBuilder sb = new StringBuilder();
            sb.append("Durum Geçişleri: ");
            for (int i = 0; i < simulationPath.size(); i++) {
                sb.append(simulationPath.get(i));
                if (i < simulationPath.size() - 1) {
                    sb.append(" -> ");
                }
            }
            sb.append("\nÜretilen Çıktı: ");
            for (String s : producedOutput) {
                sb.append(s);
            }
            areaResult.setText(sb.toString());

            // 7. Görsel Simülasyonun Güncellenmesi
            simulationPanel.setSimulationData(simulationPath, inputForTransition, producedOutput);
            simulationPanel.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * SimulationPanel, simülasyon sırasında oluşan durum geçişlerini grafiksel olarak gösterir.
     * Her durumu daire ile, durum geçişlerini ise oklarla çizerek görselleştirir.
     */
    static class SimulationPanel extends JPanel {
        private List<String> simPath = new ArrayList<>();
        private List<String> inputs = new ArrayList<>();
        private List<String> outputs = new ArrayList<>();

        /**
         * Simülasyon verilerini günceller.
         *
         * @param path   Durum geçiş yolunu içeren liste
         * @param inputs Geçişlerde kullanılan girdi sembollerini içeren liste
         * @param outputs Üretilen çıktı sembollerini içeren liste
         */
        public void setSimulationData(List<String> path, List<String> inputs, List<String> outputs) {
            this.simPath = path;
            this.inputs = inputs;
            this.outputs = outputs;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (simPath == null || simPath.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int n = simPath.size();
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int margin = 50;
            int r = 20; // Daire yarıçapı
            int gap = (n > 1) ? (panelWidth - 2 * margin) / (n - 1) : 0;

            // Her durum için merkez noktalarının hesaplanması
            List<Point> centers = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int x = margin + i * gap;
                int y = panelHeight / 2;
                centers.add(new Point(x, y));
            }

            // Durum dairelerinin çizilmesi
            for (int i = 0; i < n; i++) {
                Point p = centers.get(i);
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval(p.x - r, p.y - r, 2 * r, 2 * r);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - r, p.y - r, 2 * r, 2 * r);
                String stateName = simPath.get(i);
                FontMetrics fm = g2.getFontMetrics();
                int strWidth = fm.stringWidth(stateName);
                int strHeight = fm.getAscent();
                g2.drawString(stateName, p.x - strWidth / 2, p.y + strHeight / 2);
            }

            // Durumlar arası geçiş oklarının çizilmesi ve ok üzerine metin eklenmesi
            for (int i = 0; i < n - 1; i++) {
                Point p1 = centers.get(i);
                Point p2 = centers.get(i + 1);
                double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
                int x1 = (int) (p1.x + r * Math.cos(angle));
                int y1 = (int) (p1.y + r * Math.sin(angle));
                int x2 = (int) (p2.x - r * Math.cos(angle));
                int y2 = (int) (p2.y - r * Math.sin(angle));
                g2.drawLine(x1, y1, x2, y2);

                // Ok ucu çizimi
                int arrowSize = 6;
                Polygon arrowHead = new Polygon();
                arrowHead.addPoint(0, 0);
                arrowHead.addPoint(-arrowSize, -arrowSize);
                arrowHead.addPoint(arrowSize, -arrowSize);
                g2.translate(x2, y2);
                g2.rotate(angle);
                g2.fill(arrowHead);
                g2.rotate(-angle);
                g2.translate(-x2, -y2);

                // Okun ortasına metin eklenmesi: girdi / sonraki durumun çıktısı
                String inputSymbol = (i < inputs.size()) ? inputs.get(i) : "";
                String nextOutput = (i + 1 < outputs.size()) ? outputs.get(i + 1) : "";
                String label = inputSymbol;
                if (!nextOutput.isEmpty()) {
                    label += " / " + nextOutput;
                }
                int labelX = (x1 + x2) / 2;
                int labelY = (y1 + y2) / 2 - 10;
                g2.drawString(label, labelX, labelY);
            }
        }
    }

    /**
     * Main metod: Uygulamayı başlatır.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MooreSimulatorGUI().setVisible(true));
    }
}
