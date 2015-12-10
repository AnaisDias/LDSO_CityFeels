package com.example.ana.cityfeels.activities;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.R;

import junit.framework.Assert;

public class RouteActivityTest extends ActivityInstrumentationTestCase2<RouteActivity> {

    private RouteActivity routeActivity;
    private CityFeels application;
    private Instrumentation instrumentation;

    public RouteActivityTest() {
        super(RouteActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        this.routeActivity = getActivity();
        this.application = (CityFeels) this.routeActivity.getApplication();
        this.instrumentation = getInstrumentation();
    }

    public void testPreconditions() {
        assertNotNull("RouteActivity is null", this.routeActivity);
        assertNotNull("Application is null", this.application);
    }

    public void testOnCreate_layerButtons_basicOn() {
        Button basicLayerButton = (Button)this.routeActivity.findViewById(R.id.button1);

        assertTrue(basicLayerButton.isPressed());
    }

    public void testAfterCreate_layerButtons_exclusiveSelect() {
        Button basicLayerButton = (Button)this.routeActivity.findViewById(R.id.button1);
        Button localLayerButton = (Button)this.routeActivity.findViewById(R.id.button2);
        Button detailedLayerButton = (Button)this.routeActivity.findViewById(R.id.button3);
        Button anotherLayerButton = (Button)this.routeActivity.findViewById(R.id.button4);

        TouchUtils.clickView(this, anotherLayerButton);
        Assert.assertEquals(this.application.getCurrentDataLayer(), DataSource.DataLayer.Another);
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
