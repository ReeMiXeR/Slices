package vs.game.slices.viewmodel.game

import vs.game.slices.model.GameItem

sealed class GameState {
    data class Content(val currentItem: GameItem, val nextItem: GameItem?, val title: String) : GameState()
    data class Stub(val error: String) : GameState()
    object Loading : GameState()
}