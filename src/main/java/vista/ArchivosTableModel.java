package vista;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArchivosTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Archivo", "Botón", "Botón"};
    private final Class[] columnClass = new Class[] {String.class, String.class, String.class};
    private List<File> archivos;

    public ArchivosTableModel() {
        this.archivos = new ArrayList<>();
    }

    public void setArchivos(List<File> archivos) {
        this.archivos = archivos;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Optional.of(archivos).orElse(new ArrayList<>()).size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return switch (col) {
            case 0 -> archivos.get(row).getName();
            case 1 -> "Duplicar";
            case 2 -> "Eliminar";
            default -> throw new IllegalStateException();
        };
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
