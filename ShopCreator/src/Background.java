
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class Background {

	
	public static void insert(String name, int[] tab) {
		generate(name,tab);
		try{
		PdfReader reader = new PdfReader("Raport.pdf");
		
	    int n = reader.getNumberOfPages();

		  // Create a stamper that will copy the document to a new file
		  PdfStamper stamp = new PdfStamper(reader, 
		    new FileOutputStream("Projekty\\"+name+".pdf"));
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
		    over.newlineText();
		    over.newlineText();
		    over.newlineText();
		    //
		    
		    //
		    
		    
		    over.beginText();
		   // over.setFontAndSize(bf, 35);
		    over.endText();

		    i++;
		  }
		  
		  stamp.close();
		  reader.close();
		}catch(Exception e){
			System.out.println("error: "+e);
			
		}
	}
	
	public static void generate(String name, int[] order){
		                   
		      try{

		    	  
		    	  BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);	    	
		    	  BaseColor bc= new BaseColor(13,77,150);
		    	  Font helvetica25=new Font(helvetica,25,Font.BOLD,bc);
		    	  Font helvetica14=new Font(helvetica,14,Font.BOLD,BaseColor.WHITE);
		    	  Font helvetica12=new Font(helvetica,12,Font.NORMAL,BaseColor.BLACK);
		    	  Font helvetica12B=new Font(helvetica,12,Font.BOLD,BaseColor.BLACK);
		    	  

		    	  
		    	    
		    	  Document doc = new Document(); 
		    	  doc.setMargins(30, 30, 100, 70);
		    	  PdfWriter.getInstance(doc, new FileOutputStream("Raport.pdf"));
		    	  doc.open();
				    //  Paragraph par = new Paragraph("ŒæŸó³:",helvetica25);
				    //  par.setFont(FontFactory.getFont(FontFactory.HELVETICA,35,Font.BOLD,BaseColor.RED));
					//  doc.add(par);
		    	  doc.add(new Paragraph("Czêœci do zamówienia:",helvetica25));
		    	  doc.add(new Paragraph(name+"\n\n",helvetica12B));
		         
			      PdfPTable table = new PdfPTable(new float[] { 75, 25 });
			      PdfPCell cell1 = new PdfPCell(new Paragraph("Nazwa elementu",helvetica14));
			      PdfPCell cell2 = new PdfPCell(new Paragraph("Iloœæ sztuk",helvetica14));
			      table.setWidthPercentage(100);
			      
			      
			      //cell.setColspan(4);
			      cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			      cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			      cell1.setBackgroundColor(bc);
			      cell2.setBackgroundColor(bc);
			      
			      table.addCell(cell1);
			      table.addCell(cell2);
			      
			      
			      /////////////////////////////
			      Connection connection=sqliteConnection.dbConnector();
			      for(int i=0;i<10000;i++){
						if(order[i]>0){

							try{
							String query="select Nazwa from Elementy where Id=?";
							PreparedStatement pst=connection.prepareStatement(query);
							pst.setString(1, Integer.toString(i));
							ResultSet rs=pst.executeQuery();
							String componentName = rs.getString(1);
							
							PdfPCell cell3 = new PdfPCell(new Paragraph(componentName,helvetica12));
							PdfPCell cell4 = new PdfPCell(new Paragraph(Integer.toString(order[i]),helvetica12));

						    cell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
							
							table.addCell(cell3);
						    table.addCell(cell4);
							
							pst.close();
							rs.close();
							}catch(Exception e1){
								JOptionPane.showMessageDialog(null, e1);
							}
						}
					}
					
			      
			      /////////////////////////////
			      
			      
			      doc.add(table);
		      
		      
			      doc.close();
		      }catch(Exception e){
		    	  System.out.println(e);
		      }
		      System.out.println("Table created successfully..");   
		       
		
	}

}
