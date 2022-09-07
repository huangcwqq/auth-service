package req;

/**
 * the request use to delete user
 */
public class DeleteUserReq {

    // unique user key
    Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
