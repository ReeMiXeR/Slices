package vs.game.slices.view.adapter.airport

import android.graphics.Paint
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import vs.game.slices.R
import vs.game.slices.model.GameResultItem
import vs.game.slices.view.*
import vs.game.slices.view.adapter.base.Adapter


class GameResultAdapter : Adapter<GameResultItem, GameResultViewHolder>() {

    override val itemResourceId: Int = R.layout.item_game_result

    override fun createHolder(view: View): GameResultViewHolder {
        return GameResultViewHolder(
            view.apply {
                layoutParams.height = view.context.deviceHeight / 3
                layoutParams = layoutParams
            }
        )
    }

    override fun bind(holder: GameResultViewHolder, item: GameResultItem, position: Int) {
        val context = holder.itemView.context

        holder.itemView.layoutParams = holder.itemView.layoutParams

        with(item) {
            holder.wrongAnswer.toggleGone(item.isCorrect.not())

            if (item.isCorrect) {
                holder.correctAnswer.setTextColor(ContextCompat.getColor(context, R.color.gray_two))
            } else {
                holder.correctAnswer.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.wrongAnswer.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.wrongAnswer.precomputeText(this.item.serialName.wrongName)
            }

            holder.characterName.precomputeText(item.item.character.name)
            holder.correctAnswer.precomputeText(item.item.serialName.correctName)
            holder.correctness.precomputeText(context.getString(if (item.isCorrect) R.string.game_result_correct else R.string.game_result_wrong))
            holder.correctness.isActivated = item.isCorrect

            Glide.with(context)
                .load(Uri.parse(ASSET_PATH.format(this.item.character.imageName)))
                .transform(CenterCrop(), RoundedCorners(16.dp(context)))
                .placeholder(R.drawable.placeholder)
                .into(holder.background)
        }
    }
}