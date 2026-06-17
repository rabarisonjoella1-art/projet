package entity;
import accessory.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class Patient extends Human{
    Disease[] previous;
    private Disease[] diseases;
    private int[] levels;
    Disease[] congenital;
    Taken[] eaten;

    public Patient(String name, Date birthday, char sexe, Point2D.Double pos, Disease[] previous, Disease[] congenital, Disease[] diseases, int[] levels)
    {
        super(name, birthday, sexe, pos);
        this.previous = previous;
        this.congenital = congenital;
        this.diseases = diseases;
        this.levels = levels;
        
        /*
        for(int i = 0; i < this.getLevels().length; i++)
        {
            if(this.getLevels()[i] <= 0)
            {
                this.getDiseases()[i] = null;
            }
        }
        */

        this.eaten = new Taken[0];
    }

    //Getter
    public Disease[] getPrevious()
    {
        return this.previous;
    }

    public Disease[] getCongenital()
    {
        return this.congenital;
    }

    public Taken[] getEaten()
    {
        return this.eaten;
    }

    public Disease[] getDiseases()
    {
        return this.diseases;
    }

    public int[] getLevels()
    {
        return this.levels;
    }

    //Setters (no for now :()
    public void setLevels(int[] levels)
    {
        this.levels = levels;
    }

    public void eat(Remedy[] to_eat, Date taken_date)
    {
        for(int i = 0; i < to_eat.length; i++)
        {
            to_eat[i].getFinish().add_day(-(to_eat[i].getMarge()));
            if(to_eat[i].getFinish().compare(taken_date) == 1 || to_eat[i].getFinish().compare(taken_date) == 0)
            {
                Taken to_take = new Taken(to_eat[i], taken_date);

                Taken[] tmp = new Taken[1];
                if(this.eaten.length != 0)
                {
                    tmp = new Taken[this.eaten.length + 1];
                    for(int j = 0; j < this.eaten.length; j++)
                    {
                        tmp[j] = this.eaten[j];
                    }
                    tmp[this.eaten.length] = to_take;
                }
                else
                {
                    tmp = new Taken[1];
                    tmp[0] = to_take;
                }
                this.eaten = tmp;
            }
        }
    }

    public void traitement_taking(Date taken_date)
    {   
        if(this.eaten.length != 0)
        {
            int to_add = this.eaten.length;
            for(int i = 0; i < this.eaten.length; i++)
            {
                this.eaten[i].getRemedy().getFinish().add_day(this.eaten[i].getRemedy().getMarge());
                if(this.eaten[i].getRemedy().getFinish().compare(taken_date) == 1 || this.eaten[i].getRemedy().getFinish().compare(taken_date) == 0)
                {
                    to_add++;
                }
            }
            Taken[] again = new Taken[to_add];
            int b = 0;
            for(int i = 0; i < this.eaten.length; i++)
            {
                again[i] = this.eaten[i];
                b = i+1;
            }

            int f = -1;
            for(int i = b; i < again.length; i++)
            {
                f++;
                boolean already = false;
                for (int k = 0 + f; k < this.eaten.length; k++)
                {
                    if(this.eaten[k].getRemedy().getFinish().compare(taken_date) == 1 || this.eaten[i].getRemedy().getFinish().compare(taken_date) == 0)
                    {
                        already = true;
                        again[i] = this.eaten[k];
                    }

                    if(already == true)
                    {
                        break;
                    }
                    
                }
            }
            this.eaten = new Taken[again.length];
            this.eaten = again;
        }
    }

    public Disease[] current_disease()
    {
        int size = this.getLevels().length;
        for(int i = 0; i < this.getLevels().length; i++)
        {
            if(this.getLevels()[i] <= 0)
            {
                size--;
            }
        }
        Disease[] current = new Disease[size];
        for(int i = 0, j = 0; i < this.getLevels().length; i++)
        {
            if(this.getLevels()[i] > 0)
            {
                current[j] = this.getDiseases()[i];
                j++;
            }
        }
        return current;
    }

    public boolean hasActiveDisease(Disease disease)
    {
        for(int i = 0; i < this.getDiseases().length; i++)
        {
            if(this.getDiseases()[i] == disease && this.getLevels()[i] > 0)
            {
                return true;
            }
        }
        return false;
    }

    public void addDisease(Disease disease, int level)
    {
        if(disease == null)
        {
            return;
        }

        for(int i = 0; i < this.getDiseases().length; i++)
        {
            if(this.getDiseases()[i] == disease)
            {
                if(this.getLevels()[i] <= 0)
                {
                    this.getLevels()[i] = Math.max(1, level);
                }
                return;
            }
        }

        Disease[] newDiseases = new Disease[this.getDiseases().length + 1];
        int[] newLevels = new int[this.getLevels().length + 1];

        for(int i = 0; i < this.getDiseases().length; i++)
        {
            newDiseases[i] = this.getDiseases()[i];
            newLevels[i] = this.getLevels()[i];
        }

        newDiseases[newDiseases.length - 1] = disease;
        newLevels[newLevels.length - 1] = Math.max(1, level);

        this.diseases = newDiseases;
        this.levels = newLevels;
    }

    public void catchContagiousDiseasesFrom(Patient other)
    {
        Disease[] otherDiseases = other.getDiseases();
        int[] otherLevels = other.getLevels();

        for(int i = 0; i < otherDiseases.length; i++)
        {
            Disease disease = otherDiseases[i];
            if(disease != null && otherLevels[i] > 0 && disease.isContagious())
            {
                this.addDisease(disease, 1);
            }
        }
    }

    public String getDiseaseSummary()
    {
        ArrayList<String> lines = new ArrayList<String>();
        for(int i = 0; i < this.getDiseases().length; i++)
        {
            Disease disease = this.getDiseases()[i];
            if(disease != null && this.getLevels()[i] > 0)
            {
                lines.add(
                    disease.getName()
                    + " (niveau " + this.getLevels()[i]
                    + ", contagieuse: " + (disease.isContagious() ? "oui" : "non")
                    + ")"
                );
            }
        }

        if(lines.isEmpty())
        {
            return "Aucune maladie active";
        }
        return String.join(", ", lines);
    }

    public boolean applyRemedy(Remedy remedy)
    {
        boolean changed = false;
        for(int i = 0; i < remedy.getCure().length; i++)
        {
            Disease curedDisease = remedy.getCure()[i];
            for(int j = 0; j < this.getDiseases().length; j++)
            {
                if(this.getDiseases()[j] == curedDisease && this.getLevels()[j] > 0)
                {
                    this.getLevels()[j]--;
                    changed = true;
                }
            }
        }
        this.refreshLifeStatus();
        return changed;
    }

    public void refreshLifeStatus()
    {
        if(!this.isAlive())
        {
            return;
        }


        for(int i = 0; i < this.getDiseases().length; i++)
        {
            Disease disease = this.getDiseases()[i];
            if(disease != null && disease.getMortal() && this.getLevels()[i] >= 7)
            {
                this.setAlive(false);
                return;
            }
        }
    }

    public String toString()
    {
        return this.getName();
    }
}
