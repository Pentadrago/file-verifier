package com.github.pentadrago.fileverifier.swing;

import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author Stefan Kloe
 */
public class SwingFileVerifierTest {

    private FrameFixture window;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setUp() {
        SwingFileVerifier frame = GuiActionRunner.execute(new GuiQuery<SwingFileVerifier>() {
            @Override
            protected SwingFileVerifier executeInEDT() {
                return new SwingFileVerifier();
            }
        });
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

}
