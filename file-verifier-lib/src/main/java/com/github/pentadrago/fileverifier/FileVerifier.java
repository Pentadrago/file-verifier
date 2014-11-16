package com.github.pentadrago.fileverifier;

import com.github.pentadrago.fileverifier.algorithm.VerificationAlgorithm;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import lombok.Setter;

/**
 * This class checks the checksums by given algorithm of a directory
 * recursively.
 *
 * @author Stefan Kloe
 */
public class FileVerifier {

    @Setter
    private MessageListener statusListener;

    @Setter
    private MessageListener errorListener;

    /**
     * validates all checksum files from given directory and all sub directories
     *
     * @param startDir directory to start validating
     * @param algorithm checksum algorithm to use
     * @return Map of Directories and checked files
     * @throws FileNotFoundException if the directory cannot be found
     */
    public Map<File, VerifierResult> verify(File startDir, VerificationAlgorithm algorithm) throws FileNotFoundException {
        if (startDir.isDirectory()) {
            statusListener.receive(String.format("processing directory %s", startDir.getPath()));
            VerifierResult result = new VerifierResult();
            List<File> subDirs = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            for (File file : startDir.listFiles()) {
                if (file.isDirectory()) {
                    subDirs.add(file);
                } else {
                    Matcher matcher = algorithm.getFilePattern().matcher(file.getName());
                    if (matcher.matches()) {
                        boolean isValid = false;
                        try {
                            String checksum = algorithm.computeChecksum(file);
                            if (matcher.group(1).equalsIgnoreCase(checksum)) {
                                statusListener.receive(String.format("%s OK", file.getName()));
                                isValid = true;
                            } else {
                                errorListener.receive(String.format("%s checksum mismatch [%s]", file.getPath(), checksum));
                            }
                        } catch (IOException ex) {
                            errorListener.receive(String.format("error loading %s - %s", file.getName(), ex.getCause()));
                        }
                        result.addFile(file.getName(), isValid);
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            long usedTime = (endTime - startTime) / 1000;
            Map<File, VerifierResult> resultMap = new LinkedHashMap<>();
            if (result.getFileCount() > 0) {
                statusListener.receive(String.format("processed %d files: %d OK - ran %d seconds", result.getFileCount(), result.getValidFileCount(), usedTime));
                resultMap.put(startDir, result);
            }
            for (File subDir : subDirs) {
                resultMap.putAll(verify(subDir, algorithm));
            }
            return resultMap;
        } else {
            throw new FileNotFoundException(String.format("error loading directory %s", startDir.getName()));
        }
    }

}
