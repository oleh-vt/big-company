package big.company.output;

public class SystemStandardOutWriter implements ConsoleOutputWriter{

  @Override
  public void write(String text) {
    System.out.println(text);
  }
}
