package gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class LanguageChangeButton extends JMenuItem {
    private final MainApplicationFrame mainApplicationFrame;

    public LanguageChangeButton(String title, MainApplicationFrame mainApplicationFrame) {
        super(title);
        this.mainApplicationFrame = mainApplicationFrame;
        addActionListener(new LanguageChangeListener());

        if (title.equals(mainApplicationFrame.getLocalizedString("language.english", mainApplicationFrame.getLocale()))) {
            setText("English");
        } else if (title.equals(mainApplicationFrame.getLocalizedString("language.russian", mainApplicationFrame.getLocale()))) {
            setText("Русский");
        }
    }

    private class LanguageChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedLanguage = getText();
            if (selectedLanguage.equals("English")) {
                mainApplicationFrame.changeLocale(Locale.ENGLISH);
            } else if (selectedLanguage.equals("Русский")) {
                mainApplicationFrame.changeLocale(new Locale("ru", "RU"));
            }
        }
    }
}