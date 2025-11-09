package servicios;

import entidades.RegistroTemperatura;
import java.util.List;

public interface ILectorDatos {
    List<RegistroTemperatura> leerRegistros(String rutaArchivo) throws Exception;
}

