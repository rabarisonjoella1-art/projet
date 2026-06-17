package main;

import accessory.Chamber;
import accessory.Date;
import accessory.Hospital;
import accessory.Remedy;
import entity.Disease;
import entity.Doctor;
import entity.Patient;
import entity.Visitor;
import fenetre.MaFenetre;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Rectangle2D.Double rect1 = new Rectangle2D.Double(3, 5, 2, 3);
        Rectangle2D.Double rect2 = new Rectangle2D.Double(5, 1, 2, 2);
        Rectangle2D.Double rect3 = new Rectangle2D.Double(8, 4, 3, 4);

        Chamber ch1 = new Chamber(1, rect1);
        Chamber ch2 = new Chamber(2, rect2);
        Chamber ch3 = new Chamber(3, rect3);

        // test aléa morgue
        Chamber morgue = new Chamber(4, new Rectangle2D.Double(12, 8, 2, 3));

        Date birthday = new Date(20, 3, 2008);
        Date date1 = new Date(12, 1, 2000);
        Date date2 = new Date(23, 11, 2003);
        Date date3 = new Date(20, 10, 1994);
        Date date4 = new Date(12, 3, 2010);
        Date date5 = new Date(14, 5, 2015);

        Disease m1 = new Disease("Kibo", "Contact", false, true, 1);
        Disease m2 = new Disease("Loha", "Air", false, true, 2);
        Disease m3 = new Disease("Saina", "Naturel", false, false, 3);
        Disease m4 = new Disease("Vovony", "Contact", false, true, 4);
        Disease m5 = new Disease("Fo", "Naturel", true, false, 5);

        Remedy f1 = new Remedy(date1, 10, "Remede Kibo", 800, new Disease[]{m1,m2});
        Remedy f2 = new Remedy(date2, 20, "Remede Loha", 900, new Disease[]{m2});
        Remedy f3 = new Remedy(date3, 30, "Remede Vovony", 1200, new Disease[]{m4});
        Remedy f4 = new Remedy(date4, 40, "Remede Fo", 2000, new Disease[]{m5});
        Remedy f5 = new Remedy(date5, 50, "Remede Mixte", 1500, new Disease[]{m2, m3});

        Patient p1 = new Patient(
            "P1", birthday, 'm', new Point2D.Double(7, 3),
            new Disease[]{m1}, new Disease[0], new Disease[]{m1, m2, m3}, new int[]{4, 0, 2}
        );
        Patient p2 = new Patient(
            "P2", birthday, 'f', new Point2D.Double(10, 7),
            new Disease[]{m4}, new Disease[0], new Disease[]{m4, m2}, new int[]{7, 2}
        );
        Patient p3 = new Patient(
            "P3", birthday, 'f', new Point2D.Double(8, 11),
            new Disease[]{m5}, new Disease[0], new Disease[]{m4, m5}, new int[]{2, 7}
        );

        Patient p4 = new Patient(
            "P4", birthday, 'm', new Point2D.Double(1, 2),
            new Disease[]{m3}, new Disease[0], new Disease[]{m3, m5}, new int[]{3, 0}
        );

        p1.refreshLifeStatus();
        p2.refreshLifeStatus();
        p3.refreshLifeStatus();
        p4.refreshLifeStatus();

        p3.setAlive(true); 
        

        Doctor d1 = new Doctor("D1", date1, 'm', new Point2D.Double(4, 7), new Disease[]{m1, m3}, new Remedy[]{f1, f5});
        Doctor d2 = new Doctor("D2", date2, 'm', new Point2D.Double(13, 6), new Disease[]{m4, m5}, new Remedy[]{f3, f4});
        Doctor d3 = new Doctor("D3", date3, 'm', new Point2D.Double(12, 11), new Disease[]{m2, m3}, new Remedy[]{f2});
        Visitor v1 = new Visitor("V1", birthday, 'f', new Point2D.Double(6, 9));

        Vector personne = new Vector();
        personne.add(d3);
        personne.add(p1);
        personne.add(p2);
        personne.add(d1);
        personne.add(p3);
        personne.add(p4);
        personne.add(d2);
        personne.add(v1);

        Vector chambers = new Vector();
        chambers.add(ch1);
        chambers.add(ch2);
        chambers.add(ch3);
        // test aléa morgue

        chambers.add(morgue);

        Hospital hospital = new Hospital(personne, chambers);
        System.out.println("Initialisation de l'hopital statique.");
        SwingUtilities.invokeLater(() -> new MaFenetre(hospital));
    }
}
