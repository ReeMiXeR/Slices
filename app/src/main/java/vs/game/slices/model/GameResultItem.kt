package vs.game.slices.model

import vs.test.aviasales.ui.adapter.DisplayableItem
import java.io.Serializable

data class GameResultItem(
    val item: GameItem,
    val isCorrect: Boolean
) : Serializable, DisplayableItem