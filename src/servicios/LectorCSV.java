package servicios;

import entidades.RegistroTemperatura;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class LectorCSV implements ILectorDatos {
    
    @Override
    public List<RegistroTemperatura> leerRegistros(String rutaArchivo) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo, StandardCharsets.UTF_8))) {
            return br.lines()
                .skip(1)
                .filter(linea -> !linea.trim().isEmpty())
                .map(this::parsearLinea)
                .filter(registro -> registro != null)
                .collect(Collectors.toList());
        }
    }
    
    private RegistroTemperatura parsearLinea(String linea) {
        try {
            String[] partes = linea.split(",");
            if (partes.length != 3) {
                return null;
            }
            
            String ciudad = partes[0].trim();
            String fecha = partes[1].trim();
            double temperatura = Double.parseDouble(partes[2].trim());
            
            return new RegistroTemperatura(ciudad, fecha, temperatura);
        } catch (Exception e) {
            System.err.println("Error al parsear línea: " + linea + " - " + e.getMessage());
            return null;
        }
    }
}

