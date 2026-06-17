package accessory;

public class Taken
{
    Remedy remedy;
    Date date_take;

    public Taken(Remedy remedy, Date date_take)
    {
        this.remedy = remedy;
        this.date_take = date_take;
    }
    
    //Getters

    public Remedy getRemedy()
    {
        return this.remedy;
    }

    public Date getDate_take()
    {
        return this.date_take;
    }
}