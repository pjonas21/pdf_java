package org.example;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        PDFMergerUtility merger = new PDFMergerUtility();
        String caminhoPasta = "./arquivos";
        CombinePdf combinador = new CombinePdf(caminhoPasta, merger);
        combinador.combinePdf();

    }

}