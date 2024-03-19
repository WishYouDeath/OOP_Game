package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
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
                int result = JOptionPane.showConfirmDialog(
                        MainApplicationFrame.this,
                        "Вы уверены, что хотите закрыть приложение?",
                        "Подтвердите выход",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (result == JOptionPane.NO_OPTION) {
                    confirmClosing[0] = false; // сбросить флаг
                    return;
                }

                dispose();
                System.exit(0);
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
        Logger.debug("Протокол работает");
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
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        JMenuItem systemLookAndFeel = createSystemLookAndFeelMenuItem();
        menu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = createCrossplatformLookAndFeelMenuItem();
        menu.add(crossplatformLookAndFeel);

        return menu;
    }

    private JMenuItem createSystemLookAndFeelMenuItem()
    {
        JMenuItem menuItem = new JMenuItem("Системная схема", KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return menuItem;
    }

    private JMenuItem createCrossplatformLookAndFeelMenuItem()
    {
        JMenuItem menuItem = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return menuItem;
    }

    private JMenu createTestMenu()
    {
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        JMenuItem addLogMessageItem = createAddLogMessageItem();
        menu.add(addLogMessageItem);

        return menu;
    }

    private JMenuItem createAddLogMessageItem()
    {
        JMenuItem menuItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
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
}
