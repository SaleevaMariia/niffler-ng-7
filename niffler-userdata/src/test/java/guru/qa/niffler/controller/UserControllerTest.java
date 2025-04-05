package guru.qa.niffler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Sql(scripts = "/currentUserShouldBeReturned.sql")
  @Test
  void currentUserShouldBeReturned() throws Exception {
    mockMvc.perform(get("/internal/users/current")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("username", "dima")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("dima"))
            .andExpect(jsonPath("$.fullname").value("Dmitrii Tuchs"))
            .andExpect(jsonPath("$.currency").value("RUB"))
            .andExpect(jsonPath("$.photo").isNotEmpty())
            .andExpect(jsonPath("$.photoSmall").isNotEmpty());
  }

  @Sql(scripts = "/currentUserShouldBeReturned.sql")
  @Test
  void fullNameCanBeChanged() throws Exception {
    mockMvc.perform(post("/internal/users/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new UserJson(null, "dima", null, null,
                            "First Second", CurrencyValues.EUR, null, null, null)))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullname").value("First Second"))
            .andExpect(jsonPath("$.currency").value("EUR"));

  }
}