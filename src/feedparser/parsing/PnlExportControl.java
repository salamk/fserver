/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feedparser.parsing;

/**
 *
 * @author salam
 */
public class PnlExportControl extends javax.swing.JPanel {

    /**
     * Creates new form PnlExportControl
     */
    public PnlExportControl() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        t1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        t2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        t3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        t4 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        t5 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        t6 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        t7 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        t8 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        t9 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        t10 = new javax.swing.JTextField();

        setLayout(new java.awt.GridLayout(10, 2));

        jButton2.setText("Run-Script-01");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);

        t1.setEditable(false);
        t1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t1.setEnabled(false);
        add(t1);

        jButton1.setText("Run-Script-02");
        add(jButton1);

        t2.setEditable(false);
        t2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t2.setEnabled(false);
        add(t2);

        jButton3.setText("Daily Atoms");
        add(jButton3);

        t3.setEditable(false);
        t3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t3.setEnabled(false);
        add(t3);

        jButton4.setText("PV Atoms");
        add(jButton4);

        t4.setEditable(false);
        t4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t4.setEnabled(false);
        add(t4);

        jButton5.setText("P-Type Atoms");
        add(jButton5);

        t5.setEditable(false);
        t5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t5.setEnabled(false);
        add(t5);

        jButton6.setText("S-Type Atoms");
        add(jButton6);

        t6.setEditable(false);
        t6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t6.setEnabled(false);
        add(t6);

        jButton7.setText("Make DDF");
        add(jButton7);

        t7.setEditable(false);
        t7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t7.setEnabled(false);
        add(t7);

        jButton8.setText("Make Cat-File");
        add(jButton8);

        t8.setEditable(false);
        t8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t8.setEnabled(false);
        add(t8);

        jButton9.setText("Export @ Server");
        add(jButton9);

        t9.setEditable(false);
        t9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t9.setEnabled(false);
        add(t9);

        jButton10.setText("Invoke Timer");
        add(jButton10);

        t10.setEditable(false);
        t10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t10.setEnabled(false);
        add(t10);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new UpdateDailyQuote(t1));
        t.start();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JTextField t1;
    private javax.swing.JTextField t10;
    private javax.swing.JTextField t2;
    private javax.swing.JTextField t3;
    private javax.swing.JTextField t4;
    private javax.swing.JTextField t5;
    private javax.swing.JTextField t6;
    private javax.swing.JTextField t7;
    private javax.swing.JTextField t8;
    private javax.swing.JTextField t9;
    // End of variables declaration//GEN-END:variables
}
