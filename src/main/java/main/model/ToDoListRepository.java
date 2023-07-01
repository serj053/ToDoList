package main.model;

import main.Task;
import org.springframework.data.repository.CrudRepository;

public interface ToDoListRepository extends CrudRepository<Task, Integer> {
}
