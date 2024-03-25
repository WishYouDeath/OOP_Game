package gui;

import gui.MainFrame.MainApplicationFrame;
import java.awt.Frame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class RobotsProgram
{
  // Запуск программы
  public static void main(String[] args) {
    try {
      // Установка внешнего вида приложения на Nimbus
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Запуск GUI в потоке обработки событий
    SwingUtilities.invokeLater(() -> {
      // Создание и настройка основного фрейма приложения
      MainApplicationFrame frame = new MainApplicationFrame();
      frame.pack(); // Устанавливаем размер содержимого окна
      frame.setVisible(true); // Делаем окно видимым
      frame.setExtendedState(Frame.MAXIMIZED_BOTH); // Окно на весь экран
    });
  }
}