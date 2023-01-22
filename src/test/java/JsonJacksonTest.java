import com.fasterxml.jackson.databind.ObjectMapper;
import model.Person;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonJacksonTest {
    ClassLoader cl = FilesParsingTests.class.getClassLoader();
    @Test
    void jsonJacksonParsTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        try (
                InputStream stream = cl.getResourceAsStream("example/jsonExample.json");
                InputStreamReader reader = new InputStreamReader(stream)
        ) {
//            File fileJson = new File("src/test/resources/example/jsonExample.json");
            Person person = mapper.readValue(reader, Person.class);

            assertThat(person.id).isEqualTo(1);
            assertThat(person.name).isEqualTo("David Lynch");
            assertThat(person.email).isEqualTo("lynch@mail.ru");
            assertThat(person.address.street).isEqualTo("Prospekt");
            assertThat(person.address.appartment).isEqualTo("233");
            assertThat(person.children.get(0).age).isEqualTo("20");
            assertThat(person.children.get(0).name).isEqualTo("Laura");
        }
    }
}
