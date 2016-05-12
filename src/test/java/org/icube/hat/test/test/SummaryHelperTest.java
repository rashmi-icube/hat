package org.icube.hat.test.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.icube.hat.test.SummaryHelper;
import org.icube.hat.test.TestCount;
import org.junit.Test;

public class SummaryHelperTest {
	SummaryHelper sh = new SummaryHelper();
	int companyId = 1;
	int superAdmin = 1;
	int misAdmin = 2;
	int testAdmin = 4;

	@Test
	public void testGetTestCountList() {
		List<TestCount> testCountList = sh.getTestCountList(companyId, superAdmin);
		assertNotNull(testCountList);
		for (TestCount tc : testCountList) {
			assertNotNull(tc.getAllowed());

		}
	}
}
