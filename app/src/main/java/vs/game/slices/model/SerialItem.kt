package vs.game.slices.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SerialItem(
    @SerializedName("items")
    val items: List<CharacterItem>,
    @SerializedName("serial")
    val name: String
) : Serializable