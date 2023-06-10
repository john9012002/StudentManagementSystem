import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class StudentManagementSystem extends JFrame {
    private JLabel lblID, lblName, lblBirthDate;
    private JTextField txtID, txtName, txtBirthDate;
    private JTextArea txtOutput;
    private JButton btnAdd, btnEdit, btnDelete, btnSearch, btnPrint, btnSortByName, btnSortByAge;
    private ArrayList<Student> studentList;

    private final String FILE_NAME = "students.txt";

    public StudentManagementSystem() {
        // Set title and size of the window
        super("Student Management System");
        setSize(600, 400);

        // Set layout of the content pane
        setLayout(new BorderLayout());

        // Create student list
        studentList = new ArrayList<Student>();

        // Add components to the window
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.WEST);
        add(createOutputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Add window listener to save data when closing the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveDataToFile();
                System.exit(0);
            }
        });

        // Load data from file
        loadDataFromFile();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("STUDENT MANAGEMENT SYSTEM"));
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        lblID = new JLabel("ID:");
        lblName = new JLabel("Name:");
        lblBirthDate = new JLabel("Birth Date:");
        txtID = new JTextField();
        txtName = new JTextField();
        txtBirthDate = new JTextField();
        panel.add(lblID);
        panel.add(txtID);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblBirthDate);
        panel.add(txtBirthDate);
        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        txtOutput = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtOutput);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });
        btnPrint = new JButton("Print");
        btnPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printStudentList();
            }
        });
        btnSortByName = new JButton("Sort by Name");
        btnSortByName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortByName();
            }
        });
        btnSortByAge = new JButton("Sort by Age");
        btnSortByAge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sortByAge();
            }
        });
        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnSearch);
        panel.add(btnPrint);
        panel.add(btnSortByName);
        panel.add(btnSortByAge);
        return panel;
    }

    private void addStudent() {
        String sid = txtID.getText().trim();
        String name = txtName.getText().trim();
        String birthDate = txtBirthDate.getText().trim();
        if (sid.length() == 0 || name.length() == 0 || birthDate.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!");
            return;
        }
        Student student = new Student(sid, name, birthDate);
        studentList.add(student);
        txtOutput.append("Added student: " + student.toString() + "\n");
        clearInputFields();
    }

    private void editStudent() {
        String sid = txtID.getText().trim();
        String name = txtName.getText().trim();
        String birthDate = txtBirthDate.getText().trim();
        if (sid.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter student ID!");
            return;
        }
        Student student = findStudentById(sid);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }
        if (name.length() > 0) {
            student.setName(name);
        }
        if (birthDate.length() > 0) {
            student.setBirthDate(birthDate);
        }
        txtOutput.append("Updated student: " + student.toString() + "\n");
        clearInputFields();
    }

    private void deleteStudent() {
        String sid = txtID.getText().trim();
        if (sid.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter student ID!");
            return;
        }
        Student student = findStudentById(sid);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }
        studentList.remove(student);
        txtOutput.append("Deleted student: " + student.toString() + "\n");
        clearInputFields();
    }

    private void searchStudent() {
        String sid = txtID.getText().trim();
        if (sid.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please enter student ID!");
            return;
        }
        Student student = findStudentById(sid);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }
        txtOutput.append("Found student: " + student.toString() + "\n");
        clearInputFields();
    }

    private void printStudentList() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student to print!");
            return;
        }
        txtOutput.setText("");
        for (Student student : studentList) {
            txtOutput.append(student.toString() + "\n");
        }
    }

    private void sortByName() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student to sort!");
            return;
        }
        Collections.sort(studentList, new Comparator<Student>() {
            public int compare(Student s1, Student s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });
        txtOutput.setText("Sorted by name (A-Z):\n");
        printStudentList();
    }

    private void sortByAge() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student to sort!");
            return;
        }
        Collections.sort(studentList, new Comparator<Student>() {
            public int compare(Student s1, Student s2) {
                int cmp = s2.getAge() - s1.getAge();
                if (cmp == 0) {
                    cmp = s1.getName().compareToIgnoreCase(s2.getName());
                }
                return cmp;
            }
        });
        txtOutput.setText("Sorted by age (older first):\n");
        printStudentList();
    }

    private Student findStudentById(String sid) {
        for (Student student : studentList) {
            if (student.getId().equals(sid)) {
                return student;
            }
        }
        return null;
    }

    private void clearInputFields() {
        txtID.setText("");
        txtName.setText("");
        txtBirthDate.setText("");
        txtID.requestFocus();
    }

    private void saveDataToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            for (Student student : studentList) {
                out.println(student.toString());
            }
            out.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving data to file: " + e.getMessage());
        }
    }

    private void loadDataFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String sid = parts[0].trim();
                        String name = parts[1].trim();
                        String birthDate = parts[2].trim();
                        Student student = new Student(sid, name, birthDate);
                        studentList.add(student);
                    }
                }
                scanner.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading data from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentManagementSystem app = new StudentManagementSystem();
        app.setVisible(true);
    }
}

class Student {
    private String id;
    private String name;
    private String birthDate;

    public Student(String id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1;
        int currentDay = now.get(Calendar.DAY_OF_MONTH);
        int birthYear = Integer.parseInt(birthDate.substring(6));
        int birthMonth = Integer.parseInt(birthDate.substring(3, 5));
        int birthDay = Integer.parseInt(birthDate.substring(0, 2));
        int age = currentYear - birthYear;
        if (currentMonth < birthMonth) {
            age--;
        } else if (currentMonth == birthMonth && currentDay < birthDay) {
            age--;
        }
        return age;
    }

    public String toString() {
        return id + ", " + name + ", " + birthDate + ", " + getAge() + " years old";
    }
}

