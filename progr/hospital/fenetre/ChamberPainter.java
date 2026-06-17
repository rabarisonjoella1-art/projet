package fenetre;

import accessory.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class ChamberPainter {
    public void paint(Graphics2D g2, Hospital hospital, Dessin dessin) {
        Vector chambers = hospital.getChambers();
        for (int i = 0; i < chambers.size(); i++) {
            Chamber chamber = (Chamber) chambers.get(i);
            Rectangle2D.Double shape = chamber.getShape();

            int x = dessin.toScreenX(shape.getX());
            int y = dessin.toScreenY(shape.getY());
            int width = (int) Math.round(shape.getWidth() * Dessin.SCALE);
            int height = (int) Math.round(shape.getHeight() * Dessin.SCALE);

            g2.setColor(PlanPalette.CHAMBER_FILL);
            g2.fillRect(x, y, width, height);
            g2.setColor(PlanPalette.CHAMBER_BORDER);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRect(x, y, width, height);
            g2.drawString("Chambre " + chamber.getId(), x + 12, y + 22);
        }
    }
}
