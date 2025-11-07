package entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RegistroTemperatura {
    private final String ciudad;
    private final LocalDate fecha;
    private final double temperatura;
    
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public RegistroTemperatura(String ciudad, LocalDate fecha, double temperatura) {
        this.ciudad = ciudad;
        this.fecha = fecha;
        this.temperatura = temperatura;
    }
    
    public RegistroTemperatura(String ciudad, String fechaStr, double temperatura) {
        this.ciudad = ciudad;
        this.fecha = LocalDate.parse(fechaStr, FORMATO_FECHA);
        this.temperatura = temperatura;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public double getTemperatura() {
        return temperatura;
    }
    
    public String getFechaFormateada() {
        return fecha.format(FORMATO_FECHA);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistroTemperatura that = (RegistroTemperatura) o;
        return Double.compare(that.temperatura, temperatura) == 0 &&
               Objects.equals(ciudad, that.ciudad) &&
               Objects.equals(fecha, that.fecha);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ciudad, fecha, temperatura);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s: %.1f°C", ciudad, getFechaFormateada(), temperatura);
    }
}

