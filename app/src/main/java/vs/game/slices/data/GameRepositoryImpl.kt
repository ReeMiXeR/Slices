package vs.game.slices.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.reactivex.Single
import vs.game.slices.viewmodel.repository.GameRepository
import vs.game.slices.model.GameWrapper

class GameRepositoryImpl(
    private val context: Context,
    private val gson: Gson
) : GameRepository {

    override fun getGameData(): Single<GameWrapper> {
        return Single.fromCallable {
            context.assets
                .open(GAME_DATA_FILE_NAME)
                .run { gson.fromJson<GameWrapper>(JsonReader(this.reader()), GameWrapper::class.java) }
        }
    }

    private companion object {
        const val GAME_DATA_FILE_NAME = "game.json"
    }
}