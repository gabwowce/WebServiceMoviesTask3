package lt.eif.viko.gtamaseviciute.util;


public class OperationResult<T> {
    private boolean success;
    private String message;
    private T data;

    public OperationResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public static <T> OperationResult<T> success(T data, String message) {
        return new OperationResult<>(true, message, data);
    }

    public static <T> OperationResult<T> failure(String message) {
        return new OperationResult<>(false, message, null);
    }


}