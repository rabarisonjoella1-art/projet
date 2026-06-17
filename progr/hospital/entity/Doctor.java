package entity;
import accessory.Date;
import accessory.Remedy;
import java.awt.geom.*;

public class Doctor extends Human{
    private Disease[] speciality;
    private Remedy[] bag;

    public Doctor(String name, Date birthday, char sexe, Point2D.Double pos, Disease[] speciality)
    {
        this(name, birthday, sexe, pos, speciality, new Remedy[0]);
    }

    public Doctor(String name, Date birthday, char sexe, Point2D.Double pos, Disease[] speciality, Remedy[] bag)
    {
        super(name, birthday, sexe, pos);
        this.speciality = speciality;
        this.bag = bag;
    }

    //Getters 
    public Disease[] getSpeciality()
    {
        return this.speciality;
    }

    public Remedy[] getBag()
    {
        return this.bag;
    }

    //Setters
    public void setBirthday(Date birthday)
    {
        if(birthday.age() < 18)
        {
            System.out.println(this.getName() + "-> Too young to become a Doctor");
        }
        else if(birthday.age() > 100)
        {
            System.out.println(this.getName() + "-> Too old to become a Doctor");
        }
        else
        {
            super.setBirthday(birthday);
        }
    }
   
    public void setSpeciality(Disease[] speciality)
    {
        this.speciality = speciality;
    }

    public void setBag(Remedy[] bag)
    {
        this.bag = bag;
    }

    public boolean hasRemedy(Remedy remedy)
    {
        for(int i = 0; i < this.bag.length; i++)
        {
            if(this.bag[i] == remedy)
            {
                return true;
            }
        }
        return false;
    }

    public void removeRemedy(Remedy remedy)
    {
        Remedy[] updated = new Remedy[Math.max(0, this.bag.length - 1)];
        int j = 0;
        boolean removed = false;

        for(int i = 0; i < this.bag.length; i++)
        {
            if(this.bag[i] == remedy && !removed)
            {
                removed = true;
                continue;
            }
            if(j < updated.length)
            {
                updated[j] = this.bag[i];
                j++;
            }
        }

        if(removed)
        {
            this.bag = updated;
        }
    }

    public boolean canTreat(Disease disease)
    {
        for(int i = 0; i < this.getSpeciality().length; i++)
        {
            if(this.getSpeciality()[i] == disease)
            {
                return true;
            }
        }
        return false;
    }

    public String toString()
    {
        return this.getName();
    }
}
