package tests.gui;
import gui.GameVisualizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.awt.*;

import static gui.GameVisualizer.angleTo;

public class GameVisualizerTest {

    private GameVisualizer gameVisualizer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameVisualizer = new GameVisualizer();
    }

    @Test
    public void testMoveRobotWithinBorder() {
        double initialRobotPositionX = gameVisualizer.m_robotPositionX;
        double initialRobotPositionY = gameVisualizer.m_robotPositionY;

        gameVisualizer.moveRobot(0.1, 0, 100);

        Assertions.assertTrue(gameVisualizer.m_robotPositionX > initialRobotPositionX);
        Assertions.assertTrue(gameVisualizer.m_robotPositionY > initialRobotPositionY);
        Assertions.assertFalse(gameVisualizer.m_collisionWithBorder);
    }

    @Test
    public void testUpdateRobotDirection() {
        gameVisualizer.setTargetPosition(new Point(200, 200));
        gameVisualizer.onModelUpdateEvent();

        Assertions.assertTrue(Math.abs(gameVisualizer.m_robotDirection - angleTo(gameVisualizer.m_robotPositionX, gameVisualizer.m_robotPositionY, 200, 200)) > 0.01);
    }

    @Test
    public void testDontMoveRobotToTarget() {
        gameVisualizer.setTargetPosition(new Point(100, 100));
        gameVisualizer.onModelUpdateEvent();

        double initialRobotPositionX = gameVisualizer.m_robotPositionX;
        double initialRobotPositionY = gameVisualizer.m_robotPositionY;

        gameVisualizer.onModelUpdateEvent();

        Assertions.assertEquals(initialRobotPositionX, gameVisualizer.m_robotPositionX);
        Assertions.assertEquals(initialRobotPositionY, gameVisualizer.m_robotPositionY);
    }

    @Test
    public void testDontMoveRobotToBorder() {
        gameVisualizer.m_robotPositionX = 0;
        gameVisualizer.m_robotPositionY = 0;

        gameVisualizer.moveRobot(0.1, 0, 100);

        Assertions.assertEquals(-6.0, gameVisualizer.m_robotPositionX);
        Assertions.assertEquals(6.0, gameVisualizer.m_robotPositionY);
        Assertions.assertFalse(gameVisualizer.m_collisionWithBorder);
    }

    @Test
    public void testUpdateRobotDirectionWhenHittingBorder() {
        gameVisualizer.m_robotPositionX = 0;
        gameVisualizer.m_robotPositionY = 0;

        gameVisualizer.moveRobot(0.1, 0, 100);

        Assertions.assertTrue(Math.abs(gameVisualizer.m_robotDirection - Math.PI) > 0.01);
    }
}