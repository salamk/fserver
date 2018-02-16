/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.tickdownload;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author sania
 */
public class PnlUDQ extends javax.swing.JPanel {

    private JTable table;

    private DailyDataList ddl;

    /**
     * Creates new form PnlDailyDataView
     */
    public PnlUDQ() {
        initComponents();
        table = new JTable();
        JScrollPane scroller = new JScrollPane(table);
        pnl.add(scroller);

    }
    
    public JTable getTable(){
        return table;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        pnl.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 204, 255), 3, true));
        pnl.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);

        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setText("Cap >");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setText("Vol >");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
            .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ddl = new DailyDataList();
        table.setModel(new DailyDataTableModel(ddl));
        table.setAutoCreateRowSorter(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String answer = JOptionPane.showInputDialog("Show only symbols having \nMarket Cap Greater Then: ");
        double cap = Double.parseDouble(answer);
        showCap(cap);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String answer = JOptionPane.showInputDialog("Show only symbols having \nVolume Greater Then: ");
        double v = Double.parseDouble(answer);
        showVol(v);
    }//GEN-LAST:event_jButton3ActionPerformed

    
    private void showVol(double v) {
        ArrayList<DailyData> qlist = new ArrayList<DailyData>();
        Iterator i = ddl.iterator();
        while (i.hasNext()) {
            DailyData dd = (DailyData) i.next();
            double volume = (dd.getVolume());
            if (volume > v) {
                qlist.add(dd);
            }
        }

        TableModel tableModel = new DailyDataTableModel(qlist);
        this.table.setModel(tableModel);
    }
    
     private void showCap(double cap) {
        ArrayList<DailyData> qlist = new ArrayList<DailyData>();
        Iterator i = ddl.iterator();
        while (i.hasNext()) {
            DailyData dd = (DailyData) i.next();
            double c= (dd.getMcap());
            if (c > cap) {
                qlist.add(dd);
            }
        }

        TableModel tableModel = new DailyDataTableModel(qlist);
        this.table.setModel(tableModel);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel pnl;
    // End of variables declaration//GEN-END:variables
}

class DailyDataList extends ArrayList<DailyData> {


    public DailyDataList(ArrayList<DailyData> ddl) {
        Iterator i = ddl.iterator();
        while (i.hasNext()) {
            DailyData dd = (DailyData) i.next();
            add(dd);
        }
    }

    public DailyDataList() {

        HtmlOutput ho = new HtmlOutput();
        ArrayList al = ho.getParsedLines();
        Iterator i = al.iterator();
        
        while (i.hasNext()) {
            String line = (String) i.next();
            String[] toke = line.split(",");
            DailyData dd = new DailyData();
            dd.setQuoteDate("");
            dd.setSymbol(toke[0]);
            dd.setOpen(Double.parseDouble(toke[1]));
            dd.setHigh(Double.parseDouble((toke[2])));
            dd.setLow(Double.parseDouble(toke[3]));
            dd.setClose(Double.parseDouble(toke[4]));
            dd.setVolume(Double.parseDouble(toke[5]));
            dd.setChange(Double.parseDouble(toke[6]));
            
            double close = dd.getClose();
            double change = dd.getChange();
            double pchange = (change/close)*100;
            dd.setPchange(pchange);
            double volume = dd.getVolume();
            double mcap = volume*close;
            dd.setMcap(mcap);
            double high = dd.getHigh();
            double low = dd.getLow();
            double hldiff = high - low;
            dd.setHlDiff(hldiff);

            add(dd);
            
            System.out.println("Added to list");
        }
    }
}



class DailyData {

    private String quoteDate, symbol;
    private int index;
    private double open, high, low, close, volume, change, pchange, mcap, hlDiff;

    public DailyData() {

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

   

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPchange() {
        return pchange;
    }

    public void setPchange(double pchange) {
        this.pchange = pchange;
    }

    public double getMcap() {
        return mcap;
    }

    public void setMcap(double mcap) {
        this.mcap = mcap;
    }

    public double getHlDiff() {
        return hlDiff;
    }

    public void setHlDiff(double hlDiff) {
        this.hlDiff = hlDiff;
    }

   

}


class DailyDataTableModel extends AbstractTableModel {

    private static final int COL_INDEX = 0;
    private static final int COL_SYMBOL = 1;
    private static final int COL_OPEN = 2;
    private static final int COL_HIGH = 3;
    private static final int COL_LOW = 4;
    private static final int COL_CLOSE = 5;
    private static final int COL_VOLUME = 6;
    private static final int COL_CHANGE = 7;
    private static final int COL_PCHANGE = 8;
    private static final int COL_MCAP = 9;
    

    private String[] columnNames = {"No", "Symbol", "Open", "High",
        "Low", "Close", "Volume", "Change", "% Change", "Cap"};

    private ArrayList<DailyData> dailyDataList;

    public DailyDataTableModel(ArrayList<DailyData> dailyDataList) {
        this.dailyDataList = dailyDataList;

        int indexCount = 1;
        for (DailyData dailyData : dailyDataList) {
            dailyData.setIndex(indexCount++);
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return dailyDataList.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (dailyDataList.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DailyData dailyData = dailyDataList.get(rowIndex);
        Object returnValue = null;

        switch (columnIndex) {
            case COL_INDEX:
                returnValue = dailyData.getIndex();
                break;
            case COL_SYMBOL:
                returnValue = dailyData.getSymbol();
                break;
            case COL_OPEN:
                returnValue = dailyData.getOpen();
                break;
            case COL_HIGH:
                returnValue = dailyData.getHigh();
                break;
            case COL_LOW:
                returnValue = dailyData.getLow();
                break;
            case COL_CLOSE:
                returnValue = dailyData.getClose();
                break;
            case COL_VOLUME:
                returnValue = dailyData.getVolume();
                break;
            case COL_MCAP:
                returnValue = dailyData.getMcap();
                break;
            case COL_CHANGE:
                returnValue = dailyData.getChange();
                break;
            case COL_PCHANGE:
                returnValue = dailyData.getPchange();
                break;
            default:
                throw new IllegalArgumentException("Invalid column index");
        }

        return returnValue;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        DailyData dailyData = dailyDataList.get(rowIndex);
        if (columnIndex == COL_INDEX) {
            dailyData.setIndex((int) value);
        }
    }

}
