package vs.game.slices.model

import java.io.Serializable

data class GameItem(
    val character: CharacterItem,
    val serialName: SerialName
): Serializable