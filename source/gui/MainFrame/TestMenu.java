package gui.MainFrame;

import java.awt.event.KeyEvent;
import log.Logger;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class TestMenu {
    public static JMenu createTestMenu(String title, MainApplicationFrame mainApplicationFrame) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(KeyEvent.VK_T);

        menu.getAccessibleContext().setAccessibleDescription(MainApplicationFrame.getLocalizedString("main.application.frame.test.commands",mainApplicationFrame.getLocale()));

        JMenuItem addLogMessageItem = createAddLogMessageItem(mainApplicationFrame);
        menu.add(addLogMessageItem);

        return menu;
    }

    private static JMenuItem createAddLogMessageItem(MainApplicationFrame mainApplicationFrame) { //Метод для создания элемента меню "Добавить сообщение в лог" и его текста
        JMenuItem menuItem = new JMenuItem(MainApplicationFrame.getLocalizedString("main.application.frame.message.in.log", mainApplicationFrame.getLocale()), KeyEvent.VK_S);
        menuItem.addActionListener((event) -> Logger.debug(MainApplicationFrame.getLocalizedString("main.application.frame.new.line", mainApplicationFrame.getLocale())));
        return menuItem;
    }
}