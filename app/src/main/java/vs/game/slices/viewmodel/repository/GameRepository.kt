package vs.game.slices.viewmodel.repository

import io.reactivex.Single
import vs.game.slices.model.GameWrapper

interface GameRepository {
    fun getGameData(): Single<GameWrapper>
}