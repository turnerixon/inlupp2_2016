package inlupp2_2016.places;

import inlupp2_2016.Category;
import inlupp2_2016.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;


public abstract class Place extends JComponent {
    private Category category;
    private Position position;
    protected boolean utfalld;
    protected boolean markerad = false;
    protected boolean visad = false;
    private  Color myColor;
    protected int sizeX = 50;
    protected int sizeY = 50;


    final int[] xes = {0, 25, 50};
    final int[] yes = {0, 50, 0};

    protected Place(Position position, Category category) {

        this.position = position;
        this.category = category;
        setBounds(getCompensatedPosition().getX(), getCompensatedPosition().getY(), sizeX, sizeY);
        setPreferredSize(new Dimension(sizeX, sizeY));
    }


    public Category getCategory() {
        return category;
    }

    protected void visa(Graphics g) {

        g.setColor(myColor());
        g.fillPolygon(xes, yes, 3);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (visad) {
            visa(g);
            System.out.println("Det här är visad, visa(g) i Place");
        }
        if (markerad) {
            if (!utfalld)
            setBounds(getCompensatedPosition().getX(), getCompensatedPosition().getY(), sizeX+2, sizeY+2);
            g.setColor(Color.RED);
            g.drawRect(0, 0, sizeX, sizeY);
            System.out.println("Markerad från Place-klassen");
        }//End markerad()
        if (utfalld) {
            int fontSize = 12;
            setBounds(getCompensatedPosition().getX(), getCompensatedPosition().getY(), 200, 100);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(sizeX, 0, 250, 50);
            g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
            g.setColor(Color.BLACK);
            g.drawString(toString(), 50, 20);
        }//End if-utfälld

    } //End paintComponent

    public boolean getVisad() {
        return visad;
    }

    public void setVisad(boolean b) {
        visad = b;

        revalidate();
        repaint();
        System.out.println("SetVisad i Place-klassen");
    }//end setVisad

    public boolean getMarkerad() {
        return markerad;
    }

    public boolean getUtfalld() {
        return utfalld;
    }

    public Color myColor(){
        switch (getCategory()) {
            case Tåg:
                myColor = Color.GREEN;
                break;
            case Tunnelbana:
                myColor = Color.BLUE;
                break;
            case Buss:
                myColor = Color.RED;
                break;
            default:
                myColor = Color.BLACK;
                break;
        }
            return myColor;
    }


    public void setMarkerad(boolean b) {
        markerad = b;
        repaint();
        System.out.println("SetMarkerad Place här " +getName());
    }

    public void setUtfalld(boolean b) {
        utfalld = b;
        repaint();
        System.out.println("SetUtfälld från Place här!!!!");
    }

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        //Endast en av varje sorts muslyssnare är tillåten
        for(MouseListener mouseListener : this.getMouseListeners())
        {
            if(mouseListener.getClass() == l.getClass()){
                return;
            }
        }

        super.addMouseListener(l);
    }

    public Position getCompensatedPosition(){
        int compensatedX =  position.getX() -(sizeX/2);
        int compensatedY = position.getY() -sizeY;
        Position compensatedPosistion = new Position(compensatedX, compensatedY);

        return compensatedPosistion;
    }

    public abstract String getPrintableInfo();

} //End class Place
