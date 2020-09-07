package me.physicsarebad.warps.commands;

import org.junit.Test;

import static org.junit.Assert.*;

public class WarpCommandTest {
    @Test
    public void verifyCommandName() {
        WarpCommand command = new WarpCommand();
        //Check main command
        assertTrue(command.onCommand(null, null, "warps", new String[0]));

        //Check aliases
        assertTrue(command.onCommand(null, null, "warp", new String[0]));
    }
}