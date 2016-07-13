package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;

import javax.swing.*;
import java.awt.*;


public abstract class Place extends JComponent {
    private String name;
    private Category category;
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

    public Place(String name, Position position, Category category) {
        this.name = name;
        this.position = position;
        this.category = category;
        setBounds(position.getX(), position.getY(), 50, 50);
        setPreferredSize(new Dimension(50, 50));
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }


    public String toString() {

        return "Namn: " + getName() + "kategori: " + getCategory();
    }


   abstract protected void markera (Graphics g);


    //Visa-metod
    abstract protected void visa(Graphics g);

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (visad) {
            visa(g);
            System.out.println("Det här är visad, visa(g) i Place");
        } else {
            System.out.println("Set visible false i Place-klassen här");
        } //End if-Visad
        if(markerad){
            markera(g);
            setBounds(getX(),getY(), 52,52);
            g.setColor(Color.RED);
            g.drawRect(0, 0 ,50, 50);
            System.out.println("Markerad från Place-klassen");
        }//End markerad()
    } //End paintComponent

    public boolean getVisad() {return visad;}

    public void setVisad(boolean b) {
        visad = b;
        repaint();
        System.out.println("Set");
    }//end setVisad

    public boolean getMarkerad (){
        return markerad;
    }


    public void setMarkerad (boolean b){
        markerad=b;
        validate();
        repaint();
        System.out.println("SetMarkerad från Place här!!!!");
    }





} //End class Place
