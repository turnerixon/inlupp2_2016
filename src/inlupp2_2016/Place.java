package inlupp2_2016;

import javax.swing.*;
import java.awt.*;


public abstract class Place extends JComponent {
    private String name;
    private String category;
    private Position position;
    private boolean hopfalld;
    private boolean markerad = false;
    private boolean visad = false;


    public Place(String name, Position position) {
        this.name = name;
        this.position = position;
        setBounds(position.getX(), position.getY(), 50, 50);
        setPreferredSize(new Dimension(50, 50));
    }

    public Place(String name, Position position, String category) {
        this.name = name;
        this.position = position;
        this.category = category;
        setBounds(position.getX(), position.getY(), 50, 50);
        setPreferredSize(new Dimension(50, 50));
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }


    public String toString() {

        return "Namn: " + getName() + "kategori: " + getCategory();
    }


    //abstract protected void markera (Graphics g);


    //Visa-metod
    abstract protected void visa(Graphics g);

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (visad) {
            visa(g);
        } else {
            setVisible(false);
        } //End if-Visad

    }


    public void setVisad(boolean b) {
        visad = b;
        repaint();
    }

}
