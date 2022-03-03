package node;

/**
 * Object that holds info of an image transformation
 *  - status
 *  - msg
 */
public class TransformationData {
    
    private TransformationStatus status;
    private String msg;

    public TransformationData(
        TransformationStatus status,
        String msg
    ) {
        this.status = status;
        this.msg = msg;
    }

    public TransformationStatus getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}