package me.physicsarebad.warps.guis;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainGUITest {
    @Test
    public void testGUIName() {
        //Public Warps
        assertEquals("Failed Public Warps name test", "Public Warps", MainGUI.WarpType.PUBLIC.name+" Warps");

        //Private Warps
        assertEquals("Failed Private Warps name test", "Private Warps", MainGUI.WarpType.PRIVATE.name+" Warps");

        //Server Warps
        assertEquals("Failed Server Warps name test", "Server Warps", MainGUI.WarpType.PUBLIC.name+" Warps");
    }
}