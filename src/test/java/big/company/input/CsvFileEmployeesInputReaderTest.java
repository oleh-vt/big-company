package big.company.input;

import big.company.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvFileEmployeesInputReaderTest {

    private static final String EMPLOYEES_CSV = "employees.csv";
    private static final String HEADER = "id,firstName,lastName,salary,managerId" + System.lineSeparator();


    private EmployeesInputReader employeesInputReader;

    @TempDir
    private Path tempDir;

    private Path csvFile;

    @BeforeEach
    void setUp() throws IOException {
        csvFile = Files.createFile(tempDir.resolve(EMPLOYEES_CSV));
        employeesInputReader = new CsvFileEmployeesInputReader(csvFile);
    }

    @DisplayName("Should parse employee when all fields are present")
    @Test
    void shouldReadEmployeeWithAllFieldsSet() {
        prepareCsvFile("124 , Martin, Chekov, 45000, 123");
        var expectedEmployeeDto = new Employee(124, "Martin", "Chekov", 45000, 123L);

        List<Employee> employees = employeesInputReader.read();

        assertEquals(employees.size(), 1);
        assertEquals(employees.get(0), expectedEmployeeDto);
    }

    @DisplayName("Should skip blank lines")
    @Test
    void shouldSkipBlankLinesInFile() {
        prepareCsvFile("""
                
                124 , Martin, Chekov, 45000, 123
                """);
        var expectedEmployeeDto = new Employee(124, "Martin", "Chekov", 45000, 123L);

        List<Employee> employees = employeesInputReader.read();

        assertEquals(employees.size(), 1);
        assertEquals(employees.get(0), expectedEmployeeDto);
    }

    @DisplayName("Should parse employee when managerId field is not set")
    @Test
    void shouldReadEmployeeWhenManagerIdFieldIsNotSet() {
        prepareCsvFile("123, Joe, Doe, 60000, ");
        var expectedEmployeeDto = new Employee(123, "Joe", "Doe", 60000, null);

        List<Employee> employees = employeesInputReader.read();

        assertEquals(employees.size(), 1);
        assertEquals(employees.get(0), expectedEmployeeDto);
    }

    @DisplayName("When any of the required fields is blank, should throw an exception")
    @ParameterizedTest
    @ValueSource(strings = {"id", "firstName", "lastName", "salary"})
    void shouldThrowExceptionWhenAnyOfTheRequiredFieldsBlank(String requiredField) {
        String csvRow = "id,firstName,lastName,salary";
        prepareCsvFile(csvRow.replace(requiredField, " "));

        assertThrows(IllegalStateException.class, () -> employeesInputReader.read());
    }

    @DisplayName("When a row is incomplete, should throw an exception")
    @Test
    void shouldThrowExceptionWhenRowIsIncomplete() {
        prepareCsvFile("123, Joe, Doe");
        assertThrows(IllegalStateException.class, () -> employeesInputReader.read());
    }

    private void prepareCsvFile(String csv) {
        try {
            Files.write(csvFile, HEADER.concat(csv).getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}