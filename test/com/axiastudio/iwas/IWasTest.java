package com.axiastudio.iwas;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

public class IWasTest {

    private static String INPUT_FILE = "input.pdf";
    private static String OUTPUT_FILE = "output.pdf";

    @BeforeClass
    public static void setUpClass() throws Exception {
        // delete old files
        delFile(OUTPUT_FILE);
        delFile(INPUT_FILE);

        // test pdf
        Document document = new Document(new Rectangle(PageSize.A4));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(INPUT_FILE));
        document.open();
        document.add(new Paragraph("Hi there! I'm a test pdf!"));
        document.close();
    }

    @Test
    public void test() throws Exception {

        IWas.create()
                .load(new FileInputStream(INPUT_FILE))
                .offset(30f, 700f)
                .text("COMUNE", 10, 0f, 42f)
                .text("di Mori", 10, 0f, 34f)
                .text("Prot.N.", 8, 0f, 25f)
                .text("201300007719", 10, 0f, 16f)
                .text("04-04-13 08:11", 8, 0f, 8f)
                .text("f_728", 8, 0f, 0f)
                .datamatrix("c_f728#201300007719#673dfc1792", DatamatrixSize._22x22, 75f, 15f, 1.6f)
                .toStream(new FileOutputStream(OUTPUT_FILE));

    }

    private static Boolean delFile(String fileName) {
        File delenda = new File(fileName);
        if( delenda.exists() ){
            return delenda.delete();
        }
        return Boolean.FALSE;
    }

}