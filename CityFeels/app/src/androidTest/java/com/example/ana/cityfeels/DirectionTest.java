package com.example.ana.cityfeels;

import android.test.AndroidTestCase;

public class DirectionTest extends AndroidTestCase {

    public void testDireita() {
        Direction direction = new Direction("[ori]", 0);

        String text = direction.applyOrientation(-90);

        assertTrue(text.indexOf("À sua direita") == 0);
    }

    public void testEsquerda() {
        Direction direction = new Direction("[ori]", 0);

        String text = direction.applyOrientation(90);

        assertTrue(text.indexOf("À sua esquerda") == 0);
    }

    public void testFrente() {
        Direction direction = new Direction("[ori]", 0);

        String text = direction.applyOrientation(0);

        assertTrue(text.indexOf("À sua frente") == 0);
    }

    public void testAtras() {
        Direction direction = new Direction("[ori]", 0);

        String text = direction.applyOrientation(180);

        assertTrue(text.indexOf("Atrás de si") == 0);
    }

}
