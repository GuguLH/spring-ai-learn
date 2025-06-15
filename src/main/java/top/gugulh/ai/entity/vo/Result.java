package top.gugulh.ai.entity.vo;

import lombok.Data;

@Data
public class Result {

    private Integer ok;
    private String msg;

    public Result() {
    }

    public Result(Integer ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public static Result ok() {
        return new Result(1, "ok");
    }

    public static Result fail(String msg) {
        return new Result(0, msg);
    }
}
