package edu.eci.cvds.proyect.booking.shedules;

import java.time.DayOfWeek;

public enum Day {
    LUNES(DayOfWeek.MONDAY),
    MARTES(DayOfWeek.TUESDAY),
    MIERCOLES(DayOfWeek.WEDNESDAY),
    JUEVES(DayOfWeek.THURSDAY),
    VIERNES(DayOfWeek.FRIDAY),
    SABADO(DayOfWeek.SATURDAY),
    DOMINGO(DayOfWeek.SUNDAY);

    private final DayOfWeek dayOfWeek;

    Day(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    // Conversión desde DayOfWeek de Java
    public static Day fromDayOfWeek(DayOfWeek dayOfWeek) {
        for (Day day : values()) {
            if (day.dayOfWeek == dayOfWeek) {
                return day;
            }
        }
        throw new IllegalArgumentException("Día no válido");
    }
}