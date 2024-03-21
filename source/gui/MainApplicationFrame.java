package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.Logger;

public class MainApplicationFrame extends JFrame
{
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
                Object[] options = {getLocalizedString("confirm.closing.YES"), getLocalizedString("confirm.closing.NO")};
                int result = JOptionPane.showOptionDialog(
                        MainApplicationFrame.this, // передаем текущий фрейм в качестве родительского компонента
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
        });
    }

    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    protected LogWindow createLogWindow()//Работа с журналом
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(1000,0);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(getLocalizedString("main.application.frame.working.protocol"));
        return logWindow;
    }



//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

    private JMenuBar generateMenuBar()//Выделил отдельные методы из большого метода generateMenuBar
    {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createLookAndFeelMenu();
        menuBar.add(lookAndFeelMenu);

        JMenu testMenu = createTestMenu();
        menuBar.add(testMenu);

        return menuBar;
    }


    private JMenu createLookAndFeelMenu()
    {
        JMenu menu = new JMenu(getLocalizedString("main.application.frame.display.mode"));
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                getLocalizedString("main.application.frame.managing.display.mode"));

        JMenuItem systemLookAndFeel = createSystemLookAndFeelMenuItem();
        menu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = createCrossplatformLookAndFeelMenuItem();
        menu.add(crossplatformLookAndFeel);

        return menu;
    }

    private JMenuItem createSystemLookAndFeelMenuItem()
    {
        JMenuItem menuItem = new JMenuItem(getLocalizedString("main.application.frame.system.diagram"), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return menuItem;
    }

    private JMenuItem createCrossplatformLookAndFeelMenuItem()
    {
        JMenuItem menuItem = new JMenuItem(getLocalizedString("main.application.frame.universal.scheme"), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return menuItem;
    }

    private JMenu createTestMenu()
    {
        JMenu menu = new JMenu(getLocalizedString("main.application.frame.tests"));
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(
                getLocalizedString("main.application.frame.test.commands"));

        JMenuItem addLogMessageItem = createAddLogMessageItem();
        menu.add(addLogMessageItem);

        return menu;
    }

    private JMenuItem createAddLogMessageItem()
    {
        JMenuItem menuItem = new JMenuItem(getLocalizedString("main.application.frame.message.in.log"), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            Logger.debug(getLocalizedString("main.application.frame.new.line"));
        });
        return menuItem;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
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
