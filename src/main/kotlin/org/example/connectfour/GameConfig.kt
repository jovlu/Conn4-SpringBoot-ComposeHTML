package org.example.connectfour

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicReference

data class GameConfig(
    val columns: Int = 7,
    val rows: Int = 6,
    val connect: Int = 4,
)

data class GameSnapshot(
    val board: List<List<Int>>,
    val currentPlayer: Int = 1,
    val winner: Int = 0,
    val draw: Boolean = false,
)

@Component
class GameConfigStore {
    private val config = AtomicReference(GameConfig())
    private val snapshot = AtomicReference<GameSnapshot?>(null)

    fun current(): GameConfig = config.get()

    fun currentSnapshot(): GameSnapshot? = snapshot.get()

    fun reset() {
        config.set(GameConfig())
        snapshot.set(null)
    }

    fun update(columns: Int, rows: Int, connect: Int): GameConfig {
        val safeConfig = GameConfig(
            columns = columns.coerceIn(4, 15),
            rows = rows.coerceIn(4, 15),
            connect = connect.coerceIn(4, 10),
        )

        if (safeConfig != config.get()) {
            snapshot.set(null)
        }

        config.set(safeConfig)
        return safeConfig
    }

    fun saveSnapshot(newSnapshot: GameSnapshot) {
        val currentConfig = config.get()

        if (
            newSnapshot.board.size != currentConfig.rows ||
            newSnapshot.board.any { it.size != currentConfig.columns }
        ) {
            return
        }

        snapshot.set(newSnapshot)
    }
}
