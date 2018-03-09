package co.mafiagame.bot.telegram;

import java.util.List;

/**
 * @author Esa Hekmatizadeh
 */
public class TResult {
    private boolean ok;
    private List<TUpdate> result;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<TUpdate> getResult() {
        return result;
    }

    public void setResult(List<TUpdate> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "TResult{" +
                "ok=" + ok +
                ", result=" + result +
                '}';
    }
}
