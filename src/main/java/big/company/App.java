package big.company;

import big.company.application.Initializer;
import java.util.Objects;

public class App {

  public static void main(String[] args) {
    Initializer
        .initApplication()
        .runAnalysis(getFilePath(args));
  }

  private static String getFilePath(String[] args) {
    if (Objects.isNull(args) || args.length == 0) {
      System.err.println("File path is not provided");
      System.exit(1);
    }
    return args[0];
  }
}
