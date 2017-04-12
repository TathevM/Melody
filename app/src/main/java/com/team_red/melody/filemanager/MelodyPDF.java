package com.team_red.melody.filemanager;


import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.team_red.melody.app.MelodyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.team_red.melody.filemanager.MelodyFileManager.EXPORTED_FILE_DIRECTORY;

public class MelodyPDF {

    public MelodyPDF() {
    }

    public void export(ArrayList<String> input){
        Document doc = new Document();
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            String username = MelodyApplication.getLoggedInUser().getUserName();
            File dir = new File(root.getAbsolutePath() + EXPORTED_FILE_DIRECTORY);
            dir.mkdirs();
            File pdfFile = new File(dir, username + " " + ".pdf");

            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, outputStream);
            doc.open();

            Paragraph paragraph = new Paragraph(input.get(0));
            BaseFont baseFont = BaseFont.createFont("assets/fonts/Melody.ttf", BaseFont.IDENTITY_V, BaseFont.EMBEDDED);
            Font font = new Font(baseFont);
            paragraph.setFont(font);
            doc.add(paragraph);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            doc.close();
        }
    }
}
