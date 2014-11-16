/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.pentadrago.fileverifier.cli;

import com.github.pentadrago.fileverifier.FileVerifier;
import com.github.pentadrago.fileverifier.VerifierResult;
import com.github.pentadrago.fileverifier.algorithm.CRCVerificationAlgorithm;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * simple command line interface for file-verifier
 *
 * @author Stefan Kloe
 */
public class FileVerifierCLI {

    private static final Logger logger = LoggerFactory.getLogger(FileVerifierCLI.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        FileVerifier verifier = new FileVerifier();
        verifier.setStatusListener((String message) -> {
            logger.info(message);
        });
        verifier.setErrorListener((String message) -> {
            logger.error(message);
        });
        File root = new File(".");
        Map<File, VerifierResult> results = verifier.verify(root, new CRCVerificationAlgorithm());
        boolean everythingAlright = true;
        logger.info("\n\nresults:");
        for (Map.Entry<File, VerifierResult> entry : results.entrySet()) {
            StringBuilder message = new StringBuilder(entry.getKey().getPath()).append(": ");
            VerifierResult result = entry.getValue();
            message.append(result);
            logger.info(message.toString());
            if (!result.isAlright()) {
                everythingAlright = false;
                result.getInvalidFiles().stream().forEach((filename) -> {
                    logger.info(String.format(" -> %s", filename));
                });
            }
        }
        if (everythingAlright) {
            System.exit(0);
        } else {
            System.exit(-1);
        }

    }

}
