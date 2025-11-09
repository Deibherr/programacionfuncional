package servicios;

import entidades.RegistroTemperatura;
import java.time.LocalDate;
import java.util.List;

public interface IServicioTemperatura {
    List<RegistroTemperatura> filtrarPorRangoFechas(List<RegistroTemperatura> registros,
                                                     LocalDate fechaInicio,
                                                     LocalDate fechaFin);
}

