package com.example.ana.cityfeels.models;

import android.test.AndroidTestCase;

public class PercursoTest extends AndroidTestCase {

    public void testConstructor() {
        Percurso percurso = new Percurso(1, new int[] {2,3,4});

        assertEquals(percurso.getId(), 1);
        assertEquals(percurso.getStartingPointId(), 2);
        assertEquals(percurso.getEndingPointId(), 4);
        assertEquals(percurso.getPointsOfInterestIds().length, 3);
        assertEquals(percurso.getLastPointId(), 2);
        assertTrue(percurso.isAtStart());
        assertFalse(percurso.isAtEnd());
    }

    public void testNextPointNotAtEnd() {
        Percurso percurso = new Percurso(1, new int[] {0,1,2});

        assertEquals(percurso.nextPoint(), 1);
    }

    public void testNextPointAtEnd() {
        Percurso percurso = new Percurso(1, new int[] {0});

        assertEquals(percurso.nextPoint(), -1);
    }

}
