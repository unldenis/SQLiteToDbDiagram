package com.github.unldenis.sqlitetodbdiagram.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

class FileChooser extends JFrame implements ActionListener {

  private final AppFrame appFrame;

  public FileChooser(AppFrame appFrame) {
    this.appFrame = appFrame;
  }

  @Override
  public void actionPerformed(ActionEvent evt) {

    if (evt.getSource() == appFrame.sqlite3_button()) {
      JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

      j.setAcceptAllFileFilterUsed(false);

      j.setDialogTitle("Select a .exe file");

      FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .exe files", "exe");
      j.addChoosableFileFilter(restrict);

      int r = j.showSaveDialog(null);

      if (r == JFileChooser.APPROVE_OPTION) {
        appFrame.sqlite3_path(j.getSelectedFile().getAbsolutePath());
        appFrame.sqlite3_button().setEnabled(false);
        appFrame.checkStart();
      } else {
        System.err.println("The user cancelled the operation");
      }
    } else {
      JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

      j.setAcceptAllFileFilterUsed(false);

      j.setDialogTitle("Select a .db file");

      FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .db files", "db");
      j.addChoosableFileFilter(restrict);

      int r = j.showOpenDialog(null);

      if (r == JFileChooser.APPROVE_OPTION) {
        appFrame.database_path(j.getSelectedFile().getAbsolutePath());
        appFrame.database_button().setEnabled(false);
        appFrame.checkStart();
      } else {
        System.err.println("the user cancelled the operation");
      }
    }
  }
}
