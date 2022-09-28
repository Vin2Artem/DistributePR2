package rpc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainForm {
    private JPanel panMain;
    private JTable tblMain;
    private JButton btnGet;
    private JPanel panBtns;
    private JFrame frame;
    DefaultTableModel tableModel;

    MainForm() {
        initialize();
    }

    public enum Status {
        READY("Доставлен"),
        EXPIRED("Время хранения истекло"),
        DRIVE("В пути");

        private String title;

        Status(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private void initialize() {
        this.frame = new JFrame();
        this.frame.setContentPane(this.panMain);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(800, 500);
        this.frame.setVisible(true);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Код заказа");
        tableModel.addColumn("Товар");
        tableModel.addColumn("Статус");

        Font font = new Font("Times New Roman", Font.PLAIN, 20);
        tblMain.setFont(font);
        tblMain.getTableHeader().setFont(font);
        tblMain.setRowHeight(30);
        tblMain.setModel(tableModel);

        btnGet.setFont(font);

        btnGet.addActionListener(e -> {
            int[] rows = tblMain.getSelectedRows();
            if (rows.length == 0) {
                JOptionPane.showMessageDialog(null,
                        "Ничего не выбрано",
                        "Внимание",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                boolean isOk = true;
                for (int row : rows) {
                    if (!tblMain.getModel().getValueAt(row, 2).equals(Status.valueOf("READY").getTitle())) {
                        JOptionPane.showMessageDialog(null,
                                "Не все выбранные заказы можно принести\n" +
                                        "Проверьте, все ли выбранные заказы доставлены",
                                "Внимание",
                                JOptionPane.WARNING_MESSAGE);
                        isOk = false;
                        break;
                    }
                }
                if (isOk) {
                    JOptionPane.showMessageDialog(null,
                            "Поручение принято",
                            "Внимание",
                            JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
    }

    public void setRows(Object[] rows) {
        for (int i = 0; i < rows.length; i++)
            tableModel.addRow((Object[])rows[i]);
    }

}
