/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.DataInterface;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Duc Dung Dan
 */
public class StatisticalController {
    public static void statistics(JTable table, String[] header, String[] headerDatabase, DataInterface dataInterface) {
        ResultSet rs = dataInterface.getStatisticalData(headerDatabase[0]);
        JTableHeader sta = table.getTableHeader();
        TableColumnModel stacolumns = sta.getColumnModel();
        for (int i = 0; i < stacolumns.getColumnCount(); ++i) {
            stacolumns.getColumn(i).setHeaderValue(header[i]);
        }
        sta.repaint();
        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int rowCount = model.getRowCount();
            //Remove rows one by one from the end of the table
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            int i = 1;
            while (rs.next()) {
                model.addRow(new Object[]{i, rs.getString(headerDatabase[0]), rs.getInt(headerDatabase[1])});
                ++i;
            }
        } catch (SQLException ex) {
        }
    }
}
