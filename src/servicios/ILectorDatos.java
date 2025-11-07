package servicios;

import entidades.RegistroTemperatura;
import java.util.List;
/**
 * Esta interfaz sirve para definir cómo deben implementarse las clases que leen registros de temperatura.
 * El propósito de crear esta interfaz, en vez de poner toda la lógica únicamente en LectorCSV, es permitir
 * que en el futuro se puedan agregar otras formas de obtener los datos (por ejemplo, de una base de datos,
 * de un servicio web, o de distintos formatos de archivos) sin tener que modificar el código existente.
 * 
 * Así, el programa puede trabajar con cualquier clase que implemente esta interfaz, haciendo que el código
 * sea más flexible y más fácil de mantener y ampliar.
 */



public interface ILectorDatos {
    List<RegistroTemperatura> leerRegistros(String rutaArchivo) throws Exception;
}

