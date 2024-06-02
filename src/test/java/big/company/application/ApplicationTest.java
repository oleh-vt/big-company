package big.company.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import big.company.exception.BigCompanyApplicationException;
import big.company.input.EmployeesFileInputReader;
import big.company.output.impl.SystemStandardOutWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ApplicationTest {

  private static final String NEW_LINE = System.lineSeparator();
  private final PrintStream systemStandardOut = System.out;
  private final ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();

  private static final String FILE_NAME = "employees.csv";

  private static final String FILE_CONTENT = """
      id,firstName,lastName,salary,managerId
      1,FirstName1,LastName1,13000,
      2,FirstName2,LastName2,10000,1
      3,FirstName3,LastName3,10000,1
      4,FirstName4,LastName4,11000,1
      5,FirstName5,LastName5,8000,2
      6,FirstName6,LastName6,9000,2
      7,FirstName7,LastName7,8200,3
      8,FirstName8,LastName8,8000,3
      9,FirstName9,LastName9,8000,4
      10,FirstName10,LastName10,6000,7
      11,FirstName11,LastName11,6500,7
      12,FirstName12,LastName12,5000,10
      13,FirstName13,LastName13,4000,11
      14,FirstName14,LastName14,5000,11
      15,FirstName15,LastName15,4500,11
      16,FirstName16,LastName16,4000,14
      17,FirstName17,LastName17,3000,16
      """;

  private static final String EXPECTED_OUTPUT =
      "MANAGERS WHICH EARN LESS OR MORE THAN THEY SHOULD" + NEW_LINE
          + "  ID    FIRST NAME    LAST NAME    MISPAYMENT    VARIANCE  " + NEW_LINE
          + "  2     FirstName2    LastName2    Underpaid     200.0     " + NEW_LINE
          + NEW_LINE
          + "EMPLOYEES WITH TOO LONG REPORTING LINE" + NEW_LINE
          + "  ID    FIRST NAME     LAST NAME     REPORTING LINE EXCESS  " + NEW_LINE
          + "  17    FirstName17    LastName17    1                      " + NEW_LINE
          + NEW_LINE;

  @TempDir
  private Path tempDir;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outputCaptor));
  }

  @AfterEach
  void tearDown() {
    System.setOut(systemStandardOut);
  }

  @DisplayName("Should read the CSV file with employees and print the analysis reports")
  @Test
  void shouldRunAnalysisAndPrintReport() {
    Application application = Initializer.initApplication();
    prepareFile();

    application.runAnalysis(tempDir.resolve(FILE_NAME).toString());

    assertEquals(EXPECTED_OUTPUT, outputCaptor.toString());
  }

  @DisplayName("When application error occurs, should print error message")
  @Test
  void shouldPrintErrorMessageWhenApplicationErrorOccurs() {
    String errorMessage = "error message";
    EmployeesFileInputReader throwingInputReaderStub = path -> {
      throw new BigCompanyApplicationException(errorMessage);
    };
    Application application = new Application(throwingInputReaderStub, employees -> null, report -> "", new SystemStandardOutWriter());
    String expectedOutput = "Error: error message" + NEW_LINE;

    application.runAnalysis("");

    assertEquals(expectedOutput, outputCaptor.toString());
  }

  private void prepareFile() {
    try {
      Path file = tempDir.resolve(FILE_NAME);
      Files.createFile(file);
      Files.write(file, FILE_CONTENT.getBytes(), StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}