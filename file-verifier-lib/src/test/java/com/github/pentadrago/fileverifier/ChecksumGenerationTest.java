package com.github.pentadrago.fileverifier;

import com.github.pentadrago.fileverifier.algorithm.CRCVerificationAlgorithm;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Map;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Stefan Kloe
 */
public class ChecksumGenerationTest {

    private FileVerifier verifier;

    private Map<File, VerifierResult> fixturesResult;

    private static File rootDir;
    
    @BeforeClass
    public static void init() throws URISyntaxException {
        rootDir = new File(ChecksumGenerationTest.class.getClassLoader().getResource("fixtures").toURI());
    }

    private FileVerifier getVerifier() {
        if (verifier == null) {
            FileVerifier ver = new FileVerifier();
            MessageListener nopMessageListener = (String message) -> {
                // do nothing
            };
            ver.setStatusListener(nopMessageListener);
            ver.setErrorListener(nopMessageListener);
            verifier = ver;
        }
        return verifier;
    }

    private Map<File, VerifierResult> getFixturesResult() throws FileNotFoundException {
        return getVerifier().verify(rootDir, new CRCVerificationAlgorithm());
    }

    @Test
    public void correctChecksum() throws FileNotFoundException {
        File dir = new File(rootDir, "correctChecksum");
        Assert.assertTrue(getVerifier().verify(dir, new CRCVerificationAlgorithm()).get(dir).isAlright());
    }

    @Test
    public void incorrectChecksum() throws FileNotFoundException {
        File dir = new File(rootDir, "incorrectChecksum");
        Assert.assertFalse(getVerifier().verify(dir, new CRCVerificationAlgorithm()).get(dir).isAlright());
    }

    @Test
    public void correctChecksumRecursive() throws FileNotFoundException {
        File subDir = new File(rootDir, "correctChecksum");
        Assert.assertTrue(getFixturesResult().get(subDir).isAlright());
    }

    @Test
    public void incorrectChecksumRecursive() throws FileNotFoundException {
        File subDir = new File(rootDir, "incorrectChecksum");
        Assert.assertFalse(getFixturesResult().get(subDir).isAlright());
    }

    @Test
    public void notExistingChecksumRecursive() throws FileNotFoundException {
        File subDir = new File(rootDir, "notExisting");
        Assert.assertFalse(getFixturesResult().containsKey(subDir));
    }
}
