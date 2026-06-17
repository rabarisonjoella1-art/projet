package fenetre;

import java.awt.Color;

// Centralise les couleurs de l'interface pour eviter de les disperser dans plusieurs classes.
public final class PlanPalette {
    public static final Color BACKGROUND = new Color(245, 248, 252);
    public static final Color GRID = new Color(224, 230, 237);
    public static final Color CHAMBER_FILL = new Color(211, 229, 255);
    public static final Color CHAMBER_BORDER = new Color(68, 110, 165);
    public static final Color DOCTOR = new Color(255, 255, 255);
    public static final Color PATIENT = new Color(217, 76, 71);
    public static final Color VISITOR = new Color(76, 175, 80);
    public static final Color DEAD = new Color(0, 0, 0);
    public static final Color TEXT = new Color(35, 40, 48);

    private PlanPalette() {
    }
}
