package vs.game.slices.viewmodel.result

import vs.game.slices.viewmodel.BaseViewModel

abstract class GameResultViewModel : BaseViewModel<GameResultState, GameResultEvents>() {
    abstract fun onNewGameClicked()
}