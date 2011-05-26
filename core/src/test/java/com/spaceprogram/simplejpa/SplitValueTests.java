package com.spaceprogram.simplejpa;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;

public class SplitValueTests extends BaseTestClass {

	private void doTest(String value) {
        EntityManager em = factory.createEntityManager();
        MyTestObject object = new MyTestObject();
        object.setName(value);
        em.persist(object);
        em.close();

        em = factory.createEntityManager();
        object = em.find(MyTestObject.class, object.getId());
        Assert.assertEquals(value, object.getName());
        em.remove(object);
        em.close();
	}
	
	@Test
	public void testSmallStringValue() {
		doTest("Test");
	}
	
	@Test
	public void testMaximumValueWithNoSplitting() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < 1024; i ++) {
			s.append((char) ('a' + i % 26));
		}
		doTest(s.toString());
	}

	@Test
	public void testMinimumValueWithSplitting() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < 1025; i ++) {
			s.append((char) ('a' + i % 26));
		}
		doTest(s.toString());
	}

	@Test
	public void testValueWithLargeNumberOfSplits() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < 1024 * 12 + 1; i ++) {
			s.append((char) ('a' + i % 26));
		}
		doTest(s.toString());
	}

	@Test
	public void testValueWithSpecialCharactersAtEvenOffset() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < 1024 * 2; i ++) {
			s.append('\u00e4');
		}
		doTest(s.toString());
	}


	@Test
	public void testValueWithSpecialCharactersAtOddOffset() {
		StringBuffer s = new StringBuffer();
		s.append('a');
		for (int i = 0; i < 1024 * 2; i ++) {
			s.append('\u00e4');
		}
		doTest(s.toString());
	}

}
