package src.main.resources;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Imposta un look and feel moderno
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Creazione del frame principale con titolo personalizzato e icona
        JFrame frame = new JFrame("Web Scraping Tool Coded by Override aka _s0sa_.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        // Creazione della barra dei menu
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("Nuovo Scraping"));
        fileMenu.addSeparator();
        JMenuItem saveItem = new JMenuItem("Salva come PDF");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Esci");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Strumenti");
        toolsMenu.add(new JMenuItem("Impostazioni"));
        toolsMenu.add(new JMenuItem("Cronologia"));

        JMenu helpMenu = new JMenu("Aiuto");
        helpMenu.add(new JMenuItem("Guida"));
        helpMenu.add(new JMenuItem("Informazioni"));

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        // Pannello principale con aspetto moderno
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 245));
        frame.setContentPane(mainPanel);

        // Pannello di input con aspetto migliorato
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(120, 120, 180), 1, true),
                        "Inserisci URL del sito da analizzare",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 14),
                        new Color(60, 60, 100)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        inputPanel.setBackground(new Color(245, 245, 250));

        // Riga per l'URL con input migliorato
        JPanel urlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        urlPanel.setBackground(new Color(245, 245, 250));
        JLabel urlLabel = new JLabel("URL:");
        urlLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField urlField = new JTextField(40);
        urlField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        urlField.setToolTipText("Inserisci l'URL completo del sito da analizzare");

        JButton scrapeButton = new JButton("Analizza");
        scrapeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        scrapeButton.setBackground(new Color(100, 149, 237));
        scrapeButton.setForeground(Color.WHITE);
        scrapeButton.setFocusPainted(false);

        urlPanel.add(urlLabel);
        urlPanel.add(urlField);
        urlPanel.add(scrapeButton);

        // Pannello per le opzioni di analisi
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(new Color(245, 245, 250));

        JCheckBox imagesCheckbox = new JCheckBox("Scarica immagini", true);
        imagesCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        imagesCheckbox.setBackground(new Color(245, 245, 250));

        JCheckBox cleanTextCheckbox = new JCheckBox("Pulisci testo", true);
        cleanTextCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cleanTextCheckbox.setBackground(new Color(245, 245, 250));

        optionsPanel.add(imagesCheckbox);
        optionsPanel.add(cleanTextCheckbox);

        inputPanel.add(urlPanel);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(optionsPanel);

        // Area risultati con stile migliorato
        JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
        resultPanel.setBorder(new CompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(120, 120, 180), 1, true),
                        "Risultati dell'analisi",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("SansSerif", Font.BOLD, 14),
                        new Color(60, 60, 100)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        resultPanel.setBackground(new Color(245, 245, 250));

        // Creiamo un'area di log con stile console
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(220, 220, 220));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // Auto-scroll al fondo quando arrivano nuovi messaggi
        DefaultCaret caret = (DefaultCaret) logArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Area risultati con stile migliorato
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(250, 250, 255));
        resultArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1));
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Pannello diviso per mostrare sia i log che i risultati
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logScrollPane, resultScrollPane);
        splitPane.setResizeWeight(0.5); // Distribuisci equamente lo spazio
        splitPane.setDividerLocation(300);
        resultPanel.add(splitPane, BorderLayout.CENTER);

        // Pannello per i pulsanti di azione
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(new Color(245, 245, 250));

        JButton clearButton = new JButton("Pulisci");
        clearButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton saveButton = new JButton("Salva PDF");
        saveButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        saveButton.setEnabled(false);

        actionPanel.add(clearButton);
        actionPanel.add(saveButton);
        resultPanel.add(actionPanel, BorderLayout.SOUTH);

        // Barra di stato in basso
        JPanel statusPanel = new JPanel(new BorderLayout(5, 0));
        statusPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        statusPanel.setBackground(new Color(240, 240, 245));

        JLabel statusLabel = new JLabel("  Stato: Pronto");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(200, 20));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.EAST);

        // Aggiunta dei pannelli al pannello principale
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(resultPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        final AtomicReference<List<Article>> processedArticles = new AtomicReference<>();

        // Gestione degli eventi per il pulsante Clear
        clearButton.addActionListener(e -> {
            logArea.setText("");
            resultArea.setText("");
            progressBar.setValue(0);
            saveButton.setEnabled(false);
            statusLabel.setText("  Stato: Pronto");
        });

        // Gestione degli eventi per il pulsante Scrape
        scrapeButton.addActionListener((ActionEvent actionEvent) -> {
            String url = urlField.getText().trim();
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Per favore inserisci un URL valido",
                        "URL mancante",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            logArea.setText(""); // Pulisci l'area di log
            resultArea.setText(""); // Pulisci l'area dei risultati
            statusLabel.setText("  Stato: Analisi in corso");
            progressBar.setIndeterminate(true);
            scrapeButton.setEnabled(false);
            clearButton.setEnabled(false);

            // Funzione per aggiungere messaggi all'area di log
            Consumer<String> logMessage = message -> {
                SwingUtilities.invokeLater(() -> {
                    logArea.append("[INFO] " + message + "\n");
                });
            };

            new Thread(() -> {
                try {
                    // Inizia il processo di scraping con callback per il log
                    logMessage.accept("Avvio del processo di scraping...");
                    WebScraper scraper = new WebScraper(logMessage);
                    List<Article> articles = scraper.Scraper(url);

                    if (articles.isEmpty()) {
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setIndeterminate(false);
                            logMessage.accept("Nessun articolo trovato. Verifica l'URL o prova con un altro sito.");
                            statusLabel.setText("  Stato: Nessun risultato");
                            scrapeButton.setEnabled(true);
                            clearButton.setEnabled(true);
                        });
                        return;
                    }

                    // Processo di elaborazione dati con callback per il log
                    logMessage.accept("Avvio del processo di elaborazione dati...");
                    DataProcessor processor = new DataProcessor(logMessage);
                    List<Article> processed = processor.processData(articles);
                    processedArticles.set(processed);

                    SwingUtilities.invokeLater(() -> {
                        progressBar.setIndeterminate(false);
                        progressBar.setValue(100);

                        resultArea.setText("✅ Analisi completata con successo!\n\n");
                        resultArea.append("📊 Trovati " + processed.size() + " articoli.\n\n");

                        for (Article article : processed) {
                            resultArea.append("📄 " + article.getTitle() + "\n");
                            if (article.getSubtitle() != null && !article.getSubtitle().isEmpty()) {
                                resultArea.append("   " + article.getSubtitle() + "\n");
                            }
                            if (article.getAuthor() != null && !article.getAuthor().isEmpty()) {
                                resultArea.append("   Autore: " + article.getAuthor() + "\n");
                            }
                            resultArea.append("\n");
                        }

                        saveButton.setEnabled(true);
                        scrapeButton.setEnabled(true);
                        clearButton.setEnabled(true);
                        statusLabel.setText("  Stato: Analisi completata");
                        logMessage.accept("Processo completato con successo!");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setIndeterminate(false);
                        logMessage.accept("ERRORE: " + ex.getMessage());
                        resultArea.append("❌ Errore durante l'analisi: " + ex.getMessage() + "\n");
                        scrapeButton.setEnabled(true);
                        clearButton.setEnabled(true);
                        statusLabel.setText("  Stato: Errore nell'analisi");
                    });
                }
            }).start();
        });

        // Gestione degli eventi per il pulsante Save
        saveButton.addActionListener((ActionEvent e) -> {
            List<Article> articles = processedArticles.get();
            if (articles != null && !articles.isEmpty()) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Salva Report PDF");
                fileChooser.setFileFilter(new FileNameExtensionFilter("File PDF", "pdf"));
                fileChooser.setSelectedFile(new File("report_articoli.pdf"));

                int userChoice = fileChooser.showSaveDialog(frame);
                if (userChoice == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".pdf")) {
                        filePath += ".pdf";
                    }

                    final String outputPath = filePath;
                    statusLabel.setText("  Stato: Generazione PDF in corso");
                    progressBar.setIndeterminate(true);
                    saveButton.setEnabled(false);

                    // Funzione per aggiungere messaggi all'area di log
                    Consumer<String> logMessage = message -> {
                        SwingUtilities.invokeLater(() -> {
                            logArea.append("[PDF] " + message + "\n");
                        });
                    };

                    logMessage.accept("Avvio generazione PDF: " + outputPath);

                    new Thread(() -> {
                        try {
                            PDFGenerator pdfGenerator = new PDFGenerator(logMessage);
                            pdfGenerator.generatePdf(articles, outputPath);

                            SwingUtilities.invokeLater(() -> {
                                progressBar.setIndeterminate(false);
                                progressBar.setValue(100);
                                resultArea.append("\n✅ PDF generato con successo: " + outputPath + "\n");
                                statusLabel.setText("  Stato: PDF generato");
                                saveButton.setEnabled(true);
                                logMessage.accept("PDF generato con successo!");

                                int openFile = JOptionPane.showConfirmDialog(frame,
                                        "PDF generato con successo. Vuoi aprire il file?",
                                        "PDF Generato",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE);

                                if (openFile == JOptionPane.YES_OPTION) {
                                    try {
                                        Desktop.getDesktop().open(new File(outputPath));
                                    } catch (IOException ex) {
                                        logMessage.accept("Errore apertura file: " + ex.getMessage());
                                        JOptionPane.showMessageDialog(frame,
                                                "Impossibile aprire il file: " + ex.getMessage(),
                                                "Errore",
                                                JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> {
                                progressBar.setIndeterminate(false);
                                logMessage.accept("ERRORE: " + ex.getMessage());
                                resultArea.append("❌ Errore durante la generazione del PDF: " + ex.getMessage() + "\n");
                                statusLabel.setText("  Stato: Errore generazione PDF");
                                saveButton.setEnabled(true);
                            });
                        }
                    }).start();
                }
            }
        });

        // Sincronizzazione del menu "Salva come PDF" con il pulsante saveButton
        saveItem.addActionListener(e -> saveButton.doClick());

        // Imposta dimensioni minime e mostra il frame
        frame.setMinimumSize(new Dimension(700, 500));
        frame.setVisible(true);
    }
}