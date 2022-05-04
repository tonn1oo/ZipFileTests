package tonioo;


import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class OpenZipFileTests {
    ClassLoader classLoader = OpenZipFileTests.class.getClassLoader();

    @Test
    void openPdfTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/junit.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("files/junit.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            assertThat(entry.getName()).isEqualTo("junit.pdf");
            try (InputStream inputStream = zf.getInputStream(entry)) {
                PDF pdf = new PDF(is);
                assertThat(pdf.numberOfPages).isEqualTo(166);
                assertThat(pdf.text).contains("JUnit 5");
            }
        }
    }

    @Test
    void openCsvTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/email.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("files/email.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            assertThat(entry.getName()).isEqualTo("email.csv");
            try (InputStream inputStream = zf.getInputStream(entry)) {
                try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                    List<String[]> cont = reader.readAll();
                    assertThat(cont.get(0)).contains("Login email", "Identifier", "First name", "Last name");
                }
            }
        }
    }

    @Test
    void openxlsxTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files/miritorg.zip"));
        ZipInputStream is = new ZipInputStream(classLoader.getResourceAsStream("files/miritorg.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            assertThat(entry.getName()).isEqualTo("miritorg.xlsx");
            try (InputStream inputStream = zf.getInputStream(entry)) {
                XLS xls = new XLS(is);
                String stringCellValue = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
                assertThat(stringCellValue).contains("horeca_order@agrohold.ru");
            }
        }
    }
}





