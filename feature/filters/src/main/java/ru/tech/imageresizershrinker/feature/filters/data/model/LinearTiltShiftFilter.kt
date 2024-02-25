/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

package ru.tech.imageresizershrinker.feature.filters.data.model

import android.graphics.Bitmap
import com.awxkee.aire.Aire
import ru.tech.imageresizershrinker.core.domain.image.Transformation
import ru.tech.imageresizershrinker.core.domain.model.IntegerSize
import ru.tech.imageresizershrinker.core.filters.domain.model.Filter
import ru.tech.imageresizershrinker.core.filters.domain.model.LinearTiltShiftParams
import kotlin.math.roundToInt

internal class LinearTiltShiftFilter(
    override val value: LinearTiltShiftParams = LinearTiltShiftParams.Default
) : Transformation<Bitmap>, Filter.LinearTiltShift<Bitmap> {

    override val cacheKey: String
        get() = value.hashCode().toString()

    override suspend fun transform(
        input: Bitmap,
        size: IntegerSize
    ): Bitmap = Aire.horizontalTiltShift(
        bitmap = input,
        radius = value.blurRadius.roundToInt(),
        sigma = value.sigma,
        anchorX = value.anchorX,
        anchorY = value.anchorY,
        tiltRadius = value.holeRadius,
        angle = value.angle * Math.PI.toFloat() / 180f
    )

}