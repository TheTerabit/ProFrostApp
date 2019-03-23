import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddBigItem extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldSearch;
	private JTable table;
	private JTable table_1;
	private JTextField textFieldN;

	/**
	 * Launch the application.
	 */
	 Connection connection=null;
	private JTextField textFieldNazwij;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddBigItem frame = new AddBigItem();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setTitle("ProFrostApp - nowy przedmiot");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//table.setModel(tableModel);
	//odswiezanie tabeli lewej
	public void refreshTable()
	{
		try {
			String query="select Nazwa,Cena from Elementy";
			
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			table.getColumnModel().getColumn(0).setPreferredWidth(500);
			table.getColumnModel().getColumn(1).setPreferredWidth(20);
			pst.close();
			rs.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	//odswiezanie tabeli prawej
	public void refreshTable_1()
	{
		try {
			String query1="select Nazwa,Wartoœæ,Liczba from Buduj";
			PreparedStatement pst1=connection.prepareStatement(query1);
			ResultSet rs1=pst1.executeQuery();
			table_1.setModel(DbUtils.resultSetToTableModel(rs1));
			table_1.getColumnModel().getColumn(0).setPreferredWidth(600);
			pst1.close();
			rs1.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		} 	
	}
	
	
	//odswiezanie sumy kosztu przedmiotu
	public double refreshSum()
	{
		double cena=0;
		try{
			String query="select Wartoœæ from Buduj";
			PreparedStatement pst=connection.prepareStatement(query);
			ResultSet rs=pst.executeQuery();
			
			while(rs.next()){
				cena=round(cena+rs.getDouble(1),2);
			}
			rs.close();
			pst.close();
			
		}catch(Exception e1){
			e1.printStackTrace();
		}
		return cena;
	}
	
	
	
	//zaokraglanie
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	


	
	/**
	 * Create the frame.
	 */
	public AddBigItem() {
		
		connection=sqliteConnection.dbConnector();
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1366, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSuma = new JLabel("Suma: "+refreshSum());
		lblSuma.setBounds(1086, 604, 174, 35);
		contentPane.add(lblSuma);
		
		
		
		//pole do wyszukiwania
		textFieldSearch = new JTextField();
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				try {
					if(textFieldSearch.getText()!=null){
					
					String query="select Nazwa,Cena from Elementy where Nazwa like ?";
					PreparedStatement pst=connection.prepareStatement(query);
					pst.setString(1, '%'+textFieldSearch.getText()+'%');
					ResultSet rs=pst.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					table.getColumnModel().getColumn(0).setPreferredWidth(500);
					table.getColumnModel().getColumn(1).setPreferredWidth(20);
					pst.close();
					rs.close();
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				} 
			}
		});
		textFieldSearch.setToolTipText("Wpisz nazw\u0119, aby wyszuka\u0107...");
		textFieldSearch.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textFieldSearch.setColumns(10);
		textFieldSearch.setBounds(12, 13, 669, 25);
		contentPane.add(textFieldSearch);
		
		
		
		
		//tabele
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 51, 669, 657);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table.setRowHeight(20);
		table.setDefaultEditor(Object.class, null);
		scrollPane.setViewportView(table);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(800, 51, 529, 540);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table_1.setRowHeight(20);
		table_1.setDefaultEditor(Object.class, null);
		scrollPane_1.setViewportView(table_1);
		
		
		

		
		//zapisywanie nowego przedmiotu w bazie
		JButton btnZapiszTenPrzedmiot = new JButton("Zapisz ten przedmiot");
		btnZapiszTenPrzedmiot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					double cena=0;
					String budowa="";
					
					String query="select Wartoœæ,Id,Liczba from Buduj";
					PreparedStatement pst=connection.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					
					while(rs.next()){
						cena=round(cena+rs.getDouble(1),2);
						budowa=budowa+rs.getString(3)+','+rs.getString(2)+';';
					}
					
					query="Insert into Przedmioty (Nazwa,Cena,Budowa) values (?,?,?)";			
					pst=connection.prepareStatement(query);
					pst.setString(1, textFieldNazwij.getText());
					pst.setString(2, Double.toString(cena));
					pst.setString(3, budowa);
					pst.execute();
					rs.close();
					pst.close();
					AddBigItem.this.dispose();
					General general=new General();
					general.setVisible(true);
				
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1);
				}
				
				
				
			}
		});
		btnZapiszTenPrzedmiot.setBounds(1150, 673, 179, 35);
		contentPane.add(btnZapiszTenPrzedmiot);
		
		
		
		//przenoszenie z lewej do prawej 1 sztuki
		JButton btnPlusOne = new JButton("+1");
		btnPlusOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					int row = table.getSelectedRow();
					
					String nazwa=(table.getModel().getValueAt(row, 0).toString());
					String cena=(table.getModel().getValueAt(row, 1).toString());
					
					String query1="select * from Buduj where Nazwa=?";
					PreparedStatement pst1=connection.prepareStatement(query1);
					pst1.setString(1, nazwa);
					ResultSet rs1=pst1.executeQuery();
					
					int count=0;
					while(rs1.next()){
						count++;
					}
					
					query1="select Id from Elementy where Nazwa=?";
					pst1=connection.prepareStatement(query1);
					pst1.setString(1, nazwa);
					rs1=pst1.executeQuery();
					int id=rs1.getInt(1);
					rs1.close();
				
					if(count==0){//jezeli jeszcze nie ma elementu w prawej tabeli
					
						query1="Insert into Buduj (Nazwa,Wartoœæ,Id,Liczba) values  (?,?,?,\"1\")";
						pst1=connection.prepareStatement(query1);
						pst1.setString(1, nazwa);
						pst1.setString(2, cena);
						pst1.setString(3, Integer.toString(id));
						pst1.execute();
						refreshTable_1();
						pst1.close();
						
					}
					else
					{
						//pobranie wartosci
						String query2="select Liczba,Wartoœæ from Buduj where Nazwa=?";
						PreparedStatement pst2=connection.prepareStatement(query2);
						pst2.setString(1, nazwa);
						ResultSet rs2=pst2.executeQuery();
						String liczba=rs2.getString(1);
						String wartosc=rs2.getString(2);

						pst2.close();
						rs2.close();

						//aktualizacja
						int lp1=Integer.parseInt(liczba)+1;
						double cenaint=Double.parseDouble(cena);
						double wpc=Double.parseDouble(wartosc)+cenaint;
						String query="Update Buduj set Liczba=?,Wartoœæ=? where Nazwa=?";			
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(lp1));
						pst.setString(2, Double.toString(round(wpc,2)));
						pst.setString(3, nazwa);
						pst.execute();
					
						pst.close();
					}
					lblSuma.setText("Suma: "+refreshSum());
					
					refreshTable_1();
					try{
					table.setRowSelectionInterval(row, row);}catch(Exception e4){}
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1+"Proszê wybraæ przedmiot z listy po lewej");
				}
			}
		});
		btnPlusOne.setBounds(693, 171, 97, 47);
		contentPane.add(btnPlusOne);
		
		
		
		//przycisk +N
		JButton btnPlusN = new JButton("+n");
		btnPlusN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int row = table.getSelectedRow();
					String nazwa=(table.getModel().getValueAt(row, 0).toString());
					String cena=(table.getModel().getValueAt(row, 1).toString());
				
					String query1="select * from Buduj where Nazwa=?";
					PreparedStatement pst1=connection.prepareStatement(query1);
					pst1.setString(1, nazwa);
					ResultSet rs1=pst1.executeQuery();
					
					int count=0;
					while(rs1.next()){
						count++;
					}
					
					query1="select Id from Elementy where Nazwa=?";
					pst1=connection.prepareStatement(query1);
					pst1.setString(1, nazwa);
					rs1=pst1.executeQuery();
					int id=rs1.getInt(1);
					
					pst1.close();
					rs1.close();
				
					//wyznaczanie mnoznika
					String multiplierString=textFieldN.getText();
					int multiplier=Integer.parseInt(multiplierString);
					double cenaint=Double.parseDouble(cena);
					
					if (multiplier<=0){
						multiplier*=-1;
						textFieldN.setText(Integer.toString(multiplier));
						int a=1;
						a/=multiplier;
					}
					
					
					
					if(count==0){//jezeli nie ma w tabeli
					
						String query="insert into Buduj (Nazwa,Wartoœæ,Liczba,Id) values  (?,?,"+multiplier+",?)";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, nazwa);
						pst.setString(2, Double.toString(round(cenaint*multiplier,2)));
						pst.setString(3, Integer.toString(id));
						pst.execute();
						
						pst.close();
					}
					else
					{
						String query2="select Liczba,Wartoœæ from Buduj where Nazwa=?";
						PreparedStatement pst2=connection.prepareStatement(query2);
						pst2.setString(1, nazwa);
						ResultSet rs2=pst2.executeQuery();
						
						String liczba=rs2.getString(1);
						String wartosc=rs2.getString(2);
						
						pst2.close();
						rs2.close();
					
						
						int lp1=Integer.parseInt(liczba)+multiplier;
						
						double wpc=Double.parseDouble(wartosc)+cenaint*multiplier;
						String query="Update Buduj set Liczba=?,Wartoœæ=? where Nazwa=?";			
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(lp1));
						pst.setString(2, Double.toString(round(wpc,2)));
						pst.setString(3, nazwa);
						pst.execute();
					
						pst.close();
					}
					lblSuma.setText("Suma: "+refreshSum());
					refreshTable_1();
					try{
					table.setRowSelectionInterval(row, row);}catch(Exception e4){}
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wybraæ przedmiot z listy po lewej i uzupe³niæ powy¿sze pole liczb¹ ca³kowit¹ dodatni¹");
				}
			}
		});
		btnPlusN.setBounds(693, 371, 97, 47);
		contentPane.add(btnPlusN);
		
		
		
		//przycisk -1
		JButton btnMinusOne = new JButton("-1");
		btnMinusOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					//pobranie wartosci
					int row = table_1.getSelectedRow();
					
					String nazwa=(table_1.getModel().getValueAt(row, 0).toString());
					String wartosc=(table_1.getModel().getValueAt(row, 1).toString());
					String liczba=(table_1.getModel().getValueAt(row, 2).toString());
					int lp1=Integer.parseInt(liczba)-1;
					double wpc=Double.parseDouble(wartosc)*lp1/(lp1+1);
					
					
					if(lp1==0){//jezeli odejmue ostatnia sztuke
					
						String query="delete from Buduj where Nazwa=?";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, nazwa);
						
						pst.execute();
						
						pst.close();
						refreshTable_1();
					}
					else
					{					
						String query="Update Buduj set Liczba=?,Wartoœæ=? where Nazwa=?";			
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(lp1));
						pst.setString(2, Double.toString(round(wpc,2)));
						pst.setString(3, nazwa);
						pst.execute();
						
						refreshTable_1();
						try{
							table_1.setRowSelectionInterval(row, row);}catch(Exception e4){}
						pst.close();
					}
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wybraæ przedmiot z listy po prawej");
				}
				lblSuma.setText("Suma: "+refreshSum());
			}
		});
		btnMinusOne.setBounds(800, 598, 97, 47);
		contentPane.add(btnMinusOne);
		
		
		
		//przycisk usuwania z prawej tabeli
		JButton btnDelete = new JButton("Usu\u0144");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int row = table_1.getSelectedRow();
					String nazwa=(table_1.getModel().getValueAt(row, 0).toString());
					
					String query="delete from Buduj where Nazwa=?"; 
					PreparedStatement pst=connection.prepareStatement(query);
					pst.setString(1, nazwa);
						
					pst.execute();
						
					pst.close();
					refreshTable_1();
					lblSuma.setText("Suma: "+refreshSum());
					try{
						table_1.setRowSelectionInterval(row, row);}catch(Exception e4){ }
				}catch(Exception e3){
					JOptionPane.showMessageDialog(null, "Proszê wybraæ przedmiot z listy po prawej");
				}
			}
		});
		btnDelete.setBounds(909, 598, 97, 47);
		contentPane.add(btnDelete);
		
		
		
		textFieldN = new JTextField();
		textFieldN.setText("1");
		textFieldN.setBounds(693, 333, 97, 25);
		contentPane.add(textFieldN);
		textFieldN.setColumns(10);
		
		textFieldNazwij = new JTextField();
		textFieldNazwij.setBounds(1150, 635, 179, 25);
		contentPane.add(textFieldNazwij);
		textFieldNazwij.setColumns(10);
		
		
		//przycisk powrotu do glownego okna
		JButton btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				AddBigItem.this.dispose();
				General general=new General();
				general.setVisible(true);
			}
		});
		btnAnuluj.setBounds(959, 673, 179, 35);
		contentPane.add(btnAnuluj);
		
		
		
		
		JLabel lblNazwa = new JLabel("Nazwa:");
		lblNazwa.setBounds(1086, 637, 52, 21);
		contentPane.add(lblNazwa);
		
		//czyszczenie tabeli pomocniczej do budowy przedmiotu na start
		try{
			String query="Delete from Buduj";			
			PreparedStatement pst=connection.prepareStatement(query);

			pst.execute();	
			pst.close();
			
			}catch(Exception e2){
				JOptionPane.showMessageDialog(null, e2);
			}
		
		refreshTable();
		
	}
}
