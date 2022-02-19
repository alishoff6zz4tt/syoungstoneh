package com.example.kidstodoapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilityUnitTest {

    @Test
    public void CheckSetInParentMode() {
        Utility.setInParentMode(false);
        boolean result = Utility.isInParentMode();
        assertTrue(result);
    }

}
