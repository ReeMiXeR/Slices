package vs.game.slices.view.view

import android.content.Context
import android.graphics.Canvas
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.view_card.view.*
import vs.game.slices.R
import vs.game.slices.view.ASSET_PATH
import vs.game.slices.view.dp

class SliceView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val cornerRadius by lazy { 16.dp(context) }
    private var dataToBind: Pair<String, String>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_card, this, true)
    }

    fun tryToBind() {
        dataToBind?.let { (title , imageName) ->
            bind(title, imageName)
        }
    }

    fun bind(title: String, imageName: String) {
        if (measuredHeight == 0 && measuredWidth == 0) {
            dataToBind = title to imageName
        } else {
            card_item_title.text = title

            Glide.with(context)
                    .load(Uri.parse(ASSET_PATH.format(imageName)))
                    .transform(CenterCrop(), RoundedCorners(cornerRadius))
                    .placeholder(R.drawable.placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(card_character_image)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        tryToBind()
        super.onSizeChanged(w, h, oldw, oldh)
    }
}