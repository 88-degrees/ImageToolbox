package ru.tech.imageresizershrinker.presentation.root.transformation.filter

import android.graphics.Bitmap
import ru.tech.imageresizershrinker.R
import ru.tech.imageresizershrinker.domain.image.filters.Filter
import ru.tech.imageresizershrinker.domain.image.filters.FilterParam


class UiFastBlurFilter(
    override val value: Pair<Float, Int> = 0.5f to 25,
) : UiFilter<Pair<Float, Int>>(
    title = R.string.fast_blur,
    value = value,
    paramsInfo = listOf(
        FilterParam(R.string.scale, 0.1f..1f, 2),
        FilterParam(R.string.radius, 0f..100f, 0)
    )
), Filter.FastBlur<Bitmap>