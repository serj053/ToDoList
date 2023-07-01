package main;

import main.model.ToDoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
public class ToDoList {
    @Autowired
    private ToDoListRepository listRepository;

    @GetMapping("/")
    public String getDateTime() {
        return (new Date()).toString();
    }

    //создание задачи с указанным в запросе названием и описанием.
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setDateTime(LocalDateTime.now());
        task.setDone(true);
        listRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    //возвращение задачи по указанному id
    @GetMapping("/tasks/{ID}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        Optional<Task> taskOptional = listRepository.findById(id);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity<>(taskOptional.get(), HttpStatus.OK);
    }

    //возвращение списка всех задач
    @GetMapping("/tasks")
    public ArrayList<Task> init() {
        Iterable<Task> list = listRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task : list) {
            tasks.add(task);
        }
        return tasks;
    }

    //обновление переданных параметров
    @PatchMapping(path = "/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable(value = "id")
            int id,
            @Valid
            @RequestBody
            Task taskDetails) {
        Optional<Task> taskOptional = listRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (taskDetails.getTitle() != null)
                task.setTitle(taskDetails.getTitle());
            if (taskDetails.getDescription() != null)
                task.setDescription(taskDetails.getDescription());
            if (taskDetails.getDone() != null)
                task.setDone(taskDetails.getDone());
            final Task updatedUser = listRepository.save(task);
            return ResponseEntity.ok(updatedUser);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable int id) {
        Optional<Task> taskOptional = listRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            listRepository.delete(task);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
