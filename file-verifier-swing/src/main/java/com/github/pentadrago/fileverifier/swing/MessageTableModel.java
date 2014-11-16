package com.github.pentadrago.fileverifier.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * provides a table model that can be appended, prepended or overwritten by each
 * new message
 *
 * @author Stefan Kloe
 */
public class MessageTableModel extends AbstractTableModel {

    /**
     * controls the behavior on inserting new messages
     */
    public enum Mode {

        APPEND, OVERWRITE, PREPEND;
    }

    private final List<String> messages = new ArrayList<>();

    private final Mode mode;

    public MessageTableModel(Mode mode) {
        this.mode = mode;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return messages.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return messages.get(row);
    }

    /**
     * adds the given message according to mode
     *
     * @param message new message
     */
    public void addMessage(String message) {
        switch (mode) {
            case APPEND:
                messages.add(message);
                break;
            case PREPEND:
                messages.add(0, message);
                break;
            case OVERWRITE:
                messages.clear();
                messages.add(message);
                break;
        }
        fireTableDataChanged();
    }

    /**
     * removes all messages from table
     */
    public void clear() {
        messages.clear();
        fireTableDataChanged();
    }

}
