package entity;

import accessory.Date;
import java.awt.geom.Point2D;

public class Visitor extends Human {
    public Visitor(String name, Date birthday, char sexe, Point2D.Double pos) {
        super(name, birthday, sexe, pos);
    }
}
