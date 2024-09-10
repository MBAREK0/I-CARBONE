package services.user;

public class UserResult {
    private boolean success;
    private String message;

    public UserResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
