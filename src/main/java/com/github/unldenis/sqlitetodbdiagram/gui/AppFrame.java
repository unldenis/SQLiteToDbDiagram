package com.github.unldenis.sqlitetodbdiagram.gui;

import com.github.unldenis.sqlitetodbdiagram.driver.App;
import com.github.unldenis.sqlitetodbdiagram.util.UpdateChannel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class AppFrame extends JFrame implements UpdateChannel {

  private JButton sqlite3_button;
  private JButton database_button;

  private String sqlite3_path;
  private String database_path;

  private JProgressBar progressBar;

  public AppFrame(String title) {
    super(title);

    this.prepareFrame();

    setResizable(false);
//    setLocationRelativeTo(null);
    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void prepareFrame() {
    JPanel p = new JPanel();

    FileChooser f1 = new FileChooser(this);

    sqlite3_button = new JButton("sqlite3.exe");
    sqlite3_button.addActionListener(f1);

    database_button = new JButton("database.db");
    database_button.addActionListener(f1);

    progressBar = new JProgressBar();
    progressBar.setValue(0);

    p.add(sqlite3_button);
    p.add(database_button);
    p.add(progressBar);

    add(p);


  }


  public void checkStart() {
    if (sqlite3_path == null || database_path == null) {
      return;
    }
    sqlite3_button.setEnabled(false);
    database_button.setEnabled(false);

    App app = new App(sqlite3_path, database_path);
    app.addObserver(this);
    Thread t = new Thread(app);
    t.start();
  }

  //================================================================================
  // Update Channel
  //================================================================================

  @Override
  public void update(int progress) {
    progressBar.setValue(progress);
  }

  //================================================================================
  // Getters & Setters
  //================================================================================


  public JButton sqlite3_button() {
    return sqlite3_button;
  }

  public void sqlite3_button(JButton sqlite3_button) {
    this.sqlite3_button = sqlite3_button;
  }

  public JButton database_button() {
    return database_button;
  }

  public void database_button(JButton database_button) {
    this.database_button = database_button;
  }

  public String sqlite3_path() {
    return sqlite3_path;
  }

  public void sqlite3_path(String sqlite3_path) {
    this.sqlite3_path = sqlite3_path;
  }

  public String database_path() {
    return database_path;
  }

  public void database_path(String database_path) {
    this.database_path = database_path;
  }
}
