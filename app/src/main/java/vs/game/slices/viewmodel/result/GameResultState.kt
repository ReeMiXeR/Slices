package vs.game.slices.viewmodel.result

import vs.game.slices.model.GameResultItem

sealed class GameResultState {
    data class Content(val data: List<GameResultItem>, val score: String, val title: String) : GameResultState()
}