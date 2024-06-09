
document.querySelector('.add_task').addEventListener('click', function() {
    const task = document.getElementById('taskInput').value;
    if (task) {
      const newTask = document.createElement('li');
      newTask.innerText = task;
      // Add newTask to the task list (replace with your selector for the task list)
      document.querySelector('.card-wrapper2 .option-card2').appendChild(newTask);
      document.getElementById('taskInput').value = ''; // Clear the input field
    }
  });