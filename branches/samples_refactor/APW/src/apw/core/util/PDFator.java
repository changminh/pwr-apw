/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 * 
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer  in  the  documentation and / or other materials
 *     provided with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */
package apw.core.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Greg Matoga <greg dot matoga at gmail dot com>
 */
public class PDFator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PDFator ev = new PDFator();
        ev.getReport();
        ev.presentUserWithDoc();
    }
    private PdfWriter writer;
    private File peadophile = new File("HelloWorld.pdf");

    Document getReport() {
        Document d = new Document();
        try {
            if (peadophile.exists())
                peadophile.delete();
            writer = PdfWriter.getInstance(d, new FileOutputStream(peadophile));
            d.addTitle("KA report");
            d.addAuthor("Greg Matoga");
            d.addSubject("This example explains how to add metadata.");
            d.addKeywords("iText, Hello World, step 3, metadata");
            d.addCreator("My program using iText");
            d.open();
            PdfPTable t = new PdfPTable(40);

            for (int i = 0; i < 40; i++)
                for (int j = 0; j < 40; j++)
                    t.addCell("" + i + "; " + j);
            d.add(t);
//            Chunk fox = new Chunk("quick brown fox");
//            float superscript = 8.0f;
//            fox.setTextRise(superscript);
//            fox.setBackground(new Color(0xFF, 0xDE, 0xAD));
//            Chunk jumps = new Chunk(" jumps over ");
//            Chunk dog = new Chunk("the lazy dog");
//            float subscript = -8.0f;
//            dog.setTextRise(subscript);
//            dog.setUnderline(new Color(0xFF, 0x00, 0x00), 3.0f, 0.0f, -5.0f + subscript, 0.0f, PdfContentByte.LINE_CAP_ROUND);
//
//            d.add(fox);
//            d.add(jumps);
//            d.add(dog);
//            d.add(new Table(2, 2));

            File file;
            ConcurrentHashMap<File, File[]> cache = new ConcurrentHashMap<File, File[]>(250, .75f, 2);

            // make a PdfTemplate with the vertical text
            PdfTemplate template = writer.getDirectContent().createTemplate(20, 20);
            BaseFont bf = BaseFont.createFont("Helvetica", "winansi", false);
            String text = "Vertical";
            float size = 16;
            float width = bf.getWidthPoint(text, size);
            template.beginText();
            template.setRGBColorFillF(1, 1, 1);
            template.setFontAndSize(bf, size);
            template.setTextMatrix(0, 2);
            template.showText(text);
            template.endText();
            template.setWidth(width);
            template.setHeight(size + 2);
            // make an Image object from the template
            Image img = Image.getInstance(template);
            img.setRotationDegrees(90);
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            PdfPCell cell = new PdfPCell(img);
            cell.setPadding(4);
            cell.setBackgroundColor(new Color(0, 0, 255));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("I see a template on my right");
            table.addCell(cell);
            table.addCell("I see a template on my left");
            table.addCell(cell);
            table.addCell("I see a template everywhere");
            table.addCell(cell);
            table.addCell("I see a template on my right");
            table.addCell(cell);
            table.addCell("I see a template on my left");
            d.add(table);
            d.close();
        } catch (IOException ex) {
            Logger.getLogger(PDFator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException dec) {
            Logger.getLogger(PDFator.class.getName()).log(Level.SEVERE, null, dec);
        }

        return d;
    }

    public boolean presentUserWithDoc() {
        if (!Desktop.isDesktopSupported())
            return false;
        Desktop desk = Desktop.getDesktop();
        try {
            desk.open(peadophile);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public static void print(double[][] d) {
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++)
                System.out.printf("%9.4f ", d[i][j]);
            System.out.println();
        }
    }
}
