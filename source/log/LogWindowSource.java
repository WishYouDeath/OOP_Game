package log;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.List;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */

public class LogWindowSource {
    private final int m_iQueueLength; // Размер очереди
    private final BlockingQueue<LogEntry> m_messages; // Хранит сообщения логов
    private final BlockingQueue<LogChangeListener> m_listeners; // Хранение слушателей
    private volatile List<LogChangeListener> m_activeListeners; // Массив слушателей для изменений в логах

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayBlockingQueue<>(iQueueLength);
        m_listeners = new ArrayBlockingQueue<>(iQueueLength);
    }

    public void registerListener(LogChangeListener listener) {
        if (!m_listeners.contains(listener)) { // Если этого слушателя нет, то добавляем его
            m_listeners.add(listener);
        }
        updateActiveListeners(); // Обновляем список слушателей
    }

    public void unregisterListener(LogChangeListener listener) {
        if (m_listeners.remove(listener)) { // Удаляем слушателя и уведомляем об этом всех
            updateActiveListeners();
        }
        else{
            Logger.error("Произошла ошибка, невозможно удалить незарегистированного пользователя");
        }
    }

    public void append(LogLevel logLevel, String strMessage) { // Добавление новой записи в логи
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (m_messages.size() >= m_iQueueLength) {
            m_messages.poll(); // Если очередь уже заполнена, то удаляем самое старое сообщение
        }
        m_messages.offer(entry); // Вставлям новое сообщение
        notifyListeners(); // Уведомляем всех слушателей о новой записи
    }

    private void notifyListeners() {
        List<LogChangeListener> activeListeners = m_activeListeners; // Создаём копию
        synchronized (m_listeners) {
            if (activeListeners == null) { // Если слушателей нет, то создаём и обновляем
                activeListeners = m_activeListeners;
                updateActiveListeners();
            }
        }
        if (activeListeners != null) {
            for (LogChangeListener listener : activeListeners) {
                listener.onLogChanged(); // Уведомляем каждого слушателя
            }
        }
    }

    private void updateActiveListeners() {
        List<LogChangeListener> listeners = new ArrayList<>(m_listeners);
        m_activeListeners = listeners; // Обновляем активных слушателей
    }

    public int size() { // Возвращает текущий размер сообщений в логе
        return m_messages.size();
    }

    public List<LogEntry> range(int startFrom, int count) { // Cоздает новый список range, который является копией текущего списка логов m_messages
        List<LogEntry> range = new ArrayList<>(m_messages);
        int end = Math.min(startFrom + count, range.size());
        return range.subList(startFrom, end); // Возвращает подсписок логов из диапазона
    }

    public List<LogEntry> all() { // Возвращает копию всего списка логов
        return new ArrayList<>(m_messages);
    }
}