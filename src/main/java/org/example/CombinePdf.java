package org.example;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class CombinePdf {
    private final String caminhoPasta;
    private final PDFMergerUtility merger;

    public CombinePdf(String caminhoPasta, PDFMergerUtility merger) {
        this.caminhoPasta = caminhoPasta;
        this.merger = merger;
    }

    public void combinePdf() throws FileNotFoundException {
        File pasta = new File(this.caminhoPasta);

        if(pasta.isDirectory()) {
            List<File> pdfFiles = List.of(Objects.requireNonNull(pasta.listFiles()));
            for (File arq : pdfFiles) {
                this.merger.addSource(new File(String.valueOf(arq)));
            }
            this.merger.setDestinationFileName("pdf_combinado.pdf");
            try {
                this.merger.mergeDocuments(null);
                System.out.println("PDF mesclado com sucesso.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
