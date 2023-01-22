import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import model.Glossary;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.google.common.net.MediaType.ZIP;
import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTests {
    ClassLoader cl = FilesParsingTests.class.getClassLoader();

    @Test
    void pdfParseTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadedPdf = $("a[href='junit-user-guide-5.9.2.pdf']").download();
        PDF pdf = new PDF(downloadedPdf);
        assertThat(pdf.text).contains("Table of Contents");
    }

    @Test
    void xlsParseTest() throws Exception {
        try (InputStream xl = cl.getResourceAsStream("example/sample-xlsx-file.xlsx")) {
            XLS xls = new XLS(xl);
            assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(2)
                    .getStringCellValue()).contains("Last Name");
        }
    }

    @Test
    void csvParseTest() throws Exception {
        try (
                InputStream stream = cl.getResourceAsStream("example/qa_guru.csv");
                CSVReader csv = new CSVReader(new InputStreamReader(stream))
        ) {
            List<String[]> content = csv.readAll();
            assertThat(content.get(0)[1]).contains("lesson");
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream stream = cl.getResourceAsStream("example/glossary.json");
                InputStreamReader resource = new InputStreamReader(stream);
        ) {
            JsonObject jsonObject = gson.fromJson(resource, JsonObject.class);
            assertThat(jsonObject.get("title").getAsString()).isEqualTo("example glossary");
            assertThat(jsonObject.get("gloss_div").getAsJsonObject().get("title").getAsString()).isEqualTo("S");
        }
    }

    @Test
    void jsonParseImprovedTest() throws Exception {
        Gson gson = new Gson();
        try (
                InputStream stream = cl.getResourceAsStream("example/glossary.json");
                InputStreamReader reader = new InputStreamReader(stream)
        ) {
            Glossary jsonObject = gson.fromJson(reader, Glossary.class);
            assertThat(jsonObject.title).isEqualTo("example glossary");
            assertThat(jsonObject.glossDiv.flag).isTrue();
        }
    }
    @Test
    void zipXlsxParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/sample-xlsx-file.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                XLS xls = new XLS(zis);

                assertThat(entry.getName()).contains("sample-xlsx");
                assertThat(xls.excel.getSheetAt(0).getRow(0).getCell(2)
                        .getStringCellValue()).contains("Last Name");
            }
        }
    }

    @Test
    void zipCSVParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/qa_guru_csv.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                   CSVReader csv = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = csv.readAll();
                    assertThat(content.get(0)[1]).contains("lesson");
            }
        }
    }

    @Test
    void zipPdfParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/AppointmentLetter_pdf.zip");
                ZipInputStream zis = new ZipInputStream(resource)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                PDF pdf = new PDF(zis);

                assertThat(entry.getName()).contains("AppointmentLetter.pdf");
                assertThat(pdf.text).contains("Визовый центр BLS");
            }
        }
    }
}
