package vs.game.slices.viewmodel.game

import vs.game.slices.model.GameResultParams

sealed class GameEvent {
    data class SwitchToGameResultScreen(val data: GameResultParams) : GameEvent()
}