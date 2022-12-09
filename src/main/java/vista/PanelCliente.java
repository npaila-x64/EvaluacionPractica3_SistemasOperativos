package vista;

import controlador.ControladorCliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelCliente extends JPanel implements ActionListener {

    private JLabel titulo;
    private JButton salir;
    private JButton bconectar;
    private JTextField thostname;
    private JTable tArchivos;
    private ControladorCliente controlador;

    public PanelCliente(ControladorCliente controlador) {
        this.controlador = controlador;
        crearComponentes();
        setVisible(true);
    }

    private void crearComponentes() {
        configurarPanel();
        crearTitulo();
        crearBotonConectar();
        crearCampoDeTextoHostname();
        crearBotonSalir();
        crearTablaDeArchivos();
    }

    private void crearCampoDeTextoHostname  () {
        thostname = new JTextField();
        thostname.setFont(new Font("Arial", Font.PLAIN, 15));
        thostname.setLocation(204, 394);
        thostname.setSize(138, 29);
        thostname.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        thostname.setBorder(BorderFactory.createCompoundBorder(
                thostname.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 5)));
        this.add(thostname);
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
        bconectar = new JButton("Conectar");
        bconectar.addActionListener(this);
        bconectar.setFont(new Font("Arial", Font.PLAIN, 15));
        bconectar.setLocation(349, 393);
        bconectar.setSize(116, 31);
        bconectar.setFocusable(false);

        bconectar.setBackground(Color.BLACK);
        bconectar.setForeground(Color.WHITE);

        this.add(bconectar);
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

    public String getHostname() {
        return thostname.getText();
    }

    public void setHostname(String hostname) {
        thostname.setText(hostname);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salir) {
            this.controlador.salidaFueSolicitada();
        }
        if (e.getSource() == bconectar) {
            this.controlador.conectarFueSolicitada();
        }
    }

    public void habilitarConectar(boolean b) {
        thostname.setEditable(b);
        bconectar.setEnabled(b);
    }
}
