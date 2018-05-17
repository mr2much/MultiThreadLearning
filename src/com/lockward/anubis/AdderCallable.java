package com.lockward.anubis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class AdderCallable implements Callable<Long> {
    private String inputFile;

    public AdderCallable(String inputFile) {
        this.inputFile = inputFile;
    }

    public long doAdd() throws IOException {
        long result = 0;

        System.out.println("Reading file: " + inputFile);
        String line;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(inputFile),
                StandardCharsets.UTF_8)) {
            while((line = br.readLine()) != null) {
                result += Long.parseLong(line);
            }
        }

        return result;
    }

    @Override
    public Long call() throws IOException {
        return doAdd();
    }
}
