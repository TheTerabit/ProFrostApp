import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class AddItem extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldPrice;


	Connection connection=null;
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddItem frame = new AddItem();
					frame.setVisible(true);
					frame.setTitle("ProFrostApp");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}


	public  AddItem() {
		

		setBounds(100, 100, 424, 273);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton btnDodajTenElement = new JButton("Dodaj ten element");
		btnDodajTenElement.setBounds(218, 143, 154, 36);
		contentPane.add(btnDodajTenElement);
		
		connection=sqliteConnection.dbConnector();
		
		
		
		
		btnDodajTenElement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//Znajdz jakie id nadac nowemu elementowi
					String query="select Id from Elementy Order By Id desc";
					PreparedStatement pst=connection.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					int id=rs.getInt(1);
					rs.close();
					
					//Dodawanie Elementu
					query="insert into Elementy (Nazwa,Cena,Id) values  (?,?,?)";
					pst=connection.prepareStatement(query);
					pst.setString(1, textFieldName.getText());
					pst.setString(2, textFieldPrice.getText());
					pst.setString(3, Integer.toString(id+1));
					pst.execute();
					
					pst.close();
					AddItem.this.dispose();
					
				}catch(Exception e1) {
					JOptionPane.showMessageDialog(null, "Przedmiot o takiej nazwie ju¿ jest w bazie");
				}
			}
		});
		
		
		
		
		
		
		JLabel lblNazwa = new JLabel("Nazwa");
		lblNazwa.setBounds(12, 13, 56, 16);
		contentPane.add(lblNazwa);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(12, 42, 360, 22);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblCena = new JLabel("Cena");
		lblCena.setBounds(12, 121, 56, 16);
		contentPane.add(lblCena);
		
		textFieldPrice = new JTextField();
		textFieldPrice.setBounds(12, 150, 116, 22);
		contentPane.add(textFieldPrice);
		textFieldPrice.setColumns(10);
	}
}
