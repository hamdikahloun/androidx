/*
 * Copyright 2024 The Android Open Source Project
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

package androidx.lifecycle.viewmodel.navigation3

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.kruth.assertWithMessage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.Record
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ViewModelStoreNavContentWrapperTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun testViewModelProvided() {
        val wrapper = ViewModelStoreNavContentWrapper
        lateinit var viewModel1: MyViewModel
        lateinit var viewModel2: MyViewModel
        val record1Arg = "record1 Arg"
        val record2Arg = "record2 Arg"
        val record1 =
            Record("key1") {
                viewModel1 = viewModel<MyViewModel>()
                viewModel1.myArg = record1Arg
            }
        val record2 =
            Record("key2") {
                viewModel2 = viewModel<MyViewModel>()
                viewModel2.myArg = record2Arg
            }
        composeTestRule.setContent {
            wrapper.WrapContent(record1)
            wrapper.WrapContent(record2)
        }

        composeTestRule.runOnIdle {
            assertWithMessage("Incorrect arg for record 1")
                .that(viewModel1.myArg)
                .isEqualTo(record1Arg)
            assertWithMessage("Incorrect arg for record 2")
                .that(viewModel2.myArg)
                .isEqualTo(record2Arg)
        }
    }
}

class MyViewModel : ViewModel() {
    var myArg = "default"
}
