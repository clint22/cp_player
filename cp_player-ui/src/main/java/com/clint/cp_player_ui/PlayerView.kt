package com.clint.cp_player_ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * Created by Clint Paul on 10.04.23
 * */

class PlayerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    init {
        val inflatedView = inflate(context, R.layout.player_view, this)
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PlayerView)

        val imageViewPlay: ImageView = inflatedView.findViewById(R.id.imageViewPlay)
        val imageViewPrevious: ImageView = inflatedView.findViewById(R.id.imageViewPrevious)
        val imageViewNext: ImageView = inflatedView.findViewById(R.id.imageViewNext)
        val imageViewShuffle: ImageView = inflatedView.findViewById(R.id.imageViewShuffle)
        val imageViewRepeat: ImageView = inflatedView.findViewById(R.id.imageViewRepeat)

        // Colors
        val playerColor = typedArray.getColor(R.styleable.PlayerView_play_color, 0)
        val previousColor = typedArray.getColor(R.styleable.PlayerView_previous_color, 0)
        val nextColor = typedArray.getColor(R.styleable.PlayerView_next_color, 0)
        val shuffleColor = typedArray.getColor(R.styleable.PlayerView_shuffle_color, 0)
        val repeatColor = typedArray.getColor(R.styleable.PlayerView_repeat_color, 0)

        imageViewPlay.setColorFilter(playerColor)
        imageViewPrevious.setColorFilter(previousColor)
        imageViewNext.setColorFilter(nextColor)
        imageViewShuffle.setColorFilter(shuffleColor)
        imageViewRepeat.setColorFilter(repeatColor)

        // Size
        val playerSize = IconSize.from(typedArray.getInt(R.styleable.PlayerView_play_size, 2))
        val previousSize = IconSize.from(typedArray.getInt(R.styleable.PlayerView_previous_size, 2))
        val nextSize = IconSize.from(typedArray.getInt(R.styleable.PlayerView_next_size, 2))
        val shuffleSize = IconSize.from(typedArray.getInt(R.styleable.PlayerView_shuffle_size, 2))
        val repeatSize = IconSize.from(typedArray.getInt(R.styleable.PlayerView_repeat_size, 2))

        imageViewPlay.setImageResource(IconSize.getPlayResource(playerSize))
        imageViewPrevious.setImageResource(IconSize.getPreviousResource(previousSize))
        imageViewNext.setImageResource(IconSize.getNextResource(nextSize))
        imageViewShuffle.setImageResource(IconSize.getShuffleResource(shuffleSize))
        imageViewRepeat.setImageResource(IconSize.getRepeatResource(repeatSize))

        typedArray.recycle()
    }

    enum class IconSize {
        SMALL, MEDIUM, LARGE, XLARGE;

        companion object {

            fun from(type: Int): IconSize {
                return when (type) {
                    0 -> SMALL
                    1 -> MEDIUM
                    2 -> LARGE
                    3 -> XLARGE
                    else -> LARGE
                }
            }

            fun getPlayResource(size: IconSize): Int {
                return when (size) {
                    SMALL -> R.drawable.play_small
                    MEDIUM -> R.drawable.play_medium
                    LARGE -> R.drawable.play_large
                    XLARGE -> R.drawable.play_x_large
                }
            }

            fun getPreviousResource(size: IconSize): Int {
                return when (size) {
                    SMALL -> R.drawable.previous_small
                    MEDIUM -> R.drawable.previous_medium
                    LARGE -> R.drawable.previous_large
                    XLARGE -> R.drawable.previous_x_large
                }
            }

            fun getNextResource(size: IconSize): Int {
                return when (size) {
                    SMALL -> R.drawable.next_small
                    MEDIUM -> R.drawable.next_medium
                    LARGE -> R.drawable.next_large
                    XLARGE -> R.drawable.next_x_large
                }
            }

            fun getShuffleResource(size: IconSize): Int {
                return when (size) {
                    SMALL -> R.drawable.shuffle_small
                    MEDIUM -> R.drawable.shuffle_medium
                    LARGE -> R.drawable.shuffle_large
                    XLARGE -> R.drawable.shuffle_x_large
                }
            }

            fun getRepeatResource(size: IconSize): Int {
                return when (size) {
                    SMALL -> R.drawable.repeat_small
                    MEDIUM -> R.drawable.repeat_medium
                    LARGE -> R.drawable.repeat_large
                    XLARGE -> R.drawable.repeat_x_large
                }
            }
        }
    }


}