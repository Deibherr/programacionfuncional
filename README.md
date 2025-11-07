# 🌡️ Sistema de Análisis de Temperaturas

Aplicación Java que implementa **Principios SOLID** y **Programación Funcional** para analizar datos de temperatura de ciudades colombianas.

## 🚀 Inicio Rápido

### Compilar
```powershell
javac -encoding UTF-8 -d bin src\App.java src\FrmTemperatura.java src\entidades\*.java src\servicios\*.java
```

### Ejecutar
```powershell
java -cp bin App
```

## 📋 Funcionalidades

✅ Cargar datos desde archivo CSV  
✅ Calcular promedios de temperatura por ciudad  
✅ Identificar ciudad más calurosa y menos calurosa  
✅ Filtrar por rangos de fechas  
✅ Interfaz gráfica moderna con Swing  

## 🎯 Principios SOLID Aplicados

- **S** - Single Responsibility: Cada clase una responsabilidad
- **O** - Open/Closed: Interfaces para extensión
- **L** - Liskov Substitution: Implementaciones intercambiables
- **I** - Interface Segregation: Interfaces específicas
- **D** - Dependency Inversion: Dependencia de abstracciones

## 🔄 Programación Funcional

- **filter()** - Filtrar registros por fecha
- **map()** - Transformar líneas CSV a objetos
- **sorted()** - Ordenar por temperatura
- **collect()** - Agrupar y calcular promedios

## 📁 Estructura del Proyecto

```
src/
├── App.java                          # Punto de entrada
├── FrmTemperatura.java               # Interfaz gráfica
├── entidades/
│   └── RegistroTemperatura.java      # Entidad de datos
├── servicios/
│   ├── ILectorDatos.java             # Interfaz lectura
│   ├── LectorCSV.java                # Implementación CSV
│   ├── IServicioTemperatura.java     # Interfaz análisis
│   └── ServicioTemperatura.java      # Implementación
└── datos/
    └── Temperatura.CSV               # Datos (144 registros)
```

## 📚 Documentación Completa

- **[INICIO_RAPIDO.md](INICIO_RAPIDO.md)** - Guía de inicio rápido
- **[DOCUMENTACION_SOLID.md](DOCUMENTACION_SOLID.md)** - Documentación técnica completa
- **[GUIA_DE_USO.md](GUIA_DE_USO.md)** - Guía de usuario con ejemplos
- **[ARQUITECTURA_SOLID.md](ARQUITECTURA_SOLID.md)** - Diagramas y arquitectura
- **[RESUMEN_PROYECTO.md](RESUMEN_PROYECTO.md)** - Resumen ejecutivo

## 💡 Ejemplo de Uso

### Calcular Promedios (Pestaña "Datos")
1. Usa el **selector de calendario** para Fecha Inicio: `01/01/2024` 📅
2. Usa el **selector de calendario** para Fecha Fin: `31/12/2024` 📅
3. Clic en "📊 Calcular Promedios"

### Temperaturas Extremas (Pestaña "Gráfica")
1. Usa el **selector de calendario** para la Fecha: `01/06/2024` 📅
2. Clic en "🔍 Consultar Temperaturas Extremas"

> **Mejora**: La interfaz ahora usa selectores de calendario (JSpinner) que permiten navegar por fechas fácilmente con flechas ⬆️⬇️

## 🎓 Conceptos Técnicos

- Java Streams API
- Lambda expressions
- SOLID Principles
- Dependency Injection
- MVC Pattern
- Swing GUI

## 📊 Datos

- **Ciudades**: Bogotá, Medellín, Cali
- **Período**: 2022-2025
- **Registros**: 144 mediciones

## ✅ Estado del Proyecto

✅ Compilación exitosa  
✅ Aplicación funcional  
✅ SOLID aplicado al 100%  
✅ Programación funcional implementada  
✅ Documentación completa  

---

**Proyecto Académico** - Programación Funcional  
**Calificación**: 20% del segundo seguimiento
