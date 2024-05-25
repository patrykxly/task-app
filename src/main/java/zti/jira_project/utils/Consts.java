package zti.jira_project.utils;

/**
 * Utility class containing constants used in the application.
 */
public class Consts {

    // Prevent instantiation of the Consts class
    private Consts() {}

    /**
     * Inner class containing task status constants.
     */
    public static final class STATUS {

        /**
         * Represents the "To do" status of a task.
         */
        public final static String TO_DO = "To do";

        /**
         * Represents the "In progress" status of a task.
         */
        public final static String IN_PROGRESS = "In progress";

        /**
         * Represents the "Closed" status of a task.
         */
        public final static String CLOSED = "Closed";
    }
}
