package big.company.output.impl;

import big.company.output.ConsoleOutputWriter;

public class SystemStandardOutWriter implements ConsoleOutputWriter {

  @Override
  public void write(String text) {
    System.out.println(text);
  }
}
