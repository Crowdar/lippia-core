package com.crowdar.core;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

public class PDFManager {

    private static final String TEXT_NOT_PRESENT_ERROR_MESSAGE = "Text %s is not present in PDF.";

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

    public static void isTextPresentInPDF(String fileName, List<String> expectedsText) throws IOException {
        String pdfContent = getPdfContent(fileName);
        for (String expectedText : expectedsText) {
            Assert.assertTrue(pdfContent.contains(expectedText), String.format(TEXT_NOT_PRESENT_ERROR_MESSAGE, expectedText));
        }
    }
}
