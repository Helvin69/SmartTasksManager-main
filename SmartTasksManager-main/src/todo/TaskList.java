package todo;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.concurrent.atomic.AtomicInteger;


@XmlRootElement(name = "Task")
class TaskList {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    private int id;
    private String caption;
    private String description;
    private int priority;
    private String status;
    private Date deadline;
    private Date complete;

    public TaskList() {
        this.id = NEXT_ID.getAndIncrement();
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (caption.length() <= 50) {
            this.caption = caption;
        } else {
            throw new IllegalArgumentException("Длина заголовка превышает 50 символов");
        }
    }


    @XmlElement(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "Priority")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority >= 0 && priority <= 10) {
            this.priority = priority;
        } else {
            throw new IllegalArgumentException("Приоритет должен быть в диапазоне от 0 до 10");
        }
    }


    @XmlElement(name = "Deadline")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @XmlElement(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElement(name = "Complete")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getComplete() {
        return complete;
    }

    public void setComplete(Date complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "Task ID: " + id +
                "\nCaption: " + caption +
                "\nDescription: " + description +
                "\nPriority: " + priority +
                "\nStatus: " + status +
                "\nDeadline: " + (deadline != null ? dateFormat.format(deadline) : "") +
                "\nComplete: " + (complete != null ? dateFormat.format(complete) : "");
    }
}


