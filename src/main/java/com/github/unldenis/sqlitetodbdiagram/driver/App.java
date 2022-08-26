package com.github.unldenis.sqlitetodbdiagram.driver;

import static com.github.unldenis.sqlitetodbdiagram.Main.println;

import com.github.unldenis.sqlitetodbdiagram.util.UpdateChannel;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class App implements Runnable {

  private static final List<String> INVALID_LINES = Arrays.asList(
      "PRAGMA foreign_keys=OFF;",
      "COMMIT;"
  );
  private final String sqlitePath;
  private final String databasePath;

  private final Set<UpdateChannel> channels = new HashSet<>();

  private int currentProgress = 0;

  public App(String sqlitePath, String databasePath) {
    this.sqlitePath = sqlitePath;
    this.databasePath = databasePath;
  }

  public void addObserver(UpdateChannel channel) {
    this.channels.add(channel);
  }

  public void addProgress(int progress) {
    this.currentProgress += progress;
    for (UpdateChannel channel : this.channels) {
      channel.update(currentProgress);
    }
  }

  @Override
  public void run() {
    final String dumpSqlPath = new File(databasePath).getParent() + "\\dump.sql";

    addProgress(5);
    try {
      execute(sqlitePath, databasePath, dumpSqlPath);

      addProgress(20);

      Thread.sleep(300);

      File dumpSqlFile = new File(dumpSqlPath);
      if (!dumpSqlFile.exists()) {
        println("Dump file not created");
        return;
      }

      List<String> lines = Files.readAllLines(dumpSqlFile.toPath(), Charset.defaultCharset());
      lines.removeAll(INVALID_LINES);

      addProgress(10);

      new Selenium(this).start(lines);

      dumpSqlFile.delete();

      addProgress(5);
    } catch (IOException e) {
      println("Unexpected error: " + e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void execute(String sqliteExePath, String databasePath, String dumpSqlPath)
      throws IOException {
    List<String> params = Arrays.asList("cmd", "/c", sqliteExePath, databasePath, ".dump", ">",
        dumpSqlPath);
    new ProcessBuilder(params).start();
  }

  public String sqlitePath() {
    return sqlitePath;
  }

  public String databasePath() {
    return databasePath;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    App that = (App) obj;
    return Objects.equals(this.sqlitePath, that.sqlitePath) &&
        Objects.equals(this.databasePath, that.databasePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sqlitePath, databasePath);
  }

  @Override
  public String toString() {
    return "App[" +
        "sqlitePath=" + sqlitePath + ", " +
        "databasePath=" + databasePath + ']';
  }

}