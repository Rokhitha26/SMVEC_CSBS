import java.util.ArrayList;
import java.util.Scanner;

// Employee class to store details
class Employee {
    private int id;
    private String name;
    private double salary;

    // Constructor
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // Display employee details
    public void displayEmployee() {
        System.out.println("ID: " + id + " | Name: " + name + " | Salary: $" + salary);
    }
}

// Employee Management System
public class EmployeeManagement {
    public static void main(String[] args) {
         System.out.println("Employee Management System Initialized");
        Scanner scanner = new Scanner(System.in);
        ArrayList<Employee> employees = new ArrayList<>();

        // Adding 2 employees initially
        employees.add(new Employee(1, "Alice", 50000));
        employees.add(new Employee(2, "Bob", 60000));

        while (true) {
    System.out.println("\n1. Add Employee\n2. Display Employees\n3. Remove Employee\n4. Exit");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();

    if (choice == 1) {
        // Add Employee
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Employee Salary: ");
        double salary = scanner.nextDouble();

        employees.add(new Employee(id, name, salary));
        System.out.println("Employee added successfully!");

    } else if (choice == 2) {
        // Display Employees
        System.out.println("\nEmployee List:");
        for (Employee emp : employees) {
            emp.displayEmployee();
        }

    } else if (choice == 3) {
        // Remove Employee
        System.out.print("Enter Employee ID to remove: ");
        int removeId = scanner.nextInt();
        boolean found = false;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).id == removeId) {
                employees.remove(i);
                System.out.println("Employee removed successfully!");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Employee ID not found!");
        }

    } else if (choice == 4) {
        System.out.println("Exiting Employee Management System...");
        break;

    } else {
        System.out.println("Invalid choice! Please try again.");
    }
}

        scanner.close();
    }
}
