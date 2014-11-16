package com.github.pentadrago.fileverifier.swing;

import com.github.pentadrago.fileverifier.FileVerifier;
import com.github.pentadrago.fileverifier.VerifierResult;
import com.github.pentadrago.fileverifier.algorithm.CRCVerificationAlgorithm;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * simple GUI for file-verifier
 *
 * @author Stefan Kloe
 */
public final class SwingFileVerifier extends JFrame {

    private static final int FRAME_WIDTH = 1400;
    private static final int FRAME_HEIGHT = 800;

    private final FileVerifier fileVerifier;

    private final MessageTableModel statusMessageModel;
    private final MessageTableModel errorMessageModel;
    private final MessageTableModel resultMessageModel;
    private final JButton startVerificationButton;

    public SwingFileVerifier() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLayout(new GridLayout(7, 1));

        statusMessageModel = createMessageTableComponent("Status", MessageTableModel.Mode.PREPEND);
        errorMessageModel = createMessageTableComponent("Error", MessageTableModel.Mode.APPEND);
        resultMessageModel = createMessageTableComponent("Result", MessageTableModel.Mode.APPEND);

        JPanel statusPanel = new JPanel();
        add(statusPanel);

        startVerificationButton = new JButton("start verification");
        startVerificationButton.setName("startVerification");
        startVerificationButton.addActionListener((ActionEvent arg0) -> {
            startVerification();
        });
        statusPanel.add(startVerificationButton);

        fileVerifier = initializeVerifier();

        setVisible(true);
    }

    private MessageTableModel createMessageTableComponent(String label, MessageTableModel.Mode mode) {
        add(new JLabel(label));
        MessageTableModel model = new MessageTableModel(mode);
        JTable table = new JTable(model);
        ScrollPane pane = new ScrollPane();
        pane.setSize(FRAME_WIDTH - 10, FRAME_HEIGHT / 4 - 50);
        pane.add(table);
        add(pane);
        return model;
    }

    private FileVerifier initializeVerifier() {
        FileVerifier verifier = new FileVerifier();
        verifier.setStatusListener((String message) -> {
            statusMessageModel.addMessage(message);
        });
        verifier.setErrorListener((String message) -> {
            errorMessageModel.addMessage(message);
        });
        return verifier;
    }

    private void reset() {
        statusMessageModel.clear();
        errorMessageModel.clear();
        resultMessageModel.clear();
    }

    private void startVerification() {
        reset();
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            new Thread() {
                @Override
                public void run() {
                    startVerificationButton.setEnabled(false);
                    super.run();
                    try {
                        Map<File, VerifierResult> results = fileVerifier.verify(chooser.getSelectedFile(), new CRCVerificationAlgorithm());
                        results.entrySet().stream().map((entry) -> {
                            VerifierResult result = entry.getValue();
                            resultMessageModel.addMessage(String.format("%s: %s", entry.getKey().getPath(), result.toString()));
                            return result;
                        }).forEach((VerifierResult result) -> {
                            result.getInvalidFiles().stream().forEach((invalidFile) -> {
                                resultMessageModel.addMessage("\t\t\t\t" + invalidFile);
                            });
                        });
                    } catch (FileNotFoundException e) {
                        errorMessageModel.addMessage("verification failed: " + e);
                    }
                    startVerificationButton.setEnabled(true);
                }
            }.start();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new SwingFileVerifier();
    }

}
