package vs.game.slices.model

import java.io.Serializable
import kotlin.random.Random

data class SerialName(
    val correctName: String,
    val wrongName: String
): Serializable


fun SerialName.getShuffled(): Pair<String, String> {
    return when (Random.nextBoolean()) {
        true -> correctName to wrongName
        false -> wrongName to correctName
    }
}