package com.example.kidstodoapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilityUnitTest {

    @Test
    public void CheckSetInParentMode() {
        ParentModeUtility.getInstance().setInParentMode(true);
        boolean result = ParentModeUtility.getInstance().isInParentMode();
        assertTrue(result);
    }

}
