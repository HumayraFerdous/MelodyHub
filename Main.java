
package com.melodyhub;

import com.melodyhub.ui.MusicAppUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MusicAppUI().setVisible(true));
    }
}
