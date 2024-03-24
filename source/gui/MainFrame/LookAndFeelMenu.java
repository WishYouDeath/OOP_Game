package gui.MainFrame;

import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.UIManager;
import java.awt.event.KeyEvent;

public class LookAndFeelMenu {

    public static JMenu createLookAndFeelMenu(String title, MainApplicationFrame mainApplicationFrame) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(mainApplicationFrame.getLocalizedString("main.application.frame.managing.display.mode",mainApplicationFrame.getLocale()));

        JMenuItem systemLookAndFeel = createSystemLookAndFeelMenuItem(mainApplicationFrame);
        menu.add(systemLookAndFeel);

        JMenuItem crossplatformLookAndFeel = createCrossplatformLookAndFeelMenuItem(mainApplicationFrame);
        menu.add(crossplatformLookAndFeel);

        return menu;
    }

    private static JMenuItem createSystemLookAndFeelMenuItem(MainApplicationFrame mainApplicationFrame) {
        JMenuItem menuItem = new JMenuItem(mainApplicationFrame.getLocalizedString("main.application.frame.system.diagram", mainApplicationFrame.getLocale()), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            mainApplicationFrame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            mainApplicationFrame.invalidate();
        });
        return menuItem;
    }

    private static JMenuItem createCrossplatformLookAndFeelMenuItem(MainApplicationFrame mainApplicationFrame) {
        JMenuItem menuItem = new JMenuItem(mainApplicationFrame.getLocalizedString("main.application.frame.universal.scheme", mainApplicationFrame.getLocale()), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            mainApplicationFrame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            mainApplicationFrame.invalidate();
        });
        return menuItem;
    }
}