package vs.game.slices.viewmodel.game

import vs.game.slices.viewmodel.base.BaseViewModel

abstract class GameViewModel : BaseViewModel<GameState, GameEvent>() {
    abstract fun onAnswerClicked(answer: String)
}