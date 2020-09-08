package me.physicsarebad.warps.storage;

import me.physicsarebad.warps.guis.MainGUI;
import org.apache.commons.lang.Validate;
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

        Validate.notNull(connection);

        SQLiteController.addWarp(connection,
                1,
                "test",
                Material.ACACIA_BUTTON.toString(),
                "Test",
                "Test",
                0,
                0,
                0,
                0f,
                0f,
                "Password123",
                true,
                "INSERT INTO public_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password,glow) VALUES(?,?,?,?,?,?,?,?,?,?,?)");

        connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        Validate.notNull(connection);

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt  = connection.createStatement();
            rs = stmt.executeQuery("SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password, glow FROM public_warps");

            rs.next();

            assertEquals("Material is not the same", Material.ACACIA_BUTTON, Material.matchMaterial(rs.getString("material")));

            assertEquals("Name is not the same", "Test", rs.getString("name"));

            assertEquals("Password is not the same", "Password123", rs.getString("password"));

            assertTrue("Glow is not the same", rs.getBoolean("glow"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Validate.notNull(rs);
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Validate.notNull(stmt);
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        database.delete();
    }

    @Test
    public void updateData() {
        File database = new File("test.db");
        SQLiteController.init(database);

        Connection connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        Validate.notNull(connection);

        SQLiteController.addWarp(connection,
                1,
                "test",
                Material.ACACIA_BUTTON.toString(),
                "Test",
                "Test",
                0,
                0,
                0,
                0f,
                0f,
                "Password123",
                true,
                "INSERT INTO public_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password,glow) VALUES(?,?,?,?,?,?,?,?,?,?,?)");

        connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        Validate.notNull(connection);

        SQLiteController.update(database,
                1,
                "UpdateTest",
                "Test",
                Material.ACACIA_BUTTON.toString(),
                "Test",
                0,
                0,
                0,
                0f,
                0f,
                "Password123",
                true,
                "public_warps"
                );

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt  = connection.createStatement();
            rs = stmt.executeQuery("SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password, glow FROM public_warps");
            rs.next();
            assertEquals("Database update failed!", "UpdateTest", rs.getString(2));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Validate.notNull(rs);
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Validate.notNull(stmt);
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        database.delete();
    }

    @Test
    public void removeData() {
        File database = new File("test.db");
        SQLiteController.init(database);

        Connection connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        Validate.notNull(connection);

        SQLiteController.addWarp(connection,
                1,
                "test",
                Material.ACACIA_BUTTON.toString(),
                "Test",
                "Test",
                0,
                0,
                0,
                0f,
                0f,
                "Password123",
                true,
                "INSERT INTO public_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password,glow) VALUES(?,?,?,?,?,?,?,?,?,?,?)");


        connection = SQLiteController.getConnection("jdbc:sqlite:" + database.getAbsolutePath());

        Validate.notNull(connection);

        Statement stmt = null;
        ResultSet rs = null;
        try {
            SQLiteController.delete(1, database, MainGUI.WarpType.PUBLIC);
            stmt  = connection.createStatement();
            rs = stmt.executeQuery("SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password, glow FROM public_warps");
            assertFalse("Database deletion failed!", rs.next());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Validate.notNull(rs);
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Validate.notNull(stmt);
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                connection.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        database.delete();
    }
}