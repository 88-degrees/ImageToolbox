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

package ru.tech.imageresizershrinker.core.data.image

import android.graphics.Bitmap
import android.os.Build
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tech.imageresizershrinker.core.domain.dispatchers.DispatchersHolder
import ru.tech.imageresizershrinker.core.domain.image.ImageCompressor
import ru.tech.imageresizershrinker.core.domain.image.ImageGetter
import ru.tech.imageresizershrinker.core.domain.image.ImagePreviewCreator
import ru.tech.imageresizershrinker.core.domain.image.ImageScaler
import ru.tech.imageresizershrinker.core.domain.image.ImageTransformer
import ru.tech.imageresizershrinker.core.domain.image.model.ImageInfo
import ru.tech.imageresizershrinker.core.domain.image.model.ResizeType
import ru.tech.imageresizershrinker.core.domain.transformation.Transformation
import ru.tech.imageresizershrinker.core.settings.domain.SettingsProvider
import ru.tech.imageresizershrinker.core.settings.domain.model.SettingsState
import javax.inject.Inject
import kotlin.math.roundToInt

internal class AndroidImagePreviewCreator @Inject constructor(
    private val imageCompressor: ImageCompressor<Bitmap>,
    private val imageGetter: ImageGetter<Bitmap, ExifInterface>,
    private val imageTransformer: ImageTransformer<Bitmap>,
    private val imageScaler: ImageScaler<Bitmap>,
    settingsProvider: SettingsProvider,
    dispatchersHolder: DispatchersHolder
) : DispatchersHolder by dispatchersHolder, ImagePreviewCreator<Bitmap> {

    private var generatePreviews = SettingsState.Default.generatePreviews

    init {
        settingsProvider
            .getSettingsStateFlow()
            .onEach {
                generatePreviews = it.generatePreviews
            }.launchIn(CoroutineScope(defaultDispatcher))
    }

    override suspend fun createPreview(
        image: Bitmap,
        imageInfo: ImageInfo,
        transformations: List<Transformation<Bitmap>>,
        onGetByteCount: (Int) -> Unit
    ): Bitmap? = withContext(defaultDispatcher) {
        launch {
            onGetByteCount(
                imageCompressor.calculateImageSize(
                    image = image,
                    imageInfo = imageInfo
                ).toInt()
            )
        }

        if (!generatePreviews) return@withContext null

        if (imageInfo.height == 0 || imageInfo.width == 0) return@withContext image
        var width = imageInfo.width
        var height = imageInfo.height

        var scaleFactor = 1f
        while (!canShow(size = height * width * 4)) {
            height = (height * 0.85f).roundToInt()
            width = (width * 0.85f).roundToInt()
            scaleFactor *= 0.85f
        }

        val bytes = if (imageInfo.resizeType is ResizeType.CenterCrop) {
            compressCenterCrop(
                scaleFactor = scaleFactor,
                onImageReadyToCompressInterceptor = {
                    imageTransformer.transform(
                        image = it,
                        transformations = transformations
                    ) ?: it
                },
                image = image,
                imageInfo = imageInfo.copy(
                    width = width,
                    height = height
                )
            )
        } else {
            imageCompressor.compressAndTransform(
                image = image,
                imageInfo = imageInfo.copy(
                    width = width,
                    height = height
                ),
                onImageReadyToCompressInterceptor = {
                    imageTransformer.transform(
                        image = it,
                        transformations = transformations
                    ) ?: it
                }
            )
        }

        imageScaler.scaleUntilCanShow(imageGetter.getImage(bytes) ?: image)
    }

    override fun canShow(
        image: Bitmap?
    ): Boolean = if (image == null) false else canShow(image.size())

    private fun canShow(size: Int): Boolean {
        return size < 3096 * 3096 * 3
    }

    private val Bitmap.configSize: Int
        get() = when (config) {
            Bitmap.Config.RGB_565 -> 2
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (config == Bitmap.Config.RGBA_F16) 8 else 4
                } else 4
            }
        }

    private fun Bitmap.size(): Int {
        return width * height * configSize
    }

    private suspend fun compressCenterCrop(
        scaleFactor: Float,
        onImageReadyToCompressInterceptor: suspend (Bitmap) -> Bitmap,
        image: Bitmap,
        imageInfo: ImageInfo
    ): ByteArray = withContext(defaultDispatcher) {
        val currentImage = imageScaler.scaleImage(
            image = image,
            width = (imageInfo.width * scaleFactor).roundToInt(),
            height = (imageInfo.height * scaleFactor).roundToInt(),
            resizeType = (imageInfo.resizeType as ResizeType.CenterCrop).copy(scaleFactor = scaleFactor),
            imageScaleMode = imageInfo.imageScaleMode
        ).let {
            imageTransformer.flip(
                image = it,
                isFlipped = imageInfo.isFlipped
            )
        }.let {
            onImageReadyToCompressInterceptor(it)
        }

        runCatching {
            imageCompressor.compress(
                image = currentImage,
                imageFormat = imageInfo.imageFormat,
                quality = imageInfo.quality
            )
        }.getOrNull() ?: ByteArray(0)
    }

}