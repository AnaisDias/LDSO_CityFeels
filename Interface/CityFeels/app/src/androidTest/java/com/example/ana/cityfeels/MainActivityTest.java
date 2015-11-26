package com.example.ana.cityfeels;

import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private CityFeels application;
    private Instrumentation instrumentation;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        this.mainActivity = getActivity();
        this.application = (CityFeels) this.mainActivity.getApplication();
        this.instrumentation = getInstrumentation();
    }

    public void testPreconditions() {
        assertNotNull("MainActivity is null", this.mainActivity);
        assertNotNull("Application is null", this.application);
    }

    public void testOnCreate_layerButtons_basicOn() {
        Button basicLayerButton = (Button)this.mainActivity.findViewById(R.id.button1);

        assertTrue(basicLayerButton.isPressed());
    }

    public void testAfterCreate_layerButtons_exclusiveSelect() {
        Button basicLayerButton = (Button)this.mainActivity.findViewById(R.id.button1);
        Button localLayerButton = (Button)this.mainActivity.findViewById(R.id.button2);
        Button detailedLayerButton = (Button)this.mainActivity.findViewById(R.id.button3);
        Button anotherLayerButton = (Button)this.mainActivity.findViewById(R.id.button4);

        TouchUtils.clickView(this, anotherLayerButton);
        assertEquals(this.application.getCurrentDataLayer(), DataSource.DataLayer.Another);
        assertTrue(anotherLayerButton.isPressed());
        assertFalse(detailedLayerButton.isPressed());
        assertFalse(localLayerButton.isPressed());
        assertFalse(basicLayerButton.isPressed());

        TouchUtils.clickView(this, detailedLayerButton);
        assertEquals(this.application.getCurrentDataLayer(), DataSource.DataLayer.Detailed);
        assertTrue(detailedLayerButton.isPressed());
        assertFalse(anotherLayerButton.isPressed());
        assertFalse(localLayerButton.isPressed());
        assertFalse(basicLayerButton.isPressed());

        TouchUtils.clickView(this, localLayerButton);
        assertEquals(this.application.getCurrentDataLayer(), DataSource.DataLayer.Local);
        assertTrue(localLayerButton.isPressed());
        assertFalse(anotherLayerButton.isPressed());
        assertFalse(detailedLayerButton.isPressed());
        assertFalse(basicLayerButton.isPressed());

        TouchUtils.clickView(this, basicLayerButton);
        assertEquals(this.application.getCurrentDataLayer(), DataSource.DataLayer.Basic);
        assertTrue(basicLayerButton.isPressed());
        assertFalse(detailedLayerButton.isPressed());
        assertFalse(localLayerButton.isPressed());
        assertFalse(anotherLayerButton.isPressed());
    }

}
