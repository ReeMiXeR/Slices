package vs.game.slices.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GameWrapper(
    @SerializedName("game_items")
    val items: List<SerialItem>,
    @SerializedName("game_title")
    val title: String
) : Serializable
