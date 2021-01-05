package de.z1up.supernick.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import de.z1up.supernick.SuperNick;
import org.bukkit.Bukkit;

public class SQL {

    private String                      host;
    private String                      port;
    private String                      database;
    private String                      username;
    private String                      password;
    private Connection                  con;
    private static ExecutorService      executor;

    private final String prefix = "§8[§6MySQL§8]" + " ";

    // construcotr

    /**
     * @param config
     */
    public SQL(HashMap<String, String> config) {
        this.host = config.get("host");
        this.port = config.get("port");
        this.database = config.get("database");
        this.username = config.get("username");
        this.password = config.get("password");
    }

    // methods

    /**
     * connect with database
     */
    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true",
                        username, password);
                executor = Executors.newCachedThreadPool();
                Bukkit.getConsoleSender().sendMessage(
                        prefix + "§a" + database + "@" + host + ":" + port + " Verbindung hergestellt!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * disconnect from database
     */
    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage(
                        prefix + "§c" + database + "@" + host + ":" + port + " Verbindung getrennt!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * return if is connected
     *
     * @return
     */
    public boolean isConnected() {
        return (con == null ? false : true);
    }

    /**
     * get connection to db
     *
     * @return
     */
    public Connection getConnection() {
        try {
            if (isConnected()) {
                if (con.isValid(2)) {
                    return con;
                } else {
                    con.close();
                    Bukkit.getConsoleSender().sendMessage(
                            prefix + "§c" + database + "@" + host + ":" + port + " Verbindung getrennt!");
                    con = DriverManager.getConnection(
                            "jdbc:mysql://" + host + ":" + port + "/"
                                    + database + "?autoReconnect=true", username, password);
                    Bukkit.getConsoleSender().sendMessage(
                            prefix + "§a" + database + "@" + host + ":" + port + " Verbindung hergestellt!");
                    return con;
                }
            } else {
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/"
                                + database + "?autoReconnect=true", username, password);
                Bukkit.getConsoleSender().sendMessage(prefix + "§a" + database + "@" + host + ":"
                        + port + " Verbindung hergestellt!");
                return con;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * get statement from db
     *
     * @param statement
     * @return
     */
    public PreparedStatement getSatement(String statement) {
        try {
            return getConnection().prepareStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * execute update to db
     *
     * @param query
     * @param values
     */
    public void executeUpdate(String query, List<Object> values) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            if (values != null) {
                int stmts = values.size();
                for (int i = 1; i <= stmts; i++) {
                    Object obj = values.get(i - 1);
                    if (obj instanceof String || obj instanceof UUID) {
                        ps.setString(i, obj.toString());
                    } else if (obj instanceof Integer) {
                        ps.setInt(i, Integer.parseInt(obj.toString()));
                    } else if (obj instanceof Long) {
                        ps.setLong(i, Long.parseLong(obj.toString()));
                    } else if (obj instanceof Boolean) {
                        ps.setInt(i, (Boolean.getBoolean(obj.toString()) ? 1 : 0));
                    } else {
                        ps.setString(i, obj.toString());
                    }
                }
            }
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * get if result exists in db
     *
     * @param table
     * @param where
     * @param is
     * @return
     */
    public boolean existResult(String table, String where, Object is) {
        ResultSet rs = getResult("SELECT " + where + " FROM " + table + " WHERE " + where + " = ?", Arrays.asList(is));
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * count result from db
     *
     * @param table
     * @param where
     * @param is
     * @return
     */
    public int countResult(String table, String where, Object is) {
        ResultSet rs = getResult("SELECT count(*) FROM " + table + " WHERE " + where + " = ?", Arrays.asList(is));
        try {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * get result from db with resultset
     *
     * @param query
     * @param values
     * @return
     */
    public ResultSet getResult(String query, List<Object> values) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            if (values != null) {
                int stmts = values.size();
                for (int i = 1; i <= stmts; i++) {
                    Object obj = values.get(i - 1);
                    if (obj instanceof String || obj instanceof UUID) {
                        ps.setString(i, obj.toString());
                    } else if (obj instanceof Integer) {
                        ps.setInt(i, Integer.parseInt(obj.toString()));
                    } else if (obj instanceof Long) {
                        ps.setLong(i, Long.parseLong(obj.toString()));
                    } else if (obj instanceof Boolean) {
                        ps.setInt(i, (Boolean.getBoolean(obj.toString()) ? 1 : 0));
                    } else {
                        ps.setString(i, obj.toString());
                    }
                }
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public ResultSet getResultAsync(String query, List<Object> values) {
        final ResultSet[] rs = new ResultSet[1];
        Bukkit.getScheduler().runTaskAsynchronously(SuperNick.getInstance(), () -> rs[0] = getResult(query, values));
        return rs[0];
    }

    // Asynchronous methods

    /**
     * execute update to db async
     *
     * @param query
     * @param values
     */
    public void executeUpdateAsync(String query, List<Object> values) {
        executor.execute(() -> executeUpdate(query, values));
    }

    /**
     * get result from db with resultset async
     *
     * @param query
     * @param values
     * @param consumer
     */
    public void getResultAsync(String query, List<Object> values, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = getResult(query, values);
            Bukkit.getScheduler().runTaskAsynchronously(SuperNick.getInstance(), () -> consumer.accept(result));
        });
    }


}