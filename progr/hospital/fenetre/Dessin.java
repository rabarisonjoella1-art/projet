package fenetre;

import accessory.Chamber;
import accessory.Hospital;
import entity.Human;
import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import javax.swing.JPanel;

// Panneau principal du plan.
// Cette classe coordonne les sous-dessins au lieu de tout dessiner elle-meme.
public class Dessin extends JPanel {
    public static final int SCALE = 45;
    public static final int MARGIN = 50;
    public static final int PERSON_SIZE = 18;

    private final Hospital hospital;
    private final ChamberPainter chamberPainter;
    private final HumanPainter humanPainter;

    public Dessin(Hospital hospital) {
        this.hospital = hospital;
        this.chamberPainter = new ChamberPainter();
        this.humanPainter = new HumanPainter();

        this.setBackground(PlanPalette.BACKGROUND);
        this.setPreferredSize(buildPreferredSize());
    }

    private Dimension buildPreferredSize() {
        double maxX = 0;
        double maxY = 0;

        Vector chambers = this.hospital.getChambers();
        for (int i = 0; i < chambers.size(); i++) {
            Chamber chamber = (Chamber) chambers.get(i);
            Rectangle2D.Double shape = chamber.getShape();
            maxX = Math.max(maxX, shape.getX() + shape.getWidth());
            maxY = Math.max(maxY, shape.getY() + shape.getHeight());
        }

        Vector personne = this.hospital.getPersonne();
        for (int i = 0; i < personne.size(); i++) {
            Human human = (Human) personne.get(i);
            maxX = Math.max(maxX, human.getPos().getX() + 1);
            maxY = Math.max(maxY, human.getPos().getY() + 1);
        }

        int width = (int) Math.ceil(maxX * SCALE) + (MARGIN * 2);
        int height = (int) Math.ceil(maxY * SCALE) + (MARGIN * 2);
        return new Dimension(Math.max(width, 680), Math.max(height, 650));
    }

    public Point2D.Double toWorld(Point point) {
        double worldX = (point.getX() - MARGIN) / (double) SCALE;
        double worldY = (point.getY() - MARGIN) / (double) SCALE;
        return new Point2D.Double(worldX, worldY);
    }

    public Chamber findChamberAt(Point point) {
        Point2D.Double world = toWorld(point);
        Vector chambers = this.hospital.getChambers();
        for (int i = 0; i < chambers.size(); i++) {
            Chamber chamber = (Chamber) chambers.get(i);
            if (chamber.contient(world.getX(), world.getY())) {
                return chamber;
            }
        }
        return null;
    }

    public Human findHumanAt(Point point) {
        Vector personne = this.hospital.getPersonne();
        for (int i = personne.size() - 1; i >= 0; i--) {
            Human human = (Human) personne.get(i);
            Point2D.Double pos = human.getPos();
            int centerX = toScreenX(pos.getX());
            int centerY = toScreenY(pos.getY());
            double distance = point.distance(centerX, centerY);
            if (distance <= (PERSON_SIZE / 2.0) + 3.0) {
                return human;
            }
        }
        return null;
    }

    public int toScreenX(double x) {
        return MARGIN + (int) Math.round(x * SCALE);
    }

    public int toScreenY(double y) {
        return MARGIN + (int) Math.round(y * SCALE);
    }

    public Object findElementAt(Point point) {
        Human human = findHumanAt(point);
        if (human != null) {
            return human;
        }
        return findChamberAt(point);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));

        this.chamberPainter.paint(g2, this.hospital, this);
        this.humanPainter.paint(g2, this.hospital, this);
    }
}
