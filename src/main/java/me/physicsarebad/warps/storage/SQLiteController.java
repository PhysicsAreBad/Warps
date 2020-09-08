package me.physicsarebad.warps.storage;

import me.physicsarebad.warps.guis.MainGUI;
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

        String sql;
        switch (type) {
            case PUBLIC:
            default:
                sql = "SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password FROM public_warps";
                return getWarps(conn, sql);
            case PRIVATE:
                sql = "SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password FROM private_warps";
                return getWarps(conn, sql);
            case SERVER:
                sql = "SELECT id, creator, material, name, world_name, x, y, z, pitch, yaw, password FROM server_warps";
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
                  Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("creator"))),
                        Material.matchMaterial(rs.getString("material")),
                        rs.getString("name"),
                        loc,
                        rs.getString("password"));

                warps.add(warp);
            }

            return warps;
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
                conn.close();
            } catch (Exception e) {
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
                + "	x integer,\n"
                + "	y integer,\n"
                + "	z integer,\n"
                + "	pitch float,\n"
                + "	yaw float,\n"
                + "	password text\n"
                + ");";

        Connection conn = null;
        Statement stmt = null;
        try  {
            // create a new table
            conn = getConnection(url);
            stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }

            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    public static void addWarp(MainGUI.WarpType type, Warp warp, File fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + fileName.getAbsolutePath();

        Connection conn = getConnection(url);

        String sql;
        switch (type) {
            case PUBLIC:
                sql = "INSERT INTO public_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password) VALUES(?,?,?,?,?,?,?,?,?,?)";

                addWarp(conn,
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
                        sql);

            case PRIVATE:
                sql = "INSERT INTO private_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password) VALUES(?,?,?,?,?,?,?,?,?,?)";

                addWarp(conn,
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
                        sql);
            case SERVER:
                sql = "INSERT INTO server_warps(creator,material,name,world_name,x,y,z,pitch,yaw,password) VALUES(?,?,?,?,?,?,?,?,?,?)";

                addWarp(conn,
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
                        sql);
        }
    }

    static void addWarp(Connection conn,
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
                                String sql) {
        PreparedStatement pstmt = null;
        try  {
            pstmt = conn.prepareStatement(sql);
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
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}