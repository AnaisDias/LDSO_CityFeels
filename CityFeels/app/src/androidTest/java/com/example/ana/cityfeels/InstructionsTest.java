package com.example.ana.cityfeels;

import android.test.AndroidTestCase;

public class InstructionsTest extends AndroidTestCase {

    public void testDireita() {
        Instructions instructions = new Instructions("[ori]", 0);

        String text = instructions.applyOrientation((float)-90);

        assertTrue(text.indexOf("À sua direita") == 0);
    }

    public void testEsquerda() {
        Instructions instructions = new Instructions("[ori]", 0);

        String text = instructions.applyOrientation((float)90.0);

        assertTrue(text.indexOf("À sua esquerda") == 0);
    }

    public void testFrente() {
        Instructions instructions = new Instructions("[ori]", 0);

        String text = instructions.applyOrientation((float)0);

        assertTrue(text.indexOf("À sua frente") == 0);
    }

    public void testAtras() {
        Instructions instructions = new Instructions("[ori]", 0);

        String text = instructions.applyOrientation((float)180);

        assertTrue(text.indexOf("Atrás de si") == 0);
    }

}
