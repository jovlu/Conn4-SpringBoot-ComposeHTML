package org.example.connectfour

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HelloWorldController::class)
@Import(GameConfigStore::class, SetupPageRenderer::class)
class HelloWorldControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun homePageShowsSetupScreen() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("Connect Four")))
            .andExpect(content().string(containsString("name=\"columns\"")))
            .andExpect(content().string(containsString("name=\"rows\"")))
            .andExpect(content().string(containsString("name=\"connect\"")))
            .andExpect(content().string(containsString("action=\"/game\"")))
            .andExpect(content().string(containsString("method=\"post\"")))
            .andExpect(content().string(containsString("Start Game")))
    }
}
