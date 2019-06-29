package vs.game.slices.view.adapter.airport

import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import vs.game.slices.R
import vs.game.slices.model.GameResultItem
import vs.game.slices.view.activity.GameActivity
import vs.game.slices.view.adapter.base.ConstantAdapter
import vs.game.slices.view.deviceHeight
import vs.game.slices.view.deviceWidth
import vs.game.slices.view.dp
import vs.game.slices.view.toggleGone


class GameResultAdapter : ConstantAdapter<GameResultItem, GameResultViewHolder>() {

    override val itemResourceId: Int = R.layout.item_game_result

    override fun createHolder(view: View): GameResultViewHolder = GameResultViewHolder(view)

    override fun bind(holder: GameResultViewHolder, item: GameResultItem, position: Int) {

        val context = holder.itemView.context
        holder.itemView.layoutParams.width = context.deviceWidth / 2 - 16.dp(context) * 2 - 8.dp(context)
        holder.itemView.layoutParams.height = (context.deviceHeight / 3.3).toInt()

        holder.itemView.layoutParams = holder.itemView.layoutParams

        with(item) {
            holder.wrongAnswer.toggleGone(item.isCorrect.not())

            if (item.isCorrect) {
                holder.correctAnswer.setTextColor(ContextCompat.getColor(context, R.color.gray_two))
            } else {
                holder.correctAnswer.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.wrongAnswer.text = this.item.serialName.wrongName
                holder.wrongAnswer.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            holder.characterName.text = item.item.character.name
            holder.correctAnswer.text = item.item.serialName.correctName
            holder.correctness.text =
                context.getString(if (item.isCorrect) R.string.game_result_correct else R.string.game_result_wrong)
            holder.correctness.isActivated = item.isCorrect

            Glide.with(context)
                .load(Uri.parse(GameActivity.ASSET_PATH.format(this.item.character.imageName)))
                .transform(CenterCrop(), RoundedCorners(16.dp(context)))
                .placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.background)
        }
    }
}