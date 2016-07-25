package inlupp2_2016;

import inlupp2_2016.places.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Inlupp2_Gui extends JFrame {

    JFileChooser jfc = new JFileChooser(".");
    BildPlan bp = null;
    private JPanel categoryDisplay;
    private JScrollPane kartScroll;
    private JScrollPane categoryScroll;
    private String[] placesVal = {"Named places", "Described places"};

    private JComboBox<String> boxen = new JComboBox<String>(placesVal);
    private JList<Category> categoryList = new JList<>(Category.getSelectableValues());

    private JTextField searchField;
    private JButton searchButton;
    private JButton hideButton;
    private JButton removeButton;
    private JButton whatButton;
    private JButton hideCatButton;
    private JMenuBar progMenuBar;
    private JMenu progMenu;
    MusLyss musLyss = new MusLyss();
    WhatIsHereMusKnappLyss whatIsHereKnappLyss = new WhatIsHereMusKnappLyss();
    private Place place;
    Map<String, List<Place>> placesByName = new HashMap<>();
    public Map<Position, Place> placesByPosition = new HashMap<>();
    public Map<Category, List<Place>> placesByCategory = new HashMap<>();


    public Inlupp2_Gui() {

        super("Inlupp 2");

		/*
         * Archive-menu
		 */

        progMenuBar = new JMenuBar();
        setJMenuBar(progMenuBar);

        progMenu = new JMenu("Archive");
        progMenuBar.add(progMenu);

        JMenuItem newMapItem = new JMenuItem("New Map");
        progMenu.add(newMapItem);
        newMapItem.addActionListener(new NewMapLyss());

        JMenuItem loadPlacesItem = new JMenuItem("Load Places");
        progMenu.add(loadPlacesItem);
        loadPlacesItem.addActionListener(new LoadFileListenerLyss());
        JMenuItem saveItem = new JMenuItem("Save");
        progMenu.add(saveItem);
        saveItem.addActionListener(new SparaLyss());
        JMenuItem exitItem = new JMenuItem("Exit");
        progMenu.add(exitItem);

        // End ArchiveMenu

		/*
         * Panelen med knappar
		 */
        JPanel vanster = new JPanel();

        vanster.setLayout(new FlowLayout());
        add(vanster, BorderLayout.NORTH);
        JLabel newLabel = new JLabel("New: ");
        vanster.add(newLabel);
        vanster.add(boxen);

        searchField = new JTextField("Search");
        vanster.add(searchField);
        searchField.addMouseListener(new SearchFieldMusLyss());
        searchButton = new JButton("Search");
        vanster.add(searchButton);
        searchButton.addActionListener(new SearchLyss());
        hideButton = new JButton("Hide");
        vanster.add(hideButton);
        hideButton.addActionListener(new HideLyss());
        removeButton = new JButton("Remove");
        removeButton.addActionListener(new RemoveLyss());
        vanster.add(removeButton);
        whatButton = new JButton("What is here?");
        vanster.add(whatButton);
        whatButton.addActionListener(new WhatIsHereLyss());
        // Knapp-panelen slutar här

		/*
         * Categories-delen
		 */
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JLabel catLabel = new JLabel("Categories");
        right.add(catLabel);
        // End -Label

        // CategoryList-area
        categoryDisplay = new JPanel();
        categoryScroll = new JScrollPane(categoryDisplay);

        // CategoryButton - Buss, tunnuelbana och Tåg
        categoryDisplay.add(categoryList);
        right.add(categoryScroll);
        categoryList.addListSelectionListener(new ListLyss());
        categoryDisplay.setVisible(true);
        categoryList.setFixedCellWidth(250);

		/* Hide-knappen */
        JButton hideCatButton = new JButton("Hide category");
        right.add(hideCatButton);
        hideCatButton.addActionListener(new HideCategoryLyss());
        add(right, BorderLayout.EAST);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocation(250, 250);
        setVisible(true);

    } // End Inlupp2_Gui class

    // Läs in Kartbilden till Panelen
    class NewMapLyss implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ave) {

            FileFilter bildFilter = new FileNameExtensionFilter("Bilder", "jpg", "png");
            jfc.setFileFilter(bildFilter);

            int svar = jfc.showOpenDialog(Inlupp2_Gui.this);
            if (svar != JFileChooser.APPROVE_OPTION)
                return;
            File fil = jfc.getSelectedFile();
            String filNamn = fil.getAbsolutePath();

            if (bp != null) {
                remove(bp);
            }
            bp = new BildPlan(filNamn);

            kartScroll = new JScrollPane(bp);

            add(kartScroll);
            //PlaceLyss läggs här för att se till att användaren väljer en karta före den börja skapa platser.
            boxen.addActionListener(new PlaceLyss());
            pack();
            validate();
            repaint();
        }

    } // End NewMapLyss

	/*

	 * Kartan
	 */

    class BildPlan extends JPanel {
        private ImageIcon bild;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bild.getImage(), 0, 0, getWidth(), getHeight(), this);


        }

        public BildPlan(String filNamn) {
            bild = new ImageIcon(filNamn);

            int w = bild.getIconWidth();
            int h = bild.getIconHeight();
            setPreferredSize(new Dimension(w, h));
            setResizable(false);
            setLayout(null);

        } // End constructor BildPlan
    } // End class BildPlan


    // KnappLyss för places
    class PlaceLyss implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ave) {
            //Named place
            if (boxen.getSelectedIndex() == 0) {
                bp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                bp.addMouseListener(musLyss);
                boxen.removeActionListener(this);
                //Described place
            } else {
                bp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                bp.addMouseListener(musLyss);
                boxen.removeActionListener(this);

            } // End If-sats
        }// End ActionPerformed
    } // End PlaceLyss-knappen


    //Välj bland kategorier i listan och markera och visa de som är skapade.
    class ListLyss implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lev) {

            //public void mouseClicked(MouseEvent ave) {
            Category mycategory = categoryList.getSelectedValue();
            List<Place> platserPerKategori = placesByCategory.get(mycategory);

            avmarkeraAlla();
            if (platserPerKategori != null) {
                for (List<Place> placeList : placesByCategory.values()) {
                    if (mycategory != null || !platserPerKategori.isEmpty()) ;
                    {
                        for (Place p : placeList) {
                            p.setMarkerad(false);
                        }
                    }

                    for (Place p : platserPerKategori) {
                        p.setVisad(true);
                        System.out.println("Nu trycker jag på HideCategoryLyss");
                    }
                }
            }

        }//End ValueChanged
    } // End Listlyss

    class MusLyss extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mev) {
            int x = mev.getX();
            int y = mev.getY();
            String name = "";
            String description = "";

            Position position = new Position(x, y);
            Category category = categoryList.getSelectedValue();
            if (categoryList.isSelectionEmpty())
                category = Category.Undefined;

            if (boxen.getSelectedItem().equals("Named places")) {
                try {

                    NamedPlaceForm namedPlacesForm = new NamedPlaceForm();
                    int svar = JOptionPane.showConfirmDialog(Inlupp2_Gui.this, namedPlacesForm, "Ny Plats",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (svar != JOptionPane.OK_OPTION) {
                        return;
                    }

                    name = namedPlacesForm.getName();
                    if (name.equals("")) {
                        JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Inget namn");
                        return;
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Fel inmatning");

                }

            } else {
                try {

                    DescribedPlaceForm describedPlaceForm = new DescribedPlaceForm();
                    int svar = JOptionPane.showConfirmDialog(Inlupp2_Gui.this, describedPlaceForm, "Ny Plats",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (svar != JOptionPane.OK_OPTION) {
                        return;
                    }

                    name = describedPlaceForm.getName();
                    description = describedPlaceForm.getDescription();
                    if (name.equals("") || description.equals("")) {
                        JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Något av namn eller beskrivning saknades vid skapandet av plats. Skapa om platsen!");
                        return;
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Fel inmatning");

                }
            } // End try-catch


            if (description.isEmpty()) {
                place = new NamedPlace(name, position, category);
            } else {
                place = new DescribedPlace(name, description, position, category);
            }

            addPlace(place);
            bp.removeMouseListener(musLyss);
            boxen.addActionListener(new PlaceLyss());
            place.addMouseListener(new MusAndPlaceLyss());
            categoryList.clearSelection();
            categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            bp.validate();
            bp.repaint();
            System.out.println("Klickad");
        }

    }

    //Används för att markera och avmarkera platser
    class MusAndPlaceLyss extends MouseAdapter {

        public void mouseClicked(MouseEvent mev) {
            Place place;

            if (mev.getSource() instanceof DescribedPlace) {
                place = (DescribedPlace) mev.getSource();
            } else {
                place = (Place) mev.getSource();
            }

            // Klick på Vänsterknappen
            if (mev.getButton() == MouseEvent.BUTTON1) {
                if (place.getVisad()) {
                    place.setMarkerad(!place.getMarkerad());
                    System.out.println("Klickar på MusAndPlaceLyss knapp 1" + place.getName() + " " + " x: " + place.getX() + "och " + "y: " + place.getY());
                    //Klick på Högerknappen
                }
            } else if (mev.getButton() == MouseEvent.BUTTON3) {
                  place.setUtfalld(!place.getUtfalld());
                System.out.println("Klickar på MusPlaceLyss knapp 2");

            }

        }
    } //End MusAndPlaceLyss


    class HideLyss implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ave) {

            for (Place pos : placesByPosition.values()) {
                if (pos.getMarkerad()) {
                    pos.setVisad(false);
                    pos.setMarkerad(false);

                    for(MouseListener l : pos.getMouseListeners()){
                        pos.removeMouseListener(l);
                    }
                }
                System.out.println(pos + " HideLyss här!!");
            }

        } //HideLyss ActionPerfomed
    } //End HideLyss

    class RemoveLyss implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            for (Iterator<Map.Entry<Position, Place>> iter = placesByPosition.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<Position, Place> entry = iter.next();
                Place place = entry.getValue();
                if (place.getMarkerad()) {
                    bp.remove(place);
                    iter.remove();

                    List<Place> sammaNamnList = placesByName.get(place.getName());
                    sammaNamnList.remove(place);
                    if (sammaNamnList.isEmpty())
                        placesByName.remove(place.getName());
                    repaint();
                }
            }
        }//End ActionEvent
    }//End RemoveLyss


    class SearchLyss implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ave) {
            String soktPlats = searchField.getText();
            List<Place> funnaPlatser = placesByName.get(soktPlats);
            if (funnaPlatser == null || funnaPlatser.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Det namnet finns inte. Vänligen sök på annat namn, med minst ett tecken! ");
                return;
            }//End-if
            else {
                for (Place p : funnaPlatser) {
                    p.setVisad(true);
                    p.addMouseListener(new MusAndPlaceLyss());
                    if (!p.getMarkerad()) {
                        p.setMarkerad(true);
                    } else p.setMarkerad(false);
                }
            }
        }// End ActionEvent
    }//End SearchLyss

    class SearchFieldMusLyss extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mev) {
            searchField.setText("");
        }//End MouseEvent
    }//End SearchfieldLyss

    //Göm platser från vald kategori.
    class HideCategoryLyss implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ave) {
            Category mycategory = categoryList.getSelectedValue();
            List<Place> platserPerKategori = placesByCategory.get(mycategory);
            if (platserPerKategori == null) {
                JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Det finns ingen plats av den typen att gömma!");
            } else {
                for (Place p : platserPerKategori) {
                    p.setVisad(false);
                    p.setMarkerad(false);

                    for(MouseListener l : p.getMouseListeners()){
                        p.removeMouseListener(l);
                    }

                }
            }
        } // End ActionEvent
    } // End HideCategoryLyss

    class WhatIsHereMusKnappLyss extends MouseAdapter {
        private int startX, startY;

        @Override
        public void mousePressed(MouseEvent mev) {
            startX = mev.getX();
            startY = mev.getY();
        }

        public void mouseClicked(MouseEvent mev) {
            int musX = mev.getX();
            int musY = mev.getY();
            int extraPixel = 21;

            for (Map.Entry<Position, Place> entry : placesByPosition.entrySet()) {
                if (entry.getKey().getX() >= startX - extraPixel && entry.getKey().getX() <= startX + extraPixel && entry.getKey().getY() >= startY - extraPixel && entry.getKey().getY() <= startY + extraPixel)
                {
                    entry.getValue().setVisad(true);
                    entry.getValue().addMouseListener(new MusAndPlaceLyss());
                    System.out.println("Träffat");
                }
            }

            bp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            bp.removeMouseListener(this);

        } //End MouseEvent
    }//End WhatIsHereMusKnapp


    class WhatIsHereLyss implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ave) {
            bp.addMouseListener(whatIsHereKnappLyss);
            bp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } //End ActionEvent
    }//End WhatIsHereLyss


    //Läsa in filer med platser
    class LoadFileListenerLyss implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            //Dialog för filöppning
            FileFilter mittFileFilter = new FileNameExtensionFilter("places", "Places");
            jfc.setFileFilter(mittFileFilter);
            int svar = jfc.showOpenDialog(Inlupp2_Gui.this);
            if (svar != JFileChooser.APPROVE_OPTION)
                return;
            File fil = jfc.getSelectedFile();

            //Inläsning av filen
            String placesNamn = fil.getAbsolutePath();

            try {
                FileReader infil = new FileReader(placesNamn);
                BufferedReader in = new BufferedReader(infil);

                //Skriv ut ClassNamnet, Platsens kategori, X och Y koordinater, Platsens namn och eventuell beskrivning
                String line;
                while ((line = in.readLine()) != null) {

                    String[] tokens = line.split(",");

                    if (tokens[0].equalsIgnoreCase("named")) {

                        String category = tokens[1];
                        int x = Integer.parseInt(tokens[2]);
                        int y = Integer.parseInt(tokens[3]);
                        String name = tokens[4];

                        Position pos = new Position(x, y);
                        Category currentCategory = null;

                        if (category.equalsIgnoreCase("Buss")) {
                            currentCategory = Category.Buss;
                        } else if (category.equalsIgnoreCase("Tunnelbana")) {
                            currentCategory = Category.Tunnelbana;
                        } else if (category.equalsIgnoreCase("Tåg")) {
                            currentCategory = Category.Tåg;
                        } else {
                            currentCategory = Category.Undefined;
                        }

                        Place nyPlats = new NamedPlace(name, pos, currentCategory);

                        addPlace(nyPlats);
                        nyPlats.addMouseListener(new MusAndPlaceLyss());
                    } //End if tokens=Named

                    else {
                        String type = tokens[0];
                        String category = tokens[1];
                        int x = Integer.parseInt(tokens[2]);
                        int y = Integer.parseInt(tokens[3]);
                        String name = tokens[4];
                        String despcription = tokens[5];


                        Position pos = new Position(x, y);
                        Category currentCategory = null;

                        if (category.equalsIgnoreCase("Buss")) {
                            currentCategory = Category.Buss;
                        } else if (category.equalsIgnoreCase("Tunnelbana")) {
                            currentCategory = Category.Tunnelbana;
                        } else if (category.equalsIgnoreCase("Tåg")) {
                            currentCategory = Category.Tåg;
                        } else {
                            currentCategory = Category.Undefined;
                        }

                        Place nyPlats = new DescribedPlace(name, despcription, pos, currentCategory);

                        addPlace(nyPlats);
                        nyPlats.addMouseListener(new MusAndPlaceLyss());
                    }//Tokens = Described

                    //System.out.print(line);
                } //End While-loop

                in.close();
                infil.close();

            } catch (FileNotFoundException e) {
                System.err.println("Kan inte öppna " + placesNamn);
            } catch (IOException e) {
                System.err.println("Fel: " + e.getMessage());
            }
            bp.repaint();
        }// End ActionEvent
    }//End LoadFileListener class

    class SparaLyss implements ActionListener {
        public void actionPerformed(ActionEvent ave) {

            FileFilter bildFilter = new FileNameExtensionFilter("places", "Places");
            jfc.setFileFilter(bildFilter);

            int svar = jfc.showSaveDialog(Inlupp2_Gui.this);
            if (svar != JFileChooser.APPROVE_OPTION)
                return;
            File fil = jfc.getSelectedFile();
            String filNamn = fil.getAbsolutePath();

            try {
                FileWriter utfil = new FileWriter(filNamn);
                PrintWriter out = new PrintWriter(utfil);
                for (Place p : placesByPosition.values()) {
                    out.println(p.getPrintableInfo());
                    //Skriv ut ClassNamnet, Platsens kategori, X och Y koordinater, Platsens namn och eventuell beskrivning
                    System.out.println(p.getPrintableInfo());
                }

                out.close();
                utfil.close();

            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Kan inte öppna filen: ");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Fel : " + e.getMessage());
            }
        }//End ActionPerformed
    }//End SparaLyss


    public static void main(String[] arg) {
        new Inlupp2_Gui();

    } //End main

    private void addPlace(Place nyPlats) {


        String name = nyPlats.getName();
        Category category = nyPlats.getCategory();
        int positionX = nyPlats.getX();
        int positionY = nyPlats.getY();
        Position position = new Position(positionX, positionY);
        placesByPosition.put(position, nyPlats);


        //Möjliggöra att söka fram platser via namn
        List<Place> sammaNamnList = placesByName.get(name);
        if (sammaNamnList == null) {
            sammaNamnList = new ArrayList<>();
            placesByName.put(name, sammaNamnList);
          } //End sammaNamnList
        sammaNamnList.add(nyPlats);

        //Möjliggöra att söka fram platser via category
        List<Place> sammaKategoriList = placesByCategory.get(category);
        if (sammaKategoriList == null) {
            sammaKategoriList = new ArrayList<>();
            placesByCategory.put(category, sammaKategoriList);
        } //End sammaKategoriList
        sammaKategoriList.add(nyPlats);

        nyPlats.setVisad(true);
        bp.add(nyPlats);
        System.out.println(nyPlats.getPrintableInfo());


    }//End addPlace ()


    private void avmarkeraAlla(){
        for (List<Place> placeList : placesByCategory.values()){
            for(Place p : placeList)
            {
                p.setMarkerad(false);
            }
        }
    }
}


