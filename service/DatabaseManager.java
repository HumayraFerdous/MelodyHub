package com.melodyhub.service;

import com.melodyhub.model.Song;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/new_schema","root","*****");
    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                songs.add(new Song(rs.getInt("id"), rs.getString("title"),
                                   rs.getString("artist"), rs.getString("genre"),rs.getString("path")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }
}
