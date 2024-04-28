/*
 * Autor: Christian Frei
 * Datum: 09.06.2004
 */
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

abstract public class TabPanel extends JPanel implements ActionListener, ListSelectionListener
{
	private GUI wnd;
	
	private String strName;
	private Font fntSchrift;
	
	//private JPanel pnlGenres;
		private JButton btExit;
		private JPanel pnlListe;
			private JList lstList;
		private JPanel pnlSuchen;
			private JButton btSearch;
		private JPanel pnlDetails;
		private JPanel pnlButtons;
			private JButton btNew, btSave, btDel;
	
	// Steuerelemente 
	abstract public void setPnlDetails();
	abstract public void setPnlSuche();
	
	/*
	 * Methoden die bei bestimmten Betätigungen von Buttons auf gerufen werden
	 * aktuallisiereDetails() wird aufgerufen, sobald sich etwas an der Liste ändert
	 * aktuallisiereListe() wird auf gerufen sobald btSuchen betätigt wurde
	 * neu() wird auf gerufen sobald btNew betätigt wurde
	 * speichern() wird auf gerufen sobald btSave betätigt wurde
	 * loesche() wird auf gerufen sobald btdelbetätigt wurde
	 */
	abstract public void aktuallisiereListe();
	abstract public void aktuallisiereDetails();
	abstract public void neu();
	abstract public void speichern();
	abstract public void loesche();	
	
	
	TabPanel(GUI wnd, String strName)
	{
		this.wnd = wnd;
		this.strName = strName;
		fntSchrift = new Font("Dialog",Font.LAYOUT_LEFT_TO_RIGHT, 11);
		setPanels();
	}
	
	private void setPanels()
	/*
	 * setPanels erstellt den Rahmen für die einzelne Register
	 */
	{
		/*
		 * Teilpanels an das Registerpanel hängen
		 */
		this.setLayout(null);
		
		btExit = new JButton("Beenden");
		btExit.setSize(219, 41);
		btExit.setLocation(335,358);
		this.add(btExit);
		btExit.addActionListener(this);

			// Panel
			pnlDetails = new JPanel();
			pnlDetails.setSize(270,200);
			pnlDetails.setLocation(285,10);
			pnlDetails.setLayout(null);
			pnlDetails.setBorder(new TitledBorder("Details"));
			this.add(pnlDetails);
	
			pnlSuchen = new JPanel();
			pnlSuchen.setSize(545,140);
			pnlSuchen.setLocation(10,210);
			pnlSuchen.setLayout(null);
			pnlSuchen.setBorder(new TitledBorder("Suchen"));
			this.add(pnlSuchen);
		
				btSearch = new JButton("Suchen");
				btSearch.setSize(100, 25);
				btSearch.setLocation(435,107);
				pnlSuchen.add(btSearch);
				btSearch.addActionListener(this);
		
			pnlListe = new JPanel();
			pnlListe.setSize(270,200);
			pnlListe.setLocation(10,10);
			pnlListe.setLayout(null);
			pnlListe.setBorder(new TitledBorder(strName));
			this.add(pnlListe);
			
				// Liste der Genres
				lstList = new JList();
				lstList.setSize(250, 170);
				lstList.setLocation(10,20);
				lstList.setBorder(new BevelBorder(1,Color.lightGray,Color.darkGray));
				pnlListe.add(lstList);
				lstList.addListSelectionListener(this);
		
			/*
			 * Teilpanels: Buttons
			 */
			pnlButtons = new JPanel();
			pnlButtons.setSize(320,50);
			pnlButtons.setLocation(10,350);
			pnlButtons.setLayout(null);
			pnlButtons.setBorder(new TitledBorder("Aktionen"));
			this.add(pnlButtons);
				
				/*
				* Steuerelemente für Buttons
				*/
				btNew = new JButton("Neu");
				btNew.setSize(100, 25);
				btNew.setLocation(10,17);
				pnlButtons.add(btNew);
				btNew.addActionListener(this);
					
				btSave = new JButton("Speichern");
				btSave.setSize(100, 25);
				btSave.setLocation(110,17);
				pnlButtons.add(btSave);
				btSave.addActionListener(this);
					
				btDel = new JButton("Löschen");
				btDel.setSize(100, 25);
				btDel.setLocation(210,17);
				pnlButtons.add(btDel);
				btDel.addActionListener(this);

	}

	public JTextField getTextfield(int x, int y)
	{
		JTextField txt = new JTextField();
		txt.setFont(fntSchrift);
		txt.setSize(195, 20);
		txt.setLocation(x,y);
		
		return txt;
	}

	/*
	 * get-Methoden
	 */
	public GUI getGUI()
	{
		return wnd;
	}
	
	public TabPanel getTabPanel()
	{
		return this;
	}
	
	public Font getSchrift()
	{
		return fntSchrift;
	}
	public JButton getBtNew()
	{
		return btNew;
	}

	public JButton getBtSave()
	{
		return btSave;
	}

	public JButton getBtDel()
	{
		return btDel;
	}

	public JButton getBtSearch()
	{
		return btSearch;
	}

	public JButton getBtExit()
	{
		return btExit;
	}

	public JList getListe()
	{
		return lstList;
	}

	public JPanel getPnlSuchen()
	{
		return pnlSuchen;
	}
	
	public JPanel getPnlDetails()
	{
		return pnlDetails;
	}
	
	public void valueChanged(ListSelectionEvent event)
	/*
	 * aktuallisiereDetails() wird aufgerufen, sobald sich etwas an der Liste ändert
	 */
	{		
		if(event.getSource() == lstList && lstList.getSelectedValue() != null)
		{
			aktuallisiereDetails();
		}
	}
	
	public void actionPerformed(ActionEvent event)
	/*
	 * aktuallisiereListe() wird auf gerufen sobald btSuchen betätigt wurde
	 * neu() wird auf gerufen sobald btNew betätigt wurde
	 * speichern() wird auf gerufen sobald btSave betätigt wurde
	 * loesche() wird auf gerufen sobald btdelbetätigt wurde
	 */
	{
		
		if(event.getSource() ==  btExit)
		{
			wnd.closeWnd();
		}
		// Suchen
		else if(event.getSource() == btSearch)
		{
			aktuallisiereListe();
		}
		// Neues Buch
		if(event.getSource() == btNew)
		{
			neu();
		}
		// Buch speicheren
		if(event.getSource() == btSave && lstList.isSelectionEmpty() == false)
		{
				speichern();
		}
		// Buch löschen
		if(event.getSource() == btDel && lstList.isSelectionEmpty() == false)
		{
			loesche();		
		}
	}
	
	/*
	 * Einzelne Dialoge
	 */
	public int optionUeberschreiben()
	{
		return JOptionPane.showConfirmDialog(wnd,"Wollen Sie den Eintrag " +
												"Überschreiben?",
												"Speichern",
												JOptionPane.YES_NO_OPTION);
	}
			
	public int optionLoeschen()
	{
		return JOptionPane.showConfirmDialog(wnd,"Wollen Sie den Eintrag löschen?",
												"Löschen",
												JOptionPane.YES_NO_OPTION);
	}
	
	public void messageErrorUnbekanntAendern()
	{
		JOptionPane.showMessageDialog(this,"Unbekannt können Sie nicht speichern oder löschen.","Fehler",JOptionPane.WARNING_MESSAGE);
	}
	
	public void messageErrorEintagExistiert()
	{
		JOptionPane.showMessageDialog(this,"Der Eintrag existiert bereits.","Fehler",JOptionPane.WARNING_MESSAGE);
	}
	
	public void messageErrorAbhaengigkeit()
	{
		JOptionPane.showMessageDialog(this,"Dieser Eintrag wird von min. einem Buch verwendet.","Fehler",JOptionPane.WARNING_MESSAGE);
	}
	
	public void messageErrorNeuerEintragExistiert()
	{
		JOptionPane.showMessageDialog(this,"Ein neuer Eintrag existiert bereits.","Fehler",JOptionPane.WARNING_MESSAGE);
	}
}