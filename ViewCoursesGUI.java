import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewCoursesGUI extends BaseFrame {

    JTable table;
    DefaultTableModel model;

    public ViewCoursesGUI() {

        super("VIEW COURSES");

        String[] columns = {
                "ID",
                "Course Name",
                "Department",
                "Duration",
                "Credits",
                "Fee",
                "Capacity",
                "Reg Start",
                "Reg End"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 120, 1100, 420);

        card.setLayout(null);
        card.add(scrollPane);

        JButton updateBtn = createButton("Update");
        JButton deleteBtn = createButton("Delete");
        JButton backBtn = createButton("Back");

        updateBtn.setBounds(350, 560, 180, 40);
        deleteBtn.setBounds(550, 560, 180, 40);
        backBtn.setBounds(750, 560, 180, 40);

        card.add(updateBtn);
        card.add(deleteBtn);
        card.add(backBtn);

        loadCourses(); // loads data into table

        updateBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course first");
                return;
            }

            int courseId = (int) model.getValueAt(row, 0);

            dispose();
            new UpdateCourseGUI(courseId);

        });

        deleteBtn.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course first");
                return;
            }

            int courseId = (int) model.getValueAt(row, 0);

            try {

                Connection conn = DBConnection.getConnection();

                String sql = "DELETE FROM courses WHERE course_id=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, courseId);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Course Deleted Successfully");

                conn.close();

                model.setRowCount(0);
                loadCourses(); // refresh table

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }

        });

        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        setVisible(true);
    }

    private void loadCourses() {

        try {

            Connection conn = DBConnection.getConnection();

            String sql = "SELECT * FROM courses";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            model.setRowCount(0); // clear table before loading

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("department"),
                        rs.getString("duration"),
                        rs.getInt("credits"),
                        rs.getDouble("fee"),
                        rs.getInt("max_capacity"),
                        rs.getDate("registration_start"),
                        rs.getDate("registration_end")
                });

            }

            conn.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

    }
}