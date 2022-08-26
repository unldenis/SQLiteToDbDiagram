package com.github.unldenis.sqlitetodbdiagram;

import com.github.unldenis.sqlitetodbdiagram.driver.App;
import com.github.unldenis.sqlitetodbdiagram.gui.AppFrame;
import com.github.unldenis.sqlitetodbdiagram.util.UpdateChannel;
import java.util.Date;

public class Main implements UpdateChannel {


  public static void main(String[] args) {
    final String title = String.format("%s v%s %s", "SQLiteToDbDiagram", "0.0.1",
        new Date());
    if (args.length == 0) {
      new AppFrame(title);
      return;
    }
    println(title);

    if (args.length == 1) {
      println(String.format("Missing '%s' argument.", "Database Path"));
      System.exit(1);
    }

    App app = new App(args[0], args[1]);
    app.addObserver(new Main());
    app.run();
  }

  public static void println(String x) {
    System.out.println(x);
  }

  @Override
  public void update(int progress) {
    System.out.printf("Progress %d%n", progress);
  }
}
