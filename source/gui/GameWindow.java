package gui;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

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
        setTitle(getLocalizedString("game.window.title"));
        addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {

            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (confirmClosing[0]) {
                    return;
                }
                Object[] options = {getLocalizedString("confirm.closing.YES"), getLocalizedString("confirm.closing.NO")};
                int result = JOptionPane.showOptionDialog(
                        GameWindow.this, // передаем текущий фрейм в качестве родительского компонента
                        getLocalizedString("confirm.closing.question"),
                        getLocalizedString("confirm.closing.title"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0] // return value for YES
                );

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

    private static ResourceBundle getBundle(String baseName, Locale locale) {
        return ResourceBundle.getBundle(baseName, locale);
    }

    private String getLocalizedString(String key) {
        ResourceBundle bundle = getBundle("gui.resources.messages", getLocale());
        return bundle.getString(key);
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }
}
