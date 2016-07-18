package com.bet.manager.models.util;

import com.bet.manager.models.exceptions.InvalidMetaDataConstructorCountException;
import org.junit.Test;

public class MatchMetaDataUtilsTest {

	@Test(expected = InvalidMetaDataConstructorCountException.class)
	public void getErrorWhenParseMatchMetadataWithLessConstructParams() {
		MatchMetaDataUtils.parse("Chelsea Arsenal,1,206,1,2,3,-1,6,4,755,6,7,8,4,755,6,7,8,4,755,7,89,10,11,12,13,14,15,16,2,3-5");
	}

	@Test(expected = NumberFormatException.class)
	public void getErrorWhenGivenWrongNumberFormat() {
		MatchMetaDataUtils.parse("Chelsea Arsenal,1,206,1,2,3,-1,6,4,1,6,a,2,8,4,755,6,7,8,4,1,7,89,10,11,12,13,14,15,16,2,6,3-2");
	}

	@Test(expected = InvalidMetaDataConstructorCountException.class)
	public void getErrorWhenOneOfTheParamsIsWhitespace() {
		MatchMetaDataUtils.parse("Chelsea Arsenal,1,206,1,2,3,-1,6,4,,6,7,8,4,755,6,7,8,4,755,7,89,10,11,12,13,14,15,16,2,3-3");
	}
}
