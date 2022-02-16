package com.example.kidstodoapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilityUnitTest {

    @Test
    public void CheckSetInParentMode() {
        Utility.setInParentMode(true);
        boolean result = Utility.isInParentMode();
        assertTrue(result);
    }

}