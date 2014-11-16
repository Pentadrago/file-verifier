package com.github.pentadrago.fileverifier;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * this class represents the result of the checksum verification on a directory
 *
 * @author Stefan Kloe
 *
 */
public class VerifierResult {

    @Getter
    private int fileCount = 0;

    @Getter
    private final List<String> invalidFiles = new ArrayList<>();

    /**
     * adds a file to the result
     *
     * @param filename filename of checked file
     * @param isValid true if check was successful
     */
    public void addFile(String filename, boolean isValid) {
        if (!isValid) {
            invalidFiles.add(filename);
        }
        fileCount++;
    }

    /**
     *
     * @return the number of valid files in the result
     */
    public int getValidFileCount() {
        return fileCount - getInvalidFileCount();
    }

    /**
     *
     * @return the number of invalid files in the result
     */
    public int getInvalidFileCount() {
        return invalidFiles.size();
    }

    /**
     *
     * @return true if no invalid files were found
     */
    public boolean isAlright() {
        return invalidFiles.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder message = new StringBuilder();
        message.append(getValidFileCount());
        message.append("/").append(getFileCount());
        if (isAlright()) {
            message.append(" OK");
        } else {
            message.append(" FAILURE");
        }
        return message.toString();
    }

}
