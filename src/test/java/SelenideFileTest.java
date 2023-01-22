import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelenideFileTest {

    @Test
    void selenideDownloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File file = $("#raw-url").download();
        try (InputStream in = new FileInputStream(file)) {
            byte[] bytes = in.readAllBytes();
            String text = new String(bytes, StandardCharsets.UTF_8);
            assertTrue(text.contains("This repository is the home of the next generation of JUnit, _JUnit 5_."));
            assertThat(text).contains("This repository is the home of the next generation of JUnit, _JUnit 5_.");
        }
    }

    @Test
    void selenideUploadFile() {
        open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("example/cat.png");
        $("div.qq-file-name").shouldHave(Condition.text("cat.png"));
        $("span.qq-upload-file-selector")
                .shouldHave(Condition.attribute("title","cat.png"));
        $("span.qq-edit-filename-icon")
                .shouldHave(Condition.attribute("aria-label","Edit filename"));
    }
}
