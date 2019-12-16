package xyz.haohao.sso.core;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * common return
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 * @param <T extends Serializable>
 */
@Data
@ToString(onlyExplicitlyIncluded = true)
public class ReturnT<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;
	public static final int FAIL_CODE = 1000;

	@ToString.Include
	private int code;
	@ToString.Include
	private String msg;
	@ToString.Include
	private T data;

	public ReturnT(){}

	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private ReturnT(T data) {
		this.code = SUCCESS_CODE;
		this.data = data;
	}

	private ReturnT(String msg, T data) {
		this.code = SUCCESS_CODE;
		this.msg = msg;
		this.data = data;
	}

	private ReturnT(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static ReturnT success(){
		return new ReturnT(SUCCESS_CODE);
	}

	public static <T> ReturnT success(T data) {
		return new ReturnT(data);
	}

	public static<T> ReturnT success(String msg, T data) {
		return new ReturnT(msg, data);
	}

	public static ReturnT fail(String msg) {
		return new ReturnT(FAIL_CODE, msg);
	}

	public static ReturnT fail(int code, String msg) {
		return new ReturnT(code, msg);
	}

	public static<T> ReturnT fail(String msg, T data) {
		return new ReturnT(msg, data);
	}

	public static<T> ReturnT fail(int code, String msg, T data) {
		return new ReturnT(code, msg, data);
	}

	public boolean isSuccess(){
		return SUCCESS_CODE == code;
	}

	public boolean isFail(){
		return !isSuccess();
	}
}
