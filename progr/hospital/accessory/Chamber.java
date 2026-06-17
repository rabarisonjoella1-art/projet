package accessory;
import java.awt.geom.Rectangle2D;

public class Chamber
{
    private int id;
    private Rectangle2D.Double shape;

    public Chamber(int id, Rectangle2D.Double shape)
    {
        this.setId(id);
        this.setShape(shape);
    }

    //Getters
    public int getId()
    {
        return this.id;
    }

    public Rectangle2D.Double getShape()
    {
        return this.shape;
    }

    //Setters
    public void setId(int id)
    {
        this.id = id;
    }

    public void setShape(Rectangle2D.Double shape)
    {
        this.shape = shape;
    }

    //Usable Fonctions
    public boolean contient(double x, double y)
    {
        if((x > this.getShape().getX() && x < (this.getShape().getX() + this.getShape().getWidth())))
        {
            if((y > this.getShape().getY() && y < (this.getShape().getY() + this.getShape().getHeight())))
            {
                return true;
            }
        }
        return false;
    }
}