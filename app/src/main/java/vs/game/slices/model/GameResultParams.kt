package vs.game.slices.model

import java.io.Serializable

data class GameResultParams(
    val items: List<GameResultItem>,
    val score: String,
    val title: String
) : Serializable