package gui.MainFrame;
import log.Logger;
import gui.LogWindow;
import gui.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Locale;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final boolean[] confirmClosing = {false};

    public MainApplicationFrame() {
        // Устанавливаем размеры главного окна
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        // Устанавливаем панель содержимого главного окна
        setContentPane(desktopPane);

        // Добавляем окно журнала логов
        addWindow(createLogWindow());

        // Создаем и добавляем игровое окно
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        // Устанавливаем меню главного окна
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Вы уверены что хотите закрыть?
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmClosing[0]) {
                    return;
                }
                setDialog();
            }
        });
    }

    // Метод для добавления окна на панель
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    // Создание окна журнала логов
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(1000, 0);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(getLocalizedString("main.application.frame.working.protocol", getLocale()));
        return logWindow;
    }

    // Генерация меню главного окна
    private JMenuBar generateMenuBar() {
        // Создание меню-холдера
        JMenuBar menuBar = new JMenuBar();

        // Добавление схем
        JMenu lookAndFeelMenu = LookAndFeelMenu.createLookAndFeelMenu(getLocalizedString("main.application.frame.display.mode", getLocale()), this);
        menuBar.add(lookAndFeelMenu);
        // Добавление тестового меню
        JMenu testMenu = TestMenu.createTestMenu(getLocalizedString("main.application.frame.tests", getLocale()), this);
        menuBar.add(testMenu);
        // Добавление выбора языка
        JMenu languageMenu = createLanguageMenu();
        menuBar.add(languageMenu);
        // Добавление кнопки выхода
        JMenu fileMenu = new JMenu(getLocalizedString("main.application.frame.sure_exit", getLocale()));
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitItem = new JMenuItem(getLocalizedString("main.application.frame.exit", getLocale()));
        exitItem.addActionListener(e -> setDialog());
        exitItem.setMnemonic(KeyEvent.VK_X);
        // Добавление кнопок выхода
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    // Установка диалогового окна при закрытии
    public void setDialog(){
        Object[] options = {getLocalizedString("confirm.closing.YES", getLocale()), getLocalizedString("confirm.closing.NO", getLocale())};
        int result = JOptionPane.showOptionDialog(
                MainApplicationFrame.this,
                getLocalizedString("confirm.closing.question", getLocale()),
                getLocalizedString("confirm.closing.title", getLocale()),
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

        // Закрытие главного окна и завершение программы
        dispose();
        System.exit(0);
    }

    // Создание меню выбора языка
    private JMenu createLanguageMenu() {
        JMenu languageMenu = new JMenu(getLocalizedString("main.application.frame.language", getLocale()));
        languageMenu.setMnemonic(KeyEvent.VK_L);

        JMenuItem changeLanguageItemEn = new LanguageChangeButton(getLocalizedString("language.english", getLocale()), this);
        languageMenu.add(changeLanguageItemEn);

        JMenuItem changeLanguageItemRu = new LanguageChangeButton(getLocalizedString("language.russian", getLocale()), this);
        languageMenu.add(changeLanguageItemRu);

        return languageMenu;
    }

    // Метод изменения языка приложения
    public void changeLocale(Locale newLocale) {
        if (!(newLocale.equals(getLocale()))){ // Если мы поменяли язык по умолчанию на другой, то меняем всё на новый язык
            Locale.setDefault(newLocale); // Теперь новый язык - язык по умолчанию

            setJMenuBar(generateMenuBar());
            setContentPane(desktopPane);
            Logger.debug(getLocalizedString("main.application.frame.working.protocol", getLocale()));

            JInternalFrame[] frames = desktopPane.getAllFrames();
            for (JInternalFrame frame : frames) {
                if (frame instanceof GameWindow) {
                    ((GameWindow) frame).closeWindowWithoutConfirmation(); // Тут закрываем gameWindow без подтверждения закрытия
                    GameWindow gameWindow = new GameWindow();
                    gameWindow.setSize(400, 400);
                    addWindow(gameWindow);
                }
            }
        }
        else{
            Locale.setDefault(newLocale); // Если не меняли изначальный язык, то ничего не обновляем
        }
    }

    // Установка внешнего вида окон
    protected void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            //
        }
    }

    // Получение переведенной строки
    protected static String getLocalizedString(String key, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("gui.resources.messages", locale);
            if (bundle != null && bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        } catch (MissingResourceException e) {
            System.err.println("Нет нужной фразы: " + key);
            return "Нет нужной фразы";
        }
        return key;
    }

    // Получение текущего языка
    public Locale getLocale() {
        return Locale.getDefault();
    }
}