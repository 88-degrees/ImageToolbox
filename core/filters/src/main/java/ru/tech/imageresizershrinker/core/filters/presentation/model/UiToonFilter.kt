package ru.tech.imageresizershrinker.core.filters.presentation.model

import android.graphics.Bitmap
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.coredomain.image.filters.Filter


class UiToonFilter(
    override val value: Pair<Float, Float> = 0.2f to 10f,
) : UiFilter<Pair<Float, Float>>(
    title = R.string.toon,
    value = value,
    paramsInfo = listOf(
        R.string.threshold paramTo 0f..5f,
        R.string.quantizationLevels paramTo 0f..100f
    )
), Filter.Toon<Bitmap>