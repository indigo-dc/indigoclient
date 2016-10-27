package pl.psnc.indigo.fg.api.restful.jaxb;

/**
 * All possible states a task can be seen by the Future Gateway.
 */
public enum Status {
    SUBMIT,
    SUBMITTED,
    WAITING,
    READY,
    SCHEDULED,
    RUNNING,
    DONE,
    ABORTED,
    CANCELLED
}
