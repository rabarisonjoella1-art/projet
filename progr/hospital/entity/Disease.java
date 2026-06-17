package entity;

public class Disease{
    private int id;
    private String name;
    private String transmission;
    private Boolean mortal;
    private boolean contagious;

    public Disease(String name, String transmission, Boolean mortal, boolean contagious, int id)
    {
        this.name = name;
        this.transmission = transmission;
        this.mortal = mortal;
        this.contagious = contagious;
        this.id = id;
    }

    public Disease(String name, String transmission, Boolean mortal, int id)
    {
        this(name, transmission, mortal, false, id);
    }

    //getters
    public String getName()
    {
        return this.name;
    }
    
    public String getTransmission()
    {
        return this.transmission;
    }
    
    public boolean getMortal()
    {
        return this.mortal;
    }

    public boolean isContagious()
    {
        return this.contagious;
    }

    public int getId()
    {
        return this.id;
    }

    //setters
}
