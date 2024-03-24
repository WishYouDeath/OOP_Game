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
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmClosing[0]) {
                    return;
                }
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

                dispose();
                System.exit(0); // Если закрываем основное окно то программа прекращается
            }
        });
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(1000, 0);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(getLocalizedString("main.application.frame.working.protocol", getLocale()));
        return logWindow;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu lookAndFeelMenu = LookAndFeelMenu.createLookAndFeelMenu(getLocalizedString("main.application.frame.display.mode", getLocale()), this);
        menuBar.add(lookAndFeelMenu);

        JMenu testMenu = TestMenu.createTestMenu(getLocalizedString("main.application.frame.tests", getLocale()), this);
        menuBar.add(testMenu);

        JMenu languageMenu = createLanguageMenu();
        menuBar.add(languageMenu);

        return menuBar;
    }

    private JMenu createLanguageMenu() {
        JMenu languageMenu = new JMenu(getLocalizedString("main.application.frame.language", getLocale()));
        languageMenu.setMnemonic(KeyEvent.VK_L);

        JMenuItem changeLanguageItemEn = new LanguageChangeButton(getLocalizedString("language.english", getLocale()), this);
        languageMenu.add(changeLanguageItemEn);

        JMenuItem changeLanguageItemRu = new LanguageChangeButton(getLocalizedString("language.russian", getLocale()), this);
        languageMenu.add(changeLanguageItemRu);

        return languageMenu;
    }


    public void changeLocale(Locale newLocale) {
        Locale.setDefault(newLocale);

        setJMenuBar(generateMenuBar());
        setContentPane(desktopPane);

        Logger.debug(getLocalizedString("main.application.frame.working.protocol", getLocale()));

        getContentPane().validate();
        getContentPane().repaint();
    }

    protected void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
         //
        }
    }

    private static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("gui.resources.messages", locale);
    }

    protected String getLocalizedString(String key, Locale locale) {
        try {
            ResourceBundle bundle = getBundle(locale);
            if (bundle != null && bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        } catch (MissingResourceException e) {
            System.err.println("Нет нужной фразы: " + key);
            return "Нет нужной фразы";
        }
        return key;
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }
}