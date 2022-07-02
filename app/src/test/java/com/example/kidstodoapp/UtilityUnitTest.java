package com.example.kidstodoapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilityUnitTest {

    @Test
    public void CheckSetInParentMode() {
        ParentModeUtility.setInParentMode(true);
        boolean result = ParentModeUtility.isInParentMode();
        assertTrue(result);
    }

}
