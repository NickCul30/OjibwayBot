/*
import java.awt.*;
import java.io.*;
import org.apache.pdfbox.text.*;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.io.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


public static ArrayList<String> pdf(String fileName){
	    PDFParser parser = null;
	    PDDocument pdDoc = null;
	    COSDocument cosDoc = null;
	    PDFTextStripper pdfStripper;
	    ArrayList<String> pagesList = new ArrayList<String>();

	    File file = new File(fileName);
	    try {
	        parser = new PDFParser(new RandomAccessBufferedFileInputStream(new FileInputStream(file)));
	        parser.parse();
	        cosDoc = parser.getDocument();
	        pdfStripper = new PDFTextStripper();
	        pdDoc = new PDDocument(cosDoc);


	        for(int i=1; i< pdDoc.getNumberOfPages()+1; i++)  // loop through pages.
	        {
	        	pdfStripper.setStartPage(i);
				pdfStripper.setEndPage(i);
	        	pagesList.add( pdfStripper.getText(pdDoc));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (cosDoc != null)
	                cosDoc.close();
	            if (pdDoc != null)
	                pdDoc.close();
	        } catch (Exception e1) {
	            e.printStackTrace();
	        }

	    }
		return pagesList;
	}

*/