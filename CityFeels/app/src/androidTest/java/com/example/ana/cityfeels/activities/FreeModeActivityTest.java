package com.example.ana.cityfeels.activities;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;

import com.example.ana.cityfeels.CityFeels;
import com.example.ana.cityfeels.DataSource;
import com.example.ana.cityfeels.R;

import junit.framework.Assert;

public class FreeModeActivityTest extends ActivityInstrumentationTestCase2<FreeModeActivity> {

    private FreeModeActivity freeModeActivity;
    private CityFeels application;
    private Instrumentation instrumentation;

    public FreeModeActivityTest() {
        super(FreeModeActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        this.freeModeActivity = getActivity();
        this.application = (CityFeels) this.freeModeActivity.getApplication();
        this.instrumentation = getInstrumentation();
    }

    public void testPreconditions() {
        assertNotNull("FreeModeActivity is null", this.freeModeActivity);
        assertNotNull("Application is null", this.application);
    }

    public void testVisibleViews() {
        final View decorView = this.freeModeActivity.getWindow().getDecorView();

        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.percursoTextView));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.inicios));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.destinos));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.calcularButton));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.button1));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.button2));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.button3));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.button4));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.modoLivreTextView));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.modoLivreDirecoes));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.informacaoTextView));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.repeatButtonImage));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.radioOuvir));
        ViewAsserts.assertOnScreen(decorView, this.freeModeActivity.findViewById(R.id.radioSilenciar));
    }

    public void testCorrectLayerButtonPressed() {
        DataSource.DataLayer layer = this.application.getCurrentDataLayer();

        switch(layer) {
            case Basic:
                assertTrue(this.freeModeActivity.findViewById(R.id.button1).isPressed());
                break;
            case Local:
                assertTrue(this.freeModeActivity.findViewById(R.id.button2).isPressed());
                break;
            case Detailed:
                assertTrue(this.freeModeActivity.findViewById(R.id.button3).isPressed());
                break;
            case Another:
                assertTrue(this.freeModeActivity.findViewById(R.id.button4).isPressed());
                break;
        }
    }

    public void testLayersExclusiveSelect() {
        Button basicLayerButton = (Button)this.freeModeActivity.findViewById(R.id.button1);
        Button localLayerButton = (Button)this.freeModeActivity.findViewById(R.id.button2);
        Button detailedLayerButton = (Button)this.freeModeActivity.findViewById(R.id.button3);
        Button anotherLayerButton = (Button)this.freeModeActivity.findViewById(R.id.button4);

        TouchUtils.clickView(this, anotherLayerButton);
        Assert.assertEquals(DataSource.DataLayer.Another, this.application.getCurrentDataLayer());
        assertTrue(anotherLayerButton.isPressed());
        assertFalse(detailedLayerButton.isPressed());
        assertFalse(localLayerButton.isPressed());
        assertFalse(basicLayerButton.isPressed());

        TouchUtils.clickView(this, detailedLayerButton);
        assertEquals(DataSource.DataLayer.Detailed, this.application.getCurrentDataLayer());
        assertTrue(detailedLayerButton.isPressed());
        assertFalse(anotherLayerButton.isPressed());
        assertFalse(localLayerButton.isPressed());
        assertFalse(basicLayerButton.isPressed());

        TouchUtils.clickView(this, localLayerButton);
        assertEquals(DataSource.DataLayer.Local, this.application.getCurrentDataLayer());
        assertTrue(localLayerButton.isPressed());
        assertFalse(anotherLayerButton.isPressed());
        assertFalse(detailedLayerButton.isPressed());
        assertFalse(basicLayerButton.isPressed());

        TouchUtils.clickView(this, basicLayerButton);
        assertEquals(DataSource.DataLayer.Basic, this.application.getCurrentDataLayer());
        assertTrue(basicLayerButton.isPressed());
        assertFalse(detailedLayerButton.isPressed());
        assertFalse(localLayerButton.isPressed());
        assertFalse(anotherLayerButton.isPressed());
    }


}
