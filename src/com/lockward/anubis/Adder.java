package com.lockward.anubis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Adder {
    private String inputFile, outputFile;

    public Adder(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void doAdd() throws IOException {
        NumberFormat format = new DecimalFormat();
        long total = 0;
        String line = null;

        System.out.println("Reading file: " + inputFile);
        try(BufferedReader br = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            while(null != (line = br.readLine())) {
                total += Long.parseLong(line);
            }
        }

        System.out.println("Writing to: " + outputFile);
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(outputFile), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE)) {
            bw.write("Total: " + format.format(total));
            bw.newLine();
        }
    }
}
