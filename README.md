# Sistema de Análisis de Temperaturas

**Evaluación 2 Seguimiento - Técnicas de Programación**  
**Universidad de Antioquia - Facultad de Ingeniería**

## Descripción de la Actividad

Implementar una aplicación Java que procese información de temperaturas utilizando **programación funcional**.

## Requisitos

### Datos de Entrada

Archivo CSV con el formato:
```
Ciudad,Fecha,Temperatura
Bogotá,01/01/2024,14.5
Medellín,01/01/2024,22.3
Cali,01/01/2024,25.4
...
```

### Funcionalidades Requeridas

1. **Cargar datos desde archivo CSV**

2. **Permitir al usuario seleccionar un rango de fechas** y mostrar una **gráfica de barras** con el promedio de temperatura para cada ciudad con datos disponibles en ese rango

3. **Indicar, para una fecha específica ingresada por el usuario**, cuál fue la **ciudad más calurosa** y la **ciudad menos calurosa**

### Paradigma Funcional

Procesar la información de las colecciones usando:
- `filter()` - Filtrar elementos
- `map()` - Transformar elementos
- `sorted()` - Ordenar elementos
- `collect()` - Recolectar resultados

## Compilar y Ejecutar

```powershell
javac -encoding UTF-8 -d bin src\App.java src\FrmTemperatura.java src\entidades\*.java src\servicios\*.java
java -cp bin App
```

## Estructura del Proyecto

```
src/
├── App.java
├── FrmTemperatura.java
├── entidades/
│   └── RegistroTemperatura.java
├── servicios/
│   ├── ILectorDatos.java
│   ├── LectorCSV.java
│   ├── IServicioTemperatura.java
│   └── ServicioTemperatura.java
└── datos/
    └── Temperatura.csv
```

