import java.awt.EventQueue;
import java.awt.Image;
import java.sql.*;
import javax.swing.*;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.ScrollPane;
public class Creator {

	private JFrame frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Creator window = new Creator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	Connection connection=null;

	/**
	 * Create the application.
	 */
	public Creator() {
		initialize();
		connection=sqliteConnection.dbConnector();
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("ProFrostApp");
		frame.setBounds(100, 100, 600, 400);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("ProFrostApp");
		//frame.setIconImage(new ImageIcon(getClass().getResource("icon\\faviconprofrost.png")).getImage());
		
		
		JButton btnNowyProjekt = new JButton("Nowy projekt");
		btnNowyProjekt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String query="Delete from Wybrane";			
					PreparedStatement pst=connection.prepareStatement(query);
					pst.execute();
					pst.close();
					
					}catch(Exception e2){
						JOptionPane.showMessageDialog(null, e2);
					}
				frame.dispose();
				General general=new General();
				general.setVisible(true);
			}
		});
		btnNowyProjekt.setBounds(126, 210, 123, 50);
		frame.getContentPane().add(btnNowyProjekt);
		
		
		
		
		
		
		JButton btnKontynuuj = new JButton("Kontynuuj");
		btnKontynuuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				General general=new General();
				general.setVisible(true);
			}
		});
		btnKontynuuj.setBounds(341, 210, 123, 50);
		frame.getContentPane().add(btnKontynuuj);
		
		
		
		
		JLabel lbl = new JLabel("Czy chcesz rozpocz\u0105\u0107 nowy projekt sklepu?");
		lbl.setBounds(126, 71, 338, 97);
		lbl.setFont(new Font("Tahoma", Font.PLAIN, 17));
		frame.getContentPane().add(lbl);
		
	}
}
