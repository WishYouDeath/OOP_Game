package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;

public class LogWindow extends JInternalFrame implements LogChangeListener {
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    private final boolean[] confirmClosing = {false};

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {

            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (confirmClosing[0]) {
                    return;
                }
                int result = JOptionPane.showConfirmDialog(
                        LogWindow.this,
                        "Вы уверены, что хотите закрыть приложение?",
                        "Подтвердите выход",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.NO_OPTION) {
                    confirmClosing[0] = false;
                    return;
                }

                dispose();
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {}

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {}

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {}

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {}

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}