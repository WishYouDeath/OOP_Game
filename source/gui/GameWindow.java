package gui;
import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class GameWindow extends JInternalFrame {

    private final boolean[] confirmClosing = {false};// Флаг подтверждения закрытия окна

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        setTitle(getLocalizedString("game.window.title")); // Надпись "Игровое поле"

        addInternalFrameListener(new InternalFrameListener() { // Добавление слушателя для обработки события закрытия окна
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (confirmClosing[0]) {
                    return;
                }
                // Показ диалогового окна для подтверждения закрытия
                Object[] options = {getLocalizedString("confirm.closing.YES"), getLocalizedString("confirm.closing.NO")};
                int result = JOptionPane.showOptionDialog(
                        GameWindow.this,
                        getLocalizedString("confirm.closing.question"),
                        getLocalizedString("confirm.closing.title"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
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

        // Добавление панели с игровым визуализатором на поле
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    // Метод для закрытия окна без подтверждения
    public void closeWindowWithoutConfirmation() {
        dispose();
    }

    // Получение строки для заданного языка
    private static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("gui.resources.messages", locale);
    }

    // Получение локализованной строки по ключу
    private String getLocalizedString(String key) {
        ResourceBundle bundle = getBundle(getLocale());
        return bundle.getString(key);
    }

    // Получение текущего языка
    public Locale getLocale() {
        return Locale.getDefault();
    }
}