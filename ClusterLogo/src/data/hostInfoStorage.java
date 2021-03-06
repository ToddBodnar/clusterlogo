package data;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Stores data viewed with the hostGui
 *
 * @author Todd Bodnar
 */
public class hostInfoStorage {
    private ConcurrentLinkedQueue<runConfig> todo;
    private ConcurrentLinkedQueue<runConfig> inProgress;
    private ConcurrentLinkedQueue<runConfig> completed;
    private int totalAssignments;
    private int totalAssigned;
    private int totalCompleted;

    /**
     * Default constructor, sets all lists to new empty lists
     */
    public hostInfoStorage()
    {
        todo = new ConcurrentLinkedQueue<runConfig>();
        inProgress = new ConcurrentLinkedQueue<runConfig>();
        completed = new ConcurrentLinkedQueue<runConfig>();

        totalAssignments = 0;
        totalAssigned = 0;
        totalCompleted = 0;
    }//end hostInfoStorage

    /**
     * Adds a runConfig to the "todo" list
     * @param project the runConfig to add
     */
    public synchronized void addTodo(runConfig project)
    {
        todo.add(project);
        totalAssignments++;
    }

    public void removeAll()
    {
        todo = new ConcurrentLinkedQueue<runConfig>();
        inProgress = new ConcurrentLinkedQueue<runConfig>();
        completed = new ConcurrentLinkedQueue<runConfig>();

        totalAssignments = 0;
        totalAssigned = 0;
        totalCompleted = 0;
    }

    public void remove(runConfig item)
    {
        todo.remove(item);
        totalAssignments--;
    }

    /**
     * Removes a project from inProgress and puts it back into the todo list.
     * This is used for projects that it is assumed will not be completed by the currently assigned client
     * @param project The timed out project
     */
    public synchronized void timeOut(runConfig project)
    {
        todo.add(project);
        inProgress.remove(project);
        totalAssigned--;
    }

    /**
     * Removes a runConfig from the todo list and adds it to the inProgress list
     * Nothing is removed if project is not in the todo list, but it will still be added to the inProgress list
     * @param project the runConfig to move
     */
    public synchronized void moveFromTodoToInProgress(runConfig project)
    {
        todo.remove(project);
        inProgress.add(project);
        totalAssigned++;
    }

    /**
     * Removes a runConfig from the in progress list and adds it to the completed list
     * Nothing is removed if project is not in the in progress list, but it will still be added to the completed list list
     * @param project the runConfig to move
     */
    public synchronized void moveFromInProgressToCompleted(runConfig project)
    {
        inProgress.remove(project);
        completed.add(project);
        totalCompleted++;
    }

    /**
     * Gets the first project in the to do list
     * @return the first project in the to do list
     */
    public synchronized runConfig getProject()
    {
        return todo.peek();
    }

    public runConfig getAssignedProject()
    {
        return inProgress.peek();
    }

    /**
     * Gets the size of the to do list
     * @return the size of the to do list
     */
    public int getTodoSize()
    {
        return todo.size();
    }

    public int getInProgressSize()
    {
        return inProgress.size();
    }

    public int getCompletedSize()
    {
        return completed.size();
    }

    /**
     * Returns a String with each of the runConfigs in the to do list, one per line
     * @return a list of the elements in the to do list
     */
    public String describeTodo()
    {
        String s = "";

        Iterator<runConfig> i = todo.iterator();

        while(i.hasNext())
        {
            s+=i.next().toString()+"\n";
        }
        return s;
    }

    /**
     * Returns a String with each of the runConfigs in the in progress list, one per line
     * @return a list of the elements in the in progress list
     */
    public String describeInProgress()
    {
        String s = "";

        Iterator<runConfig> i = inProgress.iterator();

        while(i.hasNext())
        {
            s+=i.next().toString()+"\n";
        }
        return s;
    }

    /*
     * Returns a String with each of the runConfigs in the completed list, one per line
     * @return a list of the elements in the completed list
     */
    public String describeCompleted()
    {
        String s = "";

        Iterator<runConfig> i = completed.iterator();

        while(i.hasNext())
        {
            s+=i.next().toString()+"\n";
        }
        return s;
    }

    /**
     *
     * @return the total number of assignments added
     */
    public int getTotal()
    {
        return totalAssignments;
    }

    /**
     *
     * @return the number of assignments assigned to a client
     */
    public int getAssigned()
    {
        return totalAssigned;
    }

    /**
     *
     * @return the number of assignments completed
     */
    public int getCompleted()
    {
        return totalCompleted;
    }
}
