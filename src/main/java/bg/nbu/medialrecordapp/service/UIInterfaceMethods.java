package bg.nbu.medialrecordapp.service;

public interface UIInterfaceMethods<T> {
    void delete(T object);
    T save(T object);
}
