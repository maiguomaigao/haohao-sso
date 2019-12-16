package xyz.haohao.sso.core.sso;

public class SsoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SsoException(String msg) {
        super(msg);
    }

    public SsoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SsoException(Throwable cause) {
        super(cause);
    }

}
