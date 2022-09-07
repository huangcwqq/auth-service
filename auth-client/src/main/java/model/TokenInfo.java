package model;

import java.util.Date;

/**
 * store the main information of token
 */
public class TokenInfo {
    /**
     * create time
     */
    private Date createAt;

    /**
     * user id
     */
    private Long userId;

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
