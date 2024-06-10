package com.ooadprojectserver.restaurantmanagement.model.order.iterator;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 6/10/2024, Monday
 * @description:
 **/
public interface OrderLineIterator<T> {
    public boolean hasNext();
    public T next();

    default void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
