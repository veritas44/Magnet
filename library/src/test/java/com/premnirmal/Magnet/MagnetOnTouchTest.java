package com.premnirmal.Magnet;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class) @PrepareForTest({ RemoveView.class, Magnet.class })
public class MagnetOnTouchTest {

  private Magnet magnet;
  private View viewMock;
  private MotionEvent motionEventMock;

  @Before public void setUp() throws Exception {
    Display displayMock = mock(Display.class);
    Resources resourcesMock = mock(Resources.class);
    Context contextMock = mock(Context.class);

    DisplayMetrics displayMetricsMock = mock(DisplayMetrics.class);
    int initialX = 2;
    int initialY = 4;
    displayMetricsMock.widthPixels = initialX;
    displayMetricsMock.heightPixels = initialY;
    RemoveView removeViewMock = mock(RemoveView.class);
    WindowManager windowManagerMock = mock(WindowManager.class);
    IconCallback iconCallbackMock = mock(IconCallback.class);
    ImageView iconViewMock = mock(ImageView.class);
    LayoutParams paramsMock = mock(LayoutParams.class);

    doReturn(windowManagerMock).when(contextMock).getSystemService(Context.WINDOW_SERVICE);
    doReturn(displayMetricsMock).when(resourcesMock).getDisplayMetrics();
    doReturn(displayMock).when(windowManagerMock).getDefaultDisplay();
    doReturn(resourcesMock).when(contextMock).getResources();

    whenNew(RemoveView.class).withArguments(contextMock).thenReturn(removeViewMock);
    whenNew(DisplayMetrics.class).withNoArguments().thenReturn(displayMetricsMock);
    whenNew(LayoutParams.class).withAnyArguments().thenReturn(paramsMock);

    GestureDetector gestureDetectorMock = mock(GestureDetector.class);
    whenNew(GestureDetector.class).withAnyArguments().thenReturn(gestureDetectorMock);
    motionEventMock = mock(MotionEvent.class);
    doReturn(false).when(gestureDetectorMock).onTouchEvent(motionEventMock);

    magnet = Magnet.newBuilder(contextMock).setIconView(iconViewMock)
        .setIconCallback(iconCallbackMock)
        .setRemoveIconResId(R.drawable.ic_close)
        .setRemoveIconShadow(R.drawable.bottom_shadow)
        .setShouldStickToWall(true)
        .setRemoveIconShouldBeResponsive(true)
        .setInitialPosition(initialX, initialY)
        .build();

    setInternalState(magnet, "layoutParams", paramsMock);
    viewMock = mock(View.class);
  }

  @Test public void testOnTouchActionDown() throws Exception {
    // given
    doReturn(MotionEvent.ACTION_DOWN).when(motionEventMock).getAction();

    // when
    magnet.onTouch(viewMock, motionEventMock);

    // then
    assertEquals(magnet.isBeingDragged, true);
  }

  @Test public void testOnTouchActionUp() throws Exception {
    // given
    doReturn(MotionEvent.ACTION_UP).when(motionEventMock).getAction();

    // when
    magnet.onTouch(viewMock, motionEventMock);

    // then
    assertEquals(magnet.isBeingDragged, false);
  }
}
