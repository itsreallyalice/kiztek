/*
 * Copyright 2006-2019 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icepdf.ri.common;

import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.PropertyConstants;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

/**
 * The PageThumbnailComponent represents one page thumbnail preview in the larger
 * ThumbnailsPanel.  The Component when visible will try to load a thumbnail
 * from the Page object, the size of this thumbnail is defined by the document
 * and is displayed as is.  If no embedded thumbnail is found the page is
 * initialized and page thumbnail is captured and cached.  The size of this
 * thumbnail can be configure with the system property:
 * <br>
 * org.icepdf.vi.views.buffersize.vertical
 */
@SuppressWarnings("serial")
public class PageThumbnailComponent extends AbstractPageViewComponent implements MouseListener {

    private static final Logger logger =
            Logger.getLogger(PageThumbnailComponent.class.toString());

    public PageThumbnailComponent(DocumentViewController documentViewController,
                                  DocumentViewModel documentViewModel, PageTree pageTree,
                                  int pageNumber, float thumbNailZoom) {
        this(documentViewController, documentViewModel, pageTree, pageNumber, 0, 0, thumbNailZoom);
    }

    public PageThumbnailComponent(DocumentViewController documentViewController,
                                  DocumentViewModel documentViewModel, PageTree pageTree,
                                  int pageNumber,
                                  int width, int height,
                                  float thumbNailZoom) {
        super(documentViewModel, pageTree, pageNumber, width, height);

        this.documentViewController = documentViewController;

        // current state.
        pageZoom = thumbNailZoom;
        pageRotation = 0;

        addMouseListener(this);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // throw the resize event which will trigger a paint.
        updateView(PropertyConstants.DOCUMENT_VIEW_ZOOM_CHANGE, 1, pageZoom);
    }


    protected void calculateBufferLocation() {
        // grab a reference to the graphics configuration via the AWT thread,  if we get it on the worker thread
        // it sometimes return null.
        graphicsConfiguration = documentViewModel.getDocumentViewScrollPane().getGraphicsConfiguration();

        // page location in the the entire view.
        Rectangle pageLocation = this.getBounds();
        Rectangle viewPort = documentViewModel.getDocumentViewScrollPane().getViewport().getViewRect();

        // check if we need create or refresh the back pageBufferPadding.
        if (viewPort.intersects(pageLocation) && pageBufferStore.getImageReference() == null) {
            // start future task to paint back pageBufferPadding
            if (pageImageCaptureTask == null || pageImageCaptureTask.isDone() || pageImageCaptureTask.isCancelled()) {
                pageImageCaptureTask = new FutureTask<>(
                        new PageImageCaptureTask(this, pageSize, pageSize,
                                pageZoom,
                                pageRotation));
                Library.execute(pageImageCaptureTask);
            }
        }
    }

    public void dispose() {
        removeMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
        if (documentViewController != null) {
            documentViewController.getParentController().getDocumentViewController().setCurrentPageIndex(pageIndex);
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void pageInitializedCallback(Page page) {

    }

    public void pageTeardownCallback() {

    }

    public void clearSelectedText() {

    }

}