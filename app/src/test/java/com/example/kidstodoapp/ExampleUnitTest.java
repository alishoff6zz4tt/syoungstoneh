package com.example.kidstodoapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void CheckSetInParentMode() {
        Utility.setInParentMode(true);
        boolean result = Utility.isInParentMode();
        assertTrue(result);
    }

}