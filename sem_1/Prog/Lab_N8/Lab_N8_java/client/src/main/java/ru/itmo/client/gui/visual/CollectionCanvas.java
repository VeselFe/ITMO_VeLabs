package ru.itmo.client.gui.visual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.client.gui.utils.LanguageManager;
import ru.itmo.lab.common.model.StudyGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionCanvas extends JPanel
{
    private final LanguageManager languageManager = LanguageManager.getInstance();
    private final Logger logger = LoggerFactory.getLogger(CollectionCanvas.class);
    private List<StudyGroup> groups = new ArrayList<>();

    private final Map<Long, AnimatedObject> animatedObjects = new HashMap<>();

    private double currentMinX = -500, currentMaxX = 500, currentMinY = -500, currentMaxY = 500;
    private double targetMinX = -500, targetMaxX = 500, targetMinY = -500, targetMaxY = 500;

    private double pulseTime = 0;

    private static class AnimatedObject
    {
        long id;
        double currentSize;
        double targetSize;
        float alpha;
        Color color;

        AnimatedObject( long id, double startSize, double targetSize, Color color )
        {
            this.id = id;
            this.currentSize = startSize;
            this.targetSize = targetSize;
            this.alpha = 0.0f;
            this.color = color;
        }

        void update( double pulse )
        {
            if( alpha < 1.0f )
            {
                alpha = Math.min(1.0f, alpha + 0.05f);
            }
            double finalTarget = targetSize + pulse;
            currentSize += (finalTarget - currentSize) * 0.15;
        }
    }

    public CollectionCanvas()
    {
        Timer timer = new Timer(30, e -> {
            pulseTime += 0.07;

            currentMinX += (targetMinX - currentMinX) * 0.1;
            currentMaxX += (targetMaxX - currentMaxX) * 0.1;
            currentMinY += (targetMinY - currentMinY) * 0.1;
            currentMaxY += (targetMaxY - currentMaxY) * 0.1;

            for( StudyGroup group : groups )
            {
                AnimatedObject ao = animatedObjects.get(group.getId());
                if( ao != null )
                {
                    double pulse = Math.sin(pulseTime + group.getId()) * 3;
                    ao.targetSize = calculateSize(group.getStudentsCount());
                    ao.update(pulse);
                }
            }
            repaint();
        });
        timer.start();

        addMouseListener( new MouseAdapter()
        {
            @Override
            public void mouseClicked( java.awt.event.MouseEvent e )
            {
                for( int i = groups.size() - 1; i >= 0; i-- )
                {
                    StudyGroup group = groups.get(i);
                    if( isInside(e.getPoint(), group) )
                    {
                        showGroupDetails(group);
                        break;
                    }
                }
            }
        });
    }

    public void updateTexts()
    {
        repaint();
    }

    public void setGroups( List<StudyGroup> newGroups )
    {
        this.groups = newGroups;
        recalculateBounds();

        Map<Long, AnimatedObject> updatedMap = new HashMap<>();
        for (StudyGroup group : newGroups)
        {
            Color targetColor = getColorForOwner(group.getOwnerName());
            double targetSize = calculateSize(group.getStudentsCount());

            if( animatedObjects.containsKey(group.getId()) )
            {
                AnimatedObject existing = animatedObjects.get(group.getId());
                existing.color = targetColor;
                updatedMap.put(group.getId(), existing);
            }
            else
            {
                AnimatedObject newAo = new AnimatedObject(group.getId(), 0, targetSize, targetColor);
                updatedMap.put(group.getId(), newAo);
            }
        }
        animatedObjects.clear();
        animatedObjects.putAll(updatedMap);

        repaint();
    }

    @Override
    protected void paintComponent( Graphics g )
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for( StudyGroup group : groups )
        {
            AnimatedObject animatedObject = animatedObjects.get(group.getId());
            if( animatedObject != null )
            {
                drawGroup(g2d, group, (int) animatedObject.currentSize, animatedObject.alpha, animatedObject.color);
            }
        }
    }

    private void drawGroup( Graphics2D g2d, StudyGroup group, int size, float alpha, Color baseColor )
    {
        if( size <= 0 ) return;

        int centerX = mapX(group.getCoordinates().getX());
        int centerY = mapY(group.getCoordinates().getY());

        int x = centerX - (size / 2);
        int y = centerY - (size / 2);

        g2d.setColor(baseColor);
        g2d.fillOval(x, y, size, size);

        g2d.setColor(new Color(40, 40, 40));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(x, y, size, size);

        g2d.setColor(new Color(20, 20, 20));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        String label = String.format("%s (id:%d)", group.getName(), group.getId());

        FontMetrics fm = g2d.getFontMetrics();
        int textX = centerX - (fm.stringWidth(label) / 2);

        g2d.drawString(label, textX, y - 7);
    }

    private Color getColorForOwner( String ownerName )
    {
        if( ownerName == null ) return Color.GRAY;
        int hash = ownerName.hashCode();
        return new Color(
                Math.abs((hash & 0xFF0000) >> 16) % 200 + 35,
                Math.abs((hash & 0x00FF00) >> 8) % 200 + 35,
                Math.abs(hash & 0x0000FF) % 200 + 35
        );
    }

    private int mapX( double x )
    {
        double denominator = targetMaxX - targetMinX == 0 ? 1 : (currentMaxX - currentMinX);
        if (Math.abs(denominator) < 0.001) denominator = 1;
        return (int) ((x - currentMinX) / denominator * (getWidth() - 120) + 60);
    }

    private int mapY( double y )
    {
        double denominator = targetMaxY - targetMinY == 0 ? 1 : (currentMaxY - currentMinY);
        if (Math.abs(denominator) < 0.001) denominator = 1;
        return (int) (getHeight() - ((y - currentMinY) / denominator * (getHeight() - 120) + 60));
    }

    private int calculateSize( long studentsCount )
    {
        int size = (int) (25 + Math.log1p(studentsCount) * 7.5);
        return Math.max(25, Math.min(85, size));
    }

    private void recalculateBounds()
    {
        if( groups.isEmpty() ) return;

        double minX = groups.stream().mapToDouble(g -> g.getCoordinates().getX()).min().orElse(-500);
        double maxX = groups.stream().mapToDouble(g -> g.getCoordinates().getX()).max().orElse(500);
        double minY = groups.stream().mapToDouble(g -> g.getCoordinates().getY()).min().orElse(-500);
        double maxY = groups.stream().mapToDouble(g -> g.getCoordinates().getY()).max().orElse(500);

        double rangeX = Math.max(maxX - minX, 100);
        double rangeY = Math.max(maxY - minY, 100);

        targetMinX = minX - rangeX * 0.25;
        targetMaxX = maxX + rangeX * 0.25;
        targetMinY = minY - rangeY * 0.25;
        targetMaxY = maxY + rangeY * 0.25;

        if (currentMinX == -500 && currentMaxX == 500) {
            currentMinX = targetMinX; currentMaxX = targetMaxX;
            currentMinY = targetMinY; currentMaxY = targetMaxY;
        }
    }

    private boolean isInside( Point p, StudyGroup group )
    {
        AnimatedObject animatedObject = animatedObjects.get(group.getId());
        int size = (animatedObject != null) ? (int) animatedObject.currentSize : calculateSize(group.getStudentsCount());

        int centerX = mapX( group.getCoordinates().getX() );
        int centerY = mapY( group.getCoordinates().getY() );

        int x = centerX - (size / 2);
        int y = centerY - (size / 2);

        Ellipse2D ellipse = new Ellipse2D.Float(x, y, size, size);
        return ellipse.contains(p);
    }

    private void showGroupDetails( StudyGroup group )
    {
        String info = String.format(
                languageManager.getString("canvas.info.group") + ": %s\n" +
                languageManager.getString("canvas.info.owner") + ": %s\n" +
                "ID: %d\n" +
                languageManager.getString("canvas.info.students") + ": %d\n" +
                languageManager.getString("canvas.info.expelled") + ": %d\n" +
                languageManager.getString("canvas.info.semester") + ": %s\n" +
                languageManager.getString("canvas.info.form") + ": %s\n" +
                languageManager.getString("canvas.info.admin") + ": %s",
                group.getName(),
                group.getOwnerName(),
                group.getId(),
                group.getStudentsCount(),
                group.getShouldBeExp(),
                group.getSemester(),
                group.getFormOfEducation(),
                (group.getAdmin() != null ? group.getAdmin().getName() : languageManager.getString("canvas.info.no_admin"))
        );

        JOptionPane.showMessageDialog(this, info,
                languageManager.getString("canvas.info.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }
}