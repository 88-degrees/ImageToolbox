package ru.tech.imageresizershrinker.core.filters.presentation.model

import android.graphics.Bitmap
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.coredomain.image.filters.Filter
import ru.tech.imageresizershrinker.coredomain.image.filters.FilterParam


class UiHalftoneFilter(
    override val value: Float = 0.005f,
) : UiFilter<Float>(
    title = R.string.halftone,
    value = value,
    paramsInfo = listOf(
        FilterParam(valueRange = 0.001f..0.02f, roundTo = 4)
    )
), Filter.Halftone<Bitmap>