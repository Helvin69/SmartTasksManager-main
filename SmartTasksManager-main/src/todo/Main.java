package todo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Main {
    private static final String COMMAND_ADD_TASK = "new";
    private static final String COMMAND_EDIT_TASK = "edit";
    private static final String COMMAND_REMOVE_TASK = "remove";
    private static final String COMMAND_REMOVE_ALL_TASKS = "remove_all";
    private static final String COMMAND_COMPLETE_TASK = "complete";
    private static final String COMMAND_HELP_TASK = "help";
    private static final String COMMAND_EXIT = "exit";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        Main main = new Main();
        TaskListUnitTest.main(null);
        main.run();
    }

    private void loadTasks(List<TaskList> taskList) throws JAXBException {
        File file = new File("SmartTasks.xml");
        if (file.exists()) {
            TaskWrapper taskWrapper = loadTaskWrapper(file);
            if (taskWrapper != null) {
                List<TaskList> loadedTaskList = taskWrapper.getTaskList();
                if (loadedTaskList != null) {
                    taskList.addAll(loadedTaskList);
                }
            }
        }
    }

    private void run() {
        try {
            Scanner scan = new Scanner(System.in);
            List<TaskList> taskList = new ArrayList<>();
            System.out.println("Добро пожаловать в систему управления задачами!");

            loadTasks(taskList);

            boolean exitMenu = false;
            while (!exitMenu) {
                System.out.println("\nВыберите команду:");
                System.out.println("1. " + COMMAND_ADD_TASK + " - Добавить новую задачу");
                System.out.println("2. " + COMMAND_REMOVE_TASK + " - Удалить задачу");
                System.out.println("3. " + COMMAND_EDIT_TASK + " - Редактировать задачу");
                System.out.println("4. " + COMMAND_COMPLETE_TASK + " - Пометить задачу как выполненную");
                System.out.println("5. " + COMMAND_HELP_TASK + " - Вывод уже существующих задач по параметрам");
                System.out.println("6. " + COMMAND_EXIT + " - Выйти из программы");
                System.out.println("7. " + COMMAND_REMOVE_ALL_TASKS + " - Удалить все задачи");
                System.out.print("Ваш выбор: ");
                String command = scan.nextLine();

                switch (command) {
                    case COMMAND_ADD_TASK:
                        addNewTask(scan, taskList);
                        break;
                    case COMMAND_EDIT_TASK:
                        System.out.print("Введите id задачи для редактирования: ");
                        int taskId = scan.nextInt();
                        scan.nextLine();
                        editTaskById(scan, taskList, taskId);
                        break;
                    case COMMAND_REMOVE_TASK:
                        System.out.print("Введите id задачи для удаления: ");
                        taskId = scan.nextInt();
                        scan.nextLine();
                        removeTaskById(taskList, taskId);
                        break;
                    case COMMAND_REMOVE_ALL_TASKS:
                        removeAllTasks(taskList);
                        break;
                    case COMMAND_COMPLETE_TASK:
                        System.out.print("Введите id задачи для завершения: ");
                        int completeTaskId = scan.nextInt();
                        scan.nextLine();
                        completeTaskById(taskList, completeTaskId);
                        break;
                    case COMMAND_HELP_TASK:
                        System.out.println("\nВыберите параметр для вывода задач:");
                        System.out.println("1. Все задачи");
                        System.out.println("2. Новые задачи");
                        System.out.println("3. Задачи в работе");
                        System.out.println("4. Выполненные задачи");
                        System.out.println("5. Назад");
                        System.out.print("Ваш выбор: ");
                        int option = scan.nextInt();
                        scan.nextLine();
                        switch (option) {
                            case 1:
                                listAllTasks(taskList);
                                break;
                            case 2:
                                listTasksByStatus(taskList, "new");
                                break;
                            case 3:
                                listTasksByStatus(taskList, "in_progress");
                                break;
                            case 4:
                                listTasksByStatus(taskList, "done");
                                break;
                            default:
                                System.out.println("Некорректный выбор параметра.");
                                break;
                        }
                    case COMMAND_EXIT:
                        scan.close();
                        exitMenu = true;
                        break;
                    default:
                        System.out.println("Некорректная команда. Попробуйте снова.");
                        break;
                }
            }

            serializeTasks(taskList);
        } catch (JAXBException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void listAllTasks(List<TaskList> taskList) {
        if (taskList.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("Список всех задач:");
            for (TaskList task : taskList) {
                System.out.println(task);
            }
        }
    }

    private void addNewTask(Scanner scan, List<TaskList> taskList) {
        System.out.println("\nСоздайте новую задачу и задайте ей параметры.");

        do {
            TaskList task = new TaskList();

            System.out.print("Введите заголовок задачи: ");
            String caption = scan.nextLine();
            if (caption.equalsIgnoreCase(COMMAND_EXIT)) {
                break;
            }
            task.setCaption(caption);

            System.out.print("Введите описание задачи: ");
            String description = scan.nextLine();
            task.setDescription(description);

            System.out.print("Введите приоритет: ");
            int priority = scan.nextInt();
            task.setPriority(priority);
            scan.nextLine();

            System.out.print("Введите дату начала работы (в формате ГГГГ-ММ-ДД): ");
            String startDateString = scan.nextLine();
            try {
                Date startDate = dateFormat.parse(startDateString);
                task.setDeadline(startDate);
            } catch (ParseException e) {
                System.out.println("Некорректный формат даты. Задача будет сохранена без даты начала работы.");
            }

            task.setStatus("new");

            System.out.print("Введите дату окончания (в формате ГГГГ-ММ-ДД): ");
            String endDateString = scan.nextLine();
            try {
                Date endDate = dateFormat.parse(endDateString);
                task.setComplete(endDate);
            } catch (ParseException e) {
                System.out.println("Некорректный формат даты. Задача будет сохранена без даты окончания.");
            }

            taskList.add(task);

            System.out.print("Хотите добавить ещё одну задачу? (да/нет): ");
            String answer = scan.nextLine();
            if (!answer.equalsIgnoreCase("да")) {
                break;
            }
        } while (true);
    }

    private void editTaskById(Scanner scan, List<TaskList> taskList, int id) {
        TaskList taskToEdit = null;
        for (TaskList task : taskList) {
            if (task.getId() == id) {
                taskToEdit = task;
                break;
            }
        }

        if (taskToEdit != null) {
            System.out.println("\nРедактирование задачи с идентификатором " + id + ". Введите новые значения.");
            System.out.println("Оставьте поле пустым, чтобы оставить текущее значение.");

            System.out.print("Заголовок (текущее значение: " + taskToEdit.getCaption() + "): ");
            String caption = scan.nextLine();
            if (!caption.isEmpty()) {
                taskToEdit.setCaption(caption);
            }

            System.out.print("Описание (текущее значение: " + taskToEdit.getDescription() + "): ");
            String description = scan.nextLine();
            if (!description.isEmpty()) {
                taskToEdit.setDescription(description);
            }

            System.out.print("Важность (текущее значение: " + taskToEdit.getPriority() + "): ");
            String priorityStr = scan.nextLine();
            if (!priorityStr.isEmpty()) {
                try {
                    int priorityInt = Integer.parseInt(priorityStr);
                    taskToEdit.setPriority(priorityInt);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректное значение важности. Будет сохранено текущее значение.");
                }
            }

            System.out.print("Статус (текущее значение: " + taskToEdit.getStatus() + "): ");
            String status = scan.nextLine();
            if (!status.isEmpty()) {
                taskToEdit.setStatus(status);
            }

            System.out.print("Срок начала (текущее значение: " + (taskToEdit.getDeadline() != null ? dateFormat.format(taskToEdit.getDeadline()) : "") + "): ");
            String deadlineStr = scan.nextLine();
            if (!deadlineStr.isEmpty()) {
                try {
                    Date deadline = dateFormat.parse(deadlineStr);
                    taskToEdit.setDeadline(deadline);
                } catch (ParseException e) {
                    System.out.println("Некорректный формат даты. Будет сохранено текущее значение срока.");
                }
            }

            System.out.println("Задача с идентификатором " + id + " успешно отредактирована.");
        } else {
            System.out.println("Задача с идентификатором " + id + " не найдена.");
        }
    }

    private void removeTaskById(List<TaskList> taskList, int taskId) {
        boolean removed = false;
        for (int i = 0; i < taskList.size(); i++) {
            TaskList task = taskList.get(i);
            if (task.getId() == taskId) {
                taskList.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Задача с id " + taskId + " успешно удалена.");
        } else {
            System.out.println("Задача с id " + taskId + " не найдена.");
        }
    }

    private void removeAllTasks(List<TaskList> taskList) {
        taskList.clear();
        System.out.println("Все задачи успешно удалены.");
    }

    private void completeTaskById(List<TaskList> taskList, int taskId) {
        TaskList taskToComplete = findTaskById(taskList, taskId);
        if (taskToComplete != null) {
            Date currentDate = new Date();
            taskToComplete.setComplete(currentDate);
            taskToComplete.setStatus("done");
            System.out.println("Задача с идентификатором " + taskId + " успешно помечена как выполненная.");
        } else {
            System.out.println("Задача с идентификатором " + taskId + " не найдена.");
        }
    }

    private TaskList findTaskById(List<TaskList> taskList, int taskId) {
        for (TaskList task : taskList) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }

    private void serializeTasks(List<TaskList> taskList) throws JAXBException {
        File file = new File("SmartTasks.xml");
        TaskWrapper taskWrapper = new TaskWrapper();
        taskWrapper.setTaskList(taskList);

        JAXBContext jaxbContext = JAXBContext.newInstance(TaskWrapper.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(taskWrapper, file);

        System.out.println("Список задач успешно сериализован в файл SmartTasks.xml.");
    }

    private TaskWrapper loadTaskWrapper(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TaskWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (TaskWrapper) unmarshaller.unmarshal(file);
    }

    private void listTasksByStatus(List<TaskList> taskList, String status) {
        System.out.println("Задачи со статусом " + status + ":");
        for (TaskList task : taskList) {
            if (task.getStatus().equalsIgnoreCase(status)) {
                System.out.println(task);
            }

        }
    }

}
