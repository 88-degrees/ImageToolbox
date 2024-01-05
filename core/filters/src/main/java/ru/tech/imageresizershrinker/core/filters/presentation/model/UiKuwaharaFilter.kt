package ru.tech.imageresizershrinker.core.filters.presentation.model

import android.graphics.Bitmap
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.coredomain.image.filters.Filter
import ru.tech.imageresizershrinker.coredomain.image.filters.FilterParam


class UiKuwaharaFilter(
    override val value: Float = 3f,
) : UiFilter<Float>(
    title = R.string.kuwahara,
    value = value,
    paramsInfo = listOf(
        FilterParam(null, 0f..10f, 0)
    )
), Filter.Kuwahara<Bitmap>