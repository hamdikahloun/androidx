/*
 * Copyright 2017 The Android Open Source Project
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

package androidx.recyclerview.selection.testing;

import android.graphics.Point;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

/**
 * Handy-dandy wrapper class to facilitate the creation of MotionEvents.
 */
public final class TestEvents {

    /**
     * Common mouse event types...for your convenience.
     */
    public static final class Mouse {
        public static final MotionEvent CLICK = TestEvents.builder()
                .mouse()
                .primary()
                .down()
                .build();

        public static final MotionEvent CTRL_CLICK = TestEvents.builder()
                .mouse()
                .primary()
                .down()
                .ctrl()
                .build();

        public static final MotionEvent ALT_CLICK = TestEvents.builder()
                .mouse()
                .primary()
                .down()
                .alt()
                .build();

        public static final MotionEvent SHIFT_CLICK = TestEvents.builder()
                .mouse()
                .primary()
                .down()
                .shift()
                .build();

        public static final MotionEvent SECONDARY_CLICK = TestEvents.builder()
                .mouse()
                .secondary()
                .down()
                .build();

        public static final MotionEvent TERTIARY_CLICK = TestEvents.builder()
                .mouse()
                .tertiary()
                .down()
                .build();

        public static final MotionEvent MOVE = TestEvents.builder()
                .mouse()
                .move()
                .build();

        public static final MotionEvent PRIMARY_DRAG = TestEvents.builder()
                .mouse()
                .primary()
                .move()
                .build();

        public static final MotionEvent SECONDARY_DRAG = TestEvents.builder()
                .mouse()
                .secondary()
                .move()
                .build();

        public static final MotionEvent TERTIARY_DRAG = TestEvents.builder()
                .mouse()
                .tertiary()
                .move()
                .build();

        public static final MotionEvent UP = TestEvents.builder()
                .mouse()
                .up()
                .build();

        public static final MotionEvent DOWN = TestEvents.builder()
                .mouse()
                .move()
                .build();

        // NOTE: POINTER_DOWN and POINTER_UP are for secondary pointers, not main mouse pointer.
        public static final MotionEvent POINTER_DOWN =
                TestEvents.builder()
                        .mouse()
                        .down()
                        .build();

        public static final MotionEvent POINTER_UP =
                TestEvents.builder()
                        .mouse()
                        .action(MotionEvent.ACTION_POINTER_UP)
                        .build();
    }

    /**
     * Common touch event types...for your convenience.
     */
    public static final class Touch {

        public static final MotionEvent DOWN = TestEvents.builder()
                .down()
                .touch()
                .location(1, 1)
                .build();

        public static final MotionEvent MOVE = TestEvents.builder()
                .move()
                .touch()
                .location(1, 1)
                .build();

        public static final MotionEvent UP = TestEvents.builder()
                .up()
                .touch()
                .location(1, 1)
                .build();

        public static final MotionEvent TAP = TestEvents.builder()
                .touch()
                .build();
    }

    /**
     * Common touch event types...for your convenience.
     */
    public static final class Unknown {
        public static final MotionEvent CANCEL = TestEvents.builder()
                .action(MotionEvent.ACTION_CANCEL)
                .build();
    }

    static final int ACTION_UNSET = -1;

    // Add other actions from MotionEvent.ACTION_ as needed.
    @IntDef(flag = true, value = {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Action {
    }

    // Add other types from MotionEvent.TOOL_TYPE_ as needed.
    @IntDef(flag = true, value = {
            MotionEvent.TOOL_TYPE_FINGER,
            MotionEvent.TOOL_TYPE_MOUSE,
            MotionEvent.TOOL_TYPE_STYLUS,
            MotionEvent.TOOL_TYPE_UNKNOWN,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToolType {
    }

    // Add other types from InputDevice.SOURCE_* as needed.
    @IntDef(flag = true, value = {
            InputDevice.SOURCE_MOUSE,
            InputDevice.SOURCE_UNKNOWN,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
    }

    @IntDef(flag = true, value = {
            MotionEvent.BUTTON_PRIMARY,
            MotionEvent.BUTTON_SECONDARY,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Button {
    }

    @IntDef(flag = true, value = {
            KeyEvent.META_SHIFT_ON,
            KeyEvent.META_CTRL_ON,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Key {
    }

    private static final class State {
        private @Action int mAction = ACTION_UNSET;
        private @ToolType int mToolType = MotionEvent.TOOL_TYPE_UNKNOWN;
        private @Source int mSource = InputDevice.SOURCE_UNKNOWN;
        private int mPointerCount = 1;
        private Set<Integer> mButtons = new HashSet<>();
        private Set<Integer> mKeys = new HashSet<>();
        private Point mLocation = new Point(0, 0);
        private Point mRawLocation = new Point(0, 0);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Test event builder with convenience methods for common event attrs.
     */
    public static final class Builder {

        private State mState = new State();

        /**
         * @param action Any action specified in {@link MotionEvent}.
         */
        public Builder action(int action) {
            mState.mAction = action;
            return this;
        }

        public Builder type(@ToolType int type) {
            mState.mToolType = type;
            return this;
        }

        /**
         * @param source Any source type specified in {@link InputDevice}. When adding a new
         *               source, ensure it is also added to the IntDef for @Source.
         */
        public Builder source(@Source int source) {
            mState.mSource = source;
            return this;
        }

        public Builder location(int x, int y) {
            mState.mLocation = new Point(x, y);
            return this;
        }

        public Builder rawLocation(int x, int y) {
            mState.mRawLocation = new Point(x, y);
            return this;
        }

        public Builder pointerCount(int count) {
            mState.mPointerCount = count;
            return this;
        }

        /**
         * Adds one or more button press attributes.
         */
        public Builder pressButton(@Button int... buttons) {
            for (int button : buttons) {
                mState.mButtons.add(button);
            }
            return this;
        }

        /**
         * Removes one or more button press attributes.
         */
        public Builder releaseButton(@Button int... buttons) {
            for (int button : buttons) {
                mState.mButtons.remove(button);
            }
            return this;
        }

        /**
         * Adds one or more key press attributes.
         */
        public Builder pressKey(@Key int... keys) {
            for (int key : keys) {
                mState.mKeys.add(key);
            }
            return this;
        }

        /**
         * Removes one or more key press attributes.
         */
        public Builder releaseKey(@Button int... keys) {
            for (int key : keys) {
                mState.mKeys.remove(key);
            }
            return this;
        }

        public Builder up() {
            action(MotionEvent.ACTION_UP);
            return this;
        }

        /**
         * Sets action to MotionEvent#ACTION_DOWN which can be used
         * with most tool types including mouse.
         *
         * <p>NOTE: ACTION_POINTER_DOWN is used for secondary pointers.
         */
        public Builder down() {
            action(MotionEvent.ACTION_DOWN);
            return this;
        }

        public Builder move() {
            action(MotionEvent.ACTION_MOVE);
            return this;
        }

        public Builder touch() {
            type(MotionEvent.TOOL_TYPE_FINGER);
            return this;
        }

        public Builder mouse() {
            type(MotionEvent.TOOL_TYPE_MOUSE);
            return this;
        }

        public Builder unknown() {
            type(MotionEvent.TOOL_TYPE_UNKNOWN);
            return this;
        }

        public Builder shift() {
            pressKey(KeyEvent.META_SHIFT_ON);
            return this;
        }

        public Builder unshift() {
            releaseKey(KeyEvent.META_SHIFT_ON);
            return this;
        }

        public Builder ctrl() {
            pressKey(KeyEvent.META_CTRL_ON);
            return this;
        }

        public Builder alt() {
            pressKey(KeyEvent.META_ALT_ON);
            return this;
        }

        public Builder primary() {
            pressButton(MotionEvent.BUTTON_PRIMARY);
            releaseButton(MotionEvent.BUTTON_SECONDARY);
            releaseButton(MotionEvent.BUTTON_TERTIARY);
            return this;
        }

        public Builder secondary() {
            pressButton(MotionEvent.BUTTON_SECONDARY);
            releaseButton(MotionEvent.BUTTON_PRIMARY);
            releaseButton(MotionEvent.BUTTON_TERTIARY);
            return this;
        }

        public Builder tertiary() {
            pressButton(MotionEvent.BUTTON_TERTIARY);
            releaseButton(MotionEvent.BUTTON_PRIMARY);
            releaseButton(MotionEvent.BUTTON_SECONDARY);
            return this;
        }

        public MotionEvent build() {

            PointerProperties[] pointers = new PointerProperties[1];
            pointers[0] = new PointerProperties();
            pointers[0].id = 0;
            pointers[0].toolType = mState.mToolType;

            PointerCoords[] coords = new PointerCoords[1];
            coords[0] = new PointerCoords();
            coords[0].x = mState.mLocation.x;
            coords[0].y = mState.mLocation.y;

            int buttons = 0;
            for (Integer button : mState.mButtons) {
                buttons |= button;
            }

            int keys = 0;
            for (Integer key : mState.mKeys) {
                keys |= key;
            }

            return MotionEvent.obtain(
                    0,     // down time
                    1,     // event time
                    mState.mAction,
                    1,  // pointerCount,
                    pointers,
                    coords,
                    keys,
                    buttons,
                    1.0f,            // x precision
                    1.0f,            // y precision
                    0,               // device id
                    0,               // edge flags
                    mState.mSource,  // int source,
                    0                // int flags
            );
        }
    }
}
