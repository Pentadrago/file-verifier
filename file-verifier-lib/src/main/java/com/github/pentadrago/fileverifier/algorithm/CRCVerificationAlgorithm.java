package com.github.pentadrago.fileverifier.algorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * CRC32 implementation
 *
 * @author Stefan Kloe
 */
public class CRCVerificationAlgorithm implements VerificationAlgorithm {

    private Pattern pattern = Pattern.compile(".*[\\[|\\(]([0-9A-Fa-f]{8})[\\]|\\)].*");

    @Override
    public Pattern getFilePattern() {
        return pattern;
    }

    @Override
    public String computeChecksum(File file) throws IOException {
        String checksum;
        try (CheckedInputStream cis = new CheckedInputStream(new FileInputStream(file), new CRC32())) {
            byte[] buf = new byte[5000];
            while (cis.read(buf) != -1) {
            }
            checksum = Long.toString(cis.getChecksum().getValue(), 16);
        }
        if (checksum.length() < 8) {
            StringBuilder paddedChecksum = new StringBuilder(checksum);
            while (paddedChecksum.length() < 8) {
                paddedChecksum.insert(0, '0');
            }
            return paddedChecksum.toString();
        }
        return checksum;

    }

}
