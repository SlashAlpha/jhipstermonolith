package slash.process.meapp.web.rest;

import slash.process.meapp.MeApp;
import slash.process.meapp.domain.SubTask;
import slash.process.meapp.repository.SubTaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SubTaskResource} REST controller.
 */
@SpringBootTest(classes = MeApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SubTaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final Long DEFAULT_COST = 1L;
    private static final Long UPDATED_COST = 2L;

    private static final Long DEFAULT_BUDGET = 1L;
    private static final Long UPDATED_BUDGET = 2L;

    private static final Boolean DEFAULT_STARTED = false;
    private static final Boolean UPDATED_STARTED = true;

    private static final String DEFAULT_DIFFICULTY = "AAAAAAAAAA";
    private static final String UPDATED_DIFFICULTY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_CONTENT_TYPE = "image/png";

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubTaskMockMvc;

    private SubTask subTask;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTask createEntity(EntityManager em) {
        SubTask subTask = new SubTask()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .duration(DEFAULT_DURATION)
            .cost(DEFAULT_COST)
            .budget(DEFAULT_BUDGET)
            .started(DEFAULT_STARTED)
            .difficulty(DEFAULT_DIFFICULTY)
            .startDate(DEFAULT_START_DATE)
            .deadline(DEFAULT_DEADLINE)
            .contact(DEFAULT_CONTACT)
            .document(DEFAULT_DOCUMENT)
            .documentContentType(DEFAULT_DOCUMENT_CONTENT_TYPE);
        return subTask;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTask createUpdatedEntity(EntityManager em) {
        SubTask subTask = new SubTask()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .duration(UPDATED_DURATION)
            .cost(UPDATED_COST)
            .budget(UPDATED_BUDGET)
            .started(UPDATED_STARTED)
            .difficulty(UPDATED_DIFFICULTY)
            .startDate(UPDATED_START_DATE)
            .deadline(UPDATED_DEADLINE)
            .contact(UPDATED_CONTACT)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);
        return subTask;
    }

    @BeforeEach
    public void initTest() {
        subTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubTask() throws Exception {
        int databaseSizeBeforeCreate = subTaskRepository.findAll().size();

        // Create the SubTask
        restSubTaskMockMvc.perform(post("/api/sub-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subTask)))
            .andExpect(status().isCreated());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeCreate + 1);
        SubTask testSubTask = subTaskList.get(subTaskList.size() - 1);
        assertThat(testSubTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSubTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSubTask.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSubTask.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testSubTask.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testSubTask.isStarted()).isEqualTo(DEFAULT_STARTED);
        assertThat(testSubTask.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
        assertThat(testSubTask.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSubTask.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testSubTask.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testSubTask.getDocument()).isEqualTo(DEFAULT_DOCUMENT);
        assertThat(testSubTask.getDocumentContentType()).isEqualTo(DEFAULT_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSubTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subTaskRepository.findAll().size();

        // Create the SubTask with an existing ID
        subTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTaskMockMvc.perform(post("/api/sub-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subTask)))
            .andExpect(status().isBadRequest());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSubTasks() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);

        // Get all the subTaskList
        restSubTaskMockMvc.perform(get("/api/sub-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].started").value(hasItem(DEFAULT_STARTED.booleanValue())))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].documentContentType").value(hasItem(DEFAULT_DOCUMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].document").value(hasItem(Base64Utils.encodeToString(DEFAULT_DOCUMENT))));
    }

    @Test
    @Transactional
    public void getSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);

        // Get the subTask
        restSubTaskMockMvc.perform(get("/api/sub-tasks/{id}", subTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subTask.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.intValue()))
            .andExpect(jsonPath("$.started").value(DEFAULT_STARTED.booleanValue()))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.documentContentType").value(DEFAULT_DOCUMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.document").value(Base64Utils.encodeToString(DEFAULT_DOCUMENT)));
    }

    @Test
    @Transactional
    public void getNonExistingSubTask() throws Exception {
        // Get the subTask
        restSubTaskMockMvc.perform(get("/api/sub-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);

        int databaseSizeBeforeUpdate = subTaskRepository.findAll().size();

        // Update the subTask
        SubTask updatedSubTask = subTaskRepository.findById(subTask.getId()).get();
        // Disconnect from session so that the updates on updatedSubTask are not directly saved in db
        em.detach(updatedSubTask);
        updatedSubTask
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .duration(UPDATED_DURATION)
            .cost(UPDATED_COST)
            .budget(UPDATED_BUDGET)
            .started(UPDATED_STARTED)
            .difficulty(UPDATED_DIFFICULTY)
            .startDate(UPDATED_START_DATE)
            .deadline(UPDATED_DEADLINE)
            .contact(UPDATED_CONTACT)
            .document(UPDATED_DOCUMENT)
            .documentContentType(UPDATED_DOCUMENT_CONTENT_TYPE);

        restSubTaskMockMvc.perform(put("/api/sub-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubTask)))
            .andExpect(status().isOk());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeUpdate);
        SubTask testSubTask = subTaskList.get(subTaskList.size() - 1);
        assertThat(testSubTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSubTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSubTask.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubTask.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testSubTask.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testSubTask.isStarted()).isEqualTo(UPDATED_STARTED);
        assertThat(testSubTask.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testSubTask.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSubTask.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testSubTask.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testSubTask.getDocument()).isEqualTo(UPDATED_DOCUMENT);
        assertThat(testSubTask.getDocumentContentType()).isEqualTo(UPDATED_DOCUMENT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSubTask() throws Exception {
        int databaseSizeBeforeUpdate = subTaskRepository.findAll().size();

        // Create the SubTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubTaskMockMvc.perform(put("/api/sub-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subTask)))
            .andExpect(status().isBadRequest());

        // Validate the SubTask in the database
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubTask() throws Exception {
        // Initialize the database
        subTaskRepository.saveAndFlush(subTask);

        int databaseSizeBeforeDelete = subTaskRepository.findAll().size();

        // Delete the subTask
        restSubTaskMockMvc.perform(delete("/api/sub-tasks/{id}", subTask.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubTask> subTaskList = subTaskRepository.findAll();
        assertThat(subTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
