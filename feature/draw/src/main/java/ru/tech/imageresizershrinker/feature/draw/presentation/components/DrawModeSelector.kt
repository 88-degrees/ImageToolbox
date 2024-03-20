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

package ru.tech.imageresizershrinker.feature.draw.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BlurCircular
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.core.resources.material.Cube
import ru.tech.imageresizershrinker.core.resources.material.Highlighter
import ru.tech.imageresizershrinker.core.resources.material.Laser
import ru.tech.imageresizershrinker.core.ui.widget.buttons.EnhancedButton
import ru.tech.imageresizershrinker.core.ui.widget.buttons.SupportingButton
import ru.tech.imageresizershrinker.core.ui.widget.buttons.ToggleGroupButton
import ru.tech.imageresizershrinker.core.ui.widget.controls.resize_group.components.BlurRadiusSelector
import ru.tech.imageresizershrinker.core.ui.widget.modifier.ContainerShapeDefaults
import ru.tech.imageresizershrinker.core.ui.widget.modifier.container
import ru.tech.imageresizershrinker.core.ui.widget.sheets.SimpleSheet
import ru.tech.imageresizershrinker.core.ui.widget.text.AutoSizeText
import ru.tech.imageresizershrinker.core.ui.widget.text.TitleItem
import ru.tech.imageresizershrinker.feature.draw.domain.DrawMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawModeSelector(
    modifier: Modifier,
    value: DrawMode,
    onValueChange: (DrawMode) -> Unit
) {
    val state = rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .container(RoundedCornerShape(24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToggleGroupButton(
            enabled = true,
            itemCount = DrawMode.entries.size,
            title = {
                Text(
                    text = stringResource(R.string.draw_mode),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                SupportingButton(
                    onClick = {
                        state.value = true
                    }
                )
            },
            selectedIndex = remember(value) {
                derivedStateOf {
                    DrawMode.entries.indexOfFirst {
                        value::class.isInstance(it)
                    }
                }
            }.value,
            buttonIcon = {},
            itemContent = {
                Icon(
                    imageVector = DrawMode.entries[it].getIcon(),
                    contentDescription = null
                )
            },
            indexChanged = {
                onValueChange(DrawMode.entries[it])
            }
        )
        AnimatedVisibility(
            visible = value is DrawMode.PathEffect.PrivacyBlur,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            BlurRadiusSelector(
                modifier = Modifier.padding(8.dp),
                value = (value as? DrawMode.PathEffect.PrivacyBlur)?.blurRadius ?: 0,
                valueRange = 5f..50f,
                onValueChange = {
                    onValueChange(DrawMode.PathEffect.PrivacyBlur(it))
                }
            )
        }
        AnimatedVisibility(
            visible = value is DrawMode.PathEffect.Pixelation,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            PixelSizeSelector(
                modifier = Modifier.padding(8.dp),
                value = (value as? DrawMode.PathEffect.Pixelation)?.pixelSize ?: 0f,
                onValueChange = {
                    onValueChange(DrawMode.PathEffect.Pixelation(it))
                }
            )
        }
    }
    SimpleSheet(
        sheetContent = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DrawMode.entries.forEachIndexed { index, item ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .container(
                                shape = ContainerShapeDefaults.shapeForIndex(
                                    index,
                                    DrawMode.entries.size
                                ),
                                resultPadding = 0.dp
                            )
                    ) {
                        TitleItem(text = stringResource(item.getTitle()), icon = item.getIcon())
                        Text(
                            text = stringResource(item.getSubtitle()),
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            fontSize = 14.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        },
        visible = state,
        title = {
            TitleItem(text = stringResource(R.string.draw_mode))
        },
        confirmButton = {
            EnhancedButton(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { state.value = false }
            ) {
                AutoSizeText(stringResource(R.string.close))
            }
        }
    )
}

private fun DrawMode.getSubtitle(): Int = when (this) {
    is DrawMode.Highlighter -> R.string.highlighter_sub
    is DrawMode.Neon -> R.string.neon_sub
    is DrawMode.Pen -> R.string.pen_sub
    is DrawMode.PathEffect.PrivacyBlur -> R.string.privacy_blur_sub
    is DrawMode.PathEffect.Pixelation -> R.string.pixelation_sub
}

private fun DrawMode.getTitle(): Int = when (this) {
    is DrawMode.Highlighter -> R.string.highlighter
    is DrawMode.Neon -> R.string.neon
    is DrawMode.Pen -> R.string.pen
    is DrawMode.PathEffect.PrivacyBlur -> R.string.privacy_blur
    is DrawMode.PathEffect.Pixelation -> R.string.pixelation
}

private fun DrawMode.getIcon(): ImageVector = when (this) {
    is DrawMode.Highlighter -> Icons.Rounded.Highlighter
    is DrawMode.Neon -> Icons.Rounded.Laser
    is DrawMode.Pen -> Icons.Rounded.Brush
    is DrawMode.PathEffect.PrivacyBlur -> Icons.Rounded.BlurCircular
    is DrawMode.PathEffect.Pixelation -> Icons.Rounded.Cube
}