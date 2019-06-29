package vs.game.slices.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CharacterItem(
    @SerializedName("image")
    val imageName: String,
    @SerializedName("name")
    val name: String
) : Serializable