/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.foundation.text.selection.gestures

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.fetchTextLayoutResult
import androidx.compose.foundation.text.selection.gestures.util.MultiSelectionSubject
import androidx.compose.foundation.text.selection.gestures.util.TextSelectionAsserter
import androidx.compose.foundation.text.selection.gestures.util.offsetToLocalOffset
import androidx.compose.foundation.text.selection.gestures.util.offsetToSelectableId
import androidx.compose.foundation.text.selection.gestures.util.textContentIndices
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.util.fastForEach
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
internal class MultiTextSelectionGesturesBidiTest : TextSelectionGesturesBidiTest() {

    override val pointerAreaTag = "selectionContainer"
    private val ltrWord = "hello"
    private val rtlWord = RtlChar.repeat(5)
    override val textContent =
        mutableStateOf(
            """
        $ltrWord $rtlWord $ltrWord
        $rtlWord $ltrWord $rtlWord
        $ltrWord $rtlWord $ltrWord
    """
                .trimIndent()
                .trim()
        )

    override lateinit var asserter: TextSelectionAsserter

    private lateinit var texts: State<List<Pair<String, String>>>
    private lateinit var textContentIndices: State<List<IntRange>>

    @Before
    fun setupAsserter() {
        asserter =
            object :
                TextSelectionAsserter(
                    textContent = textContent.value,
                    rule = rule,
                    textToolbar = textToolbar,
                    hapticFeedback = hapticFeedback,
                    getActual = { selection.value }
                ) {
                override fun subAssert() {
                    Truth.assertAbout(MultiSelectionSubject.withContent(texts.value))
                        .that(getActual())
                        .hasSelection(
                            expected = selection,
                            startTextDirection = startLayoutDirection,
                            endTextDirection = endLayoutDirection,
                        )
                }
            }
    }

    @Composable
    override fun TextContent() {
        texts = derivedStateOf {
            textContent.value.split("\n").withIndex().map { (index, str) -> str to "testTag$index" }
        }

        textContentIndices = derivedStateOf { texts.value.textContentIndices() }

        Column {
            texts.value.fastForEach { (str, tag) ->
                BasicText(
                    text = str,
                    style =
                        TextStyle(
                            fontFamily = fontFamily,
                            fontSize = fontSize,
                        ),
                    modifier = Modifier.fillMaxWidth().testTag(tag),
                )
            }
        }
    }

    override fun characterPosition(offset: Int, isRtl: Boolean): Offset {
        val selectableIndex = textContentIndices.value.offsetToSelectableId(offset)
        val localOffset = textContentIndices.value.offsetToLocalOffset(offset)
        val (_, tag) = texts.value[selectableIndex]
        val pointerAreaPosition =
            rule.onNodeWithTag(pointerAreaTag).fetchSemanticsNode().positionInRoot
        val nodePosition = rule.onNodeWithTag(tag).fetchSemanticsNode().positionInRoot
        val textLayoutResult = rule.onNodeWithTag(tag).fetchTextLayoutResult()
        val boundingBox =
            textLayoutResult
                .getBoundingBox(localOffset)
                .translate(nodePosition - pointerAreaPosition)
        return if (isRtl) boundingBox.centerRight - Offset(2f, 0f)
        else boundingBox.centerLeft + Offset(2f, 0f)
    }
}
