package inlupp2_2016;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	private String[] categoryListItem = {"Buss", "Tunnelbana", "Tåg"};

	private JComboBox<String> boxen = new JComboBox<String>(placesVal);
	private JList<String> categoryList = new JList<String>(categoryListItem);

	private JTextField searchField;
	private JButton searchButton;
	private JButton hideButton;
	private JButton removeButton;
	private JButton whatButton;
	private JButton hideCatButton;
	private JMenuBar progMenuBar;
	private JMenu progMenu;
	MusLyss musLyss = new MusLyss();
	private Place place;
	private HashMap<Place, Position> allMyPlaces = new HashMap<>();


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
		JMenuItem saveItem = new JMenuItem("Save");
		progMenu.add(saveItem);
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
		searchButton = new JButton("Search");
		vanster.add(searchButton);
		hideButton = new JButton("Hide");
		vanster.add(hideButton);
		removeButton = new JButton("Remove");
		vanster.add(removeButton);
		whatButton = new JButton("What is here?");
		vanster.add(whatButton);
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
		categoryDisplay.setVisible(true);
		categoryList.setFixedCellWidth(250);

		/* Hide-knappen */
		JButton hideCatButton = new JButton("Hide category");
		right.add(hideCatButton);
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
				//Described place
			} else {
				bp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				bp.addMouseListener(musLyss);

			} // End If-sats
		}// End ActionPerformed
	} // End PlaceLyss-knappen


	//Osäker på vad jag ska göra här just nu
	class ListLyss implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lev) {

		}
	}


	//Hjälper till att skapa trianglarna
	class DrawPanel extends JPanel{
		protected void paintComponent(Graphics g){
			g.setColor(Color.RED);
			g.fillRect(0,0,50,50);
		}
	}




	//Utritning av trianglarna som markerar platser
	class Triangel extends DrawPanel {

		public Triangel(int x, int y) {

			setBounds(x, y, 50, 50);
			setPreferredSize(new Dimension(50, 50));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
		} //End paintComponent
	}// End Triangel


	class MusLyss extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {
			int x = mev.getX();
			int y = mev.getY();
			Position position = new Position(x, y);
			String category = categoryList.getSelectedValue();

			Triangel triangel = new Triangel(x, y);

			if (boxen.getSelectedItem().equals("Named places")) {
				try {

					NamedPlaceForm namedPlacesForm = new NamedPlaceForm();
					int svar = JOptionPane.showConfirmDialog(Inlupp2_Gui.this, namedPlacesForm, "Ny Plats",
							JOptionPane.OK_CANCEL_OPTION);
					if (svar != JOptionPane.OK_OPTION) {
						return;
					}

					String name = namedPlacesForm.getName();
					if (name.equals("")) {
						JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Inget namn");
						return;
					}

					NamedPlace namedPlace = new NamedPlace(name, position, category);
					allMyPlaces.put(namedPlace, position);

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

					String name = describedPlaceForm.getName();
					String description = describedPlaceForm.getDescription();
					if (name.equals("")) {
						JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Inget namn");
						return;
					}
					if (description.equals("")) {
						JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Beskrivning saknas");
						return;
					}

					DescribedPlace describedPlace = new DescribedPlace(name, position, description, category);
					allMyPlaces.put(describedPlace, position);

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(Inlupp2_Gui.this, "Fel inmatning");

				}
			} // End try-catch

			bp.add(triangel);
			bp.removeMouseListener(musLyss);
			bp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			bp.validate();
			bp.repaint();
			System.out.println(allMyPlaces.toString());

				/*for(Map.Entry<Place, Position> me : allMyPlaces.entrySet()){
					System.out.println(me.getKey() + " : " + me.getValue());
				} */
			System.out.println("Klickad");
		}

		//Hur funkar det här då?


	} // End MusLyss


	public static void main(String[] arg) {
		new Inlupp2_Gui();

	} //End main
}


