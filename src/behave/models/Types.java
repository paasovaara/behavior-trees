package behave.models;

public class Types {
    public enum Status {
        Running,
        Success,
        Failure
    }

    public static Status invert(Status status) {
        if (status == Status.Success) {
            return Status.Failure;
        }
        else if (status == Status.Failure) {
            return Status.Success;
        }
        return status;
    }
}
