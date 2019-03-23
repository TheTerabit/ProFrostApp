import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditDataBase extends JFrame{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	 Connection connection=null;
	private JTable table;
	private JTextField textFieldSearch;
	private JTextField textFieldName;
	private JTextField textFieldPrice;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditDataBase frame = new EditDataBase();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setTitle("ProFrostApp - Edytor bazy danych");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	//odswiezanie tabeli
	public void refreshTable(int x)
	{
		try {
			String query;
			if(x==1)
				query="select Nazwa,Cena from Elementy";
			else
				query="select Nazwa,Cena from Przedmioty";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			table.getColumnModel().getColumn(0).setPreferredWidth(600);
			table.getColumnModel().getColumn(1).setPreferredWidth(50);
			pst.close();
			rs.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}

	
	
	//zaokraglanie liczb
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	
	
	public EditDataBase() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connection=sqliteConnection.dbConnector();
		setBounds(100, 100, 1366, 786);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 49, 805, 545);
		contentPane.add(scrollPane);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 607, 805, 119);
		contentPane.add(scrollPane_1);
		
		JTextPane textPane = new JTextPane();
		scrollPane_1.setViewportView(textPane);
		textPane.setEditable(false);
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 17));
		
		JLabel lblPrice = new JLabel("");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPrice.setBounds(829, 177, 257, 23);
		contentPane.add(lblPrice);
		
		
		
		//przycisk do zmieniania ceny - dziala tylko do elementow
		JButton btnChPrice = new JButton("Zmie\u0144 cen\u0119");
		btnChPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int row=table.getSelectedRow();
					String nazwa=(table.getModel().getValueAt(row, 0).toString());					
					String query="select Id, Cena from Elementy where Nazwa=?";
					PreparedStatement pst;
					pst = connection.prepareStatement(query);
					pst.setString(1, nazwa);
					ResultSet rs=pst.executeQuery();
					
					
					int updateID = rs.getInt(1);
					double currentPrice = rs.getDouble(2);
					double newPrice = Double.parseDouble(textFieldPrice.getText());	
					double diff= newPrice-currentPrice;
					
					
					//aktualizacja tabeli Elementy
					query="update Elementy set Cena=Cena+? where Id=?";
					pst=connection.prepareStatement(query);
					pst.setString(1, Double.toString(diff));
					pst.setString(2, Integer.toString(updateID));
					pst.execute();
					
					//aktualizacja tabeli wybrane - tylko elementy
					query="update Wybrane set Wartoœæ=Wartoœæ+Liczba*? where Nazwa=?";
					pst=connection.prepareStatement(query);
					pst.setString(1, Double.toString(diff));
					pst.setString(2, nazwa);
					pst.execute();
								
					//aktualizacja tabeli przedmioty i wybrane				
					query="select Nazwa,Budowa from Przedmioty where Budowa like ?";
					pst = connection.prepareStatement(query);
					pst.setString(1, "%,"+Integer.toString(updateID)+";%");
					
					rs=pst.executeQuery();
					
					while(rs.next()){
							String currentName=rs.getString(1);
							String currentBuild=rs.getString(2);
							String[] parts = currentBuild.split(","+Integer.toString(updateID)+";");
							String[] numbers = parts[0].split(";");
							int numberOfElements = Integer.parseInt(numbers[numbers.length-1]);
						  
						  	query="update Przedmioty set Cena=Cena+? where Nazwa=?";
							pst=connection.prepareStatement(query);
							pst.setString(1, Double.toString(diff*numberOfElements));
							pst.setString(2, currentName);
							pst.execute();
							
							query="update Wybrane set Wartoœæ=Wartoœæ+Liczba*? where Nazwa=?";
							pst=connection.prepareStatement(query);
							pst.setString(1, Double.toString(diff*numberOfElements));
							pst.setString(2, currentName);
							pst.execute();
						  
					}
					
					lblPrice.setText("Cena: "+Double.toString(newPrice));
					refreshTable(1);
					pst.close();
					rs.close();
								
					} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Wprowadz now¹ cenê poprawnie!");
				}	
			}
		});
		btnChPrice.setBounds(977, 212, 109, 23);
		btnChPrice.setEnabled(false);
		contentPane.add(btnChPrice);
		
		
		
		//combobox
		String[] opcje ={"Przedmioty","Elementy"};
		JComboBox comboBox = new JComboBox(opcje);
		int comboBoxOption=comboBox.getSelectedIndex();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int comboBoxOption=comboBox.getSelectedIndex();
				refreshTable(comboBoxOption);
				if(comboBoxOption==0)
					{btnChPrice.setEnabled(false);
					textFieldPrice.setEditable(false);}
				else
					{btnChPrice.setEnabled(true);
					textFieldPrice.setEditable(true);}
			}
		});
		comboBox.setBounds(726, 11, 91, 25);
		contentPane.add(comboBox);
		
		
		//podglad nazwy
		JLabel lblName = new JLabel("");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblName.setBounds(829, 72, 507, 18);
		contentPane.add(lblName);
		
		
		//pole tekstowe do zmiany nazwy
		textFieldName = new JTextField();
		textFieldName.setToolTipText("Tutaj wpisz now\u0105 nazw\u0119");
		textFieldName.setBounds(829, 103, 386, 23);
		contentPane.add(textFieldName);
		textFieldName.setColumns(10);
		
		
		//przycisk do zmiany nazwy
		JButton btnChName = new JButton("Zmie\u0144 nazw\u0119");
		btnChName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row =table.getSelectedRow();
				String nazwa=(table.getModel().getValueAt(row, 0).toString());
				String nowaNazwa=textFieldName.getText();
				String query;
				if(comboBox.getSelectedIndex()==1)
					query="Update Elementy set Nazwa=? where Nazwa=? ";
				else
					query="Update Przedmioty set Nazwa=? where Nazwa=? ";
				PreparedStatement pst;
				try {
					pst = connection.prepareStatement(query);
					pst.setString(1, nowaNazwa);
					pst.setString(2, nazwa);
					pst.execute();
					pst.close();
					textFieldName.setText("");
					textFieldPrice.setText("");
					lblName.setText("");
					lblPrice.setText("");
					refreshTable(comboBox.getSelectedIndex());
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Taki element ju¿ istnieje");
				}
				
				
				
			}
		});
		btnChName.setBounds(1227, 103, 109, 23);
		contentPane.add(btnChName);
		
		
		//pole tekstowe do zmiany ceny
		textFieldPrice = new JTextField();
		textFieldPrice.setToolTipText("Tutaj wpisz now\u0105 cen\u0119");
		textFieldPrice.setBounds(829, 212, 136, 22);
		contentPane.add(textFieldPrice);
		textFieldPrice.setEditable(false);
		textFieldPrice.setColumns(10);
		
		
		
		//tabela
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row=table.getSelectedRow();
				
				if(comboBox.getSelectedIndex()==1)//dla elementow
				{	
					String nazwa=(table.getModel().getValueAt(row, 0).toString());
					String cena=(table.getModel().getValueAt(row, 1).toString());
					lblName.setText(nazwa);
					lblPrice.setText("Cena: "+cena);
					
					String elem="Pojedynczy element:\n\n";
					textPane.setText(elem + nazwa);
					textPane.validate();
				}
				else//dla przedmiotow
				{
					try{
						String nazwa=(table.getModel().getValueAt(row, 0).toString());
						String cena=(table.getModel().getValueAt(row, 1).toString());
						lblName.setText(nazwa);
						lblPrice.setText("Cena: "+cena);
						String query1="select Budowa from Przedmioty where nazwa=?";
						PreparedStatement pst1=connection.prepareStatement(query1);
						pst1.setString(1, nazwa);
						ResultSet rs1=pst1.executeQuery();
						String budowa=rs1.getString(1);
						String tekst=nazwa+":\n";
						String[] budowaArray=budowa.split(";");
						
						for(int i=0;i<budowaArray.length;i++){
							String[] data=budowaArray[i].split(",");
							int amount = Integer.parseInt(data[0]);
							int elementID = Integer.parseInt(data[1]);
							
							
							query1="select Nazwa from Elementy where id=?";
							pst1=connection.prepareStatement(query1);
							pst1.setString(1, Integer.toString(elementID));
							rs1=pst1.executeQuery();
							String nazwaElementu=rs1.getString(1);
							
							tekst+=Integer.toString(amount)+"x: \""+nazwaElementu+"\"\n";
				
 						}
						pst1.close();
						rs1.close();
						

						textPane.setText(tekst);
						textPane.validate();
						
					}catch(Exception e1){
						JOptionPane.showMessageDialog(null, e1);
					}	
				}
			}
		});
		table.setFont(new Font("Tahoma", Font.PLAIN, 17));
		table.setRowHeight(20);
		scrollPane.setViewportView(table);
		
		
		
		//pole tekstowe do wyszukiwania
		textFieldSearch = new JTextField();
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
					try {
						if(textFieldSearch.getText()!=null){
							
						String query;
						if(comboBox.getSelectedIndex()==1)
							query="select Nazwa,Cena from Elementy where Nazwa like ?";
						else
							query="select Nazwa,Cena from Przedmioty where Nazwa like ?";
						
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, '%'+textFieldSearch.getText()+'%');
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						table.getColumnModel().getColumn(0).setPreferredWidth(600);
						table.getColumnModel().getColumn(1).setPreferredWidth(50);
						pst.close();
						rs.close();
						}
					} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				} 
			}
		});
		textFieldSearch.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textFieldSearch.setBounds(12, 13, 702, 23);
		contentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		
	
		

		//przycisk do usuwania
		JButton btnDelete = new JButton("Usu\u0144 trwale");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					try {
						int input = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz usun¹æ ten przedmiot?", null,JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

						if(input==0){//potwierdzono usuwanie

							if(comboBox.getSelectedIndex()==0)//jezeli usuwany jest przedmiot
							{
								int row=table.getSelectedRow();
								String nazwa=(table.getModel().getValueAt(row, 0).toString());					
								String query="delete from Przedmioty where Nazwa=?";
								PreparedStatement pst=connection.prepareStatement(query);
								pst.setString(1, nazwa);
								pst.execute();
								refreshTable(comboBoxOption);
								
												
								query="delete from Wybrane where Nazwa=?";
								pst=connection.prepareStatement(query);
								pst.setString(1, nazwa);
								pst.execute();
								pst.close();
							}
						
							else{//usuwanie elementu
								
								int row=table.getSelectedRow();
								String nazwa=(table.getModel().getValueAt(row, 0).toString());					
								String query="select Id from Elementy where Nazwa=?";
								PreparedStatement pst=connection.prepareStatement(query);
								pst.setString(1, nazwa);
								
								ResultSet rs=pst.executeQuery();
								
								int deleteID = rs.getInt(1);
								
								//usuwanie z 1. tabeli
								query="delete from Elementy where Id=?";
								pst=connection.prepareStatement(query);
								pst.setString(1, Integer.toString(deleteID));
								pst.execute();
								
								
								
								//usuwanie przedmiotow i elementow z 3. tabeli
								query="delete from Wybrane where Nazwa in (select Nazwa from Przedmioty where Budowa like ?)";
								pst=connection.prepareStatement(query);
								pst.setString(1, "%,"+Integer.toString(deleteID)+";%");
								pst.execute();
								
								query="delete from Wybrane where Nazwa=?";
								pst=connection.prepareStatement(query);
								pst.setString(1, nazwa);
								pst.execute();
								
								//usuwanie przedmiotow z 2. tabeli
								query="delete from Przedmioty where Budowa like ?";
								pst=connection.prepareStatement(query);
								pst.setString(1, "%,"+Integer.toString(deleteID)+";%");
								pst.execute();
								
								
								refreshTable(comboBox.getSelectedIndex());
								pst.close();
								rs.close();
								
								JOptionPane.showMessageDialog(null, "Usuniêto");
							}
						}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				} 
			}
		});
		btnDelete.setBounds(829, 315, 145, 22);
		contentPane.add(btnDelete);
		
		
		//przycisk cofnij do glownego okna
		JButton btnBack = new JButton("Wr\u00F3\u0107");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				EditDataBase.this.dispose();
				General general=new General();
				general.setVisible(true);
			}
		});
		btnBack.setBounds(1200, 649, 136, 77);
		contentPane.add(btnBack);
		
		refreshTable(comboBoxOption);
	}
}
