package com.example.personColorAPI.service;

import com.example.personColorAPI.config.ColorConfig;
import com.example.personColorAPI.config.FileConfig;
import com.example.personColorAPI.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CsvFileReaderService}.
 * This class tests the logic for reading CSV files and converting them into {@link Person} objects.
 */
@ExtendWith(MockitoExtension.class)
class CsvFileReaderServiceTest {

    public static final String FILE_PATH = "fake";
    public static final String RUN_TIME_EXCEPTION_MESSAGE = "Error processing the CSV file";
    public static final String IO_EXCEPTION_MESSAGE = "IOException";
    public static final String VALID_CSV_ROW = "Song,Juy,12345 leipzig,1";
    public static final String CSV_ROW_WITHOUT_leipzig = "Martin,Samos,67890 ,12345";
    public static final String CSV_ROW_WITHOUT_leipzig_AND_ZIP_CODE = "Martin,Samos, ,12345";
    public static final String INVALID_CSV_ROW = "Invalid,Row,Without,ProperData";

    @Mock
    private FileConfig fileConfig;

    @Mock
    private ColorConfig colorConfig;

    @InjectMocks
    private CsvFileReaderService csvFileReaderService;

    /**
     * Tests reading a CSV file with valid rows, missing data, and invalid data.
     * Verifies that the data is correctly processed into Person objects.
     */
    @Test
    void testReadCsvFile_withMultipleScenarios_success() throws IOException {
        when(fileConfig.getFilePath()).thenReturn(FILE_PATH);

        try (MockedConstruction<FileReader> mockedFileReader = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                     (mock, context) -> {
                         when(mock.readLine()).thenReturn(VALID_CSV_ROW)
                                 .thenReturn(CSV_ROW_WITHOUT_leipzig)
                                 .thenReturn(CSV_ROW_WITHOUT_leipzig_AND_ZIP_CODE)
                                 .thenReturn(INVALID_CSV_ROW)
                                 .thenReturn(null);
                     })) {

            when(colorConfig.getColors()).thenReturn(Map.of(1, "red", 12345, "blue"));

            List<Person> persons = csvFileReaderService.readCsvFile();

            assertNotNull(persons);
            assertEquals(3, persons.size());
        }
    }

    /**
     * Tests if the method handles the case when the color map is null.
     * Verifies that the system doesn't crash if color data is unavailable.
     */
    @Test
    void testReadCsvFile_whenColorsMapIsNull_thenHandleGracefully() throws IOException {
        when(fileConfig.getFilePath()).thenReturn(FILE_PATH);

        try (MockedConstruction<FileReader> mockedFileReader = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                     (mock, context) -> {
                         when(mock.readLine()).thenReturn(VALID_CSV_ROW)
                                 .thenReturn(null);
                     })) {

            when(colorConfig.getColors()).thenReturn(null);

            List<Person> persons = csvFileReaderService.readCsvFile();

            assertNotNull(persons);
            assertEquals(1, persons.size());
        }
    }

    /**
     * Tests if an IOException is correctly handled when reading the CSV file.
     */
    @Test
    void testReadCsvFile_withIOException() {
        when(fileConfig.getFilePath()).thenReturn(FILE_PATH);

        try (MockedConstruction<FileReader> mockedFileReader = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                     (mock, context) -> {
                         when(mock.readLine()).thenThrow(new IOException(IO_EXCEPTION_MESSAGE));
                     })) {

            IOException ioException = assertThrows(IOException.class, () -> {
                csvFileReaderService.readCsvFile();
            });

            assertEquals(IO_EXCEPTION_MESSAGE, ioException.getMessage());
        }
    }

    /**
     * Tests if a RuntimeException is correctly thrown when there's an error processing the CSV file.
     */
    @Test
    void testReadCsvFile_withRuntimeException() {
        when(fileConfig.getFilePath()).thenReturn(FILE_PATH);

        try (MockedConstruction<FileReader> mockedFileReader = mockConstruction(FileReader.class);
             MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                     (mock, context) -> {
                         when(mock.readLine()).thenThrow(new RuntimeException(RUN_TIME_EXCEPTION_MESSAGE));
                     })) {

            RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
                csvFileReaderService.readCsvFile();
            });

            assertEquals(RUN_TIME_EXCEPTION_MESSAGE, runtimeException.getMessage());
        }
    }
}