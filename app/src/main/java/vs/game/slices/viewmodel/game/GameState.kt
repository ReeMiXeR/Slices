package vs.game.slices.viewmodel.game

import androidx.annotation.StringRes
import vs.game.slices.model.GameItem

sealed class GameState {
    data class Content(val currentItem: GameItem, val nextItem: GameItem?, val title: String) : GameState()
    data class Stub(@StringRes val error: Int) : GameState()
}