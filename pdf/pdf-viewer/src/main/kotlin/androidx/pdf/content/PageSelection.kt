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

package androidx.pdf.content

import androidx.annotation.RestrictTo

/**
 * Represents the list of selected content on a particular page of the PDF document. By default, the
 * selection boundary is represented from left to right. Note: Currently supports text selection
 * only.
 *
 * @param page: The page number of the selection.
 * @param start: Boundary where the selection starts.
 * @param stop: Boundary where the selection stops.
 * @param selectedContents: list of segments of selected text content.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class PageSelection(
    public val page: Int,
    public val start: SelectionBoundary,
    public val stop: SelectionBoundary,
    public val selectedTextContents: List<PdfPageTextContent>
)
