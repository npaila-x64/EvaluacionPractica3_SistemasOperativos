package vista;

import controlador.ControladorCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelMenu extends JPanel implements ActionListener {

    private JLabel titulo;
    private JButton salir;
    private JButton conectar;
    private JTable tArchivos;
    private ControladorCliente controlador;

    public PanelMenu(ControladorCliente controlador) {
        this.controlador = controlador;
        crearComponentes();
        setVisible(true);
    }

    private void crearComponentes() {
        configurarPanel();
        crearTitulo();
        crearBotonConectar();
        crearBotonSalir();
        crearTablaDeArchivos();
    }

    private void configurarPanel() {
        this.setBackground(Color.WHITE);
        this.setLayout(null);
    }

    private void crearTablaDeArchivos() {
        tArchivos = new JTable(controlador.getModeloDeTabla());
        tArchivos.setBounds(30, 40, 200, 300);
        tArchivos.setRowHeight(30);
        tArchivos.setTableHeader(null);
        tArchivos.setCellSelectionEnabled(false);
        tArchivos.getColumnModel().getColumn(0).setPreferredWidth(230);
        tArchivos.getColumnModel().getColumn(1).setPreferredWidth(85);
        tArchivos.getColumnModel().getColumn(2).setPreferredWidth(85);
        tArchivos.setFont(new Font("Arial", Font.PLAIN, 14));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                setBorder(BorderFactory.createCompoundBorder(getBorder(),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                return this;
            }
        };
        tArchivos.setDefaultRenderer(String.class, renderer);

        JScrollPane scrollPane = new JScrollPane(tArchivos);
        scrollPane.setLocation(30, 85);
        scrollPane.setSize(484, 272);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(scrollPane);

        Action duplicarArchivo = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int fila = Integer.parseInt(e.getActionCommand());
                controlador.duplicarArchivoFueSolicitado(fila);
            }
        };

        Action eliminarArchivo = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int fila = Integer.parseInt(e.getActionCommand());
                controlador.eliminarArchivoFueSolicitado(fila);
            }
        };
        new ButtonColumn(tArchivos, duplicarArchivo, 1);
        new ButtonColumn(tArchivos, eliminarArchivo, 2);
    }

    private void crearTitulo() {
        titulo = new JLabel("Cliente");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setLocation(210, 24);
        titulo.setSize(290, 41);
        this.add(titulo);
    }

    private void crearBotonConectar() {
        conectar = new JButton("Conectar");
        conectar.addActionListener(this);
        conectar.setFont(new Font("Arial", Font.PLAIN, 15));
        conectar.setLocation(215, 393);
        conectar.setSize(116, 31);
        conectar.setFocusable(false);

        conectar.setBackground(Color.BLACK);
        conectar.setForeground(Color.WHITE);

        this.add(conectar);
    }

    private void crearBotonSalir() {
        salir = new JButton("Salir");
        salir.addActionListener(this);
        salir.setFont(new Font("Arial", Font.PLAIN, 15));
        salir.setLocation(30, 393);
        salir.setSize(82, 31);
        salir.setFocusable(false);

        salir.setBackground(Color.BLACK);
        salir.setForeground(Color.WHITE);

        this.add(salir);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salir) {
            this.controlador.salidaFueSolicitada();
        }
        if (e.getSource() == conectar) {
            this.controlador.conectarFueSolicitada();
        }
    }
}
