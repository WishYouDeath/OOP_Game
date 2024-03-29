package gui;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    public boolean m_collisionWithBorder;  // Было ли столкновение с границей

    private static Timer initTimer()
    {
        return new Timer("events generator", true);
    }

    public volatile double m_robotPositionX = 100;
    public volatile double m_robotPositionY = 100;
    public double m_robotDirection = 0;
    public int m_targetPositionX = 150;
    public int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.00105;

    public GameVisualizer()
    {
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 8);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 7);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
    }

    public static final double BORDER_BUFFER = 3.5; // Отступ от границы в пикселях

    public void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    public void onModelUpdateEvent()
    {
        m_targetPositionX = (int) applyLimits(m_targetPositionX, 0, this.getWidth()); // Чтоб таргет не уходил за диапазон
        m_targetPositionY = (int) applyLimits(m_targetPositionY, 0, this.getHeight());

        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 7)
        {
            return;
        }

        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;

        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(maxVelocity, angularVelocity, 10);
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    public void moveRobot(double velocity, double angularVelocity, double duration) {

        velocity = applyLimits(velocity, 0, maxVelocity);//Ограничение скорости робота
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);//Ограничение  угла поворота
        m_robotDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);//Направление робота

        double distanceToMove = velocity * duration;//Дистанция до цели
        double newX = m_robotPositionX + Math.cos(m_robotDirection) * distanceToMove;
        double newY = m_robotPositionY + Math.sin(m_robotDirection) * distanceToMove;

        if (newX < BORDER_BUFFER) {
            newX = BORDER_BUFFER;//Ограничиваем движение робота влево
            m_robotDirection = Math.PI - m_robotDirection;//Меняем направление робота (отражаем его)
        } else if (newX > getWidth() - BORDER_BUFFER) {
            newX = getWidth() - BORDER_BUFFER;//Ограничиваем движение робота вправо
            m_robotDirection = Math.PI - m_robotDirection;
        }
        if (newY < BORDER_BUFFER) {
            newY = BORDER_BUFFER;//Ограничиваем движение робота вниз
            m_robotDirection = Math.PI + m_robotDirection;
        } else if (newY > getHeight() - BORDER_BUFFER) {
            newY = getHeight() - BORDER_BUFFER;//Ограничиваем движение робота вверх
            m_robotDirection = Math.PI + m_robotDirection;
        }
        m_robotPositionX = newX;//Присваиваем новые значения для робота
        m_robotPositionY = newY;
        if (0 <= m_robotPositionX && m_robotPositionX <= this.getWidth() && 0 <= m_robotPositionY && m_robotPositionY <= getHeight()){
            m_collisionWithBorder = false;
        }

    }
    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    private void drawRobot(Graphics2D g, double direction)
    {
        int robotCenterX = round(m_robotPositionX);
        int robotCenterY = round(m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}