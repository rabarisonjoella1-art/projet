package entity;
import accessory.*;
import java.awt.geom.*;

public class Human
{
    public static final double DEFAULT_RADIUS = 0.35;

    private String name;
    private Date birthday;
    private char sexe;
    private Point2D.Double pos;
    private boolean alive;

    public Human(String name, Date birthday, char sexe, Point2D.Double pos)
    {
        this.setName(name);
        this.setBirthday(birthday);
        this.setSexe(sexe);
        this.setPos(pos);
        this.alive = true;
    }

    //Getters
    public String getName()
    {
        return this.name;
    }

    public Date getBirthday()
    {
        return this.birthday;
    }

    public char getSexe()
    {
        return this.sexe;
    }

    public Point2D.Double getPos()
    {
        return this.pos;
    }

    public boolean isAlive()
    {
        return this.alive;
    }

    public double getRadius()
    {
        return DEFAULT_RADIUS;
    }
 
    //Setters
    public void setName(String name)
    {
        this.name = name;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public void setSexe(char sexe)
    {
        if(sexe != 'm' || sexe != 'f')
        {
            this.sexe = 'u';
        }
        this.sexe = sexe;
    }

    public void setPos(Point2D.Double pos)
    {
        this.pos = pos;
    }

    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }

    public void move(double vx, double vy)
    {
        Point2D.Double new_pos = new Point2D.Double(this.getPos().getX() + vx, this.getPos().getY() + vy);
        this.setPos(new_pos);
    }

    public void moveTo(double x, double y)
    {
        this.setPos(new Point2D.Double(x, y));
    }

    public double distanceTo(Human other)
    {
        return this.getPos().distance(other.getPos());
    }

    public boolean touches(Human other)
    {
        return this.distanceTo(other) <= (this.getRadius() + other.getRadius());
    }
}
