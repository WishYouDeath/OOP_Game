package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
    private final BlockingQueue<LogEntry> m_messages;
    private final ArrayList<LogEntry> m_previousMessages;
    private final ArrayList<LogChangeListener> m_listeners;

    public LogWindowSource(int iQueueLength) {
        m_messages = new LinkedBlockingQueue<>(iQueueLength);
        m_previousMessages = new ArrayList<>();
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        m_listeners.add(listener);
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry newEntry = new LogEntry(logLevel, strMessage);
        m_messages.add(newEntry);
        m_previousMessages.add(newEntry);
        notifyListeners();
    }

    public List<LogEntry> all() {
        return m_previousMessages;
    }

    private void notifyListeners() {
        for (LogChangeListener listener : m_listeners) {
            listener.onLogChanged();
        }
    }

    public int size() {
        return m_messages.size();
    }

    public List<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_previousMessages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_previousMessages.size());
        return m_previousMessages.subList(startFrom, indexTo);
    }

    public void unregisterListener(LogChangeListener listener) {
        m_listeners.remove(listener);
    }
}