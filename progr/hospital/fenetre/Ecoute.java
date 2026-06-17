package fenetre;

import accessory.Chamber;
import entity.Human;
import java.awt.event.*;
import java.awt.geom.Point2D;
import javax.swing.JLabel;

public class Ecoute extends MouseAdapter {
    private final Dessin dessin;
    private final JLabel status;
    private final ControlPanel controlPanel;

    public Ecoute(Dessin dessin, JLabel status, ControlPanel controlPanel) {
        this.dessin = dessin;
        this.status = status;
        this.controlPanel = controlPanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point2D.Double point = this.dessin.toWorld(e.getPoint());
        Chamber chamber = this.dessin.findChamberAt(e.getPoint());

        if (chamber != null) {
            this.status.setText(String.format("Position (%.1f, %.1f) - Chambre %d", point.getX(), point.getY(), chamber.getId()));
        } else {
            this.status.setText(String.format("Position (%.1f, %.1f) - Couloir", point.getX(), point.getY()));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Human human = this.dessin.findHumanAt(e.getPoint());
        if (human != null) {
            System.out.println("Click sur humain : " + human.getName());
            this.controlPanel.setSelectedHuman(human);
        } else {
            System.out.println("Click sans humain.");
            this.controlPanel.setSelectedHuman(null);
        }
    }
}
