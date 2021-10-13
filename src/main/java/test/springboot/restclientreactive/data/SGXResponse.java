package test.springboot.restclientreactive.data;

public class SGXResponse<T> {

    Meta meta;
    T data;

    public Meta getMeta() {
        return meta;
    }

    public T getData() {
        return data;
    }
}
