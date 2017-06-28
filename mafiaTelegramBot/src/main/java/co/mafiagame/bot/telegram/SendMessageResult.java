package co.mafiagame.bot.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Esa Hekmatizadeh
 */
public class SendMessageResult {
    private boolean ok;
    @JsonProperty("error_code")
    private Integer errorCode;
    private String description;
    private TMessage result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TMessage getResult() {
        return result;
    }

    public void setResult(TMessage result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SendMessageResult{" +
            "ok=" + ok +
            ", errorCode=" + errorCode +
            ", description='" + description + '\'' +
            ", result=" + result +
            '}';
    }
}
