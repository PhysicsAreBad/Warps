package me.physicsarebad.warps.commands;

import org.junit.Test;

import static org.junit.Assert.*;

public class WarpCommandTest {
    @Test
    public void verifyCommandName() {
        WarpCommand command = new WarpCommand();
        //Check main command
        assertTrue("Failed to run command when using /warps", command.onCommand(null, null, "warps", new String[0]));

        //Check aliases
        assertTrue("Failed to run command when using /warp", command.onCommand(null, null, "warp", new String[0]));
    }
}