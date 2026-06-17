package accessory;
import entity.Disease;

public class Remedy
{
    Date finish;
    int marge;
    String name;
    int price;
    Disease[] cure;
    
    public Remedy(Date finish, int marge, String name, int price, Disease[] cure)
    {
        this.finish = finish;
        this.marge = marge;
        this.name = name;
        this.price = price;
        this.cure = cure;
    }

    //Getter
    public Date getFinish()
    {
        return this.finish;
    }
    
    public int getMarge()
    {
        return this.marge;
    }

    public String getName()
    {
        return this.name;
    }

    public int getPrice()
    {
        return this.price;
    }

    public Disease[] getCure()
    {
        return this.cure;
    }

    public String toString()
    {
        return this.name;
    }
}
