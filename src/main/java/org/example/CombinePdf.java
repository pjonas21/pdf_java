package org.example;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CombinePdf {
    private final String caminhoPasta;
    private final  PDFMergerUtility merger;
    private final String newFileName;

    public CombinePdf(String caminhoPasta, PDFMergerUtility merger, String newFileName) {
        this.caminhoPasta = caminhoPasta;
        this.merger = merger;
        this.newFileName = newFileName;
    }

    public void combinePdf() throws FileNotFoundException {
        File pasta = new File(this.caminhoPasta);

        if(pasta.isDirectory()) {
        	File[] pastaArquivos = Objects.requireNonNull(pasta.listFiles());
            List<File> pdfFiles = new ArrayList<>(Arrays.asList(pastaArquivos));
            Collections.sort(pdfFiles);
            for (File arq : pdfFiles) {
                this.merger.addSource(new File(String.valueOf(arq)));
            }
            this.merger.setDestinationFileName(this.newFileName + ".pdf");
            try {
                this.merger.mergeDocuments(null);
                System.out.println("PDF mesclado com sucesso.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
