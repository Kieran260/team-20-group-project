package com.iamin.data.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractEntityTest extends AbstractEntity {

    private AbstractEntity entity1;
    private AbstractEntity entity2;

    @Before
    public void setUp() {
        entity1 = new AbstractEntity() {};
        entity1.setId(1L);
        entity2 = new AbstractEntity() {};
        entity2.setId(2L);
    }

    @Test
    public void testGetId() {
        Assert.assertEquals(1L, entity1.getId().longValue());
        Assert.assertEquals(2L, entity2.getId().longValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        // test that the same entity is equal to itself
        Assert.assertEquals(entity1, entity1);

        // test that two different entities with the same id are equal
        AbstractEntity entity1Copy = new AbstractEntity() {};
        entity1Copy.setId(1L);
        Assert.assertEquals(entity1, entity1Copy);

        // test that two entities with different ids are not equal
        Assert.assertNotEquals(entity1, entity2);

        // test that entities with null ids are not equal
        AbstractEntity entity1NullId = new AbstractEntity() {};
        AbstractEntity entity2NullId = new AbstractEntity() {};
        Assert.assertNotEquals(entity1NullId, entity2NullId);

        // test that entities with different types are not equal
        Assert.assertNotEquals(entity1, new Object());

        // test hash code consistency
        Assert.assertEquals(entity1.hashCode(), entity1Copy.hashCode());
    }
}
