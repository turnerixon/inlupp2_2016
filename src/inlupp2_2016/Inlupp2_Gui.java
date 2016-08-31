package inlupp2_2016;

import inlupp2_2016.places.*;
import javafx.geometry.Pos;

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
import java.awt.event.MouseListener;


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
    Map<String, List<Place>> placesByName = new HashMap<>();
    public Map<Position, Place> placesByPosition = new HashMap<>();
    public Map<Category, List<Place>> placesByCategory = new HashMap<>();
    private boolean thingsHaveChanged = false;
    private boolean isLoadedFile = false;
    private List<Place> markedPlaces = new ArrayList<>();

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
        exitItem.addActionListener(new ExitLyssnare());
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

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new StangningsLyss());
        setSize(800, 500);
        setLocation(250, 250);
        setVisible(true);

    } // End Inlupp2_Gui class

    // Läs in Kartbilden till Panelen
    class NewMapLyss implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ave) {

            if(thingsHaveChanged==true){
                JOptionPane.showMessageDialog(null, "Du måste spara pågående projekt innan du kan ladda in en ny kartbild");
                return;
            }

            FileFilter bildFilter = new FileNameExtensionFilter("Bilder", "jpg", "png");
            jfc.setFileFilter(bildFilter);
            //jfc.addChoosableFileFilter(bildFilter);
            int svar = jfc.showOpenDialog(Inlupp2_Gui.this);
            if (svar != JFileChooser.APPROVE_OPTION)
                return;
            File fil = jfc.getSelectedFile();
            String filNamn = fil.getAbsolutePath();


            if (kartScroll!= null) {
                clearAllPlaces();
                for(ActionListener actionListener: boxen.getActionListeners()){
                    boxen.removeActionListener(actionListener);
                }
                remove(kartScroll);
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


    //Välj bland kategorier i listan.
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
                        p.setMarkerad(false);
                        p.addMouseListener(new MusAndPlaceLyss());

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
                category = Category.None;

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

            Place place;
            if (description.isEmpty()) {
                place = new NamedPlace(name, position, category);
            } else {
                place = new DescribedPlace(name, description, position, category);
            }

            addPlace(place);
            thingsHaveChanged=true;
            bp.removeMouseListener(musLyss);
            boxen.addActionListener(new PlaceLyss());
            place.addMouseListener(new MusAndPlaceLyss());
            categoryList.clearSelection();
            categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            bp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            bp.validate();
            bp.repaint();

        }

    }


    class HideLyss implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ave) {

            for (Place pos : markedPlaces) {

                pos.setVisad(false);
                pos.setMarkerad(false);

                for (MouseListener l : pos.getMouseListeners()) {
                    pos.removeMouseListener(l);
                }
            }

        } //HideLyss ActionPerfomed
    } //End HideLyss

    class RemoveLyss implements ActionListener {
        public void actionPerformed(ActionEvent ave) {

            for(Place pos : markedPlaces)
            {
                //Hämta platser från respektive datastruktur
                List<String> sammaPlaceNameList = new ArrayList<String>(placesByName.keySet());
                List<Position> sammaPositionList = new ArrayList<Position>(placesByPosition.keySet());
                List<Category> sammaKategoriList = new ArrayList<Category> (placesByCategory.keySet());

                //Gå igenom platserna per namn och ta bort dem.
                for(Place p : markedPlaces){
                    if(sammaPlaceNameList.equals(p.getName())){
                        sammaPlaceNameList.remove(p);
                        if(sammaPlaceNameList.isEmpty())
                            placesByName.remove(p.getName());
                    }
                }//End for-loop

                //Gå igenom platserna i Position och ta bort dem
                for(Place p: markedPlaces){
                    if(sammaPositionList.equals(p.getPosition())) {
                        sammaPositionList.remove(p);
                        if (sammaPositionList.isEmpty())
                            placesByPosition.remove(p.getPosition());
                    }

                }//End Position-loop

                // Gå igenom platserna i Category och ta bort dem
                for(Place p : markedPlaces){
                    if(sammaKategoriList.equals(p.getCategory())){
                        sammaKategoriList.remove(p);
                        if(sammaKategoriList.isEmpty())
                            placesByCategory.remove(p.getCategory());
                    }
                }

                // Ta bort från kartan
                bp.remove(pos);



            }

            markedPlaces.clear();
            repaint();
            thingsHaveChanged=true;
        }//End ActionEvent
    }//End RemoveLyss



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
                    if(place.getMarkerad()){
                        markedPlaces.add(place);
                    } else{
                        markedPlaces.remove(place);
                    }


                } //end getVisad()
            } else if (mev.getButton() == MouseEvent.BUTTON3) {
                place.setUtfalld(!place.getUtfalld());

            }

        }
    } //End MusAndPlaceLyss

    class SearchLyss implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ave) {
            String soktPlats = searchField.getText();
            List<Place> funnaPlatser = placesByName.get(soktPlats.toLowerCase());
            avmarkeraAlla();
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

                    for (MouseListener l : p.getMouseListeners()) {
                        p.removeMouseListener(l);
                    }

                }
            }
        } // End ActionEvent
    } // End HideCategoryLyss

    class WhatIsHereMusKnappLyss extends MouseAdapter {

        public void mouseClicked(MouseEvent mev) {
            int startX = mev.getX();
            int startY = mev.getY();
            int extraPixel = 21;

            for (Map.Entry<Position, Place> entry : placesByPosition.entrySet()) {
                if (entry.getKey().getX() >= startX - extraPixel && entry.getKey().getX() <= startX + extraPixel && entry.getKey().getY() >= startY - extraPixel && entry.getKey().getY() <= startY + extraPixel) {
                    entry.getValue().setVisad(true);
                    entry.getValue().addMouseListener(new MusAndPlaceLyss());

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
            bp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } //End ActionEvent
    }//End WhatIsHereLyss


    //Läsa in filer med platser
    class LoadFileListenerLyss implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
                if (isLoadedFile) {
                    JOptionPane.showMessageDialog(null, "Du har platser i kartan som behöver sparas innan du kan ladda in fler");
                    return;

            }


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
                //FileReader infil = new FileReader(placesNamn);
                InputStreamReader infil = new InputStreamReader(new FileInputStream(placesNamn), "Cp1252");
                BufferedReader in = new BufferedReader(infil);

                //Skriv ut ClassNamnet, Platsens kategori, X och Y koordinater, Platsens namn och eventuell beskrivning
                String line;
                while ((line = in.readLine()) != null) {

                    String[] tokens = line.split(",");
                    String type = tokens[0];
                    String category = tokens[1];
                    int x = Integer.parseInt(tokens[2]);
                    int y = Integer.parseInt(tokens[3]);
                    String name = tokens[4];

                    Position pos = new Position(x, y);
                    Category currentCategory = null;
                    Place nyPlats = null;

                    if (category.equalsIgnoreCase("Buss")) {
                        currentCategory = Category.Buss;
                    } else if (category.equalsIgnoreCase("Tunnelbana")) {
                        currentCategory = Category.Tunnelbana;
                    } else if (category.equalsIgnoreCase("Tåg")) {
                        currentCategory = Category.Tåg;
                    } else {
                        currentCategory = Category.None;
                    }

                    if (type.equalsIgnoreCase("named")) {
                        nyPlats = new NamedPlace(name, pos, currentCategory);
                    } else {
                        String despcription = tokens[5];
                        nyPlats = new DescribedPlace(name, despcription, pos, currentCategory);
                    }

                    addPlace(nyPlats);
                   isLoadedFile=true;
                    nyPlats.addMouseListener(new MusAndPlaceLyss());

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
            sparaSaker();

        }//End ActionPerformed
    }//End SparaLyss

    class ExitLyssnare implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
        exitAndSave();



        }// End actionPerformed ExitLyssnare

    }//End ExitLyssnare


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
            placesByName.put(name.toLowerCase(), sammaNamnList);
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
        //thingsHaveChanged=true; kommenterar bort denna då enbart enbart om ny plats skapas via mus borde räknas som förändring. Denna finns ju redan på fil
        System.out.println(nyPlats.getPrintableInfo());


    }//End addPlace ()

    public boolean okToExitFile() {

        return true;

    }//End okToExitFile ()

    private void avmarkeraAlla() {
        for (List<Place> placeList : placesByCategory.values()) {
            for (Place p : placeList) {
                p.setMarkerad(false);
                markedPlaces.clear();
            }
        }
    }// End avmarkeraAlla

    private void markeraPlats (Place place) {
        markedPlaces.add(place);
    } //End markeraPlats

    private void avmarkeraPlats(Place place){
        markedPlaces.remove(place);
    }// End avmarkeraPlats

    private void taBortAllaMarkeradePlatser()
    {
        placesByCategory.clear();
        placesByName.clear();
        placesByPosition.clear();
        markedPlaces.clear();

    }

    private void sparaSaker() {

        if (thingsHaveChanged==false) {
            JOptionPane.showMessageDialog(null, "Du har inget att spara. Gå välj exit eller stäng ner programmet via krysset");
            return;
        }

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
            JOptionPane.showMessageDialog(null, "Kan inte Spara filen: ");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fel : " + e.getMessage());
        }
        thingsHaveChanged = false;
        isLoadedFile = false;


        }//End sparaSaker()

    private void clearAllPlaces () {
        placesByName.clear();
        placesByPosition.clear();
        placesByCategory.clear();
        taBortAllaMarkeradePlatser();
        pack();
        validate();
        repaint();
    }//End clearAllPlaces ()

    private void exitAndSave (){
        if (thingsHaveChanged==true) {
            int svar = JOptionPane.showConfirmDialog(null, "Du har osparade platser: \nVill du avsluta utan att spara, tryck JA\n Vill du spara innan du avslutar tryck NEJ!");
            if (svar == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else if (svar == JOptionPane.NO_OPTION); {
                sparaSaker();
            }

        // thigsHaveChanged ==false
        } else {
            int svarExit = JOptionPane.showConfirmDialog(null, "Vill du avsluta programmet?", "Avsluta programmet", JOptionPane.YES_NO_OPTION);
            if (svarExit == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
            if (svarExit == JOptionPane.NO_OPTION){
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            }

        }

    }//End exitAndSave ()



//Stäng programmet via stängningsrutan
    private class StangningsLyss extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            exitAndSave();
            }
        } //end StäningsLyss

}//End Inlupp2_Gui


