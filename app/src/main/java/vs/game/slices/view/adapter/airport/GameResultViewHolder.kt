package vs.game.slices.view.adapter.airport

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.item_game_result.view.*
import vs.game.slices.view.adapter.base.BaseViewHolder

class GameResultViewHolder(view: View) : BaseViewHolder(view) {
    val correctness: AppCompatTextView = view.game_result_correctness
    val characterName: AppCompatTextView = view.game_result_character_name
    val wrongAnswer: AppCompatTextView = view.item_game_result_wrong_answer
    val correctAnswer: AppCompatTextView = view.item_game_result_correct_answer
    val background: ImageView = view.game_result_background
}