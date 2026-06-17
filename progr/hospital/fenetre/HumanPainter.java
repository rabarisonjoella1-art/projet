package fenetre;

import accessory.Hospital;
import entity.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Vector;

public class HumanPainter {
    public void paint(Graphics2D g2, Hospital hospital, Dessin dessin) {
        Vector personne = hospital.getPersonne();
        for (int i = 0; i < personne.size(); i++) {
            Human pers = (Human) personne.get(i);
            Point2D.Double pos = pers.getPos();
            int x = dessin.toScreenX(pos.getX()) - (Dessin.PERSON_SIZE / 2);
            int y = dessin.toScreenY(pos.getY()) - (Dessin.PERSON_SIZE / 2);

            g2.setColor(resolveColor(pers));
            g2.fillOval(x, y, Dessin.PERSON_SIZE, Dessin.PERSON_SIZE);
            g2.setColor(resolveBorderColor(pers));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x, y, Dessin.PERSON_SIZE, Dessin.PERSON_SIZE);

            g2.setColor(PlanPalette.TEXT);
            g2.drawString(pers.getName(), x - 4, y - 8);
            drawInlineInfo(g2, pers, x + Dessin.PERSON_SIZE + 6, y + Dessin.PERSON_SIZE + 12);
        }
    }

    private Color resolveColor(Human human) {
        if (!human.isAlive()) {
            return PlanPalette.DEAD;
        }
        if (human instanceof Doctor) {
            return PlanPalette.DOCTOR;
        }
        if (human instanceof Patient) {
            return PlanPalette.PATIENT;
        }
        if (human instanceof Visitor) {
            return PlanPalette.VISITOR;
        }
        return Color.DARK_GRAY;
    }

    private Color resolveBorderColor(Human human) {
        if (!human.isAlive() || human instanceof Doctor) {
            return PlanPalette.TEXT;
        }
        return Color.WHITE;
    }

    private void drawInlineInfo(Graphics2D g2, Human human, int x, int y) {
        String info = "";

        if (human instanceof Patient) {
            info = formatInlinePatient((Patient) human);
        } else if (human instanceof Doctor) {
            info = formatInlineDoctor((Doctor) human);
        }

        if (!info.isEmpty()) {
            g2.setColor(PlanPalette.TEXT);
            g2.drawString(info, x, y);
        }
    }

    private String formatInlinePatient(Patient patient) {
        StringBuilder builder = new StringBuilder();
        boolean found = false;

        for (int i = 0; i < patient.getDiseases().length; i++) {
            Disease disease = patient.getDiseases()[i];
            if (disease != null && patient.getLevels()[i] > 0) {
                if (found) {
                    builder.append(", ");
                }
                builder.append(disease.getName())
                    .append(" n")
                    .append(patient.getLevels()[i]);
                found = true;
            }
        }

        if (!found) {
            return "Aucune maladie";
        }
        return builder.toString();
    }

    private String formatInlineDoctor(Doctor doctor) {
        StringBuilder builder = new StringBuilder();
        Disease[] specialities = doctor.getSpeciality();
        boolean found = false;

        for (int i = 0; i < specialities.length; i++) {
            if (specialities[i] != null) {
                if (found) {
                    builder.append(", ");
                }
                builder.append(specialities[i].getName());
                found = true;
            }
        }

        if (!found) {
            return "Aucune specialite";
        }
        return builder.toString();
    }
}
