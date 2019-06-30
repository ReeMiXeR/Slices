package vs.game.slices.viewmodel.result

import vs.game.slices.viewmodel.base.BaseViewModel

abstract class GameResultViewModel : BaseViewModel<GameResultState, GameResultEvents>() {
    abstract fun onNewGameClicked()
}