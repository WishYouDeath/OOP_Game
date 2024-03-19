package gui;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;

    private final boolean[] confirmClosing = {false};
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
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
                        GameWindow.this,
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
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
