package com.melodyhub.ui;

import com.melodyhub.auth.AuthService;
import com.melodyhub.model.Song;
import com.melodyhub.service.DatabaseManager;
import com.melodyhub.service.MusicPlayer;
import com.melodyhub.service.RecommendationService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Stack;

public class MusicAppUI extends JFrame {
    private final MusicPlayer player = new MusicPlayer();
    private final DatabaseManager dbManager = new DatabaseManager();
    private final RecommendationService recommender = new RecommendationService();
    private final AuthService authService = new AuthService();
    private final Stack<Song> playHistory = new Stack<>();

    public MusicAppUI() {
        setTitle("MelodyHub");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (!showLogin()) System.exit(0);

        List<Song> songs = dbManager.getAllSongs();
        DefaultListModel<String> model = new DefaultListModel<>();
        songs.forEach(song -> model.addElement(song.getTitle()));

        JList<String> songList = new JList<>(model);
        songList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(songList);

        JButton playBtn = new JButton("▶ Play");
        JButton recBtn = new JButton("🎵 Recommendations");

        JTextArea nowPlaying = new JTextArea();
        nowPlaying.setEditable(false);
        nowPlaying.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nowPlaying.setBackground(Color.WHITE);
        nowPlaying.setMargin(new Insets(10, 10, 10, 10));
        nowPlaying.setBorder(BorderFactory.createTitledBorder("Now Playing"));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        playBtn.addActionListener(e -> {
            int index = songList.getSelectedIndex();
            if (index >= 0) {
                Song song = songs.get(index);
                player.playSong(song);
                playHistory.push(song);
                nowPlaying.setText("Playing: " + song.getTitle() + " by " + song.getArtist());
                new Timer(100, event -> {
                    int val = progressBar.getValue();
                    if (val < 100) progressBar.setValue(val + 1);
                    else ((Timer) event.getSource()).stop();
                }).start();
            }
        });

        recBtn.addActionListener(e -> {
            List<Song> recs = recommender.recommend(songs, playHistory);
            if (recs.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No recommendations available.");
            } else {
                StringBuilder sb = new StringBuilder("Recommended Songs:\n");
                for (Song s : recs) sb.append(s).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        });

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.add(playBtn);
        controlPanel.add(recBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        add(new JLabel("  🎧 Song Library", SwingConstants.LEFT), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.WEST);
        add(nowPlaying, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private boolean showLogin() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authService.login(username, password)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                return true;
            } else {
                int opt = JOptionPane.showConfirmDialog(null, "User not found. Register?");
                if (opt == JOptionPane.YES_OPTION) {
                    return authService.register(username, password);
                }
            }
        }
        return false;
    }
}
