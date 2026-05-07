package org.example.connectfour

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference

data class GameConfig(
    val columns: Int = 7,
    val rows: Int = 6,
    val connect: Int = 4,
)

@Component
class GameConfigStore {
    private val config = AtomicReference(GameConfig())

    fun current(): GameConfig = config.get()

    fun reset() {
        config.set(GameConfig())
    }

    fun update(columns: Int, rows: Int, connect: Int): GameConfig {
        val safeConfig = GameConfig(
            columns = columns.coerceIn(4, 15),
            rows = rows.coerceIn(4, 15),
            connect = connect.coerceIn(4, 10),
        )

        config.set(safeConfig)
        return safeConfig
    }
}
