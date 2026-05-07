package org.example.connectfour

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(GameController::class)
@Import(GameConfigStore::class, GamePageRenderer::class)
class GameControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var gameConfigStore: GameConfigStore

    @BeforeEach
    fun resetConfig() {
        gameConfigStore.reset()
    }

    @Test
    fun gamePageUsesClassicDefaultsBeforeSetup() {
        mockMvc.perform(get("/game"))
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("Rows: 6")))
            .andExpect(content().string(containsString("Columns: 7")))
            .andExpect(content().string(containsString("Connect: 4")))
            .andExpect(content().string(containsString("Player 1 turn")))
            .andExpect(content().string(containsString("id=\"board\"")))
    }

    @Test
    fun gamePageStoresSubmittedConfig() {
        mockMvc.perform(
            post("/game")
                .param("columns", "9")
                .param("rows", "8")
                .param("connect", "5"),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(content().string(containsString("Rows: 8")))
            .andExpect(content().string(containsString("Columns: 9")))
            .andExpect(content().string(containsString("Connect: 5")))
            .andExpect(content().string(containsString("Drop pieces into a column.")))
            .andExpect(content().string(containsString("Restart")))

        mockMvc.perform(get("/game"))
            .andExpect(status().isOk)
            .andExpect(content().string(containsString("Rows: 8")))
            .andExpect(content().string(containsString("Columns: 9")))
            .andExpect(content().string(containsString("Connect: 5")))
    }
}
