package vista;

import javax.swing.*;
import java.awt.*;

public class Marco extends JFrame {

    private Container panel;
    private final CardLayout cl;

    public Marco() {
        cl = new CardLayout();
        configurarMarco();
        configurarPanel();
    }

    private void configurarPanel() {
        panel = getContentPane();
        panel.setLayout(cl);
    }

    private void configurarMarco() {
        setTitle("Interfaz De Cliente");
        setBounds(0, 0, 565, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void mostrarPanel() {
        cl.show(panel, "menu");
    }

    public void agregarPanel(PanelCliente menu) {
        panel.add("menu", menu);
    }
}
