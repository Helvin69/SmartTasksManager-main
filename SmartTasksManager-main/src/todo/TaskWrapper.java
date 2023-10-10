package todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Класс, который служит своеобразной упаковкой для списка задач.
 * @author Kirill Sanov <sanov_04@mail.ru>
 * @version 1.0
 *
 */

/** Объявляем класс TaskWrapper */
@XmlRootElement(name = "Tasks")
class TaskWrapper {


    /** Список задач */
    private List<TaskList> taskList;

    /** Геттер для сериализации списка задач  */
    @XmlElement(name = "Task")
    public List<TaskList> getTaskList() {
        return taskList;
    }

    /** Сеттер для десериализации списка задач  */
    public void setTaskList(List<TaskList> taskList) {
        this.taskList = taskList;
    }
}



class DateAdapter extends XmlAdapter<String, Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }

}