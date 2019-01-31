package io.interact.app.movie.library.core.model;

import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractModelTest<Z extends BasicAbstractEntity> {

    BiFunction<Z, Z, TestParameter> prepareMovieToBePersistWithId(Long idFor1, Long idFor2) {
        return (Z entity1, Z entity2) -> {
            this.setId(entity1, idFor1);
            this.setId(entity2, idFor2);
            return new TestParameter<Z>(entity1, entity2);
        };
    }

    @SneakyThrows
    private void setId(Z entity, Long newId) {
        assertThat(entity.getId()).isNull();
        Field idField = entity.getClass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, newId);
        assertThat(entity.getId()).isEqualTo(newId);
    }

    @Value
    static class TestValue<T, K> {
        private T value;
        private BiConsumer<T, K> setterValue;
        private Function<K, T> getterValue;
    }

    @Value
    static class TestParameter<M> {
        private M entity1;
        private M entity2;
    }

    @Value
    static class TestEqualFlow<A> {
        public final BiFunction<A, A, TestParameter> given;
        public final Function<TestParameter, Boolean> when = this::defaultWhenImpl;
        public final BiConsumer<Boolean, Boolean> then = this::defaultThenImpl;

        Boolean defaultWhenImpl(TestParameter testParameter) {
            return testParameter.getEntity1().equals(testParameter.getEntity2());
        }

        void defaultThenImpl(Boolean result, Boolean expectedValue) {
            assertThat(result).isNotNull();
            assertThat(expectedValue).isNotNull();
            assertThat(result).isEqualTo(expectedValue);
        }

    }

    abstract Z clone(Z movie);

    BiFunction<Z, Z, TestParameter> createChangeClone(TestValue testValue) {
        if (testValue == null) {
            val n1 = this.createNew();
            val n2 = this.createNew();
            return (entity1, entity2) -> new TestParameter(n1, n2);
        }
        return (entity1, entity2) -> this.changeCloneBy(entity1, entity2, testValue);
    }


    @SneakyThrows
    private Z createNew() {
        return (Z) ((Class) ((ParameterizedType) this.getClass().
                getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
    }

    TestParameter<Z> changeCloneBy(Z entity1, Z entity2, TestValue testValue) {
        final Object newValue = testValue.getValue();
        testValue.getSetterValue().accept(newValue, entity2);
        assertThat(testValue.getGetterValue().apply(entity2)).isEqualTo(newValue);
        assertThat(testValue.getGetterValue().apply(entity1)).isNotEqualTo(newValue);
        return new TestParameter<Z>(entity1, entity2);
    }

}
