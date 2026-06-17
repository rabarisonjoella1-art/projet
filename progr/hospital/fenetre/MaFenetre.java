package fenetre;

import accessory.Hospital;
import java.awt.*;
import javax.swing.*;

public class MaFenetre extends JFrame {
    public MaFenetre(Hospital hospital) {
        setTitle("Visualisation de l'hopital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Dessin dessin = new Dessin(hospital);
        JLabel status = new JLabel("Survolez le plan pour voir les coordonnees.", SwingConstants.CENTER);
        ControlPanel controlPanel = new ControlPanel(hospital, dessin);
        Ecoute ecoute = new Ecoute(dessin, status, controlPanel);
        dessin.addMouseMotionListener(ecoute);
        dessin.addMouseListener(ecoute);

        add(dessin, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(status, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
