/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * clientGuiRunner.java
 *
 * Created on Jul 14, 2010, 4:28:22 PM
 */

package gui;

/**
 *
 * @author Todd Bodnar
 */
public class clientGuiRunner extends javax.swing.JPanel {

    /** Creates new form clientGuiRunner */
    public clientGuiRunner() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        continueBox = new javax.swing.JCheckBox();
        completedLabel = new javax.swing.JLabel();

        jLabel1.setText("Cluster Logo Client");

        jLabel2.setText(tools.version.CLIENT_VERSION);

        continueBox.setText("Quit after current assignment");
        continueBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueBoxActionPerformed(evt);
            }
        });

        completedLabel.setText("0 Assignments Completed");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(continueBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addComponent(completedLabel))
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(continueBox)
                    .addComponent(completedLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void continueBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_continueBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_continueBoxActionPerformed

    public boolean noMoreBoxChecked()
    {
        return continueBox.isSelected();
    }

    public void increaseDoneCount()
    {
        doneCount++;
        completedLabel.setText(doneCount+" Assignments Completed");
    }

    private int doneCount=0;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel completedLabel;
    private javax.swing.JCheckBox continueBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

}
