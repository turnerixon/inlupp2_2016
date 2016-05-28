package inlupp2_2016;

import javafx.geometry.Pos;

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
import java.util.*;
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
	//private boolean markerad = false;
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
		hideButton.addActionListener(new HideLyss());
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
				boxen.removeActionListener(this);
				//Described place
			} else {
				bp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				bp.addMouseListener(musLyss);
				boxen.removeActionListener(this);

			} // End If-sats
		}// End ActionPerformed
	} // End PlaceLyss-knappen


	//Osäker på vad jag ska göra här just nu
	class ListLyss implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lev) {

		}
	}


	class BussPlace extends Place {
		final int[] xes = {0, 25, 50};
		final int[] yes = {0, 50, 0};

		public BussPlace(String name, Position position, String category) {
			super(name, position, "Buss");
		}

		protected void visa(Graphics g) {
			g.setColor(Color.RED);
			g.fillPolygon(xes, yes, 3);
		} //End visa)()

		protected void markera (Graphics g){

		}

	} //End BussPlace


	class TunnelbanaPlace extends Place {
		final int[] xes = {0, 25, 50};
		final int[] yes = {0, 50, 0};

		public TunnelbanaPlace(String name, Position position, String category) {
			super(name, position, "Tunnelbana");
		}

		protected void visa(Graphics g) {
			g.setColor(Color.BLUE);
			g.fillPolygon(xes, yes, 3);
		}

		protected void markera (Graphics g){ }

	} //End TunnelbanaPlace

	class TrainPlace extends Place {
		final int[] xes = {0, 25, 50};
		final int[] yes = {0, 50, 0};

		public TrainPlace(String name, Position position, String category) {super(name, position, "Tåg"); }

		protected void visa(Graphics g) {
			g.setColor(Color.GREEN);
			g.fillPolygon(xes, yes, 3);
		}

		protected void markera (Graphics g){ }

	} //End Class TrainPlace

	class NonePlace extends Place {
		final int[] xes = {0, 25, 50};
		final int[] yes = {0, 50, 0};

		public NonePlace(String name, Position position) {super(name, position, "None"); }

		protected void visa(Graphics g) {
			g.setColor(Color.BLACK);
			g.fillPolygon(xes, yes, 3);

		}

		protected void markera (Graphics g){
			System.out.println("NonePlaces markera-metod");
		}

	} //End Class NonePlace









	class MusLyss extends MouseAdapter {
	@Override
	public void mouseClicked(MouseEvent mev) {
		int x = mev.getX();
		int y = mev.getY();
		Position position = new Position(x, y);
		String category = categoryList.getSelectedValue();
			if(categoryList.isSelectionEmpty())
			category = "None";


		if (category.equals("Buss")) {
			place = new BussPlace(getName(), position, category);
			place.setVisad(true);
		} else if (category.equals("Tunnelbana")) {
			place = new TunnelbanaPlace(getName(), position, category);
			place.setVisad(true);
		} else if (category.equals("Tåg")) {
			place = new TrainPlace(getName(), position, category);
			place.setVisad(true);
		}

		else {
			place = new NonePlace(getName(), position);
			place.setVisad(true);
		}



		System.out.println("Rad 299 Kategori är: " +category);

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

		bp.add(place);
		bp.removeMouseListener(musLyss);
		boxen.addActionListener(new PlaceLyss());
		place.addMouseListener(new MusAndPlaceLyss());
		categoryList.clearSelection();
		bp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		bp.validate();
		bp.repaint();
		System.out.println(allMyPlaces.toString());
		System.out.println("Klickad");
	}

} // End MusLyss

	class MusAndPlaceLyss extends MouseAdapter {
		private boolean markerad;

		public void mouseClicked (MouseEvent mev){
			Place place = (Place)mev.getSource();
			markerad=!markerad;
			place.setMarkerad(markerad);

			if(mev.getButton()==MouseEvent.BUTTON1)

				System.out.println("Klickar på MusAndPlaceLyss"  + " x: " +place.getX() + "och " +"y: "+place.getY() );

		}
	} //End MusAndPlaceLyss

	class HideLyss implements ActionListener{
       //Det här funkar inte
		List<Place> minaPlatser = new ArrayList<>(allMyPlaces);
		@Override
		public void actionPerformed(ActionEvent ave) {

			for(Place p : minaPlatser) {
				//Måste göra någon form av loop här, tror jag
				p.getMarkerad();
				p.setMarkerad(false);
				p.setVisad(false);
			}

		}

	} //End HideLyss


	public static void main(String[] arg) {
		new Inlupp2_Gui();

	} //End main
}


