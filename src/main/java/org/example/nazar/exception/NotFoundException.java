package org.example.nazar.exception;

/**
 * این کلاس یک استثنا برای نشان دادن عدم یافت یک شیء را تعریف می‌کند.
 * این استثنا به عنوان یک استثنای زمان اجرا (RuntimeException) تعریف شده است،
 * به این معنی که نیازی به بررسی صریح آن در کد نیست.
 */
public class NotFoundException extends RuntimeException {

    /**
     * پیام پیش‌فرض برای استثنا
     */
    private static final String DEFAULT_MESSAGE = "not found";

    /**
     * سازنده اصلی استثنا با پیام سفارشی، شیء مورد نظر و نام آن
     *
     * @param message پیام سفارشی برای استثنا
     * @param obj شیء مورد نظر که یافت نشده است
     * @param name نام شیء مورد نظر
     */
    public NotFoundException(String message, Object obj, String name) {
        super(String.format("%s : %s , %s", message, obj.getClass().getSimpleName(), name));
    }

    /**
     * سازنده استثنا با شیء مورد نظر و نام آن و پیام پیش‌فرض
     *
     * @param obj شیء مورد نظر که یافت نشده است
     * @param name نام شیء مورد نظر
     */
    public NotFoundException(Object obj, String name) {
        this(DEFAULT_MESSAGE, obj, name);
    }

    /**
     * سازنده استثنا با پیام پیش‌فرض
     */
    public NotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * سازنده استثنا با پیام سفارشی
     *
     * @param msg پیام سفارشی برای استثنا
     */
    public NotFoundException(String msg) {
        super(msg);
    }
}
