import { useState, useEffect } from 'react';
import { Web5 } from '@tbd54566975/web5';
import Head from 'next/head';
export default function Todo() {
  const [web5Instance, setWeb5Instance] = useState(null);
  const [aliceDid, setAliceDid] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState('');
  const [editTaskId, setEditTaskId] = useState(null);
  const [editTaskValue, setEditTaskValue] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function connectToWeb5() {
      const { web5, did } = await Web5.connect();
      setWeb5Instance(web5);
      setAliceDid(did);
      setLoading(true);
      // Read the tasks from the DWN.

      const response = await web5.dwn.records.query({
        from: did,
        message: {
          filter: {
            dataFormat: 'application/json',
            schema: 'https://schema.org/Action',
          },
        },
      });

      const tasks = await Promise.all(
        response.records.map(async (record) => {
          try {
            const data = await record.data.json();
            return { id: record.id, text: data.name };
          } catch (error) {
            console.error(`Error parsing record data as JSON: ${error}`);
            return null;
          }
        })
      );

      // Filter out any null values from the tasks array.
      const validTasks = tasks.filter((task) => task !== null);
      setTasks(validTasks);
      console.log('this is a test')
      setLoading(false);
    }

    connectToWeb5();
  }, []);

  async function addTask(event) {
    event.preventDefault(); // Prevent form from submitting and refreshing the page
    if (web5Instance && aliceDid && newTask.trim() !== '') {
      const taskData = {
        '@context': 'https://schema.org/',
        '@type': 'Action',
        name: newTask,
        completed: false, // Add this line
      };

      const { record } = await web5Instance.dwn.records.create({
        data: taskData,
        message: {
          dataFormat: 'application/json',
          schema: 'https://schema.org/Action',
        },
      });

      // Send the record to the DWN.
      await record.send(aliceDid);

      setTasks((prevTasks) => [...prevTasks, { id: record.id, text: newTask }]);
      setNewTask('');
    }
  }


  async function deleteTask(id) {
    if (web5Instance && aliceDid) {
      await web5Instance.dwn.records.delete({
        from: aliceDid,
        message: {
          recordId: id,
        },
      });
      setTasks((prevTasks) => prevTasks.filter((task) => task.id !== id));
    }
  }

  async function updateTask(id) {
    if (web5Instance && aliceDid) {
      const { record } = await web5Instance.dwn.records.read({
        from: aliceDid,
        message: {
          recordId: id,
        },
      });

      await record.update({ data: editTaskValue });
      // Send the updated record to the DWN.
      await record.send(aliceDid);
      setTasks((prevTasks) => prevTasks.map((task) => (task.id === id ? { ...task, text: editTaskValue } : task)));
      setEditTaskId(null);
      setEditTaskValue('');
    }
  }

  return (
    <div>
      <Head>
        <title>Web5 To Do App</title>
      </Head>
      <h1>To Do</h1>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          <ul>
            <form className="add-task-container" onSubmit={addTask}>
              <input name="newTask" type="text" value={newTask} onChange={(e) => setNewTask(e.target.value)} placeholder="New task" />
              <button name="addTask" type="submit">Add task</button>
            </form>
            {tasks.map((task, index) => (
              <li key={index} className="main-task">
                {editTaskId === task.id ? (
                  <div className="tasks-container">
                    <div className="main-task-container">
                      <input type="text" value={editTaskValue} onChange={(e) => setEditTaskValue(e.target.value)} />
                    </div>
                    <div className="action-buttons">
                      <button className="action-button" onClick={() => updateTask(task.id)}>Update</button>
                      <button className="action-button" onClick={() => setEditTaskId(null)}>Cancel</button>
                    </div>
                  </div>
                ) : (
                  <div className="tasks-container">
                    <div className="main-task-container">
                      <label>{task.text}</label>
                    </div>
                    <div className="action-buttons">
                      <button className="action-button" onClick={() => { setEditTaskId(task.id); setEditTaskValue(task.text); }}>Edit</button>
                      <button name="deleteTask" className="action-button" onClick={() => deleteTask(task.id)}>Delete</button>
                    </div>
                  </div>
                )}
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );

}
