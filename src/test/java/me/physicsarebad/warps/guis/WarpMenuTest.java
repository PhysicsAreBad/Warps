package me.physicsarebad.warps.guis;

import org.junit.Test;

import static org.junit.Assert.*;

public class WarpMenuTest {
    @Test
    public void pageSizeTest() {
        //Check to see if the system will attempt to go to the last page if the last element is already on the page.
        assertTrue("Failed last page test", WarpMenu.isPageLast(4, 4*51));

        //Check to see if the system will attempt to go to the last page if the last element is not on the page.
        assertFalse("Failed non-last page test", WarpMenu.isPageLast(4, 4*51+1));
        assertFalse("Failed non-last page test", WarpMenu.isPageLast(4, 4*51+25));
    }
}