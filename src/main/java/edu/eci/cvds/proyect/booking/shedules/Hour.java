package edu.eci.cvds.proyect.booking.shedules;

public enum Hour {
    SIETE("7:00"), 
    OCHO_TREINTA("8:30"), 
    DIEZ("10:00"), 
    ONCE_TREINTA("11:30"), 
    UNA("13:00"), 
    DOS_TREINTA("14:30"), 
    CUATRO("16:00"), 
    CINCO_TREINTA("17:30");
    
    private final String time;

    Hour(String hour) {
        this.time = hour;
    }

    public String getHour() {
        return time;
    }
}
