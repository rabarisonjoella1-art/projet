package accessory;
import entity.*;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

public class Hospital
{
    private static final double DEFAULT_WIDTH = 15.0;
    private static final double DEFAULT_HEIGHT = 12.0;

    private Vector personne;
    private Vector chambers;

    public Hospital(Vector personne, Vector chambers)
    {
        this.setPersonne(personne);
        this.setChambers(chambers);
    }

    //Getters
    public Vector getPersonne(){
        return this.personne;
    }

    public Vector getChambers(){
        return this.chambers;
    }

    //Setters
    public void setPersonne(Vector personne)
    {
        this.personne = personne;
    }

    public void setChambers(Vector chambers)
    {
        this.chambers = chambers;
    }


    public void check()
    {
        System.out.println("Verification manuelle de l'hopital.");
    }

    public Rectangle2D.Double computeBounds()
    {
        double maxX = DEFAULT_WIDTH;
        double maxY = DEFAULT_HEIGHT;

        for(int i = 0; i < this.getChambers().size(); i++)
        {
            Chamber chamber = (Chamber)this.getChambers().get(i);
            Rectangle2D.Double shape = chamber.getShape();
            maxX = Math.max(maxX, shape.getX() + shape.getWidth());
            maxY = Math.max(maxY, shape.getY() + shape.getHeight());
        }

        return new Rectangle2D.Double(0, 0, maxX, maxY);
    }

    public boolean moveHumanTo(Human human, double x, double y)
    {
        Rectangle2D.Double bounds = this.computeBounds();
        if(x < bounds.getMinX() || x > bounds.getMaxX() || y < bounds.getMinY() || y > bounds.getMaxY())
        {
            System.out.println("Deplacement refuse pour " + human.getName() + " : hors de la fenetre.");
            return false;
        }
        human.moveTo(x, y);
        System.out.println("Deplacement effectue pour " + human.getName() + " vers (" + x + ", " + y + ").");
        return true;
    }

    public Chamber findChamberForHuman(Human human)
    {
        for(int i = 0; i < this.getChambers().size(); i++)
        {
            Chamber chamber = (Chamber)this.getChambers().get(i);
            if(chamber.contient(human.getPos().getX(), human.getPos().getY()))
            {
                return chamber;
            }
        }
        return null;
    }

    public Vector getPatientsInChamber(Chamber chamber)
    {
        Vector patients = new Vector();
        for(int i = 0; i < this.getPersonne().size(); i++)
        {
            if(this.getPersonne().get(i) instanceof Patient)
            {
                Patient patient = (Patient)this.getPersonne().get(i);
                if(chamber.contient(patient.getPos().getX(), patient.getPos().getY()))
                {
                    patients.add(patient);
                }
            }
        }
        return patients;
    }

    public boolean administerRemedy(Doctor doctor, Patient patient, Remedy remedy, Date takenDate)
    {
        if(!doctor.isAlive())
        {
            System.out.println("Administration refusee : medecin mort.");
            return false;
        }
        if(!patient.isAlive())
        {
            System.out.println("Administration refusee : patient mort.");
            return false;
        }
        if(!doctor.hasRemedy(remedy))
        {
            System.out.println("Administration refusee : remede absent du sac du medecin.");
            return false;
        }

        Chamber doctorChamber = this.findChamberForHuman(doctor);
        Chamber patientChamber = this.findChamberForHuman(patient);

        if(doctorChamber == null || patientChamber == null || doctorChamber.getId() != patientChamber.getId())
        {
            System.out.println("Administration refusee : medecin et patient pas dans la meme chambre.");
            return false;
        }

        boolean canTreat = false;
        for(int i = 0; i < remedy.getCure().length; i++)
        {
            if(doctor.canTreat(remedy.getCure()[i]))
            {
                canTreat = true;
            }
        }
        if(!canTreat)
        {
            System.out.println("Administration refusee : le medecin n'est pas specialise pour ce remede.");
            return false;
        }

        patient.eat(new Remedy[]{remedy}, takenDate);
        boolean applied = patient.applyRemedy(remedy);
        patient.refreshLifeStatus();
        doctor.removeRemedy(remedy);
        System.out.println("Administration de remede par " + doctor.getName() + " a " + patient.getName() + " avec " + remedy.getName() + ".");
        return applied;
    }
}
