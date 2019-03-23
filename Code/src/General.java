import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.tools.TextToPDF;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
 
import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;



public class General extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldSearch;
	private JTable table;
	private JTextField textFieldN;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					General frame = new General();
					frame.setTitle("ProFrostApp");
					frame.setVisible(true);
					
					
					String path=System.getProperty("user.dir");
					path = path.replace("/ProFrostAppMac.jar","");
					ImageIcon pfa=new ImageIcon(path+"\\Resources\\profrosticon.png");
					frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("profrosticon.png")));
					//frame.setIconImage(new ImageIcon(getClass().getResource("images\\faviconprofrost.png")).getImage());
					//frame.setTitle("ProFrostApp");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	Connection connection=null;
	private JTextField textFieldRabat;
	private JTextField textFieldPdfName;
	
	
	
	public double refreshSum()//odswiezanie sumy calkowitej wybranych przedmiotow
	{
		double cena=0;
		try{
			String query="select Wartoœæ from Wybrane";
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
	
	
	
	public void refreshTable(int x)//odswiezanie tabeli lewej do wykorzystania po akcji w zaleznosci od opcji comboboxa
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
			table.getColumnModel().getColumn(1).setPreferredWidth(20);
			pst.close();
			rs.close();
						
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	
	public void refreshTable_1()//odswiezanie tabeli prawej
	{
		try {
			String query1="select * from Wybrane";
			PreparedStatement pst1=connection.prepareStatement(query1);
			ResultSet rs1=pst1.executeQuery();
			table_1.setModel(DbUtils.resultSetToTableModel(rs1));
			table_1.getColumnModel().getColumn(0).setPreferredWidth(300);
			pst1.close();
			rs1.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		
	}
	
	
	
	public static double round(double value, int places) {//zaokraglanie wyniku
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////
	
	public General() {
		connection=sqliteConnection.dbConnector();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1368, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		
		//combobox
		String[] opcje ={"Przedmioty","Elementy"};
		JComboBox comboBox = new JComboBox(opcje);
		int comboBoxOption=comboBox.getSelectedIndex();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int comboBoxOption=comboBox.getSelectedIndex();
				refreshTable(comboBoxOption);
			}
		});
		comboBox.setBounds(446, 12, 91, 25);
		contentPane.add(comboBox);
		
		
		
		
		//pole wyszukiwania
		textFieldSearch = new JTextField();
		textFieldSearch.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textFieldSearch.setToolTipText("Wpisz nazw\u0119, aby wyszuka\u0107...");
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {//gdy wcisniesz przycisk i pole jest niepuste
				try {
					if(textFieldSearch.getText()!=null){
							
						String query;
						if(comboBox.getSelectedIndex()==1)//query (tabela) wybierane jest w zaleznosci od comboboxa
							query="select Nazwa,Cena from Elementy where Nazwa like ?";
						else
							query="select Nazwa,Cena from Przedmioty where Nazwa like ?";
						
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, '%'+textFieldSearch.getText()+'%');
						ResultSet rs=pst.executeQuery();
						table.setModel(DbUtils.resultSetToTableModel(rs));
						table.getColumnModel().getColumn(0).setPreferredWidth(600);
						table.getColumnModel().getColumn(1).setPreferredWidth(20);
						pst.close();
						rs.close();
						}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1);
				} 
			}
		});
		textFieldSearch.setBounds(12, 12, 422, 25);
		contentPane.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		
		
		
		//pasek do scrollowania tabeli
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 563, 737, 145);
		contentPane.add(scrollPane_2);
		
		

		//pole tekstowe rabatu
		textFieldRabat = new JTextField();
		textFieldRabat.setText("0");
		textFieldRabat.setBounds(1189, 563, 106, 25);
		contentPane.add(textFieldRabat);
		textFieldRabat.setColumns(10);
		
		
		
		//podglad sumy po przeliczeniu z rabatem
		JLabel lblSuma = new JLabel("Suma:");
		lblSuma.setBounds(1146, 601, 116, 25);
		try{
			double rabat=(100.0-Integer.parseInt(textFieldRabat.getText()))/100.0;
			lblSuma.setText("Suma: "+round(refreshSum()*(rabat),2));
		}
			catch(Exception e1){
				JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
			}
		contentPane.add(lblSuma);
		
		
		
		
		//podglad czesci z ktorych sklada sie wybrany przedmiot
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 17));
		scrollPane_2.setViewportView(textPane);
		textPane.setEditable(false);
		
		
		
		
		
		
		//pasek do scrollowania
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 50, 737, 502);
		contentPane.add(scrollPane);
		
		
		
		//tabela lewa - na klikniecie wyswietla czesci z ktorych zbudowany jest wybrany przedmiot
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(comboBox.getSelectedIndex()==1)//dla tabeli z elementami
				{	
					int row=table.getSelectedRow();
					String nazwa=(table.getModel().getValueAt(row, 0).toString());
					String elem="Pojedynczy element:\n\n";
					textPane.setText(elem + nazwa);
					textPane.validate();
				}
				else//dla tabeli z gotowymi przedmiotami
				{
					try{
						int row=table.getSelectedRow();
						String nazwa=(table.getModel().getValueAt(row, 0).toString());
						
						String query1="select Budowa from Przedmioty where nazwa=?";//znajduje budowe w tabeli przedmiotow
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
							
							query1="select Nazwa from Elementy where id=?";//rozpisuje elementy z budowy w textPane
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
		table.setDefaultEditor(Object.class, null);
		
		
		//przycisk otwierajacy odpowiednie okienko w celu dodania nowych rekordow do bazy
		JButton btnDodaj = new JButton("Dodaj...");
		btnDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				textPane.setText("");
				textPane.validate();
				table.clearSelection();
				if(comboBox.getSelectedIndex()==1){//dodawanie elementu
					
					AddItem ai=new AddItem();
					ai.setVisible(true);
				}
				else{//dodawanie gotowego przedmiotu
					connection.close();
					General.this.dispose();
					AddBigItem abi=new AddBigItem();
					abi.setVisible(true);
				}
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1+"klik");
				}
			}
		});
		btnDodaj.setBounds(549, 12, 91, 25);
		contentPane.add(btnDodaj);
		
		
		
		String path=System.getProperty("user.dir");
		path = path.replace("/ProFrostAppMac.jar","");
		ImageIcon arrow1=new ImageIcon(path+"\\Resources\\arrow1.png");
		ImageIcon arrow2=new ImageIcon(path+"\\Resources\\arrow2.png");
		//przycisk przerzucania przedmiotow do prawej tabeli
		
		
		
		
		JButton btnPlusOne = new JButton("+1",arrow1);
		btnPlusOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int row = table.getSelectedRow();
					String nazwa=(table.getModel().getValueAt(row, 0).toString());
					String cena=(table.getModel().getValueAt(row, 1).toString());
					
					/////////////////////////////sprawdzanie czy juz jest w prawej tabeli
					String query1="select * from Wybrane where Nazwa=?";
					PreparedStatement pst1=connection.prepareStatement(query1);
					pst1.setString(1, nazwa);
					ResultSet rs1=pst1.executeQuery();
					
					int count=0;
					while(rs1.next()){
						count++;
					}
					pst1.close();
					rs1.close();
					/////////////////////////////
					
					if(count==0){//gdy nie ma wpisuje 1 sztuke
					
						String query="insert into Wybrane (Nazwa,Wartoœæ,Liczba) values  (?,?,\"1\")";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, nazwa);
						pst.setString(2, cena);
						pst.execute();
						
						pst.close();
					}
					else//gdy juz jest zwieksza wartosc i liczbe sztuk o 1
					{
						String query2="select Liczba,Wartoœæ from Wybrane where Nazwa=?";
						PreparedStatement pst2=connection.prepareStatement(query2);
						pst2.setString(1, nazwa);
						ResultSet rs2=pst2.executeQuery();
						String liczba=rs2.getString(1);//pobranie wartosci
						String wartosc=rs2.getString(2);
						pst2.close();
						rs2.close();
								
						
						int lp1=Integer.parseInt(liczba)+1;//zwiekszenie wartosci
						double cenaint=Double.parseDouble(cena);
						double wpc=Double.parseDouble(wartosc)+cenaint;
						
						
						
						String query="Update Wybrane set Liczba=?,Wartoœæ=? where Nazwa=?";//aktualizacja wartosci			
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(lp1));
						pst.setString(2, Double.toString(round(wpc,2)));
						pst.setString(3, nazwa);
						pst.execute();
						pst.close();
					}
					refreshTable_1();
					try{//zaznaczanie tego samego wiersza tabeli
						table.setRowSelectionInterval(row, row);
					}catch(Exception e4){}
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1+"Proszê wybraæ przedmiot z listy po lewej");
				}
				try{//odswiezanie podgladu sumy ceny
					double rabat=(100.0-Integer.parseInt(textFieldRabat.getText()))/100.0;
					lblSuma.setText("Suma: "+round(refreshSum()*(rabat),2));
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
				}
			}
		});
		btnPlusOne.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPlusOne.setBounds(761, 102, 116, 57);
		contentPane.add(btnPlusOne);
		
		
		JLabel chosenRight = new JLabel("");
		chosenRight.setFont(new Font("Tahoma", Font.PLAIN, 11));
		chosenRight.setBounds(890, 28, 448, 21);
		contentPane.add(chosenRight);
		
		
		
		JButton btnPlusN = new JButton("+n",arrow2);
		btnPlusN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					///////////////////////////////////sprawdzanie czy juz jest
					int row = table.getSelectedRow();
					String nazwa=(table.getModel().getValueAt(row, 0).toString());
					String cena=(table.getModel().getValueAt(row, 1).toString());
					
					String query1="select * from Wybrane where Nazwa=?";
					PreparedStatement pst1=connection.prepareStatement(query1);
					pst1.setString(1, nazwa);
					ResultSet rs1=pst1.executeQuery();
					
					int count=0;
					while(rs1.next()){
						count++;
						
					}
					pst1.close();
					rs1.close();
					////////////////////////////////
					
					
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

					
					
					if(count==0){//wpisywanie do tabeli odpowiedniej liczby sztuk
					
						String query="insert into Wybrane (Nazwa,Wartoœæ,Liczba) values  (?,?,"+multiplier+")";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, nazwa);
						pst.setString(2, Double.toString(round(cenaint*multiplier,2)));
						pst.execute();
						pst.close();
					}
					else//aktualizowanie
					{
						String query2="select Liczba,Wartoœæ from Wybrane where Nazwa=?";//pobranie wartosci
						PreparedStatement pst2=connection.prepareStatement(query2);
						pst2.setString(1, nazwa);
						ResultSet rs2=pst2.executeQuery();
						String liczba=rs2.getString(1);
						String wartosc=rs2.getString(2);
						pst2.close();
						rs2.close();
					
	
						//zwiekszenie wartosci
						int lp1=Integer.parseInt(liczba)+multiplier;
						double wpc=Double.parseDouble(wartosc)+cenaint*multiplier;
						
						String query="Update Wybrane set Liczba=?,Wartoœæ=? where Nazwa=?";//aktualizacja			
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(lp1));
						pst.setString(2, Double.toString(round(wpc,2)));
						pst.setString(3, nazwa);
						pst.execute();
					
						pst.close();
					}
					refreshTable_1();
					try{//ponownie zaznaczenie wiersza tabeli
					table.setRowSelectionInterval(row, row);}catch(Exception e4){}
				}
				catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wybraæ przedmiot z listy po lewej i uzupe³niæ powy¿sze pole liczb¹ ca³kowit¹ dodatni¹");
				}
				try{//aktualizacja sumy calkowitej
					double rabat=(100.0-Integer.parseInt(textFieldRabat.getText()))/100.0;
					lblSuma.setText("Suma: "+round(refreshSum()*(rabat),2));
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
				}
			}
		});
		btnPlusN.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPlusN.setBounds(761, 354, 116, 57);
		contentPane.add(btnPlusN);
		
		
		//pole tekstowe dla przycisku +n
		textFieldN = new JTextField();
		textFieldN.setText("1");
		textFieldN.setBounds(761, 319, 116, 22);
		contentPane.add(textFieldN);
		textFieldN.setColumns(10);
		
		
		//scrollPane
		JScrollPane scrollPane_1 = new JScrollPane();

		scrollPane_1.setBounds(890, 50, 448, 500);
		contentPane.add(scrollPane_1);
		
		
		//pole tekstowe do nadania nazwy pliku
		textFieldPdfName = new JTextField();
		textFieldPdfName.setBounds(890, 661, 320, 25);
		contentPane.add(textFieldPdfName);
		textFieldPdfName.setColumns(10);
		
		
		//tabela prawa
		table_1 = new JTable();
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				int row=table_1.getSelectedRow();
				String nazwa=(table_1.getModel().getValueAt(row, 0).toString());
				chosenRight.setText(nazwa);
				chosenRight.validate();
			}
		});
		table_1.setRowHeight(20);
		table_1.setFont(new Font("Tahoma", Font.PLAIN, 17));
		scrollPane_1.setViewportView(table_1);
		table_1.setDefaultEditor(Object.class, null);
		
		//przycisk do zapisu pliku
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				
				String[][] wybrane=new String[100000][3];
				String title=textFieldPdfName.getText();//nazwa pliku
				int items=0;
				
				try{//zapis prawej tabeli do tablicy i pobranie wszystkich argumentow do w¹tku
					String query="select * from Wybrane";
					PreparedStatement pst=connection.prepareStatement(query);
					ResultSet rs=pst.executeQuery();
					int i=0;
					while(rs.next()){
						wybrane[i][0]=rs.getString(1);
						wybrane[i][1]=rs.getString(2);
						wybrane[i][2]=rs.getString(3);
						i++;
					}
					items=i;
					}catch(Exception e2){
						JOptionPane.showMessageDialog(null, e2);
					}
				int rabat=0;
				try{
				rabat=Integer.parseInt(textFieldRabat.getText());
				}catch(Exception e2){
					JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
				}
				
				
				
				//nowy w¹tek
				Runnable co = new CreatingOffer(wybrane,textFieldPdfName.getText(),items,rabat);
				Thread t=new Thread(co);
				t.start();
				////////////
			
				
				/*
				//generowanie listy czêœci
				XWPFDocument document = new XWPFDocument();//nowy dokument
				
				XWPFParagraph paragraph = document.createParagraph();//naglowek
				XWPFRun run = paragraph.createRun();
				run.setText("Czêœci do zamówienia: ");
				run.setFontSize(25);
				run.setColor("0D4D96");
				run.setUnderline(UnderlinePatterns.SINGLE);
				run.setBold(true);
				paragraph.setAlignment(ParagraphAlignment.LEFT);
				run.addBreak();
				
				
				//tabela
				XWPFTable table = document.createTable();
				table.setCellMargins(20, 200, 20, 200);
				
				XWPFTableRow row0 = table.getRow(0);
				
				XWPFTableCell cell0=row0.getCell(0);
				cell0.setColor("0D4D96");
				
				XWPFTableCell cell1=row0.createCell();		
				cell1.setColor("0D4D96");
				
				
				//naglowki tabeli
				XWPFParagraph paragraph1 = cell1.addParagraph();
				XWPFRun run1 = paragraph1.createRun();
				run1.setText("Iloœæ sztuk");
				run1.setBold(true);
				run1.setColor("FFFFFF");
				run1.addBreak();
				run1.setFontSize(14);
				
				XWPFParagraph paragraph0 = cell0.addParagraph();
				XWPFRun run0 = paragraph0.createRun();
				paragraph0.setAlignment(ParagraphAlignment.CENTER);
				run0.setText("Nazwa elementu");
				run0.setBold(true);
				run0.setColor("FFFFFF");
				run0.addBreak();
				run0.setFontSize(14);
				
				*/
				//tworzenie tablicy elementow na podstawie bazy danych
				int[] order = new int[10000];
				
				try{
					String[] przedmioty=new String[100000];//wyznaczanie wszystkich przemiotow do wpisania
					String query="select Nazwa from Przedmioty";
					PreparedStatement pstx=connection.prepareStatement(query);
					ResultSet rsx=pstx.executeQuery();
					int k=0;
					while(rsx.next()){
						przedmioty[k]=rsx.getString(1);
						k++;
					}
					rsx.close();
					pstx.close();
					
		
					
					
					query="select Nazwa,Liczba from Wybrane";
					PreparedStatement pst=connection.prepareStatement(query);

					ResultSet rs=pst.executeQuery();
					ResultSet[] rsi=new ResultSet[1000];
					ResultSet[] rsi2=new ResultSet[1000];
					int j=0;
					while(rs.next()){
						String currentName=rs.getString(1);
						String currentAmount=rs.getString(2);
						
						boolean check=true;
						for(int g=0;g<k;g++){//jezeli jest to przedmiot z tabeli gotowych regalow
							
							if(przedmioty[g].equals(currentName)){
								
								query="select Budowa from Przedmioty where Nazwa=?";
								pst=connection.prepareStatement(query);
								pst.setString(1, currentName);
								rsi[j]=pst.executeQuery();
								
								String currentBuild=rsi[j].getString(1);
								String[] parts = currentBuild.split(";");
								for(int i=0;i<parts.length;i++){
									String[] data = parts[i].split(",");
									int amount=Integer.parseInt(data[0]);
									int id=Integer.parseInt(data[1]);	
									order[id]+=amount*Integer.parseInt(currentAmount);
								
								}
								check=false;
								break;
							}
						}
						if(check){//jezeli jest to element
							
							query="select Id from Elementy where Nazwa=?";
							pst=connection.prepareStatement(query);
							pst.setString(1, currentName);
							rsi2[j]=pst.executeQuery();
								
							int id = rsi2[j].getInt(1);
								
							order[id]+=Integer.parseInt(currentAmount);
						}
						j++;		
					}
				pst.close();
				rs.close();
				
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, e1);
				}
				
				
				
				////////////////////////////////////////itext
				
				
				
				////////////////////////////////////////
				//wypelnianie tabeli w wordzie na podstawie tablicy
				/*
				for(int i=0;i<10000;i++){
					if(order[i]>0){
						XWPFTableRow row1=table.createRow();
						XWPFTableCell c1=row1.getCell(0);
						XWPFTableCell c2=row1.getCell(1);
						
						try{
						String query="select Nazwa from Elementy where Id=?";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(i));
						ResultSet rs=pst.executeQuery();
						String componentName = rs.getString(1);
						c1.setText(componentName);
						c2.setText(Integer.toString(order[i]));
						
						pst.close();
						rs.close();
						}catch(Exception e1){
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				}
				
				try{//zapis do pliku
					FileOutputStream output = new FileOutputStream(title);
					document.write(output);
					output.close();
					document.close();
		
				}catch(Exception  e1){
					System.out.println(e1);
					JOptionPane.showMessageDialog(null, "B³¹d zapisu: "+e1);
				}
				*/
				////////////////////////////////////////itext
				Background czesci=new Background();
				czesci.insert(title, order);
				
				
				////////////////////////////////////////
				
				
				
				try {//oczekiwanie na drugi w¹tek, a¿ tak¿e skoñczy zapis
					t.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String path=System.getProperty("user.dir");
				 try
			        { 
			            Files.deleteIfExists(Paths.get(path+"\\Raport.pdf")); 
			            Files.deleteIfExists(Paths.get(path+"\\Raport1.pdf")); 
			        } 
			        catch(NoSuchFileException e1) 
			        { 
			            System.out.println("No such file/directory exists"); 
			        } 
			        catch(DirectoryNotEmptyException e1) 
			        { 
			            System.out.println("Directory is not empty."); 
			        } 
			        catch(IOException e1) 
			        { 
			            System.out.println("Invalid permissions."); 
			        } 
			          
			        System.out.println("Deletion successful."); 
				
				JOptionPane.showMessageDialog(null, "Zapisano poprawnie!");
			}
		});
		btnOK.setBounds(1222, 639, 116, 69);
		contentPane.add(btnOK);
		
		
		
		//przycisk odejmowania jednego przedmiotu z prawej tabeli
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
					
					if(lp1==0){//gdy usuwamy ostatni element
					
						String query="delete from Wybrane where Nazwa=?";
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, nazwa);
						pst.execute();
						
						pst.close();
						refreshTable_1();
						
					}
					else
					{					
						String query="Update Wybrane set Liczba=?,Wartoœæ=? where Nazwa=?";			
						PreparedStatement pst=connection.prepareStatement(query);
						pst.setString(1, Integer.toString(lp1));
						pst.setString(2, Double.toString(round(wpc,2)));
						pst.setString(3, nazwa);
						pst.execute();
						
						refreshTable_1();
						try{
							table_1.setRowSelectionInterval(row, row);
						}catch(Exception e4){}
							
					}
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wybraæ przedmiot z listy po prawej");
				}
				try{//odswiezenie sumy calkowitej
					double rabat=(100.0-Integer.parseInt(textFieldRabat.getText()))/100.0;
					lblSuma.setText("Suma: "+round(refreshSum()*(rabat),2));
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
				}
			}
		});
		btnMinusOne.setBounds(890, 563, 116, 57);
		contentPane.add(btnMinusOne);
		
		
		//przycisk usuwania z tabeli
		JButton btnDelete = new JButton("Usu\u0144");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int row = table_1.getSelectedRow();
					String nazwa=(table_1.getModel().getValueAt(row, 0).toString());
					String query="delete from Wybrane where Nazwa=?"; 
					PreparedStatement pst=connection.prepareStatement(query);
					pst.setString(1, nazwa);	
					pst.execute();
			
					pst.close();
					refreshTable_1();
					try{
						table_1.setRowSelectionInterval(row, row);
					}catch(Exception e4){ }
				}catch(Exception e3){JOptionPane.showMessageDialog(
					null, "Proszê wybraæ przedmiot z listy po prawej");
				}
				try{
					double rabat=(100.0-Integer.parseInt(textFieldRabat.getText()))/100.0;
					lblSuma.setText("Suma: "+round(refreshSum()*(rabat),2));
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
				}
			}			
		});
		btnDelete.setBounds(1018, 563, 116, 57);
		contentPane.add(btnDelete);
		
		
		
		JLabel lblRabat = new JLabel("Rabat:");
		lblRabat.setBounds(1146, 567, 56, 16);
		contentPane.add(lblRabat);
		
		
		
		JLabel label = new JLabel("%");
		label.setBounds(1307, 565, 18, 21);
		contentPane.add(label);
		
		
		//przycisk do obliczenia rabatu
		JButton btnOblicz = new JButton("Oblicz");
		btnOblicz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
					double rabat=(100.0-Integer.parseInt(textFieldRabat.getText()))/100.0;
					lblSuma.setText("Suma: "+round(refreshSum()*(rabat),2));
				}
					catch(Exception e1){
						JOptionPane.showMessageDialog(null, "Proszê wprowadziæ poprawny rabat");
					}
						}
		});
		btnOblicz.setBounds(1260, 601, 78, 25);
		contentPane.add(btnOblicz);
		
		
		//przycisk do otwarcia okna edycji bazy danych
		JButton btnEdytuj = new JButton("Edytuj...");
		btnEdytuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					connection.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				General.this.dispose();
				EditDataBase edb=new EditDataBase();
				edb.setVisible(true);
				
			}
		});
		btnEdytuj.setBounds(652, 12, 89, 25);
		contentPane.add(btnEdytuj);
		

		
		
		//odswiezenie tabeli przy starcie okna
		refreshTable(comboBoxOption);
		refreshTable_1();
	}
}
