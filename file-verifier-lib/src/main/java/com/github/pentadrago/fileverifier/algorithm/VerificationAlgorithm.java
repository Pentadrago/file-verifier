/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.pentadrago.fileverifier.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * interface for different checksum implementations
 *
 * @author Stefan Kloe
 */
public interface VerificationAlgorithm {

    /**
     * return a pattern to find and extract checksum information in filenames
     *
     * @return RegEx pattern
     */
    Pattern getFilePattern();

    /**
     * return the checksum of a given file
     *
     * @param file which checksum should be computed
     * @return checksum of given file
     * @throws IOException if the file could not be processed
     */
    String computeChecksum(File file) throws IOException;

}
