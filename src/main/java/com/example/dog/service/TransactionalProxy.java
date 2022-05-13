package com.example.dog.service;

import com.example.dog.dao.JdbcConnectionHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionalProxy implements InvocationHandler {

    private final JdbcConnectionHolder jdbcConnectionHolder;
    private final Object target;

    public TransactionalProxy(JdbcConnectionHolder jdbcConnectionHolder, Object target) {
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

    public static Object createProxy(JdbcConnectionHolder jdbcConnectionHolder, Object target)  {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionalProxy(jdbcConnectionHolder, target));
    }
}
