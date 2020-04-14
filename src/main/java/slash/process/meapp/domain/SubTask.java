package slash.process.meapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.time.Duration;

/**
 * A SubTask.
 */
@Entity
@Table(name = "sub_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SubTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Duration duration;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "started")
    private Boolean started;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "contact")
    private String contact;

    @Lob
    @Column(name = "document")
    private byte[] document;

    @Column(name = "document_content_type")
    private String documentContentType;

    @ManyToOne
    @JsonIgnoreProperties("subTasks")
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SubTask title(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SubTask description(String description) {
        this.description = description;
        return this;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public SubTask duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public SubTask cost(Long cost) {
        this.cost = cost;
        return this;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public SubTask budget(Long budget) {
        this.budget = budget;
        return this;
    }

    public Boolean isStarted() {
        return started;
    }

    public SubTask started(Boolean started) {
        this.started = started;
        return this;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public SubTask difficulty(String difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public SubTask startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public SubTask deadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public SubTask contact(String contact) {
        this.contact = contact;
        return this;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public SubTask document(byte[] document) {
        this.document = document;
        return this;
    }

    public String getDocumentContentType() {
        return documentContentType;
    }

    public void setDocumentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
    }

    public SubTask documentContentType(String documentContentType) {
        this.documentContentType = documentContentType;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public SubTask task(Task task) {
        this.task = task;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubTask)) {
            return false;
        }
        return id != null && id.equals(((SubTask) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SubTask{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", duration='" + getDuration() + "'" +
            ", cost=" + getCost() +
            ", budget=" + getBudget() +
            ", started='" + isStarted() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", contact='" + getContact() + "'" +
            ", document='" + getDocument() + "'" +
            ", documentContentType='" + getDocumentContentType() + "'" +
            "}";
    }
}
