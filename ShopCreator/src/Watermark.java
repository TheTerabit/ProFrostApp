
import java.io.FileOutputStream;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Watermark {
	
	
	
	
	
	public static void insertImg(String fileName){
		try{
		PdfReader reader = new PdfReader(fileName);
		  int n = reader.getNumberOfPages();

		  // Create a stamper that will copy the document to a new file
		  PdfStamper stamp = new PdfStamper(reader, 
		    new FileOutputStream(fileName+"2.pdf"));
		  int i = 1;
		  PdfContentByte under;
		  PdfContentByte over;

		  Image img = Image.getInstance("papier_firmowy.jpg");
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
		    over.setFontAndSize(bf, 18);
		    over.endText();

		    i++;
		  }

		  stamp.close();
		}catch(Exception e){
			System.out.println("error: "+e);
			
		}
	}

}
