/**
 * Autogenerated by Thrift Compiler (0.13.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package pa1;


/**
 * Completion status of a JobRequest
 */
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.13.0)", date = "2022-02-19")
public enum JobStatus implements org.apache.thrift.TEnum {
  SUCCESS(0),
  FAILURE(1);

  private final int value;

  private JobStatus(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  @org.apache.thrift.annotation.Nullable
  public static JobStatus findByValue(int value) { 
    switch (value) {
      case 0:
        return SUCCESS;
      case 1:
        return FAILURE;
      default:
        return null;
    }
  }
}