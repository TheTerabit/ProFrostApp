import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatingOffer implements Runnable {
   private String[][] chosen;//parametry oferty
   private String shopName;
   private int items;
   private double rabat;
   
   public CreatingOffer(String[][] wybrane, String sn, int it,int r) {//przekazywanie parametrow oferty
       chosen=wybrane;//tablica przedmiotow w ofercie
       shopName=sn;//nazwa oferty
       items=it;//liczba przedmiotow w ofercie
       rabat=r;//wielkosc rabatu
   }
   public static double round(double value, int places) {//zaokraglanie wyniku
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
   public static void insert(String name) {
		try{
		PdfReader reader = new PdfReader("Raport1.pdf");
		
	    int n = reader.getNumberOfPages();

		  // Create a stamper that will copy the document to a new file
		  PdfStamper stamp = new PdfStamper(reader, 
		    new FileOutputStream("Projekty\\"+name));
		  int i = 1;
		  PdfContentByte under;
		  PdfContentByte over;

		  Image img = Image.getInstance("Resources\\papier_firmowy.jpg");
		  BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, 
		    BaseFont.WINANSI, BaseFont.EMBEDDED);

		  img.setAbsolutePosition(0, 0);
		  img.scaleToFit(595,842);
		  while (i <= n) 
		  {
		    // Watermark under the existing page
		    under = stamp.getUnderContent(i);
		    under.addImage(img);

		    // Text over the existing page

		    over = stamp.getOverContent(i);

		    
		    over.beginText();
		   // over.setFontAndSize(bf, 35);
		    over.endText();

		    i++;
		  }

		  stamp.close();
		 // File file = new File("Raport1.pdf"); 
          reader.close();
	      //file.delete();
		}catch(Exception e){
			System.out.println("error: "+e);
			
		}
	}
   public void run() {
	   try{
	  BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);	    	
 	  BaseColor bc= new BaseColor(13,77,150);
 	  Font helvetica25=new Font(helvetica,25,Font.BOLD,bc);
 	  Font helvetica14=new Font(helvetica,14,Font.BOLD,BaseColor.WHITE);
 	  Font helvetica12=new Font(helvetica,12,Font.NORMAL,BaseColor.BLACK);
 	  Font helvetica17=new Font(helvetica,17,Font.BOLD,BaseColor.BLACK);

 	    
 	  		Document doc = new Document(); 
 	  		doc.setMargins(30, 30, 100, 70);
 	  		PdfWriter.getInstance(doc, new FileOutputStream("Raport1.pdf"));
 	  		doc.open();
		    //  Paragraph par = new Paragraph("ŒæŸó³:",helvetica25);
		    //  par.setFont(FontFactory.getFont(FontFactory.HELVETICA,35,Font.BOLD,BaseColor.RED));
			//  doc.add(par);
 	      doc.add(new Paragraph("Oferta: "+shopName,helvetica25));
      
 	      
 			String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

 			
 		doc.add(new Paragraph(timeStamp+"\n\n",helvetica17)); 
 	      
 	      
 	      
 	      
 	      
 	      
 	      
 	      
 	      
 	      
	      PdfPTable table = new PdfPTable(new float[] { 100, 10, 20 });
	      table.setHorizontalAlignment(Element.ALIGN_LEFT);
	      table.setWidthPercentage(100);
	      PdfPCell cell1 = new PdfPCell(new Paragraph("Nazwa elementu",helvetica14));
	      PdfPCell cell2 = new PdfPCell(new Paragraph("Iloœæ",helvetica14));
	      PdfPCell cell3 = new PdfPCell(new Paragraph("Koszt",helvetica14));
	      
	      
	      //cell.setColspan(4);
	      cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	      cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	      cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	      cell1.setBackgroundColor(bc);
	      cell2.setBackgroundColor(bc);
	      cell3.setBackgroundColor(bc);
	      
	      table.addCell(cell1);
	      table.addCell(cell2);
	      table.addCell(cell3);
	      
	      
	      
	      /////////////////////////////
	      Connection connection=sqliteConnection.dbConnector();
	   
	      
	   
	   
	  	double suma=0;
		for(int i=0;i<items;i++){
			
			suma+=Double.parseDouble(chosen[i][1]);
			
			if(chosen[i][1].charAt(chosen[i][1].length()-2)=='.'){
				chosen[i][1]+="0";
			}
			chosen[i][1].replaceAll(".", ",");
			chosen[i][1]+="z³";
			
			Paragraph px=new Paragraph(chosen[i][2],helvetica12);
			Paragraph py=new Paragraph(chosen[i][1],helvetica12);
			px.setAlignment(Element.ALIGN_CENTER);
			py.setAlignment(Element.ALIGN_RIGHT);
			PdfPCell c1= new PdfPCell();
			//c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			//c1.setPadding(2);
			c1.addElement(px);
			
			PdfPCell c2= new PdfPCell();
			//c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			//c1.setPadding(2);
			c2.addElement(py);
			
			table.addCell(new Paragraph(chosen[i][0],helvetica12));
		    table.addCell(c1);
		    table.addCell(c2);
			

		}
		
	   
	   
	   
	   
		doc.add(table);
	      
		//suma i rabat
				String s=Double.toString(suma);
				s.replaceAll(".", ",");
				s+="z³";
				
				Paragraph p1=new Paragraph("Koszt ca³kowity: "+s,helvetica12);
				p1.setAlignment(Element.ALIGN_RIGHT);
				
				Paragraph p2=new Paragraph("Rabat: "+rabat+"%",helvetica12);
				p2.setAlignment(Element.ALIGN_RIGHT);
				

				
				doc.add(p1);
				doc.add(p2);
				double rabat2=(100.00-rabat)/100.00;
				System.out.println(rabat2);
				double wynik=suma*rabat2;
				System.out.println(wynik);
				
				
				BigDecimal BDa = new BigDecimal(suma);
				 
				BigDecimal BDb = new BigDecimal(rabat2);

				BigDecimal BDc = BDa.multiply(BDb);
				BigDecimal scaled = BDc.setScale(2, RoundingMode.HALF_UP);
				
				
				Paragraph p3=new Paragraph("Do zap³aty: "+scaled+"z³",helvetica12);
				p3.setAlignment(Element.ALIGN_RIGHT);
				doc.add(p3);
				
		
				
				
		
	      
	    doc.close();
	   }catch(Exception e){
		   System.out.println(e);
	   }
	   insert("Oferta - "+shopName+".pdf");
	   /*
	   	//nowy dokument
	    XWPFDocument document = new XWPFDocument();
	    
	    //nag³ówek
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText("Oferta \""+shopName+"\": ");
		run.setFontSize(25);
		run.setColor("0D4D96");
		run.setUnderline(UnderlinePatterns.SINGLE);
		run.setBold(true);
		paragraph.setAlignment(ParagraphAlignment.LEFT);

		
		
		
		//data
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

		XWPFParagraph paragraphTime = document.createParagraph();
		XWPFRun runt = paragraphTime.createRun();
		runt.setText(timeStamp);
		runt.setFontSize(17);
		runt.setBold(true);
		paragraphTime.setAlignment(ParagraphAlignment.LEFT);

		
		
		
		
		
		//tabela
		XWPFTable table = document.createTable();
		table.setCellMargins(20, 200, 20, 200);		
		XWPFTableRow row0 = table.getRow(0);
		XWPFTableCell cell0=row0.getCell(0);
		XWPFTableCell cell2=row0.createCell();
		XWPFTableCell cell1=row0.createCell();
		
		cell2.setColor("0D4D96");
		cell0.setColor("0D4D96");
		cell1.setColor("0D4D96");
		
		
		//naglowki tabeli
		XWPFParagraph paragraph2 = cell2.addParagraph();
		XWPFRun run2 = paragraph2.createRun();
		run2.setText("Iloœæ");
		run2.setBold(true);
		run2.setColor("FFFFFF");
		run2.addBreak();
		run2.setFontSize(14);
		
		XWPFParagraph paragraph1 = cell1.addParagraph();
		XWPFRun run1 = paragraph1.createRun();
		run1.setText("Koszt");
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
		
		
		
		
		//uzupelnianie tabeli
		double suma=0;
		for(int i=0;i<items;i++){
			
			suma+=Double.parseDouble(chosen[i][1]);
			
			if(chosen[i][1].charAt(chosen[i][1].length()-2)=='.'){
				chosen[i][1]+="0";
			}
			chosen[i][1].replaceAll(".", ",");
			chosen[i][1]+="z³";
			
			XWPFTableRow row1=table.createRow();
			XWPFTableCell c1=row1.getCell(0);
			XWPFTableCell c2=row1.getCell(1);
			XWPFTableCell c3=row1.getCell(2);
			
			c1.setText(chosen[i][0]);
			c3.setText(chosen[i][1]);
			c2.setText(chosen[i][2]);
		}
		
		
		
		//suma i rabat
		String s=Double.toString(suma);
		s.replaceAll(".", ",");
		s+="z³";
		XWPFParagraph paragraph3 = document.createParagraph();
		XWPFRun run3 = paragraph3.createRun();
		run3.setText("Koszt ca³kowity: "+s);
		run3.setBold(true);
		run3.addBreak();
		run3.setText("Rabat: "+rabat+"%");
		run3.addBreak();
		System.out.println(suma+" "+rabat);
		double rabat2=(100.00-rabat)/100.00;
		System.out.println(rabat2);
		double wynik=suma*rabat2;
		System.out.println(wynik);
		
		
		BigDecimal BDa = new BigDecimal(suma);
		 
		BigDecimal BDb = new BigDecimal(rabat2);

		BigDecimal BDc = BDa.multiply(BDb);
		BigDecimal scaled = BDc.setScale(2, RoundingMode.HALF_UP);
		
		run3.setText("Do zap³aty: "+scaled+"z³");
		paragraph3.setAlignment(ParagraphAlignment.RIGHT);
		
		
		
		
		//zapis
		String title="Oferta - "+shopName+".docx";
		try{
			FileOutputStream output = new FileOutputStream(title);
			document.write(output);
			output.close();
			document.close();
			
		}catch(Exception  e1){
			JOptionPane.showMessageDialog(null, "B³¹d zapisu: "+e1);
		}
		*/
   }

}
