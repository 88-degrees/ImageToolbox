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

package ru.tech.imageresizershrinker.core.settings.domain.model

import ru.tech.imageresizershrinker.core.domain.Domain

sealed class FontFam(val ordinal: Int) : Domain {
    data object Montserrat : FontFam(1)
    data object Caveat : FontFam(2)
    data object Comfortaa : FontFam(3)
    data object Handjet : FontFam(4)
    data object YsabeauSC : FontFam(5)
    data object Jura : FontFam(6)
    data object Podkova : FontFam(7)
    data object Tektur : FontFam(8)
    data object DejaVu : FontFam(9)
    data object BadScript : FontFam(10)
    data object RuslanDisplay : FontFam(11)
    data object Catterdale : FontFam(12)
    data object FRM32 : FontFam(13)
    data object TokeelyBrookings : FontFam(14)
    data object Nunito : FontFam(15)
    data object Nothing : FontFam(16)
    data object WOPRTweaked : FontFam(17)
    data object AlegreyaSans : FontFam(18)
    data object MinecraftGnu : FontFam(19)
    data object GraniteFixed : FontFam(20)
    data object NokiaPixel : FontFam(21)
    data object Ztivalia : FontFam(22)
    data object Axotrel : FontFam(23)
    data object LcdOctagon : FontFam(24)
    data object LcdMoving : FontFam(25)
    data object Unisource : FontFam(26)
    data object System : FontFam(0)

    companion object {
        fun fromOrdinal(int: Int?): FontFam = when (int) {
            1 -> Montserrat
            2 -> Caveat
            3 -> Comfortaa
            4 -> Handjet
            5 -> YsabeauSC
            6 -> Jura
            7 -> Podkova
            8 -> Tektur
            9 -> DejaVu
            10 -> BadScript
            11 -> RuslanDisplay
            12 -> Catterdale
            13 -> FRM32
            14 -> TokeelyBrookings
            15 -> Nunito
            16 -> Nothing
            17 -> WOPRTweaked
            18 -> AlegreyaSans
            19 -> MinecraftGnu
            20 -> GraniteFixed
            21 -> NokiaPixel
            22 -> Ztivalia
            23 -> Axotrel
            24 -> LcdOctagon
            25 -> LcdMoving
            26 -> Unisource
            else -> System
        }
    }
}