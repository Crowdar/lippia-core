package com.crowdar.core;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFManager {

    public static String getPdfContent(String fileName) throws IOException {
        String text;
        PDDocument document = PDDocument.load(DownloadManager.getFile(fileName));
        try {
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();

                text = stripper.getText(document);
            } else {
                text = "PDF is encrypted";
            }
        } finally {
            document.close();
        }
        return text;
    }

    public static boolean isTextPresentInPDF(String fileName, String expectedText) throws IOException {
        return getPdfContent(fileName).contains(expectedText);
    }
}
