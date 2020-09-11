package me.physicsarebad.warps.storage;

import me.physicsarebad.warps.guis.MainGUI;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLiteController {

    public static void init(File fileName) {
        createNewTable("public_warps", fileName.getAbsolutePath());
        createNewTable("private_warps", fileName.getAbsolutePath());
        createNewTable("server_warps", fileName.getAbsolutePath());
    }

    public static List<Warp> getWarps(MainGUI.WarpType type, File file) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + file.getAbsolutePath();

        Connection conn = getConnection(url);

        Validate.notNull(conn, "Database connection failed!");

        String sql;
        switch (type) {
            case PUBLIC:
            default:
                sql = "SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password, glow FROM public_warps";
                return getWarps(conn, sql);
            case PRIVATE:
                sql = "SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password, glow FROM private_warps";
                return getWarps(conn, sql);
            case SERVER:
                sql = "SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password, glow FROM server_warps";
                return getWarps(conn, sql);
        }
    }

    private static List<Warp> getWarps(Connection conn, String sql){

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt  = conn.createStatement();
            rs = stmt.executeQuery(sql);
            // loop through the result set
            List<Warp> warps = new ArrayList<>();
            while (rs.next()) {
                Location loc = new Location(
                        Bukkit.getWorld(rs.getString("world_name")),
                        rs.getInt("x"),
                        rs.getInt("y"),
                        rs.getInt("z"),
                        rs.getFloat("pitch"),
                        rs.getFloat("yaw"));

                Warp warp = new Warp(
                        rs.getInt("id"),
                        Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("creator"))),
                        Material.matchMaterial(rs.getString("material")),
                        rs.getString("name"),
                        loc,
                        rs.getString("password"),
                        rs.getBoolean("glow"));

                warps.add(warp);
            }

            return warps;
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
                Validate.notNull(conn);
                conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return new ArrayList<>();
    }

    public static void createNewTable(String tableName, String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName;

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	creator text NOT NULL,\n"
                + "	material text NOT NULL,\n"
                + "	name text NOT NULL,\n"
                + "	world_name text NOT NULL,\n"
                + "	x integer NOT NULL,\n"
                + "	y integer NOT NULL,\n"
                + "	z integer NOT NULL,\n"
                + "	pitch float NOT NULL,\n"
                + "	yaw float NOT NULL,\n"
                + "	password text,\n"
                + "	glow bool NOT NULL\n"
                + ");";

        Connection conn = null;
        Statement stmt = null;
        try {
            // create a new table
            conn = getConnection(url);
            Validate.notNull(conn);
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Validate.notNull(stmt);
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                Validate.notNull(conn);
                conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void addWarp(MainGUI.WarpType type, Warp warp, File fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName.getAbsolutePath();

        Connection conn = getConnection(url);
        Validate.notNull(conn);

        String sql;
        switch (type) {
            case PUBLIC:
                sql = "INSERT INTO public_warps(id,creator,material,name,world_name,x,y,z,pitch,yaw,password,glow) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                addWarp(conn,
                        warp.getId(),
                        warp.getCreator().getUniqueId().toString(),
                        warp.getMaterial().toString(),
                        warp.getName(),
                        warp.getWarpLocation().getWorld().getName(),
                        warp.getWarpLocation().getBlockX(),
                        warp.getWarpLocation().getBlockY(),
                        warp.getWarpLocation().getBlockZ(),
                        warp.getWarpLocation().getPitch(),
                        warp.getWarpLocation().getYaw(),
                        warp.getPassword(),
                        warp.getGlow(),
                        sql);

            case PRIVATE:
                sql = "INSERT INTO private_warps(id,creator,material,name,world_name,x,y,z,pitch,yaw,password,glow) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                addWarp(conn,
                        warp.getId(),
                        warp.getCreator().getUniqueId().toString(),
                        warp.getMaterial().toString(),
                        warp.getName(),
                        warp.getWarpLocation().getWorld().getName(),
                        warp.getWarpLocation().getBlockX(),
                        warp.getWarpLocation().getBlockY(),
                        warp.getWarpLocation().getBlockZ(),
                        warp.getWarpLocation().getPitch(),
                        warp.getWarpLocation().getYaw(),
                        warp.getPassword(),
                        warp.getGlow(),
                        sql);
            case SERVER:
                sql = "INSERT INTO server_warps(id,creator,material,name,world_name,x,y,z,pitch,yaw,password,glow) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

                addWarp(conn,
                        warp.getId(),
                        warp.getCreator().getUniqueId().toString(),
                        warp.getMaterial().toString(),
                        warp.getName(),
                        warp.getWarpLocation().getWorld().getName(),
                        warp.getWarpLocation().getBlockX(),
                        warp.getWarpLocation().getBlockY(),
                        warp.getWarpLocation().getBlockZ(),
                        warp.getWarpLocation().getPitch(),
                        warp.getWarpLocation().getYaw(),
                        warp.getPassword(),
                        warp.getGlow(),
                        sql);
        }
    }

    static void addWarp(Connection conn,
                                int id,
                                String creatorUUID,
                                String material,
                                String name,
                                String worldName,
                                int x,
                                int y,
                                int z,
                                float pitch,
                                float yaw,
                                String password,
                                boolean glow,
                                String sql) {
        PreparedStatement pstmt = null;
        try  {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, creatorUUID);
            pstmt.setString(3, material);
            pstmt.setString(4, name);
            pstmt.setString(5, worldName);
            pstmt.setInt(6, x);
            pstmt.setInt(7, y);
            pstmt.setInt(8, z);
            pstmt.setFloat(9, pitch);
            pstmt.setFloat(10, yaw);
            pstmt.setString(11, password);
            pstmt.setBoolean(12, glow);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Validate.notNull(pstmt);
                pstmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void delete(int id, File file, MainGUI.WarpType type) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + file.getAbsolutePath();

        String sql = "";

        switch (type) {
            case PRIVATE:
                sql = "DELETE FROM private_warps WHERE id = ?";
                break;
            case PUBLIC:
                sql = "DELETE FROM public_warps WHERE id = ?";
                break;
            case SERVER:
                sql = "DELETE FROM server_warps WHERE id = ?";
                break;
        }

        Connection conn = getConnection(url);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {

            }

            try {
                conn.close();
            } catch (Exception e) {

            }
        }
    }

    public static void update(MainGUI.WarpType type, Warp warp, int id, File file) {
        switch (type) {
            case PUBLIC:
                update(file,
                        id,
                        warp.getCreator().getUniqueId().toString(),
                        warp.getMaterial().toString(),
                        warp.getName(),
                        warp.getWarpLocation().getWorld().getName(),
                        warp.getWarpLocation().getBlockX(),
                        warp.getWarpLocation().getBlockY(),
                        warp.getWarpLocation().getBlockZ(),
                        warp.getWarpLocation().getPitch(),
                        warp.getWarpLocation().getYaw(),
                        warp.getPassword(),
                        warp.getGlow(),
                        "public_warps");
                break;
            case SERVER:
                update(file,
                        id,
                        warp.getCreator().getUniqueId().toString(),
                        warp.getMaterial().toString(),
                        warp.getName(),
                        warp.getWarpLocation().getWorld().getName(),
                        warp.getWarpLocation().getBlockX(),
                        warp.getWarpLocation().getBlockY(),
                        warp.getWarpLocation().getBlockZ(),
                        warp.getWarpLocation().getPitch(),
                        warp.getWarpLocation().getYaw(),
                        warp.getPassword(),
                        warp.getGlow(),
                        "server_warps");
                break;
            case PRIVATE:
                update(file,
                        id,
                        warp.getCreator().getUniqueId().toString(),
                        warp.getMaterial().toString(),
                        warp.getName(),
                        warp.getWarpLocation().getWorld().getName(),
                        warp.getWarpLocation().getBlockX(),
                        warp.getWarpLocation().getBlockY(),
                        warp.getWarpLocation().getBlockZ(),
                        warp.getWarpLocation().getPitch(),
                        warp.getWarpLocation().getYaw(),
                        warp.getPassword(),
                        warp.getGlow(),
                        "private_warps");
        }
    }

    static void update(File file,
                              int id,
                              String creatorUUID,
                              String material,
                              String name,
                              String worldName,
                              int x,
                              int y,
                              int z,
                              float pitch,
                              float yaw,
                              String password,
                              boolean glow,
                              String tableName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + file.getAbsolutePath();

        String sql = "UPDATE " + tableName + " SET creator = ? , "
                + "material = ?, "
                + "name = ?, "
                + "world_name = ?, "
                + "x = ?, "
                + "y = ?, "
                + "z = ?, "
                + "pitch = ?, "
                + "yaw = ?, "
                + "password = ?, "
                + "glow = ? "
                + "WHERE id = ?";

        Connection conn = getConnection(url);
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, creatorUUID);
            pstmt.setString(2, material);
            pstmt.setString(3, name);
            pstmt.setString(4, worldName);
            pstmt.setInt(5, x);
            pstmt.setInt(6, y);
            pstmt.setInt(7, z);
            pstmt.setFloat(8, pitch);
            pstmt.setFloat(9, yaw);
            pstmt.setString(10, password);
            pstmt.setBoolean(11, glow);
            pstmt.setInt(12, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {

            }

            try {
                conn.close();
            } catch (Exception e) {

            }
        }
    }

    static Connection getConnection(String url) {
        try  {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
