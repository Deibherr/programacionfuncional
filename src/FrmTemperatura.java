import entidades.RegistroTemperatura;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import servicios.ILectorDatos;
import servicios.IServicioTemperatura;
import servicios.LectorCSV;
import servicios.ServicioTemperatura;

public class FrmTemperatura extends JFrame {
    
    private final ILectorDatos lectorDatos;
    private final IServicioTemperatura servicioTemperatura;
    private List<RegistroTemperatura> registros;
    private JPanel spnFechaInicio;
    private JPanel spnFechaFin;
    private JTable tablaPromedios;
    private DefaultTableModel modeloTabla;
    private GraficaBarras graficaBarras;
    
    public FrmTemperatura() {
        this.lectorDatos = new LectorCSV();
        this.servicioTemperatura = new ServicioTemperatura();
        this.registros = new java.util.ArrayList<>();
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Sistema de Análisis de Temperaturas");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));
        
        JPanel panelSuperior = crearPanelControles();
        add(panelSuperior, BorderLayout.NORTH);
        
        JPanel panelGrafica = crearPanelTabla();
        add(panelGrafica, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        
        graficaBarras = new GraficaBarras();
        panel.add(graficaBarras, BorderLayout.CENTER);
        String[] columnas = {"Ciudad", "Promedio (°C)", "Máximo (°C)", "Mínimo (°C)", "Registros"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPromedios = new JTable(modeloTabla);
        tablaPromedios.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaPromedios.setRowHeight(25);
        tablaPromedios.setGridColor(new Color(220, 220, 220));
        tablaPromedios.setSelectionBackground(new Color(184, 207, 229));
        tablaPromedios.getTableHeader().setBackground(new Color(70, 130, 180));
        tablaPromedios.getTableHeader().setForeground(Color.WHITE);
        tablaPromedios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaPromedios.getTableHeader().setPreferredSize(new Dimension(0, 30));
        
        JScrollPane scrollTabla = new JScrollPane(tablaPromedios);
        scrollTabla.setPreferredSize(new Dimension(0, 150));
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Datos Detallados",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        panel.add(scrollTabla, BorderLayout.SOUTH);
        
        return panel;
    }
    
    
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton btnCargarArchivo = new JButton("📁 Cargar CSV");
        btnCargarArchivo.setBackground(new Color(52, 152, 219));
        btnCargarArchivo.setForeground(Color.WHITE);
        btnCargarArchivo.setFocusPainted(false);
        btnCargarArchivo.setPreferredSize(new Dimension(120, 25));
        btnCargarArchivo.setFont(new Font("Arial", Font.BOLD, 11));
        btnCargarArchivo.addActionListener(e -> seleccionarYCargarArchivo());
        panel.add(btnCargarArchivo);
        
        panel.add(Box.createHorizontalStrut(30));
        
        spnFechaInicio = crearSelectorFechaConBoton(2024, Calendar.JANUARY, 1);
        panel.add(spnFechaInicio);
        
        panel.add(Box.createHorizontalStrut(10));
        
        spnFechaFin = crearSelectorFechaConBoton(2024, Calendar.DECEMBER, 31);
        panel.add(spnFechaFin);
        
        panel.add(Box.createHorizontalStrut(30));
        
        JButton btnGraficar = new JButton("📊 Graficar");
        btnGraficar.setBackground(new Color(70, 130, 180));
        btnGraficar.setForeground(Color.WHITE);
        btnGraficar.setFocusPainted(false);
        btnGraficar.setPreferredSize(new Dimension(110, 25));
        btnGraficar.setFont(new Font("Arial", Font.BOLD, 11));
        btnGraficar.addActionListener(e -> calcularPromedios());
        panel.add(btnGraficar);
        
        JButton btnConsultar = new JButton("🔍 Consultar");
        btnConsultar.setBackground(new Color(46, 204, 113));
        btnConsultar.setForeground(Color.WHITE);
        btnConsultar.setFocusPainted(false);
        btnConsultar.setPreferredSize(new Dimension(110, 25));
        btnConsultar.setFont(new Font("Arial", Font.BOLD, 11));
        btnConsultar.addActionListener(e -> consultarTemperaturasExtremas());
        panel.add(btnConsultar);
        
        return panel;
    }
    
    private JPanel crearSelectorFechaConBoton(int año, int mes, int dia) {
        JPanel panel = new JPanel(new BorderLayout(2, 0));
        panel.setOpaque(false);
        
        Calendar cal = Calendar.getInstance();
        cal.set(año, mes, dia);
        
        JTextField txtFecha = new JTextField(String.format("%02d/%02d/%04d", dia, mes + 1, año));
        txtFecha.setEditable(false);
        txtFecha.setBackground(Color.WHITE);
        txtFecha.setFont(new Font("Arial", Font.PLAIN, 12));
        txtFecha.setPreferredSize(new Dimension(85, 25));
        txtFecha.setHorizontalAlignment(JTextField.CENTER);
        
        JButton btnCalendario = new JButton("📅");
        btnCalendario.setBackground(new Color(100, 149, 237));
        btnCalendario.setForeground(Color.WHITE);
        btnCalendario.setFocusPainted(false);
        btnCalendario.setPreferredSize(new Dimension(30, 25));
        btnCalendario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        btnCalendario.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));
        
        btnCalendario.addActionListener(e -> {
            Calendar fechaActual = Calendar.getInstance();
            fechaActual.setTime(obtenerFechaDeTextField(txtFecha));
            
            CalendarioDialog dialogo = new CalendarioDialog(this, fechaActual);
            dialogo.setVisible(true);
            
            if (dialogo.getFechaSeleccionada() != null) {
                Calendar fechaNueva = dialogo.getFechaSeleccionada();
                txtFecha.setText(String.format("%02d/%02d/%04d", 
                    fechaNueva.get(Calendar.DAY_OF_MONTH),
                    fechaNueva.get(Calendar.MONTH) + 1,
                    fechaNueva.get(Calendar.YEAR)));
            }
        });
        
        panel.add(txtFecha, BorderLayout.CENTER);
        panel.add(btnCalendario, BorderLayout.EAST);
        
        return panel;
    }
    
    private Date obtenerFechaDeTextField(JTextField txtFecha) {
        try {
            String[] partes = txtFecha.getText().split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]) - 1;
            int año = Integer.parseInt(partes[2]);
            
            Calendar cal = Calendar.getInstance();
            cal.set(año, mes, dia);
            return cal.getTime();
        } catch (Exception e) {
            return new Date();
        }
    }
    
    private JTextField obtenerTextField(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                return (JTextField) comp;
            }
        }
        return null;
    }
    
    private LocalDate dateToLocalDate(Date date) {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }
    
    private LocalDate obtenerFechaDePanel(JPanel panel) {
        JTextField txtFecha = obtenerTextField(panel);
        if (txtFecha != null) {
            return dateToLocalDate(obtenerFechaDeTextField(txtFecha));
        }
        return LocalDate.now();
    }
    
    private void seleccionarYCargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo CSV de temperaturas");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
            }
            
            @Override
            public String getDescription() {
                return "Archivos CSV (*.csv)";
            }
        });
        
        fileChooser.setCurrentDirectory(new java.io.File("src/datos"));
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File archivoSeleccionado = fileChooser.getSelectedFile();
            cargarDatos(archivoSeleccionado.getAbsolutePath());
        }
    }
    
    private void cargarDatos(String rutaArchivo) {
        try {
            registros = lectorDatos.leerRegistros(rutaArchivo);
            
            java.util.Set<String> ciudadesEncontradas = registros.stream()
                .map(RegistroTemperatura::getCiudad)
                .collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new));
            
            JOptionPane.showMessageDialog(this,
                "✅ Archivo cargado exitosamente\n\n" +
                "Registros: " + registros.size() + "\n" +
                "Ciudades: " + ciudadesEncontradas.size() + "\n" +
                "Archivo: " + new java.io.File(rutaArchivo).getName(),
                "Datos Cargados",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            String mensaje = "Error al cargar datos: " + e.getMessage();
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(mensaje);
        }
    }
    
    private void calcularPromedios() {
        try {
            if (registros == null || registros.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay datos cargados.\n\nPor favor, cargue un archivo CSV primero usando el botón '📁 Cargar CSV'",
                    "Sin Datos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate fechaInicio = obtenerFechaDePanel(spnFechaInicio);
            LocalDate fechaFin = obtenerFechaDePanel(spnFechaFin);
            
            if (fechaInicio.isAfter(fechaFin)) {
                JOptionPane.showMessageDialog(this,
                    "La fecha de inicio no puede ser posterior a la fecha de fin",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<RegistroTemperatura> registrosFiltrados = servicioTemperatura.filtrarPorRangoFechas(
                registros, fechaInicio, fechaFin);
            
            Map<String, Double> promedios = registrosFiltrados.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    RegistroTemperatura::getCiudad,
                    java.util.stream.Collectors.averagingDouble(RegistroTemperatura::getTemperatura)
                ));
            
            Map<String, Double> maximos = registrosFiltrados.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    RegistroTemperatura::getCiudad,
                    java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.maxBy(
                            java.util.Comparator.comparingDouble(RegistroTemperatura::getTemperatura)
                        ),
                        opt -> opt.map(RegistroTemperatura::getTemperatura).orElse(0.0)
                    )
                ));
            
            Map<String, Double> minimos = registrosFiltrados.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    RegistroTemperatura::getCiudad,
                    java.util.stream.Collectors.collectingAndThen(
                        java.util.stream.Collectors.minBy(
                            java.util.Comparator.comparingDouble(RegistroTemperatura::getTemperatura)
                        ),
                        opt -> opt.map(RegistroTemperatura::getTemperatura).orElse(0.0)
                    )
                ));
            
            Map<String, Long> conteos = registrosFiltrados.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    RegistroTemperatura::getCiudad,
                    java.util.stream.Collectors.counting()));
            
            modeloTabla.setRowCount(0);
            
            graficaBarras.actualizarDatos(registrosFiltrados);
            
            promedios.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    String ciudad = entry.getKey();
                    double promedio = entry.getValue();
                    double maximo = maximos.getOrDefault(ciudad, 0.0);
                    double minimo = minimos.getOrDefault(ciudad, 0.0);
                    long cantidad = conteos.getOrDefault(ciudad, 0L);
                    modeloTabla.addRow(new Object[]{
                        ciudad,
                        String.format("%.2f", promedio),
                        String.format("%.2f", maximo),
                        String.format("%.2f", minimo),
                        cantidad
                    });
                });
            
            if (promedios.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No se encontraron datos en el rango de fechas seleccionado",
                    "Sin Datos",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            String mensaje = "Error al calcular promedios: " + e.getMessage();
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(mensaje);
        }
    }
    
    private void consultarTemperaturasExtremas() {
        try {
            if (registros == null || registros.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay datos cargados.\n\nPor favor, cargue un archivo CSV primero.",
                    "Sin Datos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            LocalDate fechaInicio = obtenerFechaDePanel(spnFechaInicio);
            LocalDate fechaFin = obtenerFechaDePanel(spnFechaFin);
            
            List<RegistroTemperatura> registrosFiltrados = servicioTemperatura.filtrarPorRangoFechas(
                registros, fechaInicio, fechaFin);
            
            if (registrosFiltrados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay datos en el rango de fechas seleccionado.",
                    "Sin Datos",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            java.util.Optional<RegistroTemperatura> masCalurosa = registrosFiltrados.stream()
                .max(java.util.Comparator.comparingDouble(RegistroTemperatura::getTemperatura));
            
            java.util.Optional<RegistroTemperatura> menosCalurosa = registrosFiltrados.stream()
                .min(java.util.Comparator.comparingDouble(RegistroTemperatura::getTemperatura));
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("CONSULTA DE TEMPERATURAS EXTREMAS\n");
            mensaje.append("Rango: ").append(fechaInicio.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            mensaje.append(" - ").append(fechaFin.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            mensaje.append("\n\n");
            
            if (masCalurosa.isPresent()) {
                RegistroTemperatura reg = masCalurosa.get();
                mensaje.append("🌡️ CIUDAD MÁS CALUROSA:\n");
                mensaje.append(String.format("   %s - %.1f°C\n", reg.getCiudad(), reg.getTemperatura()));
                mensaje.append(String.format("   Fecha: %s\n", reg.getFechaFormateada()));
            }
            
            mensaje.append("\n");
            
            if (menosCalurosa.isPresent()) {
                RegistroTemperatura reg = menosCalurosa.get();
                mensaje.append("❄️ CIUDAD MENOS CALUROSA:\n");
                mensaje.append(String.format("   %s - %.1f°C\n", reg.getCiudad(), reg.getTemperatura()));
                mensaje.append(String.format("   Fecha: %s\n", reg.getFechaFormateada()));
            }
            
            if (masCalurosa.isPresent() && menosCalurosa.isPresent()) {
                double diferencia = masCalurosa.get().getTemperatura() - menosCalurosa.get().getTemperatura();
                mensaje.append(String.format("\n📊 Diferencia: %.1f°C", diferencia));
            }
            
            JOptionPane.showMessageDialog(this,
                mensaje.toString(),
                "Resultados de Consulta",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al consultar temperaturas: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private class GraficaBarras extends JPanel {
        private Map<String, Double> promediosPorCiudad;
        private String titulo;
        
        public GraficaBarras() {
            this.promediosPorCiudad = new java.util.LinkedHashMap<>();
            this.titulo = "Gráfica de Barras - Promedio de Temperatura";
            setBackground(new Color(235, 235, 235));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }
        
        public void actualizarDatos(List<RegistroTemperatura> datos) {
            this.promediosPorCiudad = datos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    RegistroTemperatura::getCiudad,
                    java.util.LinkedHashMap::new,
                    java.util.stream.Collectors.averagingDouble(RegistroTemperatura::getTemperatura)
                ));
            this.titulo = "Gráfica de Barras - Promedio de Temperatura por Ciudad";
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (promediosPorCiudad.isEmpty()) {
                g.setFont(new Font("Arial", Font.BOLD, 16));
                g.setColor(Color.DARK_GRAY);
                String mensaje = "Seleccione un rango de fechas y presione Calcular";
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
                int y = getHeight() / 2;
                g.drawString(mensaje, x, y);
                return;
            }
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            int marginLeft = 80;
            int marginRight = 40;
            int marginTop = 60;
            int marginBottom = 100;
            int graphWidth = width - marginLeft - marginRight;
            int graphHeight = height - marginTop - marginBottom;
            
            g2d.setColor(new Color(245, 245, 245));
            g2d.fillRect(marginLeft, marginTop, graphWidth, graphHeight);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.setColor(Color.BLACK);
            FontMetrics fmTitulo = g2d.getFontMetrics();
            int tituloX = (width - fmTitulo.stringWidth(titulo)) / 2;
            g2d.drawString(titulo, tituloX, 30);
            
            double minTemp = promediosPorCiudad.values().stream()
                .mapToDouble(Double::doubleValue)
                .min().orElse(0.0);
            double maxTemp = promediosPorCiudad.values().stream()
                .mapToDouble(Double::doubleValue)
                .max().orElse(30.0);
            
            double rango = maxTemp - minTemp;
            if (rango < 1) rango = 10;
            minTemp = Math.floor((minTemp - rango * 0.1) / 5) * 5;
            maxTemp = Math.ceil((maxTemp + rango * 0.1) / 5) * 5;
            
            g2d.setColor(new Color(210, 210, 210));
            g2d.setStroke(new BasicStroke(1));
            int numLineas = 6;
            for (int i = 0; i <= numLineas; i++) {
                int y = marginTop + graphHeight - (i * graphHeight / numLineas);
                g2d.drawLine(marginLeft, y, marginLeft + graphWidth, y);
            }
            
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(marginLeft, marginTop, marginLeft, marginTop + graphHeight);
            g2d.drawLine(marginLeft, marginTop + graphHeight, marginLeft + graphWidth, marginTop + graphHeight);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            for (int i = 0; i <= numLineas; i++) {
                int y = marginTop + graphHeight - (i * graphHeight / numLineas);
                double temp = minTemp + (i * (maxTemp - minTemp) / numLineas);
                String label = String.format("%.0f°C", temp);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(label, marginLeft - fm.stringWidth(label) - 10, y + 5);
            }
            
            int numCiudades = promediosPorCiudad.size();
            int barWidth = Math.min(100, (graphWidth - (numCiudades + 1) * 30) / numCiudades);
            int espacioEntre = (graphWidth - numCiudades * barWidth) / (numCiudades + 1);
            int xPos = marginLeft + espacioEntre;
            
            Color[] colores = {
                new Color(52, 152, 219),
                new Color(46, 204, 113),
                new Color(231, 76, 60),
                new Color(241, 196, 15),
                new Color(155, 89, 182)
            };
            
            int colorIndex = 0;
            for (Map.Entry<String, Double> entry : promediosPorCiudad.entrySet()) {
                String ciudad = entry.getKey();
                double temperatura = entry.getValue();
                
                int barHeight = (int)((temperatura - minTemp) / (maxTemp - minTemp) * graphHeight);
                int barY = marginTop + graphHeight - barHeight;
                
                Color color = colores[colorIndex % colores.length];
                GradientPaint gradient = new GradientPaint(
                    xPos, barY, color.brighter(),
                    xPos, marginTop + graphHeight, color
                );
                g2d.setPaint(gradient);
                g2d.fillRect(xPos, barY, barWidth, barHeight);
                
                g2d.setColor(color.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(xPos, barY, barWidth, barHeight);
                
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                String valorStr = String.format("%.1f°C", temperatura);
                FontMetrics fm = g2d.getFontMetrics();
                int valorX = xPos + (barWidth - fm.stringWidth(valorStr)) / 2;
                g2d.drawString(valorStr, valorX, barY - 10);
                
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                fm = g2d.getFontMetrics();
                int ciudadX = xPos + (barWidth - fm.stringWidth(ciudad)) / 2;
                g2d.drawString(ciudad, ciudadX, marginTop + graphHeight + 25);
                
                xPos += barWidth + espacioEntre;
                colorIndex++;
            }
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
            g2d.translate(20, height / 2);
            g2d.rotate(-Math.PI / 2);
            String labelY = "Temperatura (°C)";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(labelY, -fm.stringWidth(labelY) / 2, 0);
            g2d.setTransform(oldTransform);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            String labelX = "Ciudad";
            fm = g2d.getFontMetrics();
            g2d.drawString(labelX, marginLeft + graphWidth / 2 - fm.stringWidth(labelX) / 2, height - 15);
        }
    }
    
    private class CalendarioDialog extends JDialog {
        private Calendar fechaSeleccionada;
        private Calendar fechaMostrada;
        private JLabel lblMesAño;
        private JPanel panelDias;
        
        public CalendarioDialog(JFrame parent, Calendar fechaInicial) {
            super(parent, "Seleccionar Fecha", true);
            this.fechaMostrada = (Calendar) fechaInicial.clone();
            this.fechaSeleccionada = null;
            
            inicializarComponentes();
            setSize(320, 300);
            setLocationRelativeTo(parent);
            setResizable(false);
        }
        
        private void inicializarComponentes() {
            setLayout(new BorderLayout(5, 5));
            
            JPanel panelNav = new JPanel(new BorderLayout());
            panelNav.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            JButton btnAnterior = new JButton("◀");
            btnAnterior.addActionListener(e -> cambiarMes(-1));
            
            JButton btnSiguiente = new JButton("▶");
            btnSiguiente.addActionListener(e -> cambiarMes(1));
            
            lblMesAño = new JLabel("", JLabel.CENTER);
            lblMesAño.setFont(new Font("Arial", Font.BOLD, 14));
            
            panelNav.add(btnAnterior, BorderLayout.WEST);
            panelNav.add(lblMesAño, BorderLayout.CENTER);
            panelNav.add(btnSiguiente, BorderLayout.EAST);
            
            add(panelNav, BorderLayout.NORTH);
            
            panelDias = new JPanel(new GridLayout(7, 7, 2, 2));
            panelDias.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            add(panelDias, BorderLayout.CENTER);
            
            JButton btnHoy = new JButton("Hoy");
            btnHoy.addActionListener(e -> {
                fechaMostrada = Calendar.getInstance();
                actualizarCalendario();
            });
            
            JPanel panelInferior = new JPanel(new FlowLayout());
            panelInferior.add(btnHoy);
            add(panelInferior, BorderLayout.SOUTH);
            
            actualizarCalendario();
        }
        
        private void cambiarMes(int delta) {
            fechaMostrada.add(Calendar.MONTH, delta);
            actualizarCalendario();
        }
        
        private void actualizarCalendario() {
            panelDias.removeAll();
            
            String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                             "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            lblMesAño.setText(meses[fechaMostrada.get(Calendar.MONTH)] + " " + fechaMostrada.get(Calendar.YEAR));
            
            String[] diasSemana = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
            for (String dia : diasSemana) {
                JLabel lbl = new JLabel(dia, JLabel.CENTER);
                lbl.setFont(new Font("Arial", Font.BOLD, 11));
                lbl.setForeground(new Color(70, 130, 180));
                panelDias.add(lbl);
            }
            
            Calendar cal = (Calendar) fechaMostrada.clone();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int primerDiaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1;
            int diasEnMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            for (int i = 0; i < primerDiaSemana; i++) {
                panelDias.add(new JLabel(""));
            }
            
            Calendar hoy = Calendar.getInstance();
            for (int dia = 1; dia <= diasEnMes; dia++) {
                final int diaFinal = dia;
                JButton btnDia = new JButton(String.valueOf(dia));
                btnDia.setFont(new Font("Arial", Font.PLAIN, 11));
                btnDia.setMargin(new Insets(2, 2, 2, 2));
                btnDia.setPreferredSize(new Dimension(35, 30));
                
                if (fechaMostrada.get(Calendar.YEAR) == hoy.get(Calendar.YEAR) &&
                    fechaMostrada.get(Calendar.MONTH) == hoy.get(Calendar.MONTH) &&
                    dia == hoy.get(Calendar.DAY_OF_MONTH)) {
                    btnDia.setBackground(new Color(100, 149, 237));
                    btnDia.setForeground(Color.WHITE);
                    btnDia.setOpaque(true);
                }
                
                btnDia.addActionListener(e -> seleccionarDia(diaFinal));
                panelDias.add(btnDia);
            }
            
            panelDias.revalidate();
            panelDias.repaint();
        }
        
        private void seleccionarDia(int dia) {
            fechaSeleccionada = (Calendar) fechaMostrada.clone();
            fechaSeleccionada.set(Calendar.DAY_OF_MONTH, dia);
            dispose();
        }
        
        public Calendar getFechaSeleccionada() {
            return fechaSeleccionada;
        }
    }
}
