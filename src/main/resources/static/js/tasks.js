import { performRequestWithBody, performRequestWithoutBody } from "./api.js";
import { showToast, TOAST_VARIANT } from "./utils.js";

const BASE_TASKS_URL = '/tasks';
const BASE_USERS_URL = '/users'
const createTaskModal = new bootstrap.Modal(document.getElementById('createTaskModal'));
const editTaskModal = new bootstrap.Modal(document.getElementById('editTaskModal'));

function fetchTasks() {
    performRequestWithoutBody(BASE_TASKS_URL, 'GET', onFetchSuccess, onFetchFail);
}

const onFetchSuccess = (response) => {
    response.json().then(async tasks => {
        const groupedTasks = {
            'to do': [],
            'in progress': [],
            'closed': []
        };

        tasks.forEach(task => {
            groupedTasks[task.status.toLowerCase()].push(task);
        });

        await populateTableRows(groupedTasks);
    });
}

const populateTableRows = async (groupedTasks) => {
    const taskTableBody = document.getElementById('taskTableBody');
    taskTableBody.innerHTML = '';
    const maxRowCount = Math.max(...Object.values(groupedTasks).map(group => group.length));
    for (let i = 0; i < maxRowCount; i++) {
        const row = document.createElement('tr');
        for (const status in groupedTasks) {
            const task = groupedTasks[status][i];
            const cell = document.createElement('td');
            if (task) {
                const assigneeEmail = await getUserEmail(task.userId);
                cell.innerHTML = getTaskCardHTML(task, assigneeEmail);
                addEditTaskEventListener(cell, task.id);
                addDeleteTaskEventListener(cell, task.id);
            }
            row.appendChild(cell);
        }
        taskTableBody.appendChild(row);
    }
}


const getUserEmail = (userId) => {
    return new Promise((resolve, reject) => {
        performRequestWithoutBody(`${BASE_USERS_URL}/${userId}`, 'GET',
            response => {
                response.json().then(user => {
                    resolve(user.email);
                }).catch(error => {
                    console.error(error);
                    reject(error);
                });
            },
            error => {
                console.error(error);
                reject(error);
            }
        );
    });
}

const fetchAssignees = () => {
    performRequestWithoutBody(BASE_USERS_URL, 'GET', onFetchAssigneesSuccess, onFetchAssigneesFail);
}

const onFetchAssigneesSuccess = (response) => {
    response.json().then(users => {
        document.querySelectorAll('.taskAssignee').forEach(assigneeSelect => {
            assigneeSelect.innerHTML = '';
            users.forEach(user => {
                const option = document.createElement('option');
                option.value = user.id;
                option.textContent = user.email;
                assigneeSelect.appendChild(option);
            });
        })
    });
}

const onFetchAssigneesFail = (error) => {
    console.error(error);
    showToast('Fetching assignees failed', TOAST_VARIANT.ERROR);
}

const getTaskCardHTML = (task, assigneeEmail) => {
    return `
        <div class="card">
            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <span>${task.title}</span>
                <div class="action-buttons">
                    <button class="btn edit-task-button" style="color:#ffcc00" data-task-id="${task.id}">
                        <i class="fa-solid fa-pen-to-square fa-xl"></i>
                    </button>
                    <button class="btn delete-task-button" style="color:#cc3300" data-task-id="${task.id}">
                        <i class="fa fa-trash fa-xl"></i>
                    </button>
                </div>
            </div>  
            <div class="card-body">
                <p class="card-text">Status: ${task.status}</p>
                <p class="card-text">Assignee: ${assigneeEmail}</p>
                <p class="card-text">Description: <br />${task.description}</p>
            </div>
        </div>
    `;
}

const addEditTaskEventListener = (cell) => {
    const editButton = cell.querySelector('.edit-task-button');
    editButton.addEventListener('click', () => {
        const taskId = editButton.getAttribute('data-task-id');
        openEditTaskModal(taskId);
    });
}

const openEditTaskModal = (taskId) => {
    fetchTaskDetailsAndOpenModal(taskId);
};

const fetchTaskDetailsAndOpenModal = (taskId) => {
    return performRequestWithoutBody(`${BASE_TASKS_URL}/${taskId}`, 'GET', onFetchTaskDetailsSuccess, onFetchTaskDetailsFail);
};

const onFetchTaskDetailsSuccess = (response) => {
    response.json().then(task => {
        populateEditModal(task);
        editTaskModal.show();
    });
}

const onFetchTaskDetailsFail = (error) => {
    console.error('Error fetching task details:', error);
    showToast('Failed to fetch task details', TOAST_VARIANT.ERROR);
}

const populateEditModal = (task) => {
    document.getElementById('editTaskId').value = task.id;
    document.getElementById('editTaskTitle').value = task.title;
    document.getElementById('editTaskStatus').value = task.status;
    document.getElementById('editTaskDescription').value = task.description;
    document.getElementById('editTaskAssignee').value = task.userId;

};

document.getElementById('editTaskForm').addEventListener('submit', (event) => {
    event.preventDefault();

    const taskId = document.getElementById('editTaskId').value;
    const taskData = {
        title: document.getElementById('editTaskTitle').value,
        description: document.getElementById('editTaskDescription').value,
        userId: document.getElementById('editTaskAssignee').value,
        status: document.getElementById('editTaskStatus').value
    };

    updateTask(taskId, taskData);
});

const updateTask = (taskId, taskData) => {
    performRequestWithBody(`${BASE_TASKS_URL}/${taskId}`, 'PUT', JSON.stringify(taskData), onEditSuccess, onEditFail);
};

const onEditSuccess = (response) => {
    showToast('Task edited successfully', TOAST_VARIANT.SUCCESS);
    editTaskModal.hide();
    fetchTasks();
}

const onEditFail = (error) => {
    console.error(error);
    showToast('Failed to edit task', TOAST_VARIANT.ERROR);
}

const addDeleteTaskEventListener = (cell) => {
    const deleteButton = cell.querySelector('.delete-task-button');
    deleteButton.addEventListener('click', () => {
        const taskId = deleteButton.getAttribute('data-task-id');
        deleteTask(taskId);
    });
}

const deleteTask = (taskId) => {
    performRequestWithoutBody(`${BASE_TASKS_URL}/${taskId}`, 'DELETE', onDeleteSuccess, onDeleteFail);
}

const onDeleteSuccess = (response) => {
    showToast('Task deleted successfully', TOAST_VARIANT.SUCCESS);
    fetchTasks();
}

const onDeleteFail = (error) => {
    console.error(error);
    showToast('Failed to delete task', TOAST_VARIANT.ERROR);
}

const onFetchFail = (error) => {
    console.error(error);
    showToast('Fetching tasks failed', TOAST_VARIANT.ERROR);
}

document.getElementById('openCreateTaskModalBtn').addEventListener('click', () => {
    createTaskModal.show();
});

const getFilteredAssignees = (event) => {
    const query = event.target.value.toLowerCase();
    performRequestWithoutBody(BASE_USERS_URL, 'GET', response => {
        response.json().then(users => {
            const filteredUsers = users.filter(user => user.email.toLowerCase().includes(query));
            document.querySelectorAll('.taskAssignee').forEach(assigneeSelect => {
                assigneeSelect.innerHTML = '';
                filteredUsers.forEach(user => {
                    const option = document.createElement('option');
                    option.value = user.id;
                    option.textContent = user.email;
                    assigneeSelect.appendChild(option);
                });
            })
        });
    }, onFetchAssigneesFail);
}

document.querySelectorAll('.assigneeSearch').forEach(assigneeSearch => {
    assigneeSearch.addEventListener('input', getFilteredAssignees);
})

document.getElementById('createTaskForm').addEventListener('submit', (event) => {
    event.preventDefault();

    const taskData = {
        title: document.getElementById('taskTitle').value,
        description: document.getElementById('taskDescription').value,
        userId: document.getElementById('taskAssignee').value
    };

    performRequestWithBody(BASE_TASKS_URL, 'POST', JSON.stringify(taskData), onCreateSuccess, onCreateFail);
});

const onCreateSuccess = (response) => {
    response.json().then(() => {
        fetchTasks();
        createTaskModal.hide();
        showToast('Task created successfully', TOAST_VARIANT.SUCCESS);
    });
}

const onCreateFail = (error) => {
    console.error(error);
    showToast('Creating task failed', TOAST_VARIANT.ERROR);
}

(() => {
    fetchTasks();
    fetchAssignees();
})();