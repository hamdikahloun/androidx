/*
 * Copyright 2021 The Android Open Source Project
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
package androidx.emoji2.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.MetricAffectingSpan;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.jspecify.annotations.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EmojiSpanTest {

    @Before
    public void setup() {
        EmojiCompat.reset(NoFontTestEmojiConfig.emptyConfig());
    }

    @Test
    public void testGetSize() {
        final int dimensionX = 18;
        final int dimensionY = 20;
        final int fontHeight = 10;
        final float expectedRatio = fontHeight * 1.0f / dimensionY;
        final TextPaint paint = mock(TextPaint.class);

        // mock TextPaint to return test font metrics
        when(paint.getFontMetricsInt(any(FontMetricsInt.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final FontMetricsInt fontMetrics = (FontMetricsInt) invocation.getArguments()[0];
                fontMetrics.ascent = 0;
                fontMetrics.descent = -fontHeight;
                return null;
            }
        });

        final TypefaceEmojiRasterizer metadata = mock(TypefaceEmojiRasterizer.class);
        when(metadata.getWidth()).thenReturn(dimensionX);
        when(metadata.getHeight()).thenReturn(dimensionY);
        final EmojiSpan span = new TypefaceEmojiSpan(metadata);

        final int resultSize = span.getSize(paint, "", 0, 0, null);
        assertEquals((int) (dimensionX * expectedRatio), resultSize);
        assertEquals(expectedRatio, span.getRatio(), 0.01f);
        assertEquals((int) (dimensionX * expectedRatio), span.getWidth());
        assertEquals((int) (dimensionY * expectedRatio), span.getHeight());
    }

    @Test
    public void testBackgroundIndicator() {
        // control the size of the emoji span
        final TypefaceEmojiRasterizer metadata = mock(TypefaceEmojiRasterizer.class);
        when(metadata.getWidth()).thenReturn(10);
        when(metadata.getHeight()).thenReturn(10);

        final EmojiSpan span = new TypefaceEmojiSpan(metadata);
        TextPaint textPaint = new TextPaint();
        final int spanWidth = span.getSize(textPaint, "", 0, 0, null);
        // prepare parameters for draw() call
        final Canvas canvas = mock(Canvas.class);
        final float x = 10;
        final int top = 15;
        final int y = 20;
        final int bottom = 30;

        // verify the case where indicators are disabled
        EmojiCompat.reset(NoFontTestEmojiConfig.emptyConfig().setEmojiSpanIndicatorEnabled(false));
        TextPaint paint = new TextPaint();
        span.draw(canvas, "a", 0 /*start*/, 1 /*end*/, x, top, y, bottom, paint);

        verify(canvas, times(0)).drawRect(eq(x), eq((float) top), eq(x + spanWidth),
                eq((float) bottom), any(Paint.class));

        // verify the case where indicators are enabled
        EmojiCompat.reset(NoFontTestEmojiConfig.emptyConfig().setEmojiSpanIndicatorEnabled(true));
        reset(canvas);
        span.draw(canvas, "a", 0 /*start*/, 1 /*end*/, x, top, y, bottom, textPaint);

        verify(canvas, times(1)).drawRect(eq(x), eq((float) top), eq(x + spanWidth),
                eq((float) bottom), any(Paint.class));
    }
    @Test
    public void testBackgroundColor_doesDrawbackground() {
        // control the size of the emoji span
        final TypefaceEmojiRasterizer metadata = mock(TypefaceEmojiRasterizer.class);
        when(metadata.getWidth()).thenReturn(10);
        when(metadata.getHeight()).thenReturn(10);

        final EmojiSpan span = new TypefaceEmojiSpan(metadata);
        TextPaint textPaint = new TextPaint();
        final int spanWidth = span.getSize(textPaint, "", 0, 0, null);
        final Spannable spannable = new SpannableString("Hello");
        spannable.setSpan(new BackgroundColorSpan(Color.parseColor("#FF0000")), 0, 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // prepare parameters for draw() call
        final Canvas canvas = mock(Canvas.class);
        final float x = 10;
        final int top = 15;
        final int y = 20;
        final int bottom = 30;

        // verify the case where background draw happens (position 0)
        EmojiCompat.reset(NoFontTestEmojiConfig.emptyConfig().setEmojiSpanIndicatorEnabled(false));
        TextPaint paint = new TextPaint();
        span.draw(canvas, spannable, 0 /*start*/, 1 /*end*/, x, top, y, bottom, paint);

        verify(canvas, times(1)).drawRect(eq(x), eq((float) top), eq(x + spanWidth),
                eq((float) bottom), any(Paint.class));
    }

    @Test
    public void testBackgroundColor_inPositionNextToEmoji_doesNotDrawBackground() {
        // control the size of the emoji span
        final TypefaceEmojiRasterizer metadata = mock(TypefaceEmojiRasterizer.class);
        when(metadata.getWidth()).thenReturn(10);
        when(metadata.getHeight()).thenReturn(10);

        final EmojiSpan span = new TypefaceEmojiSpan(metadata);
        TextPaint textPaint = new TextPaint();
        final int spanWidth = span.getSize(textPaint, "", 0, 0, null);
        final Spannable spannable = new SpannableString("Hello");
        spannable.setSpan(new BackgroundColorSpan(Color.parseColor("#FF0000")), 0, 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // prepare parameters for draw() call
        final Canvas canvas = mock(Canvas.class);
        final float x = 10;
        final int top = 15;
        final int y = 20;
        final int bottom = 30;

        // verify the case where background draw happens (position 0)
        EmojiCompat.reset(NoFontTestEmojiConfig.emptyConfig().setEmojiSpanIndicatorEnabled(false));
        span.draw(canvas, spannable, 1 /*start*/, 2 /*end*/, x, top, y, bottom, textPaint);

        verify(canvas, never()).drawRect(eq(x), eq((float) top), eq(x + spanWidth),
                eq((float) bottom), any(Paint.class));
    }


    @Test
    public void testMetrcisSpan_notInvoked_byBackgroundCode() {
        // control the size of the emoji span
        final TypefaceEmojiRasterizer metadata = mock(TypefaceEmojiRasterizer.class);
        when(metadata.getWidth()).thenReturn(10);
        when(metadata.getHeight()).thenReturn(10);

        final EmojiSpan span = new TypefaceEmojiSpan(metadata);
        TextPaint textPaint = new TextPaint();
        final Spannable spannable = new SpannableString("Hello");
        MyMetricSpan subject = new MyMetricSpan();
        spannable.setSpan(subject, 0, 1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // prepare parameters for draw() call
        final Canvas canvas = mock(Canvas.class);
        final float x = 10;
        final int top = 15;
        final int y = 20;
        final int bottom = 30;

        // verify the case where background draw happens (position 0)
        EmojiCompat.reset(NoFontTestEmojiConfig.emptyConfig().setEmojiSpanIndicatorEnabled(false));
        TextPaint paint = new TextPaint();
        span.draw(canvas, spannable, 0 /*start*/, 1 /*end*/, x, top, y, bottom, paint);

        assertFalse(subject.mDidUpdateDrawState);
        assertFalse(subject.mDidUpdateMeasureState);
    }

    class MyMetricSpan extends MetricAffectingSpan {
        boolean mDidUpdateMeasureState = false;
        boolean mDidUpdateDrawState = false;

        @Override
        public void updateMeasureState(@NonNull TextPaint textPaint) {
            mDidUpdateMeasureState = true;
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            mDidUpdateDrawState = true;
        }
    }

}
