package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogWindow extends JInternalFrame implements LogChangeListener {
    private final LogWindowSource m_logSource; // Источник логов
    private final TextArea m_logContent; // Компонент для отображения логов

    private final boolean[] confirmClosing = {false}; // Флаг подтверждения закрытия окна

    // Конструктор класса LogWindow
    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this); // Регистрация слушателя
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        setTitle(getLocalizedString("log.window.title")); // Устанавливаем имя окну
        addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {// Добавление слушателя для обработки события закрытия окна
                if (confirmClosing[0]) {
                    return;
                }
                // Показ диалогового окна для подтверждения закрытия
                Object[] options = {getLocalizedString("confirm.closing.YES"), getLocalizedString("confirm.closing.NO")};
                int result = JOptionPane.showOptionDialog(
                        LogWindow.this,
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
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    // Метод для обновления содержимого логов
    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    // Получение строки для заданного языка
    private static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("gui.resources.messages", locale);
    }

    // Метод для получения локализованной строки по ключу
    private String getLocalizedString(String key) {
        ResourceBundle bundle = getBundle(getLocale());
        return bundle.getString(key);
    }

    // Метод для получения текущего языка
    public Locale getLocale() {
        return Locale.getDefault();
    }

    // Обработчик изменения логов
    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}