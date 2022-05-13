package com.example.dog.service;

import com.example.dog.dao.JdbcConnectionHolder;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class CglibTransactionalDogService implements MethodInterceptor {

    private final JdbcConnectionHolder jdbcConnectionHolder;
    private final Object target;

    public CglibTransactionalDogService(JdbcConnectionHolder jdbcConnectionHolder, Object target) {
        this.jdbcConnectionHolder = jdbcConnectionHolder;
        this.target = target;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result;
        if (method.getName().startsWith("get")) {
            jdbcConnectionHolder.setReadOnly(true);
        }
        jdbcConnectionHolder.startTransaction();
        try {
            result = method.invoke(target, args);
            jdbcConnectionHolder.commit();
            return result;
        } catch (Throwable e) {
            jdbcConnectionHolder.rollback();
            throw e.getCause();
        } finally {
            jdbcConnectionHolder.close();
        }
    }

    public static <T> T createProxy(JdbcConnectionHolder jdbcConnectionHolder, T target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibTransactionalDogService(jdbcConnectionHolder, target));

        Constructor<?> constructor = Stream.of(target.getClass().getConstructors())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available constructor to create proxy object"));
        Class<?>[] argTypes = constructor.getParameterTypes();
        Object[] args = new Object[argTypes.length];
        //noinspection unchecked
        return (T) enhancer.create(argTypes, args);
    }
}