package com.axiastudio.iwas;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: tiziano
 * Date: 11/10/14
 * Time: 10:01
 */
public class IWas {

    private PdfReader reader = null;
    private PdfStamper stamper = null;
    private List<Stamp> stamps = new ArrayList<>();
    private Collection<Integer> pages = null;
    private Float xOffset = 0f;
    private Float yOffset = 0f;
    private Float xPosition = 0f;
    private Float yPosition = 0f;

    public static IWas create() {
        return new IWas();
    }

    public IWas load(InputStream inputStream) throws IOException {
        reader = new PdfReader(inputStream);
        return this;
    }

    /**
     * The offset of the stamp
     *
     * @param x
     * @param y
     *
     */
    public IWas offset(Float x, Float y) {
        xOffset = x;
        yOffset = y;
        return this;
    }
    /**
     * Sets the pages for the stamp
     *
     * @param pages List of pages
     *
     */
    public IWas pages(Collection<Integer> pages){
        this.pages = pages;
        return this;
    }

    /**
     * Stamp a Datamatrix
     *
     * @param code The code to generate
     * @param size The size of the Datamatrix
     *
     */
    public IWas datamatrix(String code, DatamatrixSize size, Float x, Float y, Float scale) {
        Stamp stamp = new Stamp(code, StampType.DATAMATRIX);
        stamp.setPosition(x, y);
        stamp.setDatamatrixSize(size);
        stamp.setScale(scale);
        stamps.add(stamp);
        return this;
    }
    public IWas datamatrix(String code, Float x, Float y, Float scale) {
        return datamatrix(code, DatamatrixSize._20x20, x, y, scale);
    }
    public IWas datamatrix(String code, Float x, Float y) {
        return datamatrix(code, DatamatrixSize._20x20, x, y, 1f);
    }

    /**
     * Stamp a text line
     *
     * @param text The text
     *
     */
    public IWas text(String text, Integer size, Float x, Float y, Float rotation) {
        Stamp stamp = new Stamp(text, StampType.TEXT);
        stamp.setPosition(x, y);
        stamp.setFontiSize(size);
        stamp.setRotation(rotation);
        stamps.add(stamp);
        return this;
    }
    public IWas text(String text, Integer size, Float x, Float y) {
        return text(text, size, x, y, 0f);
    }
    public IWas text(String text) {
        return text(text, 24, 0f, 0f, 0f);
    }

    public Boolean toStream(OutputStream outputStream) throws IOException {
        try {
            stamper = new PdfStamper(reader, outputStream);
            Integer i=0;
            while( true ){
                i++;
                if( pages != null && !pages.contains(i) ){
                    break;
                }
                PdfContentByte content = stamper.getOverContent(i);
                if( content == null ){
                    break;
                }
                for( Stamp stamp: stamps ){
                    switch( stamp.getType() ){
                        case DATAMATRIX:
                            Image datamatrix = getDatamatrix(stamp.getCode(), stamp.getDatamatrixHeight(),
                                    stamp.getDatamatrixWidth(), stamp.getDatamatrixScale());
                            datamatrix.setAbsolutePosition(xOffset + stamp.getX(), yOffset + stamp.getY());
                            content.addImage(datamatrix);
                            break;
                        case TEXT:
                            Phrase phrase = getText(stamp.getCode(), stamp.getFontSize());
                            ColumnText.showTextAligned(content, Element.ALIGN_LEFT, phrase, xOffset + stamp.getX(), yOffset + stamp.getY(), stamp.getRotation());
                            break;
                    }
                }
            }
            stamper.close();
            return Boolean.TRUE;
        } catch (DocumentException e) {
            return Boolean.FALSE;
        }

    }

    private static Image getDatamatrix(String code, Integer height, Integer width)
            throws UnsupportedEncodingException, BadElementException {
        return getDatamatrix(code, height, width, 1f);
    }

    private static Image getDatamatrix(String code, Integer height, Integer width, Float scale)
            throws UnsupportedEncodingException, BadElementException {
        BarcodeDatamatrix bc = new BarcodeDatamatrix();
        bc.setOptions(BarcodeDatamatrix.DM_ASCII);
        bc.setHeight(height);
        bc.setWidth(width);
        bc.generate(code);
        Image image = bc.createImage();
        image.scaleAbsolute(image.getWidth()*scale, image.getHeight()*scale);
        return image;
    }

    private static Phrase getText(String text, Integer size) {
        Phrase phrase = new Phrase(text, new Font(Font.FontFamily.HELVETICA, size, Font.NORMAL, BaseColor.BLACK));
        return phrase;
    }
}
