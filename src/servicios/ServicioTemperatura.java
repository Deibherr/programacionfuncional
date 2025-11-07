package servicios;

import entidades.RegistroTemperatura;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioTemperatura implements IServicioTemperatura {
    
    @Override
    public List<RegistroTemperatura> filtrarPorRangoFechas(List<RegistroTemperatura> registros,
                                                             LocalDate fechaInicio,
                                                             LocalDate fechaFin) {
        return registros.stream()
            .filter(r -> !r.getFecha().isBefore(fechaInicio) && !r.getFecha().isAfter(fechaFin))
            .sorted(Comparator.comparing(RegistroTemperatura::getFecha)
                             .thenComparing(RegistroTemperatura::getCiudad))
            .collect(Collectors.toList());
    }
}

