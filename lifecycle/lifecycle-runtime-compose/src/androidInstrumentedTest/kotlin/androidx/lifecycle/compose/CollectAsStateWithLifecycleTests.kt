/*
 * Copyright 2022 The Android Open Source Project
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

package androidx.lifecycle.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.kruth.assertThat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import kotlin.test.Test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalTestApi::class)
class CollectAsStateWithLifecycleTests {

    @Test
    fun test_flowState_getsInitialValue() = runComposeUiTest {
        val _sharedFlow = MutableSharedFlow<String>()
        val flow: Flow<String> = _sharedFlow

        var realValue: String? = null
        setContent {
            withLifecycleOwner { realValue = flow.collectAsStateWithLifecycle("0").value }
        }

        runOnIdle { assertThat(realValue).isEqualTo("0") }
    }

    @Test
    fun test_stateFlowState_getsInitialValue() = runComposeUiTest {
        val stateFlow: StateFlow<String> = MutableStateFlow("0")

        var realValue: String? = null
        setContent {
            withLifecycleOwner { realValue = stateFlow.collectAsStateWithLifecycle().value }
        }

        runOnIdle { assertThat(realValue).isEqualTo("0") }
    }

    @Test
    fun test_flowState_getsSubsequentFlowEmissions() = runComposeUiTest {
        val _sharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)
        val flow: Flow<String> = _sharedFlow

        var realValue: String? = null
        setContent {
            withLifecycleOwner(Lifecycle.State.RESUMED) {
                realValue = flow.collectAsStateWithLifecycle("0").value
            }
        }

        assertThat(_sharedFlow.tryEmit("1")).isTrue()
        runOnIdle { assertThat(realValue).isEqualTo("1") }
    }

    @Test
    fun test_stateFlowState_getsSubsequentFlowEmissions() = runComposeUiTest {
        val stateFlow = MutableStateFlow("0")

        var realValue: String? = null
        setContent {
            withLifecycleOwner(Lifecycle.State.RESUMED) {
                realValue = stateFlow.collectAsStateWithLifecycle().value
            }
        }

        stateFlow.value = "1"

        runOnIdle { assertThat(realValue).isEqualTo("1") }
    }

    @Test
    fun test_flowState_doesNotGetEmissionsBelowMinState() = runComposeUiTest {
        val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)
        val _stateFlow = MutableStateFlow("initialValue")
        val flow: Flow<String> = _stateFlow

        var realValue: String? = null
        setContent {
            realValue =
                flow
                    .collectAsStateWithLifecycle(
                        initialValue = "0",
                        lifecycle = lifecycleOwner.lifecycle
                    )
                    .value
        }

        _stateFlow.value = "1"
        _stateFlow.value = "2"
        runOnIdle { assertThat(realValue).isEqualTo("0") }
    }

    @Test
    fun test_stateFlowState_doesNotGetEmissionsBelowMinState() = runComposeUiTest {
        val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)
        val stateFlow = MutableStateFlow("0")

        var realValue: String? = null
        setContent {
            realValue =
                stateFlow.collectAsStateWithLifecycle(lifecycle = lifecycleOwner.lifecycle).value
        }

        stateFlow.value = "1"
        stateFlow.value = "2"

        runOnIdle { assertThat(realValue).isEqualTo("0") }
    }

    @Test
    fun test_flowState_getsEmissionsWhenAboveMinState() = runComposeUiTest {
        val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)
        val _sharedFlow = MutableSharedFlow<String>(extraBufferCapacity = 2)
        val flow: Flow<String> = _sharedFlow

        var realValue: String? = null
        setContent {
            realValue =
                flow
                    .collectAsStateWithLifecycle(
                        initialValue = "0",
                        lifecycle = lifecycleOwner.lifecycle
                    )
                    .value
        }

        assertThat(_sharedFlow.tryEmit("1")).isTrue()
        runOnIdle { assertThat(realValue).isEqualTo("0") }

        lifecycleOwner.currentState = Lifecycle.State.RESUMED
        assertThat(_sharedFlow.tryEmit("2"))
        runOnIdle { assertThat(realValue).isEqualTo("2") }
    }

    @Test
    fun test_stateFlowState_getsEmissionsWhenAboveMinState() = runComposeUiTest {
        val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.RESUMED)
        val stateFlow = MutableStateFlow("0")

        var realValue: String? = null
        setContent {
            realValue =
                stateFlow.collectAsStateWithLifecycle(lifecycle = lifecycleOwner.lifecycle).value
        }

        runOnIdle { assertThat(realValue).isEqualTo("0") }

        stateFlow.value = "1"
        stateFlow.value = "2"

        runOnIdle { assertThat(realValue).isEqualTo("2") }
    }

    @Test
    fun test_stateFlowState_getsLastEmissionWhenLifecycleIsAboveMin() = runComposeUiTest {
        val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)
        val stateFlow = MutableStateFlow("0")

        var realValue: String? = null
        setContent {
            realValue =
                stateFlow.collectAsStateWithLifecycle(lifecycle = lifecycleOwner.lifecycle).value
        }

        runOnIdle { assertThat(realValue).isEqualTo("0") }

        stateFlow.value = "1"
        stateFlow.value = "2"
        lifecycleOwner.currentState = Lifecycle.State.RESUMED

        runOnIdle { assertThat(realValue).isEqualTo("2") }
    }

    @Composable
    private fun withLifecycleOwner(
        state: Lifecycle.State = Lifecycle.State.CREATED,
        content: @Composable () -> Unit
    ) {
        val lifecycleOwner = TestLifecycleOwner(state)
        CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner, content = content)
    }
}
