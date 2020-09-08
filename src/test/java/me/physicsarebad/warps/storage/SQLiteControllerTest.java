package me.physicsarebad.warps.storage;

import org.bukkit.Material;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class SQLiteControllerTest {
    @Test
    public void createDatabase() {
        File database = new File("test.db");
        SQLiteController.init(database);

        assertTrue("Database was not created!", database.exists());
        database.delete();
    }

    @Test
    public void storeData() {
        File database = new File("test.db");
        SQLiteController.init(database);

        Connection connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());
        SQLiteController.addWarp(connection, "test",
                Material.ACACIA_BUTTON.toString(),
                "Test",
                "Test",
                0,
                0,
                0,
                0f,
                0f,
                "Password123",
                "INSERT INTO public_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password) VALUES(?,?,?,?,?,?,?,?,?,?)");

        connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt  = connection.createStatement();
            rs = stmt.executeQuery("SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password FROM public_warps");

            rs.next();

            assertEquals("Material is not the same", Material.ACACIA_BUTTON, Material.matchMaterial(rs.getString("material")));

            assertEquals("Name is not the same", "Test", rs.getString("name"));

            assertEquals("Password is not the same", "Password123", rs.getString("password"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }

            try {
                stmt.close();
            } catch (Exception e) {
            }

            try {
                connection.close();
            } catch (Exception e) {
            }
        }

        database.delete();
    }
}