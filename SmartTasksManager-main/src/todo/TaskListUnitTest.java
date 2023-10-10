package todo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskListUnitTest {

    public static void main(String[] args) {
        TaskListUnitTest unitTest = new TaskListUnitTest();
        unitTest.testSetAndGetMethods();
    }

    public void testSetAndGetMethods() {
        TaskList task = new TaskList();
        task.setCaption("Test Task");
        task.setDescription("This is a test task");
        task.setPriority(5);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deadline = dateFormat.parse("2023-07-31");
            task.setDeadline(deadline);

            Date complete = dateFormat.parse("2023-08-01");
            task.setComplete(complete);
        } catch (Exception e) {
            System.out.println("Exception occurred while parsing dates");
        }

        if (!"Test Task".equals(task.getCaption())) {
            System.out.println("Caption does not match expected value");
        }

        if (!"This is a test task".equals(task.getDescription())) {
            System.out.println("Description does not match expected value");
        }

        if (5 != task.getPriority()) {
            System.out.println("Priority does not match expected value");
        }

        Date deadline = task.getDeadline();
        if (deadline == null || !"2023-07-31".equals(dateFormat.format(deadline))) {
            System.out.println("Deadline does not match expected value");
        }

        Date complete = task.getComplete();
        if (complete == null || !"2023-08-01".equals(dateFormat.format(complete))) {
            System.out.println("Complete date does not match expected value");
        }
    }
}

