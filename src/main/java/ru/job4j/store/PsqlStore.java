package ru.job4j.store;

import ru.job4j.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection cnn;

    public PsqlStore(Properties cfg)
                                throws ClassNotFoundException, SQLException {
        Class.forName(cfg.getProperty("jdbc.driver"));
        cnn = DriverManager.getConnection(
                        cfg.getProperty("jdbc.url"),
                        cfg.getProperty("jdbc.username"),
                        cfg.getProperty("jdbc.password")
        );
    }

    private Post getPostFromDb(ResultSet rs) {
        Post post = new Post();
        try {
            post.setId(rs.getInt("id"));
            post.setTitle(rs.getString("name"));
            post.setDescription(rs.getString("text"));
            post.setLink(rs.getString("link"));
            post.setCreated(rs.getTimestamp("created").toLocalDateTime());
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return post;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = cnn.prepareStatement(
                "INSERT INTO post (name, text, link, created) "
                + "VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
            try (ResultSet generatedKey = ps.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    post.setId(generatedKey.getInt("id"));
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement ps = cnn.prepareStatement(
                "SELECT * FROM post ORDER BY created DESC"
        )) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                   posts.add(
                           getPostFromDb(rs)
                   );
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        try (PreparedStatement ps = cnn.prepareStatement(
                "SELECT * FROM post WHERE id = ?"
        )) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getPostFromDb(rs);
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws SQLException {
        if (cnn != null) {
            cnn.close();
        }
    }
}
