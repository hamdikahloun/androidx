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

package androidx.pdf.fetcher;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.RestrictTo;

import java.io.File;

/**
 * A very simple disk cache that caches file contents using an {@link Uri} and mimeType as key.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class DiskCache {
    private static final String TAG = DiskCache.class.getSimpleName();

    /** The root folder where this cache lives, in the app's cache directory. */
    private static final String SUB_CACHE_DIR = "projector-disk";

    /** A temporary folder which contains incomplete files until they've been fully fetched. */
    private static final String TMP_CACHE_DIR = "projector-tmp";

    private final File mCacheRoot;
    private final File mTmpCacheRoot;
    public DiskCache(Context context) {
        mCacheRoot = getDiskCacheDir(context);
        mTmpCacheRoot = getTmpCacheDir(context);
        mCacheRoot.mkdir();
        mTmpCacheRoot.mkdir();
    }

    // TODO: Make this private. Currently used by FileProvider to access a cached file.
    static File getDiskCacheDir(Context context) {
        return new File(context.getCacheDir(), SUB_CACHE_DIR);
    }

    private static File getTmpCacheDir(Context context) {
        return new File(context.getCacheDir(), TMP_CACHE_DIR);
    }
}