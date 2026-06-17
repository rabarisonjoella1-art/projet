package fenetre;

import accessory.*;
import entity.*;
import java.awt.*;
import java.util.Vector;
import javax.swing.*;

public class ControlPanel extends JPanel {
    private final Hospital hospital;
    private final Dessin dessin;
    private final JLabel selectedLabel;
    private final JTextField xField;
    private final JTextField yField;
    private final JButton moveButton;
    // private final JButton killButton;
    private final JLabel medecineLabel;
    private final JComboBox patientBox;
    private final JComboBox remedyBox;
    private final JButton remedyButton;
    private Human selectedHuman;

    public ControlPanel(Hospital hospital, Dessin dessin) {
        this.hospital = hospital;
        this.dessin = dessin;

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setPreferredSize(new Dimension(320, 500));

        this.selectedLabel = new JLabel("Aucun humain selectionne.");
        this.xField = new JTextField(8);
        this.yField = new JTextField(8);
        this.moveButton = new JButton("Deplacer");
        // this.killButton = new JButton("Tuer");
        this.medecineLabel = new JLabel("Administration indisponible.");
        this.patientBox = new JComboBox();
        this.remedyBox = new JComboBox();
        this.remedyButton = new JButton("Donner remede");

        add(this.selectedLabel);
        add(new JLabel("X :"));
        add(this.xField);
        add(new JLabel("Y :"));
        add(this.yField);
        add(this.moveButton);
        // add(this.killButton);
        add(this.medecineLabel);
        add(new JLabel("Patient :"));
        add(this.patientBox);
        add(new JLabel("Remede :"));
        add(this.remedyBox);
        add(this.remedyButton);

        this.moveButton.addActionListener(e -> moveSelectedHuman());
        // this.killButton.addActionListener(e -> killSelectedHuman());
        this.remedyButton.addActionListener(e -> administerRemedy());

        refreshDoctorForm();
    }

    public void setSelectedHuman(Human selectedHuman) {
        this.selectedHuman = selectedHuman;
        if (selectedHuman == null) {
            System.out.println("Clique sans humain selectionne.");
            this.selectedLabel.setText("Aucun humain selectionne.");
            this.xField.setText("");
            this.yField.setText("");
            refreshDoctorForm();
            return;
        }

        System.out.println("Humain clique : " + selectedHuman.getName());
        this.selectedLabel.setText(
            selectedHuman.getClass().getSimpleName()
            + " " + selectedHuman.getName()
            + " position (" + selectedHuman.getPos().getX()
            + ", " + selectedHuman.getPos().getY() + ")"
        );
        this.xField.setText(String.valueOf(selectedHuman.getPos().getX()));
        this.yField.setText(String.valueOf(selectedHuman.getPos().getY()));
        refreshDoctorForm();
    }

    private void moveSelectedHuman() {
        if (this.selectedHuman == null) {
            System.out.println("Action deplacement refusee : aucun humain selectionne.");
            return;
        }

        try {
            double x = Double.parseDouble(this.xField.getText());
            double y = Double.parseDouble(this.yField.getText());
            this.hospital.moveHumanTo(this.selectedHuman, x, y);
            this.selectedLabel.setText(
                this.selectedHuman.getClass().getSimpleName()
                + " " + this.selectedHuman.getName()
                + " position (" + this.selectedHuman.getPos().getX()
                + ", " + this.selectedHuman.getPos().getY() + ")"
            );
            refreshDoctorForm();
            this.dessin.repaint();
        } catch (NumberFormatException ex) {
            System.out.println("Action deplacement refusee : X ou Y invalide.");
        }
    }

    private void killSelectedHuman() {
        if (this.selectedHuman == null) {
            System.out.println("Action tuer refusee : aucun humain selectionne.");
            return;
        }

        if (!this.selectedHuman.isAlive()) {
            System.out.println("Action tuer refusee : humain deja mort.");
            return;
        }

        this.selectedHuman.setAlive(false);
        System.out.println("Action tuer effectuee sur " + this.selectedHuman.getName() + ".");
        refreshDoctorForm();
        this.dessin.repaint();
    }

    private void refreshDoctorForm() {
        this.patientBox.removeAllItems();
        this.remedyBox.removeAllItems();

        if (!(this.selectedHuman instanceof Doctor)) {
            this.medecineLabel.setText("Administration reservee au medecin.");
            this.patientBox.setEnabled(false);
            this.remedyBox.setEnabled(false);
            this.remedyButton.setEnabled(false);
            return;
        }

        Doctor doctor = (Doctor) this.selectedHuman;
        Chamber chamber = this.hospital.findChamberForHuman(doctor);
        if (chamber == null) {
            this.medecineLabel.setText("Le medecin doit etre dans une chambre.");
            this.patientBox.setEnabled(false);
            this.remedyBox.setEnabled(false);
            this.remedyButton.setEnabled(false);
            System.out.println("Formulaire remede indisponible : medecin hors chambre.");
            return;
        }

        Vector patients = this.hospital.getPatientsInChamber(chamber);
        for (int i = 0; i < patients.size(); i++) {
            this.patientBox.addItem(patients.get(i));
        }

        Remedy[] remedies = doctor.getBag();
        for (int i = 0; i < remedies.length; i++) {
            this.remedyBox.addItem(remedies[i]);
        }

        this.medecineLabel.setText("Chambre " + chamber.getId() + " : formulaire remede actif.");
        this.patientBox.setEnabled(patients.size() > 0);
        this.remedyBox.setEnabled(remedies.length > 0);
        this.remedyButton.setEnabled(patients.size() > 0 && remedies.length > 0);
    }

    private void administerRemedy() {
        if (!(this.selectedHuman instanceof Doctor)) {
            System.out.println("Action remede refusee : l'humain selectionne n'est pas un medecin.");
            return;
        }

        Doctor doctor = (Doctor) this.selectedHuman;
        Object patientObject = this.patientBox.getSelectedItem();
        Object remedyObject = this.remedyBox.getSelectedItem();

        if (!(patientObject instanceof Patient) || !(remedyObject instanceof Remedy)) {
            System.out.println("Action remede refusee : patient ou remede manquant.");
            return;
        }

        boolean success = this.hospital.administerRemedy(
            doctor,
            (Patient) patientObject,
            (Remedy) remedyObject,
            new Date(23, 4, 2026)
        );

        if (success) {
            System.out.println("Action remede effectuee.");
            refreshDoctorForm();
            this.dessin.repaint();
        } else {
            System.out.println("Action remede echouee.");
        }
    }
}
