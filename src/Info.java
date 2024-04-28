import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Info extends JDialog implements ActionListener
{
	private GUI wnd;
	private JButton btOK;
	private JLabel lblTitel;
	private JLabel lblInfo;
	private JLabel lblErstellt;
	private JLabel lblErsteller;
	
	Info(GUI wnd)
	{
		super(wnd,"Info",true);
		this.wnd = wnd;
		
		Point pntWnd = wnd.getLocationOnScreen();
		
		this.setSize(200,200);
		this.setLocation((int)pntWnd.getX()+186, (int)pntWnd.getY()+145);
		this.getContentPane().setLayout(null);

		lblTitel = new JLabel("Bücherverwaltung");
		lblTitel.setSize(200, 20);
		lblTitel.setHorizontalAlignment(JLabel.CENTER);
		lblTitel.setLocation(0,15);
		this.getContentPane().add(lblTitel);
		
		lblInfo = new JLabel("Workshop Resultat");
		lblInfo.setSize(200, 20);
		lblInfo.setHorizontalAlignment(JLabel.CENTER);
		lblInfo.setLocation(0,40);
		this.getContentPane().add(lblInfo);
		
		lblErstellt = new JLabel("erstellt von");
		lblErstellt.setSize(200, 20);
		lblErstellt.setHorizontalAlignment(JLabel.CENTER);
		lblErstellt.setLocation(0,65);
		this.getContentPane().add(lblErstellt);
		
		lblErsteller = new JLabel("von Christian Frei");
		lblErsteller.setSize(200, 20);
		lblErsteller.setHorizontalAlignment(JLabel.CENTER);
		lblErsteller.setLocation(0,90);
		this.getContentPane().add(lblErsteller);

		btOK = new JButton("OK");
		btOK.setSize(100, 25);
		btOK.setLocation(50,135);
		this.getContentPane().add(btOK);
		btOK.addActionListener(this);

		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == btOK)
		{
			this.dispose();
		}
		
	}
}
