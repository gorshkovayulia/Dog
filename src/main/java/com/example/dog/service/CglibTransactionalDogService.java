package com.example.dog.service;

import com.example.dog.dao.JdbcConnectionHolder;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class CglibTransactionalDogService implements InvocationHandler {

    private final JdbcConnectionHolder jdbcConnectionHolder;
    private final Object target;

    public CglibTransactionalDogService(JdbcConnectionHolder jdbcConnectionHolder, Object target) {
        this.target = target;
        this.jdbcConnectionHolder = jdbcConnectionHolder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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

    public static Object createProxy(JdbcConnectionHolder jdbcConnectionHolder, Object target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibTransactionalDogService(jdbcConnectionHolder, target));
        return enhancer.create();
    }
}