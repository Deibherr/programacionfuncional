package servicios;

import entidades.RegistroTemperatura;
import java.time.LocalDate;
import java.util.List;
/**
 * Esta interfaz sirve para definir las operaciones que se esperan para los servicios que manejan
 * registros de temperatura (por ejemplo, filtrar los datos por rango de fechas).
 * 
 * No se implementa ni se escribe directamente en la clase ServicioTemperatura porque una interfaz
 * sólo declara qué métodos deben existir, pero no cómo se realizan. Así, se puede crear más adelante
 * otra clase diferente que implemente esta interfaz de otra forma, sin tener que modificar el resto
 * del programa. También ayuda a hacer el código más flexible y fácil de probar o ampliar.
 */


public interface IServicioTemperatura {
    List<RegistroTemperatura> filtrarPorRangoFechas(List<RegistroTemperatura> registros,
                                                     LocalDate fechaInicio,
                                                     LocalDate fechaFin);
}

